package org.xobo.coke.utility;

import static java.nio.charset.StandardCharsets.UTF_8;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URISyntaxException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import javax.crypto.Cipher;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;

public class RsaUtil {

  static private Base64.Encoder encoder = Base64.getMimeEncoder();

  public static String decrypt(String cipherText, PrivateKey privateKey) throws Exception {
    byte[] bytes = Base64.getDecoder().decode(cipherText);

    Cipher decriptCipher = Cipher.getInstance("RSA");
    decriptCipher.init(Cipher.DECRYPT_MODE, privateKey);

    return new String(decriptCipher.doFinal(bytes), UTF_8);
  }

  public static String encrypt(String plainText, PublicKey publicKey) throws Exception {
    Cipher encryptCipher = Cipher.getInstance("RSA");
    encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);

    byte[] cipherText = encryptCipher.doFinal(plainText.getBytes(UTF_8));

    return Base64.getEncoder().encodeToString(cipherText);
  }

  public static KeyPair generateKeyPair() throws Exception {
    KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
    generator.initialize(2048, new SecureRandom());
    KeyPair pair = generator.generateKeyPair();

    return pair;
  }

  public static KeyPair getKeyPairFromKeyStore() throws Exception {
    // Generated with:
    // keytool -genkeypair -alias mykey -storepass s3cr3t -keypass s3cr3t -keyalg RSA -keystore
    // keystore.jks

    InputStream ins = RsaUtil.class.getResourceAsStream("/keystore.jks");

    KeyStore keyStore = KeyStore.getInstance("JCEKS");
    keyStore.load(ins, "s3cr3t".toCharArray()); // Keystore password
    KeyStore.PasswordProtection keyPassword = // Key password
        new KeyStore.PasswordProtection("s3cr3t".toCharArray());

    KeyStore.PrivateKeyEntry privateKeyEntry =
        (KeyStore.PrivateKeyEntry) keyStore.getEntry("mykey", keyPassword);

    java.security.cert.Certificate cert = keyStore.getCertificate("mykey");
    PublicKey publicKey = cert.getPublicKey();
    PrivateKey privateKey = privateKeyEntry.getPrivateKey();

    return new KeyPair(publicKey, privateKey);
  }

  public static PrivateKey loadPrivateKey(String privateKeyContent)
      throws InvalidKeySpecException, NoSuchAlgorithmException, IOException, URISyntaxException {
    privateKeyContent = privateKeyContent.replaceAll("\\n", "")
        .replace("-----BEGIN RSA PRIVATE KEY-----", "").replace("-----END RSA PUBLIC KEY-----", "");
    KeyFactory kf = KeyFactory.getInstance("RSA");
    PKCS8EncodedKeySpec keySpecPKCS8 =
        new PKCS8EncodedKeySpec(Base64.getMimeDecoder().decode(privateKeyContent));
    PrivateKey privKey = kf.generatePrivate(keySpecPKCS8);

    return privKey;
  }

  public static RSAPublicKey loadPublicKey(String publicKeyContent)
      throws InvalidKeySpecException, NoSuchAlgorithmException, IOException, URISyntaxException {
    publicKeyContent = publicKeyContent.replaceAll("\\n", "")
        .replace("-----BEGIN RSA PUBLIC KEY-----", "").replace("-----END RSA PUBLIC KEY-----", "");;

    KeyFactory kf = KeyFactory.getInstance("RSA");

    X509EncodedKeySpec keySpecX509 =
        new X509EncodedKeySpec(Base64.getMimeDecoder().decode(publicKeyContent));
    RSAPublicKey pubKey = (RSAPublicKey) kf.generatePublic(keySpecX509);
    return pubKey;
  }

  public static void main(String... argv) throws Exception {
    // First generate a public/private key pair
    KeyPair pair = generateKeyPair();
    // KeyPair pair = getKeyPairFromKeyStore();

    String priKey = write(pair.getPrivate());
    String pubKey = write(pair.getPublic());
    System.out.println(priKey);
    System.out.println();
    System.out.println(pubKey);

    // Our secret message
    String message = "the answer to life the universe and everything";

    // Encrypt the message
    String cipherText = encrypt(message, pair.getPublic());

    // Now decrypt it
    String decipheredMessage = decrypt(cipherText, pair.getPrivate());

    System.out.println(decipheredMessage);

    // Let's sign our message
    String signature = sign("foobar", pair.getPrivate());

    // Let's check the signature
    boolean isCorrect = verify("foobar", signature, pair.getPublic());
    System.out.println("Signature correct: " + isCorrect);

    pub(pair.getPublic());
  }

  public static String sign(String plainText, PrivateKey privateKey) throws Exception {
    Signature privateSignature = Signature.getInstance("SHA256withRSA");
    privateSignature.initSign(privateKey);
    privateSignature.update(plainText.getBytes(UTF_8));

    byte[] signature = privateSignature.sign();

    return Base64.getEncoder().encodeToString(signature);
  }

  public static boolean verify(String plainText, String signature, PublicKey publicKey) {
    Signature publicSignature;
    try {
      publicSignature = Signature.getInstance("SHA256withRSA");
      publicSignature.initVerify(publicKey);
      publicSignature.update(plainText.getBytes(UTF_8));

      byte[] signatureBytes = Base64.getDecoder().decode(signature);

      return publicSignature.verify(signatureBytes);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }


  public static void pub(PublicKey pub) throws IOException {
    byte[] pubBytes = pub.getEncoded();

    SubjectPublicKeyInfo spkInfo = SubjectPublicKeyInfo.getInstance(pubBytes);
    ASN1Primitive primitive = spkInfo.parsePublicKey();
    byte[] publicKeyPKCS1 = primitive.getEncoded();
    PemObject pemObject = new PemObject("RSA PUBLIC KEY", publicKeyPKCS1);
    StringWriter stringWriter = new StringWriter();
    PemWriter pemWriter = new PemWriter(stringWriter);
    pemWriter.writeObject(pemObject);
    pemWriter.close();
    System.out.println(stringWriter.toString());
    String pemString = stringWriter.toString();
  }

  static public String write(PrivateKey privateKey) throws IOException {
    StringWriter out = new StringWriter();
    out.write("-----BEGIN RSA PRIVATE KEY-----\n");
    writeBase64(out, privateKey);
    out.write("-----END RSA PRIVATE KEY-----\n");
    return out.toString();
  }


  static public String write(PublicKey publicKey) throws IOException {
    StringWriter out = new StringWriter();
    out.write("-----BEGIN RSA PUBLIC KEY-----\n");
    writeBase64(out, publicKey);
    out.write("-----END RSA PUBLIC KEY-----\n");
    return out.toString();
  }

  static private void writeBase64(Writer out, Key key)
      throws java.io.IOException {
    byte[] buf = key.getEncoded();
    out.write(encoder.encodeToString(buf));
    out.write("\n");
  }
}

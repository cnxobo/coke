package coke.rsa;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.xobo.coke.utility.RsaUtil;


public class Main {

  protected final static Logger LOGGER = Logger.getLogger(Main.class);

  public static final int KEY_SIZE = 1024;

  public static void main(String[] args)
      throws Exception {
    Security.addProvider(new BouncyCastleProvider());
    LOGGER.info("BouncyCastle provider added.");

    RSAPrivateKey priv =
        (RSAPrivateKey) RsaUtil.loadPrivateKey(IOUtils.toString(new FileInputStream(
            "/Users/Bing/Library/Containers/com.tencent.WeWorkMac/Data/Library/Application Support/WXWork/Data/1688854047526149/Cache/File/2019-03/privateKey.txt")));
    RSAPublicKey pub = RsaUtil.loadPublicKey(IOUtils.toString(new FileInputStream(
        "/Users/Bing/Library/Containers/com.tencent.WeWorkMac/Data/Library/Application Support/WXWork/Data/1688854047526149/Cache/File/2019-03/publicKey.txt")));

    String signature =
        "M9bMbJfl2l4eHeThqTW5cNl7uZrWKpgLKlxQx4rePEUp+UWvnW6fpKihKtWR3liB12phdaX2sQkRt/HYCn4cxlMkjr9lDgckQZ991FW5I+jSMCqPC5dnh5wcea1BqcpbYxxtPROF3BecGRxZDEC6ZJ4RN8WCcIoe5OyecbLVrvX/zT8wLUg0umnIlnkTUPy+i/5crJ6uFRg7abmSaDGE8l7t5Y63cB0E2886ADS+QG0xqxqKYyPoNgIL+wGNVYHMjqIeiwOYqmjQ3VUnZuKnmugDjZGtuCnp+o6wrg3f0KOwvEH97PBOBzyTWXAhfDPeXMNIcn5o96PSO7IrJfQ7aw==";

    String plainText = "name=XXX&certNo=XXX";
    String sign2 = RsaUtil.sign(plainText, priv);
    boolean verify = RsaUtil.verify(plainText, sign2, pub);
    System.out.println(verify);
    System.out.println(sign2);
    writePemFile(pub, "RSA PUBLIC KEY", "id_rsa.pub");


  }

  private static KeyPair generateRSAKeyPair()
      throws NoSuchAlgorithmException, NoSuchProviderException {
    KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", "BC");
    generator.initialize(KEY_SIZE);

    KeyPair keyPair = generator.generateKeyPair();
    LOGGER.info("RSA key pair generated.");
    return keyPair;
  }

  private static void writePemFile(Key key, String description, String filename)
      throws FileNotFoundException, IOException {
    PemFile pemFile = new PemFile(key, description);
    pemFile.write(filename);

    LOGGER.info(String.format("%s successfully writen in file %s.", description, filename));
  }

}

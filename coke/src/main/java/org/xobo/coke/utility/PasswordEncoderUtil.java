package org.xobo.coke.utility;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

public class PasswordEncoderUtil {
  static Map<String, PasswordEncoder> encoderMap = new LinkedHashMap<String, PasswordEncoder>();
  static {
    encoderMap.put("Bcrypt  ", new BCryptPasswordEncoder());
    encoderMap.put("Standard", new StandardPasswordEncoder());

  }

  public static void encrypt(String plain) {
    System.out.println("Encode: " + plain);
    for (Entry<String, PasswordEncoder> entry : encoderMap.entrySet()) {
      System.out.println(entry.getKey() + "\t" + entry.getValue().encode(plain));
    }
  }

  public static void main(String[] args) {
    encrypt("hrofirst2015");
  }

}

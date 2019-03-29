package org.xobo.coke.service;

/**
 * @author Bing
 * 
 */
public interface ObjectSerializeService {
  public static final String BEAN_ID = "ck.objectSerializeService";

  byte[] serialize(String type, Object obj);

  <T> T deserialize(String type, byte[] data, Class<T> clazz);
}

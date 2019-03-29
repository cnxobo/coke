package org.xobo.coke.service;

/**
 * 对Java类做序列化
 * 
 * @author Bing
 * 
 */
public interface ObjectSerializerProvider {

  String getType();

  byte[] serialize(Object obj);

  <T> T deserialize(byte[] data, Class<T> clazz);

}

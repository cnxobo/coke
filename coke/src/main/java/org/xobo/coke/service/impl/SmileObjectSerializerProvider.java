package org.xobo.coke.service.impl;

import java.io.IOException;
import org.springframework.stereotype.Service;
import org.xobo.coke.service.ObjectSerializerProvider;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.smile.SmileFactory;

/**
 * @author Bing
 * 
 */
@Service("ck.smileObjectSerializerProvider")
public class SmileObjectSerializerProvider implements ObjectSerializerProvider {

  public static final String TYPE = "smile";

  /*
   * (non-Javadoc)
   * 
   * @see com.hrhelper.hro.framework.core.service.ObjectSerializerProvider#getType()
   */
  @Override
  public String getType() {
    return TYPE;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.hrhelper.hro.framework.core.service.ObjectSerializerProvider#serrialize(java.lang.Object)
   */
  @Override
  public byte[] serialize(Object obj) {
    SmileFactory f = new SmileFactory();
    ObjectMapper mapper = new ObjectMapper(f);
    try {
      return mapper.writeValueAsBytes(obj);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.hrhelper.hro.framework.core.service.ObjectSerializerProvider#deserialize(byte[],
   * java.lang.Class)
   */
  @Override
  public <T> T deserialize(byte[] data, Class<T> clazz) {
    SmileFactory f = new SmileFactory();
    ObjectMapper mapper = new ObjectMapper(f);
    try {
      return mapper.readValue(data, clazz);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}

package org.xobo.coke.service.impl;



import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xobo.coke.service.ObjectSerializeService;
import org.xobo.coke.service.ObjectSerializerProvider;


/**
 * 对象序列化.
 * 
 * @author Bing
 * 
 */
@Service(ObjectSerializeService.BEAN_ID)
public class ObjectSerializeServiceImpl implements ObjectSerializeService {

  /*
   * (non-Javadoc)
   * 
   * @see com.hrhelper.hro.framework.core.service.ObjectSerializeService#serialize(java.lang.String,
   * java.lang.Object)
   */
  @Override
  public byte[] serialize(String type, Object obj) {
    return findProvider(type).serialize(obj);
  }

  /**
   * 加载指定类型的序列化工具.
   * 
   * @param type 序列化工具类型
   * @return 对应的序列化工具
   */
  public ObjectSerializerProvider findProvider(String type) {
    ObjectSerializerProvider provider = providerMap.get(type);
    if (provider == null) {
      throw new RuntimeException("未知的类型" + type);
    }
    return provider;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.hrhelper.hro.framework.core.service.ObjectSerializeService#deserialize(java.lang.String,
   * byte[], java.lang.Class)
   */
  @Override
  public <T> T deserialize(String type, byte[] data, Class<T> clazz) {
    return findProvider(type).deserialize(data, clazz);
  }

  private Map<String, ObjectSerializerProvider> providerMap = new HashMap<>();

  /**
   * 加载系统中所有的序列化提供者.
   * 
   * @param objectSerializerProviders 序列化提供者
   */
  @Autowired
  public void setObjectSerializerProviders(
      Collection<ObjectSerializerProvider> objectSerializerProviders) {
    if (objectSerializerProviders == null || objectSerializerProviders.isEmpty()) {
      return;
    }

    for (ObjectSerializerProvider objectSerializerProvider : objectSerializerProviders) {
      providerMap.put(objectSerializerProvider.getType(), objectSerializerProvider);
    }
  }
}

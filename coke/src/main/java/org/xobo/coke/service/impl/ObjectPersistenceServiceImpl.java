package org.xobo.coke.service.impl;

import java.util.LinkedHashMap;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xobo.coke.dao.CokeHibernateL;
import org.xobo.coke.filestorage.domain.CokeBlob;
import org.xobo.coke.service.ObjectPersistenceService;
import org.xobo.coke.service.ObjectSerializeService;

/**
 * @author Bing
 * 
 */
@Service(ObjectPersistenceService.BEAN_ID)
@Transactional
public class ObjectPersistenceServiceImpl implements ObjectPersistenceService {

  @Resource(name = CokeHibernateL.BEAN_ID)
  private CokeHibernateL hdao;

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.hrhelper.hro.framework.core.service.HrObjectPersistenceService#insert(java.lang.Object)
   */
  @Override
  public CokeBlob insert(Object object) {
    return insert(defaultType, object);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.hrhelper.hro.framework.core.service.HrObjectPersistenceService#insert(java.lang.String,
   * java.lang.Object)
   */
  @Override
  public CokeBlob insert(String type, Object object) {
    byte[] data = objectSerializeService.serialize(type, object);
    CokeBlob blob = new CokeBlob(type, data, object.getClass().getName());
    hdao.insertEntity(blob);
    return blob;
  }

  @Value("${coke.defaultObjectSerializer}")
  private String defaultType;

  @Resource(name = ObjectSerializeService.BEAN_ID)
  private ObjectSerializeService objectSerializeService;

  /*
   * (non-Javadoc)
   * 
   * @see com.hrhelper.hro.framework.core.service.HrObjectPersistenceService#get(java.lang.Long)
   */
  @Override
  public Object get(Long id) {
    CokeBlob blob = (CokeBlob) hdao.getSession().get(CokeBlob.class, id);
    String className = blob.getClassName();
    Class<?> clazz;
    try {
      clazz = Class.forName(className);
    } catch (ClassNotFoundException e) {
      clazz = LinkedHashMap.class;
    }
    return objectSerializeService.deserialize(blob.getType(), blob.getData(), clazz);
  }
}

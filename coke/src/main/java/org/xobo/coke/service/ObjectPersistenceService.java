package org.xobo.coke.service;

import org.xobo.coke.filestorage.domain.CokeBlob;

/**
 * @author Bing
 * 
 */
public interface ObjectPersistenceService {
  public static final String BEAN_ID = "ck.objectPersistenceService";

  CokeBlob insert(Object object);

  CokeBlob insert(String type, Object object);

  Object get(Long id);

}

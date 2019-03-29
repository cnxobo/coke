package org.xobo.coke.filestorage.service;

import org.xobo.coke.coke.domain.CkException;

public interface ExceptionPersistService {

  public static final String BEAN_ID = "olaf.exceptionPersistService";

  Long persist(Throwable e);

  CkException loadException(Long id);
}

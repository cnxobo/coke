package org.xobo.coke.filestorage.service;

import org.xobo.coke.filestorage.domain.CokeBlob;

public interface CokeBlobService {
  CokeBlob put(byte[] data);

  CokeBlob get(Long id);

}

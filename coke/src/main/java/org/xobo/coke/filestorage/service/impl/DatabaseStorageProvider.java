package org.xobo.coke.filestorage.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.annotation.Resource;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xobo.coke.filestorage.domain.CokeBlob;
import org.xobo.coke.filestorage.service.CokeBlobService;
import org.xobo.coke.filestorage.service.FileStorageProvider;

@Service
public class DatabaseStorageProvider implements FileStorageProvider {

  public static final String ProviderType = "Database";


  @Override
  public String getType() {
    return ProviderType;
  }

  @Override
  public String put(InputStream inputStream) throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    IOUtils.copy(inputStream, baos);
    CokeBlob cokeBlob = cokeBlobService.put(baos.toByteArray());
    return cokeBlob.getId().toString();
  }

  @Override
  public String put(MultipartFile file)
      throws IllegalStateException, IOException {
    return put(file.getInputStream());
  }

  @Override
  public InputStream getInputStream(String relativePath) throws FileNotFoundException {
    InputStream inputStream = null;
    CokeBlob cokeBlob = cokeBlobService.get(Long.valueOf(relativePath));
    if (cokeBlob != null) {
      byte[] data = cokeBlob.getData();
      inputStream = new ByteArrayInputStream(data);
    } else {
      throw new FileNotFoundException("CokeBlob Reocrd not found " + relativePath);
    }
    return inputStream;
  }

  @Resource
  private CokeBlobService cokeBlobService;

}

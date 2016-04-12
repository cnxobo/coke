package org.xobo.coke.filestorage.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageProvider {

  String getType();

  String put(InputStream inputStream) throws IOException;

  String put(MultipartFile file) throws IllegalStateException, IOException;

  InputStream getInputStream(String relativePath) throws FileNotFoundException;
}

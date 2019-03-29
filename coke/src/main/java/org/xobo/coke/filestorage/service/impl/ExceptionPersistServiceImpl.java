package org.xobo.coke.filestorage.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.zip.GZIPOutputStream;
import javax.annotation.Resource;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;
import org.xobo.coke.coke.domain.CkException;
import org.xobo.coke.dao.CokeHibernate;
import org.xobo.coke.filestorage.service.ExceptionPersistService;
import org.xobo.coke.utility.ExceptionUtil;

@Service(ExceptionPersistService.BEAN_ID)
public class ExceptionPersistServiceImpl implements ExceptionPersistService {

  @Override
  public CkException loadException(Long id) {
    return (CkException) hdao.getSession().get(CkException.class, id);
  }

  @Override
  public Long persist(Throwable e) {
    String error = ExceptionUtil.stackTraceToString(e);
    CkException ckException = new CkException(compress(error));
    ckException.setCreateDate(new Date());
    hdao.getSession().save(ckException);
    return ckException.getId();
  }

  public byte[] compress(String str) {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try (GZIPOutputStream gzipOS = new GZIPOutputStream(baos)) {
      gzipOS.write(str.getBytes(StandardCharsets.UTF_8));
    } catch (IOException e) {
      throw new RuntimeException(e);
    } finally {
      IOUtils.closeQuietly(baos);
    }
    return baos.toByteArray();
  }

  @Resource(name = CokeHibernate.BEAN_ID)
  private CokeHibernate hdao;


}

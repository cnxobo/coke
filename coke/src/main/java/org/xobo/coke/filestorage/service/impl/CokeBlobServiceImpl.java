package org.xobo.coke.filestorage.service.impl;

import java.util.Date;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xobo.coke.dao.CokeHibernate;
import org.xobo.coke.filestorage.domain.CokeBlob;
import org.xobo.coke.filestorage.service.CokeBlobService;
import com.google.common.base.Charsets;

@Service
@Transactional
public class CokeBlobServiceImpl implements CokeBlobService {

  @Override
  public CokeBlob put(byte[] data) {
    return put(null, data);

  }

  public CokeBlob put(String type, byte[] data) {
    CokeBlob cokeBlob = new CokeBlob();
    cokeBlob.setData(data);
    cokeBlob.setType(type);
    cokeBlob.setCreateTime(new Date());
    cokeHibernate.getSession().save(cokeBlob);
    return cokeBlob;

  }

  @Override
  public CokeBlob get(Long id) {
    return (CokeBlob) cokeHibernate.getSession().get(CokeBlob.class, id);
  }


  @Resource(name = CokeHibernate.BEAN_ID)
  private CokeHibernate cokeHibernate;


  @Override
  public String getContent(Long id) {
    CokeBlob cokeBlob = get(id);
    if (cokeBlob == null) {
      return null;
    }
    return new String(cokeBlob.getData(), Charsets.UTF_8);
  }

  @Override
  public Long putContent(String data) {
    CokeBlob cokeBlob = put("String", data.getBytes(Charsets.UTF_8));
    return cokeBlob.getId();
  }

  @Override
  public CokeBlob update(Long id, String data) {
    CokeBlob cokeBlob = get(id);
    if (cokeBlob == null) {
      return null;
    }
    cokeBlob.setData(data.getBytes(Charsets.UTF_8));
    cokeBlob.setUpdateDate(new Date());

    return cokeBlob;
  }
}

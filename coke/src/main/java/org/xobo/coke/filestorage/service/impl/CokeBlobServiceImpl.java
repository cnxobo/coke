package org.xobo.coke.filestorage.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xobo.coke.dao.CokeHibernate;
import org.xobo.coke.filestorage.domain.CokeBlob;
import org.xobo.coke.filestorage.service.CokeBlobService;

@Service
@Transactional
public class CokeBlobServiceImpl implements CokeBlobService {

  @Override
  public CokeBlob put(byte[] data) {
    CokeBlob cokeBlob = new CokeBlob();
    cokeBlob.setData(data);
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
}

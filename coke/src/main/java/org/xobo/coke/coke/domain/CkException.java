package org.xobo.coke.coke.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity(name = "CK_EXCEPTION")
public class CkException {
  private Long id;
  private String exception;
  // 压缩后的异常信息
  private byte[] compressInfo;
  private Date createDate;

  public CkException() {

  }

  public CkException(String exception) {
    this.exception = exception;
  }

  public CkException(byte[] compressInfo) {
    this.compressInfo = compressInfo;
  }

  @Id
  @GeneratedValue
  @Column(name = "ID")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "EXCEPTION")
  @Lob
  public String getException() {
    return exception;
  }

  public void setException(String exception) {
    this.exception = exception;
  }

  @Column(name = "CREATE_DATE")
  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  @Lob
  @Column(name = "COMPRESS_INFO")
  public byte[] getCompressInfo() {
    return compressInfo;
  }

  public void setCompressInfo(byte[] compressInfo) {
    this.compressInfo = compressInfo;
  }

}

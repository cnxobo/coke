package org.xobo.coke.filestorage.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import org.xobo.coke.model.BaseModel;

@Entity(name = "CK_BLOB")
public class CokeBlob extends BaseModel {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  private byte[] data;
  private Date createTime;
  private String className;
  private String type;

  public CokeBlob() {}

  public CokeBlob(String type, byte[] data) {
    this.type = type;
    this.data = data;
  }

  public CokeBlob(String type, byte[] data, String className) {
    this.type = type;
    this.data = data;
    this.className = className;
  }

  @Lob
  @Column(name = "DATA")
  public byte[] getData() {
    return data;
  }

  public void setData(byte[] data) {
    this.data = data;
  }

  @Column(name = "TYPE")
  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  @Column(name = "CREATE_TIME")
  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }


  @Column(name = "CLASS_NAME")
  public String getClassName() {
    return className;
  }

  public void setClassName(String className) {
    this.className = className;
  }

}

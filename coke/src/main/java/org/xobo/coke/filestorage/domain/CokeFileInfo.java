package org.xobo.coke.filestorage.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;
import org.xobo.coke.model.BaseModel;

@Entity(name = "CK_FILE_INFO")
public class CokeFileInfo extends BaseModel {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  private String fileNo;
  private String filename;
  private String fileStorageType;
  private String relativePath;
  private Date createTime;
  private String createBy;
  private String absolutePath;

  @Column(name = "FILE_NO", unique = true)
  public String getFileNo() {
    return fileNo;
  }

  public void setFileNo(String fileNo) {
    this.fileNo = fileNo;
  }

  @Column(name = "FILENAME")
  public String getFilename() {
    return filename;
  }

  public void setFilename(String filename) {
    this.filename = filename;
  }

  @Column(name = "FILESTORAGE_TYPE")
  public String getFileStorageType() {
    return fileStorageType;
  }

  public void setFileStorageType(String fileStorageType) {
    this.fileStorageType = fileStorageType;
  }

  @Column(name = "RELATIVE_PATH")
  public String getRelativePath() {
    return relativePath;
  }

  public void setRelativePath(String relativePath) {
    this.relativePath = relativePath;
  }

  @Column(name = "CREATE_TIME")
  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  @Column(name = "CREATE_BY")
  public String getCreateBy() {
    return createBy;
  }

  public void setCreateBy(String createBy) {
    this.createBy = createBy;
  }

  @Transient
  public String getAbsolutePath() {
    return absolutePath;
  }

  public void setAbsolutePath(String absolutePath) {
    this.absolutePath = absolutePath;
  }

}

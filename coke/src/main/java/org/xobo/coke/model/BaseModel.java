package org.xobo.coke.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import org.hibernate.annotations.GenericGenerator;
import com.bstek.dorado.annotation.PropertyDef;

@MappedSuperclass
public abstract class BaseModel implements IBase<Long>, Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  @PropertyDef(label = "序号")
  private Long id;
  @PropertyDef(label = "创建人")
  private String createUser;
  @PropertyDef(label = "创建日期")
  private Date createDate;
  @PropertyDef(label = "更新人")
  private String updateUser;
  @PropertyDef(label = "更新日期")
  private Date updateDate;
  @PropertyDef(label = "有效")
  private Boolean deleted = false;

  @PropertyDef(label = "创建人")
  private String createUserCname;
  @PropertyDef(label = "更新人")
  private String updateUserCname;

  public BaseModel() {}

  public BaseModel(Long id) {
    this.id = id;
  }

  @Id
  @GenericGenerator(
      name = "sequenceGenerator",
      strategy = "enhanced-sequence",
      parameters = {
          @org.hibernate.annotations.Parameter(
              name = "optimizer",
              value = "pooled"),
          // @org.hibernate.annotations.Parameter(
          // name = "prefer_sequence_per_entity",
          // value = "true"),
          @org.hibernate.annotations.Parameter(
              name = "initial_value",
              value = "1"),
          @org.hibernate.annotations.Parameter(
              name = "increment_size",
              value = "5")
      })
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "sequenceGenerator")
  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getCreateUser() {
    return createUser;
  }


  public void setCreateUser(String createUser) {
    this.createUser = createUser;
  }


  public Date getCreateDate() {
    return createDate;
  }


  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }


  public String getUpdateUser() {
    return updateUser;
  }


  public void setUpdateUser(String updateUser) {
    this.updateUser = updateUser;
  }


  public Date getUpdateDate() {
    return updateDate;
  }


  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }


  public Boolean getDeleted() {
    return deleted;
  }


  public void setDeleted(Boolean deleted) {
    this.deleted = deleted;
  }

  @Transient
  public String getCreateUserCname() {
    return createUserCname;
  }

  public void setCreateUserCname(String createUserCname) {
    this.createUserCname = createUserCname;
  }

  @Transient
  public String getUpdateUserCname() {

    return updateUserCname;
  }

  public void setUpdateUserCname(String updateUserCname) {
    this.updateUserCname = updateUserCname;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    BaseModel other = (BaseModel) obj;
    if (id == null) {
      if (other.id != null) {
        return false;
      }
    } else if (!id.equals(other.id)) {
      return false;
    }
    return true;
  }
}

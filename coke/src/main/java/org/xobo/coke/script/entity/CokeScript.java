package org.xobo.coke.script.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import org.xobo.coke.model.BaseModel;
import com.bstek.dorado.annotation.PropertyDef;

@Entity(name = "CK_SCRIPT")
public class CokeScript extends BaseModel {

  /**
   * 
   */
  private static final long serialVersionUID = 2375011398296635129L;

  @PropertyDef(label = "引擎")
  private String engine;
  @PropertyDef(label = "编码")
  private String code;
  @PropertyDef(label = "名称")
  private String name;
  @PropertyDef(label = "描述")
  private String description;
  @PropertyDef(label = "备注")
  private String remark;
  private Long contentId;



  @Column(name = "CODE")
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  @Column(name = "NAME")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Column(name = "DESCRIPTION")

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Column(name = "REMARK")
  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  @Column(name = "CONTENT_ID")
  public Long getContentId() {
    return contentId;
  }

  public void setContentId(Long contentId) {
    this.contentId = contentId;
  }

  @Column(name = "ENGINE")
  public String getEngine() {
    return engine;
  }

  public void setEngine(String engine) {
    this.engine = engine;
  }



}

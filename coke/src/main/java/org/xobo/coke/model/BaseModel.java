package org.xobo.coke.model;

import java.util.Date;

import javax.persistence.MappedSuperclass;

import com.bstek.dorado.annotation.PropertyDef;

@MappedSuperclass
public abstract class BaseModel<K> extends SuperBaseModel<K> {
	@PropertyDef(label = "创建人")
	private String createUser;
	@PropertyDef(label = "创建日期")
	private Date createDate;
	@PropertyDef(label = "更新人")
	private String updateUser;
	@PropertyDef(label = "更新日期")
	private Date updateDate;

	@PropertyDef(label = "有效")
	private Boolean deleted;

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

}

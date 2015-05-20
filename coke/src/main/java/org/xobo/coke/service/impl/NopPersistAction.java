package org.xobo.coke.service.impl;

import org.hibernate.Session;
import org.xobo.coke.model.BaseModel;
import org.xobo.coke.service.PersistAction;

public class NopPersistAction implements PersistAction<Object> {

	@Override
	public void afterCreate(Session session, BaseModel<Object> entity, BaseModel<Object> parent) {
	}

	@Override
	public void beforeUpdate(Session session, BaseModel<Object> entity, BaseModel<Object> parent) {
	}

	@Override
	public void beforeDelete(Session session, BaseModel<Object> entity, BaseModel<Object> parent) {
	}

	@Override
	public void beforeCreate(Session session, BaseModel<Object> entity, BaseModel<Object> parent) {
		// TODO Auto-generated method stub

	}

}

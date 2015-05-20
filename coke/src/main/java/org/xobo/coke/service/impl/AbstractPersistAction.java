package org.xobo.coke.service.impl;

import org.hibernate.Session;
import org.xobo.coke.model.BaseModel;
import org.xobo.coke.service.PersistAction;

public class AbstractPersistAction<T> implements PersistAction<T> {

	@Override
	public void beforeCreate(Session session, BaseModel<T> entity, BaseModel<T> parent) {

	}

	@Override
	public void afterCreate(Session session, BaseModel<T> entity, BaseModel<T> parent) {

	}

	@Override
	public void beforeUpdate(Session session, BaseModel<T> entity, BaseModel<T> parent) {

	}

	@Override
	public void beforeDelete(Session session, BaseModel<T> entity, BaseModel<T> parent) {

	}

}

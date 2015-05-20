package org.xobo.coke.service;

import org.hibernate.Session;
import org.xobo.coke.model.BaseModel;

public interface PersistAction<T> {
	void beforeCreate(Session session, BaseModel<T> entity, BaseModel<T> parent);

	void afterCreate(Session session, BaseModel<T> entity, BaseModel<T> parent);

	void beforeUpdate(Session session, BaseModel<T> entity, BaseModel<T> parent);

	void beforeDelete(Session session, BaseModel<T> entity, BaseModel<T> parent);
}

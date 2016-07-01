package org.xobo.coke.service.impl;

import org.hibernate.Session;
import org.xobo.coke.model.IBase;
import org.xobo.coke.service.PersistAction;

public class AbstractPersistAction<T> implements PersistAction<T> {

  @Override
  public void beforeCreate(Session session, IBase<T> entity, IBase<T> parent) {

  }

  @Override
  public void afterCreate(Session session, IBase<T> entity, IBase<T> parent) {

  }

  @Override
  public void beforeUpdate(Session session, IBase<T> entity, IBase<T> parent) {

  }

  @Override
  public void beforeDelete(Session session, IBase<T> entity, IBase<T> parent) {

  }

}

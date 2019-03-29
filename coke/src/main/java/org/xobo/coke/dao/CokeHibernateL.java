package org.xobo.coke.dao;

import org.springframework.stereotype.Repository;

@Repository(CokeHibernateL.BEAN_ID)
public class CokeHibernateL extends HibernateSupportDao<Long> {
  public static final String BEAN_ID = "coke.hibernateL";
}

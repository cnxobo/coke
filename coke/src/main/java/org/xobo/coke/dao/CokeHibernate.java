package org.xobo.coke.dao;

import org.springframework.stereotype.Repository;

@Repository(CokeHibernate.BEAN_ID)
public class CokeHibernate extends HibernateSupportDao<String> {
  public static final String BEAN_ID = "coke.hibernate";
}

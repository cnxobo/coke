package org.xobo.coke.querysupporter.service;

import java.util.Map;

import org.xobo.coke.querysupporter.model.QueryResolver;

import com.bstek.dorado.data.provider.Criteria;

public interface SqlBuilder {
  public static final String BEAN_ID = "coke.sqlBuilder";

  QueryResolver extractQuery(Class<?> clazz, Criteria criteria, Map<String, Object> queryParameter,
      String alias);

  QueryResolver extractCriteria(Class<?> clazz, Criteria criteria, String alias);

}

package org.xobo.coke.service;

import java.util.Map;

import org.xobo.coke.dao.model.QueryResolver;

import com.bstek.dorado.data.provider.Criteria;

public interface QueryDcriteriaToSql {
	public static final String BEAN_ID = "coke.dcriteriaToSql";

	QueryResolver extractQuery(Class<?> clazz, Criteria criteria, Map<String, Object> queryParameter, String alias);

	QueryResolver extractCriteria(Class<?> clazz, Criteria criteria, String alias);

}

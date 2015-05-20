package org.xobo.coke.service;

import java.util.Map;

import org.xobo.coke.entity.PropertyWrapper;

public interface QueryPropertyWrapperService {
	static final String BEAN_ID = "coke.queryPropertyWrapperLoaderService";
	public static final String PropertyOperatorSpliter = "_";

	PropertyWrapper find(Class<?> clazz, String property, Map<String, PropertyWrapper> propertyWrapperMap);
}

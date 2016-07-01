package org.xobo.coke.querysupporter.service;

import java.util.Map;

import org.xobo.coke.querysupporter.model.PropertyWrapper;

public interface QueryPropertyWrapperService {
  static final String BEAN_ID = "coke.queryPropertyWrapperLoaderService";
  public static final String PROPERTY_OPERATOR_SPLITER = "@";

  PropertyWrapper find(Class<?> clazz, String property,
      Map<String, PropertyWrapper> propertyWrapperMap);
}

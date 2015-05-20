package org.xobo.coke.service.impl;

import java.lang.reflect.Field;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xobo.coke.dataType.MapMap;
import org.xobo.coke.entity.PropertyWrapper;
import org.xobo.coke.model.UnkonwDataType;
import org.xobo.coke.service.QueryPropertyWrapperService;
import org.xobo.coke.service.ReflectionRegister;

import com.bstek.dorado.data.provider.filter.FilterOperator;
import com.bstek.dorado.data.type.DataType;
import com.bstek.dorado.data.type.manager.DataTypeManager;

@Service(QueryPropertyWrapperService.BEAN_ID)
public class QueryPreLoadPropertyWrapperServiceImpl implements QueryPropertyWrapperService, ReflectionRegister {

	private static final Logger logger = LoggerFactory.getLogger(HibernateEntityEnhancerImpl.class);

	private static MapMap<Class<?>, String, PropertyWrapper> golbalPropertyWrapperMap = MapMap.concurrentHashMap();

	@Autowired
	private DataTypeManager dataTypeManager;

	@Override
	public PropertyWrapper find(Class<?> clazz, String property, Map<String, PropertyWrapper> propertyWrapperMap) {

		PropertyWrapper propertyWrapper = null;
		if (propertyWrapperMap != null) {
			propertyWrapper = propertyWrapperMap.get(property);
		}

		if (propertyWrapper == null) {
			propertyWrapper = golbalPropertyWrapperMap.get(clazz, property);
		}

		if (propertyWrapper != null) {
			DataType dataType = propertyWrapper.getDataType();
			if (dataType == null) {
				dataType = extractPropertyType(propertyWrapper.getType(), propertyWrapper.getProperty());
				if (dataType == null) {
					dataType = new UnkonwDataType();
				}
				propertyWrapper.setDataType(dataType);
			}
		}
		return propertyWrapper;
	}

	private FilterOperator getFilterOperator(String operator) {
		if ("eq".equals(operator)) {
			return FilterOperator.eq;
		} else if ("ge".equals(operator)) {
			return FilterOperator.ge;
		} else if ("gt".equals(operator)) {
			return FilterOperator.gt;
		} else if ("in".equals(operator)) {
			return FilterOperator.in;
		} else if ("le".equals(operator)) {
			return FilterOperator.le;
		} else if ("like".equals(operator)) {
			return FilterOperator.like;
		} else if ("likeEnd".equals(operator)) {
			return FilterOperator.likeEnd;
		} else if ("lt".equals(operator)) {
			return FilterOperator.lt;
		} else if ("ne".equals(operator)) {
			return FilterOperator.ne;
		} else if ("between".equals(operator)) {
			return FilterOperator.between;
		} else {
			return FilterOperator.eq;
		}
	}

	private DataType extractPropertyType(Class<?> type, String property) {
		DataType propertyDataType = null;
		try {
			propertyDataType = dataTypeManager.getDataType(type);
		} catch (Exception e) {
			logger.info("Could not find datatype for {}", type);
		}
		return propertyDataType;
	}

	public static final String[] initOperator = new String[] { /* "eq", */"ge", "gt", "in", "le", "like", "likeEnd",
			"lt", "ne", "between" };

	@Override
	public void register(Class<?> clazz, Field field) {
		Map<String, PropertyWrapper> classPropertyWrapper = golbalPropertyWrapperMap.get(clazz);

		Class<?> type = field.getType();
		String property = field.getName();
		if (!type.equals(Object.class)) {
			classPropertyWrapper.put(property, new PropertyWrapper(property, type, FilterOperator.eq));
			for (String operator : initOperator) {
				String newProperty = property + QueryPropertyWrapperService.PropertyOperatorSpliter + operator;
				classPropertyWrapper.put(newProperty, new PropertyWrapper(property, type, getFilterOperator(operator)));
			}
		}

	}
}

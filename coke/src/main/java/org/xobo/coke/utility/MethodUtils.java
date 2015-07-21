package org.xobo.coke.utility;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.springframework.util.ReflectionUtils;

import com.thoughtworks.paranamer.BytecodeReadingParanamer;
import com.thoughtworks.paranamer.CachingParanamer;
import com.thoughtworks.paranamer.Paranamer;

public class MethodUtils {
	private static Paranamer paranamer = new CachingParanamer(new BytecodeReadingParanamer());

	private static Map<Method, String[]> parameterNamesMap = new ConcurrentHashMap<Method, String[]>();

	public static Object invokeMethod(Object target, String methodName, Map<String, Object> parameter)
			throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, SecurityException,
			NoSuchMethodException, InstantiationException {
		Class<?>[] paramTypes = null;
		Method method = ReflectionUtils.findMethod(BeanReflectionUtils.getClass(target), methodName,
				paramTypes);
		return invokeMethod(target, method, parameter);
	}

	@SuppressWarnings("unchecked")
	public static Object invokeMethod(Object target, Method method, Map<String, Object> parameter)
			throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, InstantiationException {
		String[] parameterNames = parameterNamesMap.get(method);
		if (parameterNames == null) {
			parameterNames = paranamer.lookupParameterNames(method);
			if (parameterNames == null) {
				parameterNames = new String[] {};
			}
			parameterNamesMap.put(method, parameterNames);
		}
		Type[] parametersType = method.getGenericParameterTypes();
		Object[] realArgs = new Object[parametersType.length];
		for (int i = 0; i < parameterNames.length; i++) {
			String name = parameterNames[i];
			Object value;
			if (parameterNames.length == 1) {
				value = parameter;
			} else {
				value = parameter.get(name);
			}
			if (value != null) {
				Type ptype = parametersType[i];
				if (ptype instanceof Class<?>) {
					Class<?> type = (Class<?>) ptype;
					if (!type.isAssignableFrom(value.getClass())) {
						value = ConvertUtils.convert(value, type);
					}
					if (!type.isPrimitive() && !type.isAssignableFrom(value.getClass())
							&& !type.getPackage().getName().startsWith("java") && value instanceof Map) {
						Object instance = type.newInstance();
						BeanUtils.populate(instance, (Map<String, ? extends Object>) value);
						value = instance;
					}
					if (Collection.class.isAssignableFrom(value.getClass())) {
						Collection<Object> newValues = new ArrayList<Object>();
						for (Object object : (Collection<Object>) value) {
							if (object instanceof Map) {
								// Object entity = clazzType.newInstance();
								// BeanUtils.populate(entity, (Map<String, ? extends Object>) object);
								// newValues.add(entity);
							}
						}
						value = newValues;
					}
				} else if (ptype instanceof ParameterizedType) {
					ParameterizedType parameterizedType = (ParameterizedType) ptype;
					Type[] typeArguments = parameterizedType.getActualTypeArguments();
					Type rawType = parameterizedType.getRawType();
					Collection<Object> collection = new ArrayList<Object>();
					if (rawType instanceof Class<?> && Collection.class.isAssignableFrom((Class<?>) rawType)) {
						if (typeArguments.length > 0) {
							Type typeArgument = typeArguments[0];
							Class<?> rt = (Class<?>) typeArgument;

							for (Object object : (Collection<Object>) value) {
								Object entity = rt.newInstance();
								BeanUtils.populate(entity, (Map<String, ? extends Object>) object);
								collection.add(entity);
							}
						}
						value = collection;
					}
				}

			}
			realArgs[i] = value;
		}
		return method.invoke(target, realArgs);
	}
}

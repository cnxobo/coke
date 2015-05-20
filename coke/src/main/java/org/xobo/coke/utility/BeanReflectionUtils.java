package org.xobo.coke.utility;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import javassist.util.proxy.ProxyObject;

public class BeanReflectionUtils {

	public static Collection<Field> loadClassFields(Class<?> clazz) {
		Collection<Field> fields = new ArrayList<Field>();
		Class<?> currentClazz = clazz;
		while (!currentClazz.equals(Object.class)) {
			fields.addAll(Arrays.asList(currentClazz.getDeclaredFields()));
			currentClazz = currentClazz.getSuperclass();
		}
		return fields;
	}

	public static Class<?> getClass(Object instance) {
		Class<?> clazz = instance.getClass();
		if (instance instanceof ProxyObject) {
			clazz = clazz.getSuperclass();
		}
		return clazz;
	}
}

package org.xobo.coke.querysupporter.service;

import java.lang.reflect.Field;

/**
 * @author bing
 * 
 */
public interface ReflectionRegister {
	void register(Class<?> clazz, Field field);
}

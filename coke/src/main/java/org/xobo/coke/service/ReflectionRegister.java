package org.xobo.coke.service;

import java.lang.reflect.Field;

public interface ReflectionRegister {
	void register(Class<?> clazz, Field field);
}

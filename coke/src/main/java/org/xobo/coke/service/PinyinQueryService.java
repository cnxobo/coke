package org.xobo.coke.service;

import java.util.Collection;

public interface PinyinQueryService {
	static final String BEAN_ID = "coke.pinyinQueryService";

	Collection<String> get(Class<?> clazz, String property);
}

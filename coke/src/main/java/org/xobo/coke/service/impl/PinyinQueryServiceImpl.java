package org.xobo.coke.service.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.xobo.coke.annotation.Pinyin;
import org.xobo.coke.dataType.MapMap;
import org.xobo.coke.service.PinyinQueryService;
import org.xobo.coke.service.ReflectionRegister;

@Service(PinyinQueryService.BEAN_ID)
public class PinyinQueryServiceImpl implements PinyinQueryService, ReflectionRegister {
	private static final MapMap<Class<?>, String, Collection<String>> pinyinMap = MapMap.concurrentHashMap();

	@Override
	public Collection<String> get(Class<?> clazz, String property) {
		Collection<String> unionPropertyList = pinyinMap.get(clazz, property);

		if (unionPropertyList == null) {
			return Collections.emptyList();
		} else {
			return unionPropertyList;
		}
	}

	@Override
	public void register(Class<?> clazz, Field field) {
		Pinyin pinyin = field.getAnnotation(Pinyin.class);
		if (pinyin != null) {
			String property = field.getName();
			Collection<String> unionPropertyList = pinyinMap.get(clazz, property);
			if (unionPropertyList == null) {
				unionPropertyList = new ArrayList<String>();
				pinyinMap.add(clazz, property, unionPropertyList);
			}
			String quan = StringUtils.isEmpty(pinyin.quan()) ? "Quanpin" : pinyin.quan();
			String jian = StringUtils.isEmpty(pinyin.quan()) ? "Jianpin" : pinyin.jian();
			unionPropertyList.add(property + quan);
			unionPropertyList.add(property + jian);
			unionPropertyList.add(property);
		}

	}

	public static MapMap<Class<?>, String, Collection<String>> getPinyinmap() {
		return pinyinMap;
	}

}

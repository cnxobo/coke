package org.xobo.coke.service;

import java.util.Collection;

import org.xobo.coke.model.DictEntry;

public interface DictService {
	static final String BEAN_ID = "coke.dictService";

	/**
	 * @param type
	 *            匹配DictEntriesProvider实现类getType
	 * @param categorykey
	 *            字典分类
	 * @param extraTypes
	 *            其他额外参数
	 * @return
	 */
	Collection<DictEntry> lookup(String type, Object categorykey, Object... extraTypes);

	Object define(String register, Object entryKey, Object categorykey, Object... extraTypes);

	void removeCache(Object... types);
}

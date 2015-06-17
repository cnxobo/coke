package org.xobo.coke.model;

public interface DetailModel<K> extends IDetail<K> {

	K getRoot();

	@Override
	K getParentId();

	@Override
	void setParentId(K pid);
}

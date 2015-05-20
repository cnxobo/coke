package org.xobo.coke.model;

public interface DetailModel<K> {

	K getRoot();

	K getParentId();

	void setParentId(K pid);
}

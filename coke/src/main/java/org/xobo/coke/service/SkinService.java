package org.xobo.coke.service;

import java.util.Collection;

import org.xobo.coke.model.SkinInfo;

public interface SkinService {
	public static final String BEAN_ID = "coke.skinService";

	Collection<SkinInfo> loadSkinInfos();

	void changeSkin(String skinName);

	void setDefaultSkin();
}

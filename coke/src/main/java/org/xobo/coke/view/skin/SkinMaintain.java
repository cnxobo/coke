package org.xobo.coke.view.skin;

import java.util.Collection;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.xobo.coke.model.SkinInfo;
import org.xobo.coke.service.SkinService;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.Expose;

@Controller("coke.skinMaintain")
public class SkinMaintain {
	@Resource(name = SkinService.BEAN_ID)
	private SkinService skinService;

	public void initSkin() {
		skinService.setDefaultSkin();
	}

	@DataProvider
	public Collection<SkinInfo> loadSkinInfos() {
		return skinService.loadSkinInfos();
	}

	@Expose
	public void changeSkin(String skinName) {
		skinService.changeSkin(skinName);
	}
}

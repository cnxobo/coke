package org.xobo.coke.service.impl;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.xobo.coke.model.SkinInfo;
import org.xobo.coke.service.SkinPersister;
import org.xobo.coke.service.SkinService;

import com.bstek.dorado.web.DoradoContext;
import com.bstek.dorado.web.WebConfigure;

@Service(SkinService.BEAN_ID)
public class SkinServiceImpl implements SkinService {

	@Autowired(required = false)
	private SkinPersister skinPersister;

	private Collection<SkinInfo> skinInfos;

	@Override
	public Collection<SkinInfo> loadSkinInfos() {
		return skinInfos;
	}

	@Override
	public void setDefaultSkin() {
		if (skinPersister != null) {
			changeSkin(skinPersister.getSkinName());
		}
	}

	@Override
	public void changeSkin(String skinName) {
		if (StringUtils.hasText(skinName)) {
			String oldSkinName = WebConfigure.getString("view.skin");
			if (!skinName.equals(oldSkinName)) {
				WebConfigure.set(DoradoContext.SESSION, "view.skin", skinName);
				if (skinPersister != null) {
					if (!skinName.equals(skinPersister.getSkinName())) {
						skinPersister.persistSkin(skinName);
					}
				}
			}
		}
	}

	public static final String Skin = "skin";
	public static final String skinPath = "/dorado/skins/";
	public static final String targetFile = "/core.css";
	public static final String ie6 = ".ie6";

	final String path = "sample/folder";
	final File jarFile = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath());

	private Collection<String> loadSkinsByPackage() {
		Collection<String> skinNames = new ArrayList<String>();
		PathMatchingResourcePatternResolver resovler = new PathMatchingResourcePatternResolver();
		Resource[] resources;
		try {
			resources = resovler.getResources("classpath*:dorado/skins/*/core.css");
			for (Resource resource : resources) {
				String path = resource.getURL().getPath();
				if (StringUtils.hasText(path)) {
					int startIndex = path.lastIndexOf(skinPath);
					if (startIndex > 0) {
						startIndex = startIndex + skinPath.length();
						String skinName = path.substring(startIndex, path.length() - targetFile.length());
						if (StringUtils.hasText(skinName) && !skinName.endsWith(ie6)) {
							skinNames.add(skinName);
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return skinNames;
	}

	public static void main(String[] args) throws IOException, URISyntaxException {

		new SkinServiceImpl().loadSkinsByPackage();
	}

	public Collection<SkinInfo> getSkinInfos() {
		return skinInfos;
	}

	@Autowired
	public void setSkinInfos(Collection<SkinInfo> skinInfos) {
		Map<String, SkinInfo> skinInfoMap = new HashMap<String, SkinInfo>();
		if (skinInfos != null) {
			for (SkinInfo skinInfo : skinInfos) {
				skinInfoMap.put(skinInfo.getName(), skinInfo);
			}
		}

		Collection<String> skinNames = loadSkinsByPackage();
		for (String skinName : skinNames) {
			SkinInfo skinInfo = skinInfoMap.get(skinName);
			if (skinInfo == null) {
				skinInfo = new SkinInfo(skinName);
				skinInfoMap.put(skinName, skinInfo);
			}
		}
		this.skinInfos = new ArrayList<SkinInfo>(skinInfoMap.values());

	}
}

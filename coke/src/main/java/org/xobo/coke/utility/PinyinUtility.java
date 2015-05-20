package org.xobo.coke.utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import org.springframework.util.StringUtils;
import org.xobo.coke.model.Pinyin;

public class PinyinUtility {
	private static HanyuPinyinOutputFormat pyFormat = new HanyuPinyinOutputFormat();
	static {
		pyFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		pyFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
	}

	public static Collection<Pinyin> toPinyin(String hanzi) {
		if (!StringUtils.hasText(hanzi)) {
			return Collections.emptyList();
		}

		Collection<Collection<String>> pinYinTokenList = new ArrayList<Collection<String>>();
		for (int i = 0; i < hanzi.length(); i++) {
			String[] pinyins = null;
			try {
				pinyins = PinyinHelper.toHanyuPinyinStringArray(hanzi.charAt(i), pyFormat);
				if (pinyins == null) {
					continue;
				}
				pinYinTokenList.add(new HashSet<String>(Arrays.asList(pinyins)));
			} catch (BadHanyuPinyinOutputFormatCombination e) {
				e.printStackTrace();
			}
		}
		List<Pinyin> pinyinList = new ArrayList<Pinyin>();
		for (Collection<String> singlePinYinTokenList : pinYinTokenList) {
			if (pinyinList.size() == 0) {
				for (String pinyin : singlePinYinTokenList) {
					pinyinList.add(new Pinyin(pinyin));
				}
			} else {
				List<Pinyin> newPinYinList = new ArrayList<Pinyin>();
				for (Pinyin r : pinyinList) {
					for (String pinyin : singlePinYinTokenList) {
						newPinYinList.add(r.append(pinyin));
					}
				}
				pinyinList = newPinYinList;
			}
		}
		return pinyinList;
	}

	public static void main(String[] args) {
		toPinyin("茜乐的");
		toPinyin("茜茜");
		toPinyin("周兵");
		toPinyin("的士");
	}
}

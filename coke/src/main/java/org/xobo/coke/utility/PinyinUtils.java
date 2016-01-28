package org.xobo.coke.utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.springframework.util.StringUtils;
import org.xobo.coke.model.Pinyin;

import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;

public class PinyinUtils {

  public static Collection<Pinyin> toPinyin(String hanzi) {
    if (!StringUtils.hasText(hanzi)) {
      return Collections.emptyList();
    }
    

    Collection<Collection<String>> pinYinTokenList = new ArrayList<Collection<String>>();
    for (int i = 0; i < hanzi.length(); i++) {
      String[] pinyins = null;
      pinyins = PinyinHelper.convertToPinyinArray(hanzi.charAt(i), PinyinFormat.WITHOUT_TONE);
      if (pinyins == null) {
        continue;
      }
      pinYinTokenList.add(new HashSet<String>(Arrays.asList(pinyins)));
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
    System.out.println(pinyinList);
    return pinyinList;
  }

  public static void main(String[] args) {
    toPinyin("茜乐的");
    toPinyin("茜茜");
    toPinyin("周兵");
    toPinyin("的士");
  }
}

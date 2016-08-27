package org.xobo.coke.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xobo.coke.dataType.ListMap;
import org.xobo.coke.model.DictEntry;
import org.xobo.coke.service.DictEntriesProvider;
import org.xobo.coke.service.DictService;

import com.bstek.bdf2.core.cache.ApplicationCache;

@Service(DictService.BEAN_ID)
public class DictServiceImpl implements DictService {

  private Map<String, DictEntriesProvider> dictItemsServiceMap =
      new HashMap<String, DictEntriesProvider>();

  @Autowired(required = false)
  public void setDictItemsServices(Collection<DictEntriesProvider> dictItemsServices) {
    if (dictItemsServices != null) {
      for (DictEntriesProvider dictItemsRegister : dictItemsServices) {
        String type = dictItemsRegister.getType();
        if (StringUtils.isNotEmpty(type)) {
          dictItemsServiceMap.put(type, dictItemsRegister);
        }
      }
    }
  }

  @Resource(name = ApplicationCache.BEAN_ID)
  private ApplicationCache applicationCache;

  private ListMap<String, String> cacheKeys = ListMap.concurrentHashMap();

  public static final String Prefix = "DictEntry:";

  public static final String KeySeparater = "|";


  @Override
  public Collection<DictEntry> lookup(String type, Object categorykey, Object... extraTypes) {
    try {
      return loadDictEntryMap(type, categorykey, extraTypes).values();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  @SuppressWarnings("unchecked")
  private Map<Object, DictEntry> loadDictEntryMap(String type, Object categorykey,
      Object... extraTypes) {
    assert categorykey != null;

    StringBuilder cacheKeyBuilder = new StringBuilder(Prefix + type + KeySeparater + categorykey);
    for (Object object : extraTypes) {
      cacheKeyBuilder.append(KeySeparater).append(object);
    }

    String cacheKey = cacheKeyBuilder.toString();
    Map<Object, DictEntry> dictEntryMap =
        (Map<Object, DictEntry>) applicationCache.getCacheObject(cacheKey);
    if (dictEntryMap == null) {
      DictEntriesProvider dictItemsService;
      Collection<DictEntry> dictEntryList = null;
      if ((dictItemsService = dictItemsServiceMap.get(type)) != null) {
        dictEntryList = dictItemsService.lookup(categorykey, extraTypes);
      }

      dictEntryMap = new LinkedHashMap<Object, DictEntry>();
      if (dictEntryList == null) {
        dictEntryList = new ArrayList<DictEntry>();
      } else {
        for (DictEntry dictEntry : dictEntryList) {
          dictEntryMap.put(dictEntry.getKey(), dictEntry);
        }
      }

      cacheKeys.add(Prefix + type, cacheKey);
      applicationCache.putCacheObject(cacheKey, dictEntryMap);
    }
    return dictEntryMap;
  }

  @Override
  public void removeCache(Object... types) {
    if (types.length == 0) {
      Collection<Collection<String>> keyLists = cacheKeys.getData().values();
      for (Collection<String> keyList : keyLists) {
        for (String key : keyList) {
          applicationCache.removeCacheObject(key);
        }
      }
      cacheKeys = ListMap.concurrentHashMap();
    } else {
      for (Object object : types) {
        String k = Prefix + object;
        Collection<String> keys = cacheKeys.getValue(k);
        if (keys == null) {
          return;
        }
        for (String key : keys) {
          applicationCache.removeCacheObject(key);
        }
        cacheKeys.remove(k);
      }
    }
  }

  @Override
  public Object define(String type, Object categorykey, Object entryKey, Object... extraTypes) {
    DictEntry dictEntry = loadDictEntryMap(type, categorykey, extraTypes).get(entryKey);
    return dictEntry != null ? dictEntry.getValue() : null;
  }

}

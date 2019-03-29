package org.xobo.coke.script.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.stereotype.Service;
import org.xobo.coke.dao.CokeHibernateL;
import org.xobo.coke.filestorage.service.CokeBlobService;
import org.xobo.coke.script.entity.CokeScript;
import org.xobo.coke.script.model.ScriptEngineInfo;
import org.xobo.coke.script.service.ScriptService;

@Service
public class ScriptServiceImpl implements ScriptService {
  ScriptEngineManager scriptEngineManager = new ScriptEngineManager();

  private Map<String, Invocable> invocableMap = new ConcurrentHashMap<>();

  @Override
  public Collection<ScriptEngineInfo> loadScriptEngineInfos() {
    List<ScriptEngineFactory> engineFactories = scriptEngineManager.getEngineFactories();
    if (engineFactories.size() == 0) {
      System.out.println("本JVM尚不支持任何脚本引擎");
      return null;
    }

    Collection<ScriptEngineInfo> scriptEngineInfos = new ArrayList<>();
    for (ScriptEngineFactory engineFactory : engineFactories) {
      ScriptEngineInfo scriptEngineInfo = new ScriptEngineInfo();
      scriptEngineInfo.setEngineName(engineFactory.getEngineName());
      scriptEngineInfo.setName(engineFactory.getNames().get(0));
      scriptEngineInfo.setNames(engineFactory.getNames().toString());
      scriptEngineInfo.setLanguageName(engineFactory.getLanguageName());
      scriptEngineInfo.setThreading((String) engineFactory.getParameter("THREADING"));
      scriptEngineInfos.add(scriptEngineInfo);
    }
    return scriptEngineInfos;
  }

  @Override
  public Object invokeFunction(String code, String functionName, Object... args) {
    Invocable invocable = invocableMap.get(code);
    if (invocable == null) {
      return null;
    }
    try {
      return invocable.invokeFunction(functionName, args);
    } catch (NoSuchMethodException | ScriptException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  @PostConstruct
  public void initScripts() {
    Collection<CokeScript> scripts = loadScripts();
    for (CokeScript cokeScript : scripts) {
      final ScriptEngine scriptEngine = scriptEngineManager.getEngineByName(cokeScript.getEngine());
      final String scriptContent = cokeClobService.getContent(cokeScript.getContentId());
      if (scriptEngine == null || StringUtils.isEmpty(scriptContent)) {
        continue;
      }

      if (scriptEngine instanceof Compilable) {
        try {
          CompiledScript compiledScript = ((Compilable) scriptEngine).compile(scriptContent);
          compiledScript.eval();
        } catch (ScriptException e) {
          e.printStackTrace();
          continue;
        }
      }

      if (scriptEngine instanceof Invocable) {
        invocableMap.put(cokeScript.getCode(), (Invocable) scriptEngine);
      } else {
        invocableMap.put(cokeScript.getCode(), new Invocable() {

          @Override
          public Object invokeMethod(Object thiz, String name, Object... args)
              throws ScriptException, NoSuchMethodException {
            return scriptEngine.eval(scriptContent);
          }

          @Override
          public Object invokeFunction(String name, Object... args)
              throws ScriptException, NoSuchMethodException {
            return scriptEngine.eval(scriptContent);
          }

          @Override
          public <T> T getInterface(Object thiz, Class<T> clasz) {
            return null;
          }

          @Override
          public <T> T getInterface(Class<T> clasz) {
            return null;
          }
        });
      }
    }
  }

  @SuppressWarnings("unchecked")
  public Collection<CokeScript> loadScripts() {
    DetachedCriteria dc = DetachedCriteria.forClass(CokeScript.class);
    return (Collection<CokeScript>) hdao.query(dc);
  }


  @Resource
  private CokeBlobService cokeClobService;

  @Resource
  private CokeHibernateL hdao;
}

package org.xobo.coke.script.service;

import java.util.Collection;
import org.xobo.coke.script.model.ScriptEngineInfo;

public interface ScriptService {
  public Object invokeFunction(String code, String functionName, Object... args);

  void initScripts();

  Collection<ScriptEngineInfo> loadScriptEngineInfos();

}

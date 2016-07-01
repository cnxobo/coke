package org.xobo.coke.service;

import java.util.Map;

import javax.annotation.Resource;

import com.bstek.dorado.core.el.ContextVarsInitializer;

public class CokeContextVarsInitializer implements ContextVarsInitializer {

  @Resource(name = DictService.BEAN_ID)
  private DictService dictService;

  @Override
  public void initializeContext(Map<String, Object> vars) throws Exception {
    vars.put("coke.dict", dictService);
  }

}

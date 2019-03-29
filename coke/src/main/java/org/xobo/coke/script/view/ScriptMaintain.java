package org.xobo.coke.script.view;

import java.util.Collection;
import java.util.Map;
import javax.annotation.Resource;
import javax.script.ScriptException;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.stereotype.Component;
import org.xobo.coke.dao.CokeHibernate;
import org.xobo.coke.dao.CokeHibernateL;
import org.xobo.coke.filestorage.domain.CokeBlob;
import org.xobo.coke.filestorage.service.CokeBlobService;
import org.xobo.coke.script.entity.CokeScript;
import org.xobo.coke.script.model.ScriptEngineInfo;
import org.xobo.coke.script.service.ScriptService;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Page;

@Component
public class ScriptMaintain {
  @DataProvider
  public void loadHrScripts(Page<CokeScript> page, Criteria criteria,
      Map<String, Object> queryParameter) {
    DetachedCriteria dc = hdao.buildDetachedCriteria(queryParameter, criteria, CokeScript.class);
    hdao.pagingQuery(page, dc);
  }

  @DataProvider
  public CokeBlob loadClob(Long id) {
    return hrClobService.get(id);
  }

  @DataResolver
  public void saveHrScripts(Collection<CokeScript> cokeScripts) {
    if (cokeScripts == null || cokeScripts.isEmpty()) {
      return;
    }
    for (CokeScript cokeScript : cokeScripts) {
      CokeBlob cokeBlob = EntityUtils.getValue(cokeScript, "scriptContent");
      EntityState state = EntityUtils.getState(cokeBlob);
      if (state == EntityState.NEW) {
        hdaoL.insertEntity(cokeBlob);
        cokeScript.setContentId(cokeBlob.getId());
      } else if (state == EntityState.MODIFIED) {
        hdaoL.updateEntity(cokeBlob);
      }
    }
    hdaoL.persistEntities(cokeScripts);

  }

  @DataProvider
  public Collection<ScriptEngineInfo> loadScriptingEngine() {
    return scriptService.loadScriptEngineInfos();
  }


  @Expose
  public Object invokeScript(String code, String functionName, Collection<Object> parameters)
      throws NoSuchMethodException, ScriptException {
    Object[] varargs = null;
    if (parameters != null) {
      varargs = new Object[parameters.size()];
      parameters.toArray(varargs);
    }
    return scriptService.invokeFunction(code, functionName, varargs);
  }

  @Expose
  public void initScript() {
    scriptService.initScripts();
  }

  @Resource
  private ScriptService scriptService;

  @Resource
  private CokeBlobService hrClobService;

  @Resource
  private CokeHibernate hdao;
  @Resource
  private CokeHibernateL hdaoL;

}

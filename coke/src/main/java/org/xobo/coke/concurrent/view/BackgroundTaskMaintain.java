package org.xobo.coke.concurrent.view;

import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.xobo.coke.concurrent.domain.BackgroundTaskLog;
import org.xobo.coke.concurrent.service.BackgroundTaskExecutorService;
import org.xobo.coke.dao.CokeHibernate;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Page;

@Controller
public class BackgroundTaskMaintain {

  @DataProvider
  public void loadBackgroundTaskLogs(Page<BackgroundTaskLog> page, Criteria criteria,
      Map<String, Object> parameter) {
    DetachedCriteria dc =
        cokeHibernate.buildDetachedCriteria(parameter, criteria, BackgroundTaskLog.class);
    cokeHibernate.pagingQuery(page, dc);
  }

  @Expose
  public void addBackgroundTask(String type, String desc, String backgroundTaskBeanId,
      Map<String, Object> parameter) {
    backgroundTaskExecutorService.execute(type, desc, backgroundTaskBeanId, parameter);
  }

  @Expose
  public void operation(String operation, String taskId) {
    if ("runAgain".equals(operation)) {
      backgroundTaskExecutorService.runAgain(taskId);
    } else if ("cancel".equals(operation)) {
      backgroundTaskExecutorService.cancel(taskId);
    }

  }


  @Autowired
  private BackgroundTaskExecutorService backgroundTaskExecutorService;

  @Resource
  private CokeHibernate cokeHibernate;

}

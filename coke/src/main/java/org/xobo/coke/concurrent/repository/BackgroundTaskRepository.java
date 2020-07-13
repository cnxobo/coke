package org.xobo.coke.concurrent.repository;

import java.util.Collection;
import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.xobo.coke.concurrent.domain.BackgroundTaskLog;
import org.xobo.coke.dao.CokeHibernate;

@Repository
@Transactional
public class BackgroundTaskRepository {

  public int updateResult(String taskId, String status, String result) {
    result = org.apache.commons.lang3.StringUtils.abbreviate(result, 10000);
    String hql = "update " + BackgroundTaskLog.class.getName()
        + " t set t.status = :status, t.result = :result, t.endDate = :endDate where t.taskId = :taskId";
    return cokeHibernate.getSession().createQuery(hql).setString("status", status)
        .setTimestamp("endDate", new Date())
        .setString("result", result).setString("taskId", taskId).executeUpdate();

  }

  public void save(BackgroundTaskLog backgroundTaskLog) {
    cokeHibernate.getSession().save(backgroundTaskLog);
  }

  @SuppressWarnings("unchecked")
  public BackgroundTaskLog getByTaskId(String taskId) {
    DetachedCriteria dc = DetachedCriteria.forClass(BackgroundTaskLog.class);
    dc.add(Restrictions.eq("taskId", taskId));
    Collection<BackgroundTaskLog> backgroundTaskLogs =
        (Collection<BackgroundTaskLog>) cokeHibernate.query(dc);
    return backgroundTaskLogs.isEmpty() ? null : backgroundTaskLogs.iterator().next();
  }

  @SuppressWarnings("unchecked")
  public Collection<BackgroundTaskLog> findProcessingTasksByProcessor(String processor) {
    DetachedCriteria dc = DetachedCriteria.forClass(BackgroundTaskLog.class);
    dc.add(Restrictions.eq("status", "P"));
    if (StringUtils.isNotEmpty(processor)) {
      dc.add(Restrictions.eq("processBy", processor));
    }
    return (Collection<BackgroundTaskLog>) cokeHibernate.query(dc);
  }

  @Resource
  private CokeHibernate cokeHibernate;

}

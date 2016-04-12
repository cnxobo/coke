package org.xobo.coke.concurrent.service.impl;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xobo.coke.concurrent.domain.BackgroundTaskLog;
import org.xobo.coke.concurrent.repository.BackgroundTaskRepository;
import org.xobo.coke.concurrent.service.BackgroundTaskLogService;
import org.xobo.coke.utility.JSONUtil;

@Service
public class BackgroundTaskLogServiceImpl implements BackgroundTaskLogService {


  @Override
  public void markSuccess(String taskId, Map<String, Object> result) {
    String jsonResult = null;
    if (result != null) {
      jsonResult = JSONUtil.toJSON(result);
    }
    backgroundTaskRepository.updateResult(taskId, "S", jsonResult);

  }

  @Override
  public void markFailure(String taskId, Throwable t) {
    String messsage = ExceptionUtils.getRootCauseMessage(t);
    backgroundTaskRepository.updateResult(taskId, "F", messsage);
  }

  @Override
  public String addBackgroundTask(String type, String desc, String backgroundTaskId,
      Map<String, Object> parameter, String nodeName) {
    BackgroundTaskLog backgroundTaskLog = new BackgroundTaskLog();
    String taskId = UUID.randomUUID().toString();
    backgroundTaskLog.setTaskId(taskId);
    backgroundTaskLog.setType(type);
    backgroundTaskLog.setDesc(desc);
    backgroundTaskLog.setBackgroundTaskId(backgroundTaskId);
    String json = JSONUtil.toJSON(parameter);
    backgroundTaskLog.setParameter(json);
    backgroundTaskLog.setStatus("P");
    backgroundTaskLog.setCreateDate(new Date());
    backgroundTaskLog.setProcessBy(nodeName);
    backgroundTaskRepository.save(backgroundTaskLog);
    return taskId;
  }

  @Autowired
  private BackgroundTaskRepository backgroundTaskRepository;

  @Override
  public void markStatus(String taskId, String status) {
    backgroundTaskRepository.updateResult(taskId, status, null);
  }

  @Override
  public BackgroundTaskLog findBackgroundTaskLog(String taskId) {
    return backgroundTaskRepository.getByTaskId(taskId);
  }

  @Override
  public Collection<BackgroundTaskLog> getBackgroundTaskLogs(String processor) {
    return backgroundTaskRepository.findProcessingTasksByProcessor(processor);
  }

}

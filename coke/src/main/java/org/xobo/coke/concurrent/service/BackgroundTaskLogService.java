package org.xobo.coke.concurrent.service;

import java.util.Collection;
import java.util.Map;

import org.xobo.coke.concurrent.domain.BackgroundTaskLog;


public interface BackgroundTaskLogService {

  void markSuccess(String taskId, Map<String, Object> result);

  void markFailure(String taskId, Throwable t);

  void markStatus(String taskId, String status);

  String addBackgroundTask(String type, String desc, String backgroundTaskId,
      Map<String, Object> parameter, String nodeName);

  BackgroundTaskLog findBackgroundTaskLog(String taskId);


  Collection<BackgroundTaskLog> getBackgroundTaskLogs(String processor);

}

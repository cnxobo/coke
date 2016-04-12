package org.xobo.coke.concurrent.service;

import java.util.Map;


public interface BackgroundTaskExecutorService {

  void cancel(String taskId);

  void execute(String type, String desc, String backgroundTaskId,
      Map<String, Object> parameter);

  void runAgain(String taskId);

}

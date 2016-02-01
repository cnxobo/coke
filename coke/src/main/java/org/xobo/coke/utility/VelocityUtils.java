package org.xobo.coke.utility;

import java.util.Properties;

import org.apache.velocity.app.VelocityEngine;

public class VelocityUtils {
  private static VelocityEngine velocityEngine;

  static {
    velocityEngine = new VelocityEngine();
    Properties p = new Properties();
    p.put("runtime.log.logsystem.class", "org.apache.velocity.runtime.log.NullLogChute");
    velocityEngine.init(p);

  }

  public static VelocityEngine getEngine() {
    return velocityEngine;
  }

}

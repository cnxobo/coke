package org.xobo.coke.utility;

@Deprecated
public class TimeCost {
  private String name;
  private long timeStart;
  private long timeEnd;

  public TimeCost() {
    timeStart = System.currentTimeMillis();
  }

  public TimeCost(String name) {
    this.name = name;
    this.timeStart = System.currentTimeMillis();
  }

  public long end() {
    timeEnd = System.currentTimeMillis();
    return timeEnd - timeStart;
  }

  @Override
  public String toString() {
    if (name == null) {
      name = "";
    }
    return name + " cost " + (timeEnd - timeStart) + "ms";
  }


}

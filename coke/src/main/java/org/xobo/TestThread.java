package org.xobo;
public class TestThread {
  public static void main(String args[]) {

    Thread t = new Thread() {

      public void run() {
        pong();
      }
    };

    t.run();
    System.out.print("ping");

  }

  static void pong() {

    System.out.print("pong");

  }

}
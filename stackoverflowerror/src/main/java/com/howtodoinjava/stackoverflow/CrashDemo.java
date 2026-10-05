package com.howtodoinjava.stackoverflow;

/** Lets the error reach the top of the main thread, so the JVM prints the full stack trace. */
public final class CrashDemo {

  private CrashDemo() {
  }

  public static void main(String[] args) {
    long total = Sums.sumToNoBase(5);
    System.out.println(total);
  }
}

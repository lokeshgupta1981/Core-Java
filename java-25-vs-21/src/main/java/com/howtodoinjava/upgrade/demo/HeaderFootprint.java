package com.howtodoinjava.upgrade.demo;

/**
 * Measures the heap used per small object. Run with -XX:+UseSerialGC -Xmx1g,
 * with and without -XX:+UseCompactObjectHeaders (JDK 25 only).
 */
public class HeaderFootprint {

  record Point(int x, int y) { }

  public static void main(String[] args) {
    int count = 5_000_000;
    Point[] points = new Point[count];
    long before = usedAfterGc();
    for (int i = 0; i < count; i++) {
      points[i] = new Point(i, i);
    }
    long after = usedAfterGc();
    System.out.println("bytes per Point = " + Math.round((after - before) / (double) count));
    System.out.println("last x = " + points[count - 1].x());
  }

  private static long usedAfterGc() {
    Runtime runtime = Runtime.getRuntime();
    for (int i = 0; i < 3; i++) {
      System.gc();
    }
    return runtime.totalMemory() - runtime.freeMemory();
  }
}

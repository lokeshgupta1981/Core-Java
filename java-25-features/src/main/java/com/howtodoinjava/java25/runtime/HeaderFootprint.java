package com.howtodoinjava.java25.runtime;

import java.lang.management.ManagementFactory;

/** JEP 519: measures the heap used by one million small objects. */
public class HeaderFootprint {

  record Page(int number, int words) {
  }

  static final int COUNT = 1_000_000;

  public static void main(String[] args) {
    boolean compact = ManagementFactory.getRuntimeMXBean().getInputArguments()
        .contains("-XX:+UseCompactObjectHeaders");
    long before = usedHeap();
    Page[] pages = new Page[COUNT];
    long afterArray = usedHeap();
    for (int i = 0; i < COUNT; i++) {
      pages[i] = new Page(i, i * 2);
    }
    long afterObjects = usedHeap();
    long perObject = Math.round((afterObjects - afterArray) / (double) COUNT);
    System.out.println("UseCompactObjectHeaders=" + compact);
    System.out.println("Heap for 1,000,000 Page objects: " + (afterObjects - afterArray) / 1_000_000 + " MB");
    System.out.println("Bytes per Page object: " + perObject);
    System.out.println("(array of references: " + (afterArray - before) / 1_000_000 + " MB, last page " + pages[COUNT - 1].number() + ")");
  }

  private static long usedHeap() {
    for (int i = 0; i < 3; i++) {
      System.gc();
    }
    Runtime rt = Runtime.getRuntime();
    return rt.totalMemory() - rt.freeMemory();
  }
}

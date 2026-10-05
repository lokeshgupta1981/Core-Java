package com.howtodoinjava.interview.jvm;

import java.util.HashMap;
import java.util.Map;

/**
 * A memory leak through a static map that only grows.
 * Run: java -Xmx64m -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=target -cp target/classes com.howtodoinjava.interview.jvm.LeakDemo
 */
public class LeakDemo {

  private static final Map<String, byte[]> CACHE = new HashMap<>();   // never evicts

  public static void main(String[] args) {
    for (int i = 0; ; i++) {
      CACHE.put("report-" + i, new byte[1024 * 1024]);   // 1 MB per entry
    }
  }
}

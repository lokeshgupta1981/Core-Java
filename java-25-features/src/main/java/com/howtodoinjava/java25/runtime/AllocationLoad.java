package com.howtodoinjava.java25.runtime;

import java.util.ArrayList;
import java.util.List;

/** Creates short-lived and long-lived objects so that a GC log has something to show. */
public class AllocationLoad {

  public static void main(String[] args) {
    List<byte[]> keep = new ArrayList<>();
    long checksum = 0;
    for (int i = 0; i < 400_000; i++) {
      byte[] temp = new byte[1024];             // dies young
      checksum += temp.length;
      if (i % 100 == 0) {
        keep.add(new byte[1024]);               // survives
      }
    }
    System.out.println("Allocated " + checksum / 1_000_000 + " MB, kept " + keep.size() + " arrays");
  }
}

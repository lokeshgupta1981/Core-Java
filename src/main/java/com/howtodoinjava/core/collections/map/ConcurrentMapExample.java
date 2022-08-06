package com.howtodoinjava.core.collections.map;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Stream;

public class ConcurrentMapExample {
  public static void main(String[] args) {
    ConcurrentHashMap<Integer, String> cmap = new ConcurrentHashMap<>();
    cmap.put(1, "Delhi");
    cmap.put(2, "NewYork");
    cmap.put(3, "London");

    // Iterating concurrent map keys
    for (Integer entry : cmap.keySet()) {
      System.out.println("Entry -- " + entry);
    }

    // Iterating concurrent map values
    for (String value : cmap.values()) {
      System.out.println("Value -- " + value);
    }

    // Iterating concurrent map entries
    for (Map.Entry<Integer, String> entry : cmap.entrySet()) {
      System.out.println(entry.getKey() + " -- " + entry.getValue());
    }

    Stream.of(cmap.entrySet()).forEach(System.out::println);
  }
}

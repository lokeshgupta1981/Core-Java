package com.howtodoinjava.core.collections.map;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentSkipListMap;

public class ConcurrentSkipListMapExample {

  public static void main(String[] args) {
    ConcurrentSkipListMap<String, String> skipListMap = new ConcurrentSkipListMap<>();

    skipListMap.put("key1", "value1");
    skipListMap.put("key3", "value3");
    skipListMap.put("key2", "value2");
    skipListMap.put("key4", "value4");

    System.out.println(skipListMap);

    //1
    ConcurrentSkipListMap<String, String> skipListMap1
        = new ConcurrentSkipListMap<>();

    //2
    ConcurrentSkipListMap<String, String> skipListMap2
        = new ConcurrentSkipListMap<>(Comparator.reverseOrder());

    //3
    ConcurrentSkipListMap<String, String> skipListMap3
        = new ConcurrentSkipListMap<>(Map.of("key", "value"));

    //4
    ConcurrentSkipListMap<String, String> skipListMap4
        = new ConcurrentSkipListMap<>(new TreeMap<>(Map.of("key", "value")));
  }
}

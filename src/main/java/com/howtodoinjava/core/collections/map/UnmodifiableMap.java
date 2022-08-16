package com.howtodoinjava.core.collections.map;

import org.apache.commons.collections4.MapUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class UnmodifiableMap {
  public static void main(String[] args) {
    Map<String, String> mutableMap = new HashMap<>();
    mutableMap.put("key1", "value1");

    Map<String, String> unmodifiableMap
        = Collections.unmodifiableMap(mutableMap);

    //Throws java.lang.UnsupportedOperationException
    //unmodifiableMap.put("key2", "value2");

    //Changes are visible in both maps
    mutableMap.put("key2", "value2");
    System.out.println(unmodifiableMap);  //{key1=value1, key2=value2}

    unmodifiableMap = MapUtils.unmodifiableMap(mutableMap);
    unmodifiableMap.put("key2", "value2");
  }
}

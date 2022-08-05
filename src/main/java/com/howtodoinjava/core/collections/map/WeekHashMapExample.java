package com.howtodoinjava.core.collections.map;

import java.util.Map;
import java.util.WeakHashMap;

public class WeekHashMapExample {
  public static void main(String[] args) throws InterruptedException {
    Map<MapKey, String> map = new WeakHashMap<>();

    MapKey key1 = new MapKey("1");
    MapKey key2 = new MapKey("2");

    map.put(key1, "1");
    map.put(key2, "2");

    System.out.println(map);

    key1 = null;

    System.gc();
    Thread.sleep(10000);

    System.out.println(map);
  }
}

final class MapKey {
  String key;
  public MapKey(String key) {
    this.key = key;
  }

  public String getKey() {
    return key;
  }

  @Override
  public String toString() {
    return "MapKey{" +
        "key='" + key + '\'' +
        '}';
  }
}
package com.howtodoinjava.interview.design;

import java.util.LinkedHashMap;
import java.util.Map;

/** LRU cache: access-ordered LinkedHashMap that drops the eldest entry above capacity. Not thread-safe. */
public class LruCache<K, V> extends LinkedHashMap<K, V> {

  private final int capacity;

  public LruCache(int capacity) {
    super(16, 0.75f, true);            // true = access order
    this.capacity = capacity;
  }

  @Override
  protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
    return size() > capacity;
  }
}

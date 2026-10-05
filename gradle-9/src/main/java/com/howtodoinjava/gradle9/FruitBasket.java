package com.howtodoinjava.gradle9;

import java.util.LinkedHashMap;
import java.util.Map;

public class FruitBasket {

  private final Map<String, Integer> counts = new LinkedHashMap<>();

  public void add(String fruit, int count) {
    if (fruit == null || fruit.isBlank()) {
      throw new IllegalArgumentException("Fruit name must not be blank");
    }
    if (count <= 0) {
      throw new IllegalArgumentException("Count must be positive: " + count);
    }
    counts.merge(fruit.strip(), count, Integer::sum);
  }

  public int countOf(String fruit) {
    return counts.getOrDefault(fruit, 0);
  }

  public int total() {
    return counts.values().stream().mapToInt(Integer::intValue).sum();
  }

  public Map<String, Integer> asMap() {
    return Map.copyOf(counts);
  }
}

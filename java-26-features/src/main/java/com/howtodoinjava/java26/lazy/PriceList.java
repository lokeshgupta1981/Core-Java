package com.howtodoinjava.java26.lazy;

import java.util.Map;

public record PriceList(Map<String, Integer> prices) {

  public static int loads = 0;

  public static PriceList load() {
    loads++;
    return new PriceList(Map.of("apple", 5, "banana", 3));
  }
}

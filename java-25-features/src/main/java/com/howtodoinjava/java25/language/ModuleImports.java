package com.howtodoinjava.java25.language;

import module java.base;      // JEP 511: every package that java.base exports

public class ModuleImports {

  public static Map<String, Integer> pagesByTitle(List<String> titles) {
    return titles.stream()
        .collect(Collectors.toMap(Function.identity(), String::length, (a, b) -> a, TreeMap::new));
  }

  public static Path catalogFile() {
    return Path.of("catalog", "books.csv");
  }
}

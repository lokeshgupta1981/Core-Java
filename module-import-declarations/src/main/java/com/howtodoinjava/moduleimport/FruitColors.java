package com.howtodoinjava.moduleimport;

import module java.base;      // java.util.List
import module java.desktop;   // java.awt.List and java.awt.Color

import java.util.List;        // the single-type import wins: List is java.util.List

public class FruitColors {

  public static Map<String, Color> colors(List<String> fruits) {
    Map<String, Color> result = new LinkedHashMap<>();
    for (String fruit : fruits) {
      result.put(fruit, fruit.equals("banana") ? Color.YELLOW : Color.RED);
    }
    return result;
  }

  public static void main(String[] args) {
    System.out.println(colors(List.of("apple", "banana")));
  }
}

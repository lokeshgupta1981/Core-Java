package com.howtodoinjava.moduleimport;

import module java.base;      // all 58 packages that java.base exports

public class QuickReference {

  public static void main(String[] args) {
    List<String> fruits = List.of("apple", "banana", "avocado");               // java.util.List
    Map<Character, List<String>> groups = fruits.stream()
        .collect(Collectors.groupingBy(f -> f.charAt(0)));                     // java.util.stream.Collectors
    Path file = Path.of("fruits.txt");                                         // java.nio.file.Path
    LocalDate today = LocalDate.of(2026, 10, 5);                               // java.time.LocalDate

    System.out.println(groups);   // {a=[apple, avocado], b=[banana]}
    System.out.println(file);     // fruits.txt
    System.out.println(today);    // 2026-10-05
  }
}

package com.howtodoinjava.core.streams;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ChainedPredicate {
  public static void main(final String[] args) {
    List<String> list = Arrays.asList("Aa", "Bb", "Cc", "Dd", "Ab", "Bc");

    Predicate<String> startsWithA = s -> s.startsWith("A");
    Predicate<String> startsWithB = s -> s.startsWith("B");
    Predicate<String> containsC = s -> s.contains("c");
    Predicate<String> notContainsC = s -> !s.contains("c");
    
    Predicate<String> complexPredicate = startsWithA.or(startsWithB)
      .and(containsC.negate());

    List<String> items = list.stream()
      .filter(complexPredicate)
      .collect(Collectors.toList());

    System.out.println(items);
  }
}

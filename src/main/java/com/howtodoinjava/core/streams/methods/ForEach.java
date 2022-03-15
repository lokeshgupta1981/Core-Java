package com.howtodoinjava.core.streams.methods;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

public class ForEach {
  public static void main(final String[] args) {
    List<Integer> list = Arrays.asList(2, 4, 6, 8, 10);
    Consumer<Integer> action = System.out::println;

    list.stream()
      .forEach(action);

    list.stream()
      .sorted(Comparator.reverseOrder())
      .forEach(action);

    // for-each loop
    for (Integer i : list) {
      System.out.println(i);
    }
  }
}

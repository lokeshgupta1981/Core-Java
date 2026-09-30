package com.howtodoinjava.core.array;

import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Stream;

/**
 * Demonstrates sorting a {@code String[]} alphabetically and in reverse
 * order, using both the Stream API ({@link Stream#sorted}) and
 * {@link Arrays#sort(Object[], Comparator)}.
 */
public class ArraySortAlphabatical {

  public static void main(final String[] args) {

    // Unsorted string array
    String[] strArray = {"Alex", "Charles", "Dean", "Amanda", "Brian"};

    // Sorting the strings
    strArray = Stream.of(strArray)
        .sorted()
        .toArray(String[]::new);

    // Sorted array
    System.out.println("Sorted : " + Arrays.toString(strArray));

    strArray = Stream.of(strArray)
        .sorted(Comparator.reverseOrder())
        .toArray(String[]::new);

    // Reverse Sorted array
    System.out.println("Reverse Sorted : " + Arrays.toString(strArray));

    strArray = new String[]{"Alex", "Charles", "Dean", "Amanda",
        "Brian"};

    // Sorting the strings
    Arrays.sort(strArray);

    // Sorted array
    System.out.println("Sorted : " + Arrays.toString(strArray));

    Arrays.sort(strArray, Comparator.reverseOrder());

    // Reverse Sorted array
    System.out.println("Reverse Sorted : " + Arrays.toString(strArray));
  }
}

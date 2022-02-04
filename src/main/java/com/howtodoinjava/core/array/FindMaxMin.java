package com.howtodoinjava.core.array;

import java.util.Arrays;
import java.util.Collections;
import java.util.IntSummaryStatistics;

import org.apache.commons.lang3.ArrayUtils;

public class FindMaxMin {

  public static void main(final String[] args) {

    // Min = 0, Max = 100
    int[] items = { 0, 10, 30, 2, 7, 5, 90, 76, 100, 45, 55 };

    // 1
    int max = Arrays.stream(items)
      .max()
      .getAsInt(); // 100

    int min = Arrays.stream(items)
      .min()
      .getAsInt(); // 0

    System.out.println(max);
    System.out.println(min);

    // 2
    IntSummaryStatistics stats = Arrays.stream(items)
      .summaryStatistics();
    System.out.println(stats.getMax());
    System.out.println(stats.getMin());

    // 3
    min = Collections.min(Arrays.asList(ArrayUtils.toObject(items)));
    max = Collections.max(Arrays.asList(ArrayUtils.toObject(items)));

    System.out.println(max);
    System.out.println(min);

    // 4
    Arrays.sort(items);

    max = items[items.length - 1];
    min = items[0];

    System.out.println(max);
    System.out.println(min);

    // 5
    max = items[0];
    min = items[0];

    for (int i = 1; i < items.length; i++) {
      if (items[i] > max) {
        max = items[i];
      }
      else if (items[i] < min) {
        min = items[i];
      }
    }

    System.out.println(max);
    System.out.println(min);

    // 6

    min = getMax(items, 0, items[0]);
    min = getMin(items, 0, items[0]);

    System.out.println(max);
    System.out.println(min);
  }

  public static int getMax(final int[] numbers, final int a, final int n) {
    return a >= numbers.length ? n
      : Math.max(n, getMax(numbers, a + 1, numbers[a] > n ? numbers[a] : n));
  }

  private static int getMin(final int[] numbers, final int a, final int n) {
    return a == numbers.length ? n
      : Math.min(n, getMin(numbers, a + 1, numbers[a] < n ? numbers[a] : n));
  }
}

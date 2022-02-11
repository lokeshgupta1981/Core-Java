package com.howtodoinjava.core.array;

import java.util.Arrays;

public class SumAndAverageOfArray {
  public static void main(String[] args) {

    int[] intArray = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    Integer[] integerArray = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

    long sum;
    double average;

    //1. Sum of Array Items
    sum = Arrays.stream(intArray).sum();
    System.out.println(sum);

    sum = Arrays.stream(integerArray)
        .mapToInt(i -> i)
        .sum();
    System.out.println(sum);

    sum = Arrays.stream(intArray)
        .summaryStatistics()
        .getSum();
    System.out.println(sum);

    sum = 0;
    for (int value : intArray) {
      sum += value;
    }
    System.out.println(sum);

    //2. Average of Array Items
    average = Arrays.stream(intArray)
        .average()
        .orElse(Double.NaN);
    System.out.println(average);

    average = Arrays.stream(intArray)
        .summaryStatistics()
        .getAverage();
    System.out.println(average);
  }
}

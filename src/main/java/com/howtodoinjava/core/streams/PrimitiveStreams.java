package com.howtodoinjava.core.streams;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class PrimitiveStreams {
  @SuppressWarnings("unused")
  public static void main(final String[] args) {
    // 1 from array of primitives
    int[] integers = { 1, 2, 3, 4, 5, 6 };
    IntStream intStream = Arrays.stream(integers);
    int min = intStream.min()
      .getAsInt();
    System.out.println(min);

    // 2 from values
    int max = IntStream.of(1, 2, 3, 4, 5, 6)
      .max()
      .getAsInt();

    System.out.println(max);

    int sum = IntStream.of(1, 2, 3, 4, 5, 6)
      .sum();

    double avg = IntStream.of(1, 2, 3, 4, 5, 6)
      .average()
      .getAsDouble();

    sum = IntStream.range(1, 10)
      .sum();
    sum = IntStream.rangeClosed(1, 10)
      .sum();

    // Primitive Stream to List
    List<Integer> listOfIntegers = IntStream.of(1, 2, 3, 4, 5)
      .boxed()
      .collect(Collectors.toList());

    List<Long> listOfLongs = LongStream.of(1, 2, 3, 4, 5)
      .boxed()
      .collect(Collectors.toList());

    List<Double> listOfDoubles = DoubleStream.of(1.0, 2.0, 3.0, 4.0, 5.0)
      .boxed()
      .collect(Collectors.toList());

    // Primitive Stream to List
    listOfIntegers = IntStream.of(1, 2, 3, 4, 5)
      .mapToObj(Integer::valueOf)
      .collect(Collectors.toList());

    listOfLongs = LongStream.of(1, 2, 3, 4, 5)
      .mapToObj(Long::valueOf)
      .collect(Collectors.toList());

    listOfDoubles = DoubleStream.of(1.0, 2.0, 3.0, 4.0, 5.0)
      .mapToObj(Double::valueOf)
      .collect(Collectors.toList());
    
    //Primitive Stream to Array
    int[] intArray = IntStream.of(1, 2, 3, 4, 5).toArray();
    long[] longArray = LongStream.of(1, 2, 3, 4, 5).toArray();
    double[] doubleArray = DoubleStream.of(1, 2, 3, 4, 5).toArray();
  }
}

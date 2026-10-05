package com.howtodoinjava.core.array;

import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;


/**
 * Demonstrates converting between arrays and streams: {@link Arrays#stream}
 * for both object and primitive arrays, and {@link Stream#toArray} /
 * {@link IntStream#toArray} to go back to an array.
 */
public class ConvertBetweenArrayAndStream {
  public static void main(final String[] args) {
    int[] primitiveArray = {0,1,2,3,4};
    String[] stringArray = {"a", "b", "c", "d", "e"};

    //1. Array -> Stream
    Stream<String> strStream = Arrays.stream(stringArray);
    IntStream intStream = Arrays.stream(primitiveArray);
    Stream<Integer> integerStream = Arrays.stream(primitiveArray).boxed();

    //2.Stream -> Array
    stringArray = strStream.toArray(String[]::new);
    primitiveArray = intStream.toArray();
    primitiveArray = integerStream.mapToInt(i -> i).toArray();
  }
}

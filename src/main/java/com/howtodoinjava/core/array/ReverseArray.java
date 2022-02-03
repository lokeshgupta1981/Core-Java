package com.howtodoinjava.core.array;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.IntStream;

import org.apache.commons.lang3.ArrayUtils;

public class ReverseArray {
  public static void main(final String[] args) {
    // 1
    String[] array = { "A", "B", "C", "D", "E" };
    Collections.reverse(Arrays.asList(array));
    System.out.println(Arrays.toString(array));

    // 2
    array = new String[] { "A", "B", "C", "D", "E" };
    for (int i = 0; i < array.length / 2; i++) {
      String temp = array[i];
      array[i] = array[array.length - 1 - i];
      array[array.length - 1 - i] = temp;
    }
    System.out.println(Arrays.toString(array));

    final String[] arr = { "A", "B", "C", "D", "E" };
    Object[] reversedArr = IntStream.rangeClosed(1, array.length)
      .mapToObj(i -> arr[arr.length - i])
      .toArray();
    System.out.println(Arrays.toString(reversedArr));

    //3
    ArrayUtils.reverse(arr);
    System.out.println(Arrays.toString(arr));
  }
}

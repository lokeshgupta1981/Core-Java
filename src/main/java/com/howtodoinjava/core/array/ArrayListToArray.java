package com.howtodoinjava.core.array;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Demonstrates converting an {@link ArrayList} of {@link Integer} into a
 * primitive {@code int[]} using the Stream API.
 */
public class ArrayListToArray {
  public static void main(final String[] args) {
    ArrayList<Integer> listOfIntegers = new ArrayList<>();
    listOfIntegers.add(1);
    listOfIntegers.add(2);
    listOfIntegers.add(3);

    System.out.println(listOfIntegers);

    int[] intArray = listOfIntegers.stream()
        .mapToInt(Integer::intValue)
        .toArray();

    System.out.println(Arrays.toString(intArray));
  }
}

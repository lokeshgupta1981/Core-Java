package com.howtodoinjava.core.array;

import java.util.Arrays;

public class ConvertStringArrayToIntArray {

  public static void main(String[] args) {
    String[] strArray = new String[]{"1", "2", "3"};

    //1
    int[] intArray = Arrays.stream(strArray)
        .mapToInt(Integer::parseInt)
        .toArray();

    System.out.println(Arrays.toString(intArray));

    Integer[] integerArray = Arrays.stream(strArray)
        .map(Integer::parseInt)
        .toArray(Integer[]::new);

    System.out.println(Arrays.toString(integerArray));

    //2
    String[] invalidStrArray = new String[]{"1", "2", "3", "four", "5"};
    int[] intArray1 = Arrays.stream(invalidStrArray).mapToInt(str -> {
      try {
        return Integer.parseInt(str);
      } catch (NumberFormatException nfe) {
        return -1;
      }
    }).toArray();

    System.out.println(Arrays.toString(intArray1));
  }
}

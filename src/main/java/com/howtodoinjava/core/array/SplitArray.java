package com.howtodoinjava.core.array;

import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SplitArray {
  public static void main(String[] args) {
    int[] original = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

    //1. Divide in two equal parts
    int splitSize = original.length / 2;
    int[] splitArrayPart1 = Arrays.copyOfRange(original, 0, splitSize);
    int[] splitArrayPart2 = Arrays.copyOfRange(original, splitSize,
        original.length);

    System.out.println(Arrays.toString(splitArrayPart1));
    System.out.println(Arrays.toString(splitArrayPart2));

    //2.Split at a given index
    int givenIndex = 3;
    splitArrayPart1 = Arrays.copyOfRange(original, 0, givenIndex);
    splitArrayPart2 = Arrays.copyOfRange(original, givenIndex,
        original.length);

    System.out.println(Arrays.toString(splitArrayPart1));
    System.out.println(Arrays.toString(splitArrayPart2));

    //3. Split array of equal sizes
    List<Integer[]> arrayParts = splitArray(ArrayUtils.toObject(original), 3);
    for(Integer[] o: arrayParts) {
      System.out.println(Arrays.toString(o));
    }
  }

  public static <T> List<T[]> splitArray(T[] array,
                                         int splitSize) {
    int numberOfArrays = array.length / splitSize;
    int remainder = array.length % splitSize;

    int start = 0;
    int end = 0;

    List<T[]> list = new ArrayList<>();
    for (int i = 0; i < numberOfArrays; i++) {
      end += splitSize;
      list.add(Arrays.copyOfRange(array, start, end));
      start = end;
    }

    if(remainder > 0) {
      list.add(Arrays.copyOfRange(array, start, (start + remainder)));
    }
    return list;
  }
}

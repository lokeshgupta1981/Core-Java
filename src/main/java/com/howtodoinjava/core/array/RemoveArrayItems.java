package com.howtodoinjava.core.array;

import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RemoveArrayItems {
  public static void main(String[] args) {
    Integer[] originalArray = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

    //1. Apache Commons's ArrayUtils.remove()
    Integer[] reducedArray = ArrayUtils.removeAll(originalArray, 5, 6, 7);
    System.out.println("Reduced Array Length : " + reducedArray.length);
    System.out.println("Reduced Array : " + Arrays.toString(reducedArray));

    //2. Using List
    List<Integer> tempList = new ArrayList<>(Arrays.asList(originalArray));
    tempList.remove(7);
    reducedArray = tempList.toArray(new Integer[0]);
    System.out.println("Reduced Array Length : " + reducedArray.length);
    System.out.println("Reduced Array : " + Arrays.toString(reducedArray));

    //3. Removing element by value
    reducedArray = ArrayUtils.removeAllOccurrences(originalArray, 4);
    System.out.println("Reduced Array Length : " + reducedArray.length);
    System.out.println("Reduced Array : " + Arrays.toString(reducedArray));

    //4. Removing using for-loop and shifting the remaining items
    removeIndexAndShift(originalArray, 6);
    System.out.println("Reduced Array Length : " + originalArray.length);
    System.out.println("Reduced Array : " + Arrays.toString(originalArray));

    removeIndexAndShift(originalArray, 2);
    System.out.println("Reduced Array Length : " + originalArray.length);
    System.out.println("Reduced Array : " + Arrays.toString(originalArray));
  }

  static <T> void removeIndexAndShift(T[] array, int indexToRemove) {
    for (int i = indexToRemove; i < array.length - 1; i++) {
      array[i] = array[i + 1];
    }
    //optionally we can set the last element to null
    array[array.length - 1] = null;
  }
}

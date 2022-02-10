package com.howtodoinjava.core.array;

import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RemoveArrayItems {
  public static void main(String[] args) {
    Integer[] originalArray = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
    Integer[] reducedArray;

    //1. Apache Commons's ArrayUtils.remove()
    reducedArray = ArrayUtils.remove(originalArray, 5);
    System.out.println("Reduced Array Length : " + reducedArray.length);
    System.out.println("Reduced Array : " + Arrays.toString(reducedArray));

    //2. Using List
    List<Integer> tempList = new ArrayList<>(Arrays.asList(originalArray));
    tempList.remove(5);
    reducedArray = tempList.toArray(new Integer[0]);
    System.out.println("Reduced Array Length : " + reducedArray.length);
    System.out.println("Reduced Array : " + Arrays.toString(reducedArray));

    //3. Removing element by value
    reducedArray = ArrayUtils.removeElement(originalArray, 0);
    System.out.println("Reduced Array Length : " + reducedArray.length);
    System.out.println("Reduced Array : " + Arrays.toString(reducedArray));
  }
}

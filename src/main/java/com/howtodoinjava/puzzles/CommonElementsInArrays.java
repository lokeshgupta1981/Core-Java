package com.howtodoinjava.puzzles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommonElementsInArrays {

  public static void main(String[] args) {

    Integer[][] arrays = {
        {1, 2, 3, 4, 5},
        {2, 4, 6, 8},
        {2, 3, 4, 7},
        {4, 5, 8, 9}
    };

    List<Integer> commonElements = findCommonElements(arrays);
    System.out.println("Common elements in the arrays: " + commonElements);
  }

  public static List<Integer> findCommonElements(Integer[][] arrays) {
    if (arrays == null || arrays.length == 0) {
      return List.of(); //empty arguments
    }

    List<Integer> commonElements = Arrays.asList(arrays[0]);

    for (int i = 1; i < arrays.length; i++) {
      List<Integer> temp = new ArrayList<>();
      for (int num : arrays[i]) {
        if (commonElements.contains(num)) {
          temp.add(num);
        }
      }
      commonElements = temp;
    }
    return commonElements;
  }
}

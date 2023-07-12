package com.howtodoinjava.puzzles;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections.MultiHashMap;
import org.apache.commons.collections.MultiMap;

public class FindAllDuplicateNumbers {

  public static void main(String[] args) {
    Integer[] array = {1, 2, 3, 2, 4, 3, 5, 6, 5, 7, 8, 8, 9};

    //1
    MultiMap multiMap = new MultiHashMap();
    for (int num : array) {
      multiMap.put(num, num);
    }

    List duplicates = multiMap.keySet().stream()
        .filter(i -> ((ArrayList) multiMap.get(i)).size() > 1)
        .toList();

    System.out.println(duplicates);
  }
}

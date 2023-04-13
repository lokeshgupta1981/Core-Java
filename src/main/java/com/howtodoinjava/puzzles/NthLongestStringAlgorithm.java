package com.howtodoinjava.puzzles;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class NthLongestStringAlgorithm {

  public static void main(String[] args) {
    List<String> list = List.of("Yuri", "Ron", "Interview", "Longest", "List", "Contain");

    for(int i=1; i<=4; i++){
      System.out.println("Words with " + i + "th length: " + findNthLongestElement(list, i));
    }
  }

  private static List<String> findNthLongestElement(List<String> list, int n) {
    if (n < 1) {
      return null; //Handle invalid case
    }

    TreeMap<Integer, List<String>> map = new TreeMap<>();

    for (String str : list) {
      Integer length = str.length();
      List<String> tempList = map.get(length) != null ? map.get(length) : new ArrayList<String>();
      tempList.add(str);
      map.put(length, tempList);
    }
    return map.get(map.descendingKeySet().toArray()[n - 1]);
  }
}

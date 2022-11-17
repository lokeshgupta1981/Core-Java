package com.howtodoinjava.core.collections;

import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;

public class ConvertArrayToList {

  public static void main(String[] args) {
    Map<String, Integer> map1 = Map.of("A", 1, "B", 2, "E", 5, "F", 6);
    Map<String, Integer> map2 = Map.of("A", 1, "B", 2, "C", 3, "D", 4);

    MapDifference<String, Integer> diff = Maps.difference(map1, map2);

    System.out.println(diff.areEqual());            //false

    System.out.println(diff.entriesInCommon());     //{A=1, B=2}
    System.out.println(diff.entriesOnlyOnLeft());   //{F=6, E=5}
    System.out.println(diff.entriesOnlyOnRight());  //{D=4, C=3}
  }

private static boolean checkEqualMapsWithArrayTypeValues(
    Map<String, Integer[]> firstMap,
    Map<String, Integer[]> secondMap) {

  if (firstMap.size() != secondMap.size())
    return false;

  return firstMap.entrySet().stream()
      .allMatch(e -> Arrays.equals(e.getValue(), secondMap.get(e.getKey())));
}

}

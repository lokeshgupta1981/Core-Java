package com.howtodoinjava.core.collections.map;

import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Assertions;

public class MapDifferenceDemo {

  public static void main(String[] args) {
    Map<String, Integer> map1 = Map.of("A", 1, "B", 2);
    Map<String, Integer> map2 = Map.of("A", 1, "B", 2);
    Map<String, Integer> map3 = Map.of("C", 1, "D", 2);

    Assertions.assertTrue(map1.equals(map2));
    Assertions.assertFalse(map1.equals(map3));

    Map<String, Integer[]> map4 = Map.of("A", new Integer[]{1}, "B", new Integer[]{2});
    Map<String, Integer[]> map5 = Map.of("A", new Integer[]{1}, "B", new Integer[]{2});

    Assertions.assertFalse(map4.equals(map5));
    Assertions.assertTrue(checkEqualMapsWithArrayTypeValues(map4, map5));

    Map<String, Integer> map6 = Map.of("A", 1, "B", 2, "C", 3, "D", 4);

    Assertions.assertTrue(map1.keySet().equals(map2.keySet()));
    Assertions.assertFalse(map1.keySet().equals(map6.keySet()));

    HashSet<String> unionKeys = new HashSet<>(map1.keySet());
    unionKeys.addAll(map3.keySet());
    unionKeys.removeAll(map1.keySet());

    Assertions.assertEquals(Set.of("C", "D"), unionKeys);

    //Guava MapDifference
    Map<String, Integer> map7 = Map.of("A", 1, "B", 2, "E", 5, "F", 6);
    Map<String, Integer> map8 = Map.of("A", 1, "B", 2, "C", 3, "D", 4);

    MapDifference diff = Maps.difference(map7, map8);

    Assertions.assertFalse(diff.areEqual());

    Assertions.assertEquals(Map.of("A", 1, "B", 2), diff.entriesInCommon());
    Assertions.assertEquals(Map.of("E", 5, "F", 6), diff.entriesOnlyOnLeft());
    Assertions.assertEquals(Map.of("C", 3, "D", 4), diff.entriesOnlyOnRight());

  }

  private static boolean checkEqualMapsWithArrayTypeValues(
      Map<String, Integer[]> firstMap,
      Map<String, Integer[]> secondMap) {

    if (firstMap.size() != secondMap.size()) {
      return false;
    }

    return firstMap.entrySet().stream()
        .allMatch(e -> Arrays.equals(e.getValue(), secondMap.get(e.getKey())));
  }

}

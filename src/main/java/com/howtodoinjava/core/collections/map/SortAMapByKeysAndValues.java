package com.howtodoinjava.core.collections.map;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class SortAMapByKeysAndValues {

  public static void main(String[] args) {
    Map<String, Integer> unsortedMap = Map.of("a", 1, "c", 3, "b", 2, "e", 5, "d", 4);

    LinkedHashMap<String, Integer> sortedMap = unsortedMap.entrySet()
        .stream()
        .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
        .collect(Collectors.toMap(
            Map.Entry::getKey,
            Map.Entry::getValue,
            (oldValue, newValue) -> oldValue, LinkedHashMap::new));

    System.out.println(sortedMap);

    Map<String, Integer> sortedTreeMap = new TreeMap<>(Comparator.reverseOrder());
    sortedTreeMap.putAll(unsortedMap);

    System.out.println(sortedTreeMap);
  }

}

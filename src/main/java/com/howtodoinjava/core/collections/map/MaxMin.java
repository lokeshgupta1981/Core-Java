package com.howtodoinjava.core.collections.map;

import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

public class MaxMin {

  public static void main(String[] args) {
    Map<String, Integer> map = Map.of("key1", 200, "key2", 100, "key3", 300);

    //1
    Optional<Entry<String, Integer>> maxEntry = map.entrySet()
        .stream()
        .max(Map.Entry.comparingByValue());
    Integer maxValue = maxEntry.get().getValue();

    Optional<Entry<String, Integer>> minEntry = map.entrySet()
        .stream()
        .min(Map.Entry.comparingByValue());
    Integer minValue = minEntry.get().getValue();

    System.out.println(maxValue);
    System.out.println(minValue);

    //2
    Entry<String, Integer> maxEntry1 = Collections.max(map.entrySet(),
        Comparator.comparing(Entry::getValue));
    System.out.println(maxEntry1.getValue());

    Entry<String, Integer> minEntry1 = Collections.min(map.entrySet(),
        Comparator.comparing(Entry::getValue));
    System.out.println(minEntry1.getValue());

    //3
    Entry<String, Integer> entryWithMaxValue = null;
    Entry<String, Integer> entryWithMinValue = null;

    for (Map.Entry<String, Integer> currentEntry : map.entrySet()) {
      if (entryWithMaxValue == null || currentEntry.getValue() > entryWithMaxValue.getValue()) {
        entryWithMaxValue = currentEntry;
      } else {
        entryWithMinValue = currentEntry;
      }
    }

    System.out.println("Entry with highest value: " + entryWithMaxValue);
    System.out.println("Entry with lowest value: " + entryWithMinValue);

  }
}

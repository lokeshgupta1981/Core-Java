package com.howtodoinjava.core.streams.collect;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Collecting Stream items into a Map with Collectors.toMap().
 * Article: https://howtodoinjava.com/java8/collect-stream-to-map/
 */
public class CollectStreamItemsToMap {

  public static void main(String[] args) {

    List<Item> items = List.of(
        new Item(1, "Item1"),
        new Item(2, "Item2"),
        new Item(3, "Item3"));

    List<Item> itemsWithDuplicates = List.of(
        new Item(1, "Item1"),
        new Item(2, "Item2"),
        new Item(3, "Item3-1"),
        new Item(3, "Item3-2"),
        new Item(3, "Item3-3"));

    // 1. Key and value mappers
    Map<Long, String> names = items.stream()
        .collect(Collectors.toMap(Item::id, Item::name));
    System.out.println("1. names = " + names);

    Map<Long, Item> byId = items.stream()
        .collect(Collectors.toMap(Item::id, Function.identity()));
    System.out.println("1. byId = " + byId);

    // 2. Duplicate keys throw IllegalStateException
    try {
      Map<Long, String> fails = itemsWithDuplicates.stream()
          .collect(Collectors.toMap(Item::id, Item::name));
      System.out.println("2. fails = " + fails);
    } catch (IllegalStateException e) {
      System.out.println("2. " + e);
    }

    // 3. Merge function decides which value to keep
    Map<Long, String> lastWins = itemsWithDuplicates.stream()
        .collect(Collectors.toMap(Item::id, Item::name, (oldValue, newValue) -> newValue));
    System.out.println("3. lastWins = " + lastWins);

    Map<Long, String> firstWins = itemsWithDuplicates.stream()
        .collect(Collectors.toMap(Item::id, Item::name, (oldValue, newValue) -> oldValue));
    System.out.println("3. firstWins = " + firstWins);

    Map<Long, String> joined = itemsWithDuplicates.stream()
        .collect(Collectors.toMap(Item::id, Item::name, (a, b) -> a + ", " + b));
    System.out.println("3. joined = " + joined);

    Map<Long, Integer> counts = itemsWithDuplicates.stream()
        .collect(Collectors.toMap(Item::id, item -> 1, Integer::sum));
    System.out.println("3. counts = " + counts);

    // 4. Null values throw NullPointerException
    List<Item> itemsWithNull = List.of(
        new Item(1, "Item1"),
        new Item(2, null),
        new Item(3, "Item3"));

    try {
      Map<Long, String> fails = itemsWithNull.stream()
          .collect(Collectors.toMap(Item::id, Item::name));
      System.out.println("4. fails = " + fails);
    } catch (NullPointerException e) {
      System.out.println("4. " + e + " at " + e.getStackTrace()[0]);
    }

    Map<Long, String> withNulls = itemsWithNull.stream()
        .collect(HashMap::new, (map, item) -> map.put(item.id(), item.name()), Map::putAll);
    System.out.println("4. withNulls = " + withNulls);

    Map<Long, String> withDefault = itemsWithNull.stream()
        .collect(Collectors.toMap(Item::id, item -> item.name() == null ? "" : item.name()));
    System.out.println("4. withDefault = " + withDefault);

    // 5. Map type with the supplier
    List<Item> unsortedItems = List.of(
        new Item(30, "Item30"),
        new Item(10, "Item10"),
        new Item(20, "Item20"));

    Map<Long, String> hashOrder = unsortedItems.stream()
        .collect(Collectors.toMap(Item::id, Item::name));
    System.out.println("5. hashOrder = " + hashOrder);

    LinkedHashMap<Long, String> insertionOrder = unsortedItems.stream()
        .collect(Collectors.toMap(Item::id, Item::name, (o, n) -> n, LinkedHashMap::new));
    System.out.println("5. insertionOrder = " + insertionOrder);

    TreeMap<Long, String> sortedKeys = unsortedItems.stream()
        .collect(Collectors.toMap(Item::id, Item::name, (o, n) -> n, TreeMap::new));
    System.out.println("5. sortedKeys = " + sortedKeys);

    // 6. Unmodifiable map
    Map<Long, String> unmodifiable = items.stream()
        .collect(Collectors.toUnmodifiableMap(Item::id, Item::name));
    System.out.println("6. unmodifiable = " + unmodifiable);

    try {
      unmodifiable.put(4L, "Item4");
    } catch (UnsupportedOperationException e) {
      System.out.println("6. " + e);
    }

    // 7. toMap() vs groupingBy()
    Map<Long, List<String>> grouped = itemsWithDuplicates.stream()
        .collect(Collectors.groupingBy(Item::id, Collectors.mapping(Item::name, Collectors.toList())));
    System.out.println("7. grouped = " + grouped);
  }
}

record Item(long id, String name) {

}

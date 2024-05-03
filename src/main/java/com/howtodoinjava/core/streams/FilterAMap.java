package com.howtodoinjava.core.streams;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FilterAMap {

  @SuppressWarnings("unused")
  public static void main(String[] args) {

    Map<Integer, User> usersMap = Map.of(
        1, new User(1, "Alex"),
        2, new User(2, "Allen"),
        3, new User(3, "Brian"),
        4, new User(4, "Bob"),
        5, new User(5, "Charles"),
        6, new User(6, "David"),
        7, new User(7, "Don"),
        8, new User(8, "Dave"));

    // Collect Keys filtered by List of Keys
    List<Integer> idList = List.of(1, 3, 5, 7);

    Map<Integer, User> filteredMap1 = usersMap.entrySet()
        .stream()
        .filter(entry -> idList.contains(entry.getKey()))
        .collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue));

    System.out.println(filteredMap1);

    // Collect entries filtered by List of Keys

    Map<Integer, User> filteredMap2 = usersMap.entrySet()
        .stream()
        .filter(entry -> idList.contains(entry.getValue().id()))
        .collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue));

    System.out.println(filteredMap2);

    // Collect Values filtered by List of Keys

    List<User> usersList1 = usersMap.values()
        .stream()
        .filter(user -> idList.contains(user.id()))
        .toList();

    System.out.println(usersList1);

    //3
    List<User> usersList2 = new ArrayList<>();

    usersMap.forEach((_, value) -> {
      if (idList.contains(value.id())) {
        usersList2.add(value);
      }
    });

    System.out.println(usersList2);

    System.out.println("====================");

    // Using Generic filters
    Predicate<Integer> predicate = key -> key > 4;
    Comparator<Integer> comparator = Comparator.comparing(Function.identity());

    Map<Integer, User> filteredMap = Filters.byKey(usersMap, predicate);
    System.out.println(filteredMap);

    Map<Integer, User> filteredAndSortedMap = Filters.sortedByKey(usersMap, predicate, comparator);
    System.out.println(filteredAndSortedMap);

    Predicate<User> valuePredicate = user -> user.name().startsWith("D");
    Comparator<User> valueComparator = Comparator.comparing(User::name);

    Map<Integer, User> filteredMapByValue = Filters.byValue(usersMap, valuePredicate);
    System.out.println(filteredMapByValue);

    Map<Integer, User> filteredAndSortedMapByValue = Filters.sortedByValue(usersMap, valuePredicate,
        valueComparator);
    System.out.println(filteredAndSortedMapByValue);

    Map<Integer, User> filteredMapByKeyAndValue = Filters.byValue(
        Filters.byKey(usersMap, predicate), valuePredicate);
    System.out.println(filteredMapByKeyAndValue);

    Map<Integer, User> filteredMapByKeyAndValue_V2 = usersMap.entrySet().stream()
        .filter((entry) -> entry.getKey() > 4 && entry.getValue().name().startsWith("D"))
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    System.out.println(filteredMapByKeyAndValue);
  }

  static final class Filters {

    private Filters() {
      throw new AssertionError("Cannot be instantiated");
    }

    public static <K, V> Map<K, V> byKey(Map<K, V> map, Predicate<K> predicate) {

      Objects.requireNonNull(map);
      Objects.requireNonNull(predicate);

      return map.entrySet()
          .stream()
          .filter(item -> predicate.test(item.getKey()))
          .collect(Collectors.toMap(
              Map.Entry::getKey, Map.Entry::getValue));
    }

    public static <K, V> Map<K, V> sortedByKey(
        Map<K, V> map, Predicate<K> predicate, Comparator<K> c) {

      Objects.requireNonNull(map);
      Objects.requireNonNull(predicate);
      Objects.requireNonNull(c);

      return map.entrySet()
          .stream()
          .filter(item -> predicate.test(item.getKey()))
          .sorted(Map.Entry.comparingByKey(c))
          .collect(Collectors.toMap(
              Map.Entry::getKey, Map.Entry::getValue,
              (_, c2) -> c2, LinkedHashMap::new));
    }


    public static <K, V> Map<K, V> byValue(
        Map<K, V> map, Predicate<V> predicate) {

      Objects.requireNonNull(map);
      Objects.requireNonNull(predicate);

      return map.entrySet()
          .stream()
          .filter(item -> predicate.test(item.getValue()))
          .collect(Collectors.toMap(
              Map.Entry::getKey, Map.Entry::getValue));
    }

    public static <K, V> Map<K, V> sortedByValue(Map<K, V> map,
        Predicate<V> predicate, Comparator<V> c) {

      Objects.requireNonNull(map);
      Objects.requireNonNull(predicate);
      Objects.requireNonNull(c);

      return map.entrySet()
          .stream()
          .filter(item -> predicate.test(item.getValue()))
          .sorted(Map.Entry.comparingByValue(c))
          .collect(Collectors.toMap(
              Map.Entry::getKey, Map.Entry::getValue,
              (_, c2) -> c2, LinkedHashMap::new));
    }
  }
}

  record User(Integer id, String name) {
  }

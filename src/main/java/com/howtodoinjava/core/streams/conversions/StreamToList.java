package com.howtodoinjava.core.streams.conversions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Collects the items of a Stream into a List with Stream.toList(), Collectors.toList(),
 * Collectors.toUnmodifiableList() and Collectors.toCollection(), and prints the result,
 * the list class and the exceptions each unmodifiable list throws.
 */
public class StreamToList {

  record Fruit(String name, int price) {
  }

  public static void main(final String[] args) {

    // 1. Quick reference
    List<String> fruits = Stream.of("apple", "banana", "cherry").toList();
    List<String> fruitList = Stream.of("apple", "banana", "cherry").collect(Collectors.toList());
    System.out.println("toList(): " + fruits);
    System.out.println("Collectors.toList(): " + fruitList);

    fruitList.add("mango");
    System.out.println("Collectors.toList() after add: " + fruitList);
    try {
      fruits.add("mango");
    } catch (UnsupportedOperationException e) {
      System.out.println("toList() add: " + e);
    }

    try {
      Stream.of("apple", null).collect(Collectors.toUnmodifiableList());
    } catch (NullPointerException e) {
      System.out.println("quick toUnmodifiableList() with null: " + e);
    }
    System.out.println("quick toList() with null: " + Stream.of("apple", null).toList());

    // 2. Stream.toList()
    List<String> sorted = Stream.of("cherry", "apple", "banana").sorted().toList();
    System.out.println("sorted toList(): " + sorted);
    try {
      sorted.set(0, "mango");
    } catch (UnsupportedOperationException e) {
      System.out.println("toList() set: " + e);
    }
    try {
      sorted.sort(null);
    } catch (UnsupportedOperationException e) {
      System.out.println("toList() sort: " + e);
    }

    List<String> withNull = Stream.of("apple", null, "cherry").toList();
    System.out.println("toList() with null: " + withNull);
    System.out.println("toList() contains(null): " + withNull.contains(null));

    // 3. Collectors.toUnmodifiableList()
    List<String> unmodifiable = Stream.of("apple", "banana", "cherry")
        .collect(Collectors.toUnmodifiableList());
    System.out.println("toUnmodifiableList(): " + unmodifiable);
    try {
      Stream.of("apple", null, "cherry").collect(Collectors.toUnmodifiableList());
    } catch (NullPointerException e) {
      System.out.println("toUnmodifiableList() with null: " + e);
    }
    try {
      unmodifiable.contains(null);
    } catch (NullPointerException e) {
      System.out.println("toUnmodifiableList() contains(null): " + e);
    }

    // 4. Collectors.toList() with null
    List<String> mutableWithNull = Stream.of("apple", null, "cherry").collect(Collectors.toList());
    System.out.println("Collectors.toList() with null: " + mutableWithNull);

    // 5. List classes
    System.out.println("class toList(): " + fruits.getClass().getName());
    System.out.println("class Collectors.toList(): " + fruitList.getClass().getName());
    System.out.println("class toUnmodifiableList(): " + unmodifiable.getClass().getName());

    System.out.println(Stream.of("apple").toList().getClass().getName());
    System.out.println(Stream.of("apple").collect(Collectors.toList()).getClass().getName());

    // 6. Collectors.toCollection()
    ArrayList<String> arrayList = Stream.of("apple", "banana", "cherry")
        .collect(Collectors.toCollection(ArrayList::new));
    LinkedList<String> linkedList = Stream.of("apple", "banana", "cherry")
        .collect(Collectors.toCollection(LinkedList::new));
    linkedList.addFirst("mango");
    System.out.println("ArrayList: " + arrayList);
    System.out.println("LinkedList: " + linkedList);

    // 7. Filter, then collect
    List<Fruit> cheapFruits = Stream.of(
            new Fruit("apple", 5),
            new Fruit("banana", 3),
            new Fruit("cherry", 9),
            new Fruit("mango", 7))
        .filter(f -> f.price() < 6)
        .toList();
    System.out.println("cheap fruits: " + cheapFruits);

    List<String> cheapNames = Stream.of(new Fruit("apple", 5), new Fruit("cherry", 9))
        .filter(f -> f.price() < 6)
        .map(Fruit::name)
        .toList();
    System.out.println("cheap names: " + cheapNames);

    // 8. Primitive streams
    List<Integer> numbers = IntStream.rangeClosed(1, 5).boxed().toList();
    List<String> labels = IntStream.rangeClosed(1, 3).mapToObj(i -> "item-" + i).toList();
    List<Integer> fromArray = Arrays.stream(new int[]{4, 8, 15}).boxed().toList();
    System.out.println("IntStream boxed: " + numbers);
    System.out.println("IntStream mapToObj: " + labels);
    System.out.println("int[] boxed: " + fromArray);

    // 9. Infinite stream
    List<Integer> firstTen = IntStream.iterate(1, i -> i + 1)
        .limit(10)
        .boxed()
        .toList();
    List<Integer> evens = Stream.iterate(2, i -> i <= 10, i -> i + 2).toList();
    System.out.println("first ten: " + firstTen);
    System.out.println("evens: " + evens);

    // 10. Parallel stream keeps the encounter order
    List<Integer> parallel = IntStream.rangeClosed(1, 10).parallel().boxed().toList();
    System.out.println("parallel toList(): " + parallel);

    // 11. Stream can be consumed only once
    Stream<String> stream = Stream.of("apple", "banana");
    stream.toList();
    try {
      stream.toList();
    } catch (IllegalStateException e) {
      System.out.println("second toList(): " + e);
    }

    // 12. Full stack trace of the UnsupportedOperationException
    try {
      fruits.add("mango");
    } catch (UnsupportedOperationException e) {
      e.printStackTrace(System.out);
    }
  }
}

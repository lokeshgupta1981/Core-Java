package com.howtodoinjava.core.streams;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.time.*;
import java.nio.file.*;
import java.math.*;
import java.io.*;

import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.stream.*;
import java.util.function.*;
import java.io.*;
import java.nio.file.*;
import java.nio.charset.*;
import java.time.*;
import java.math.*;

/**
 * Examples for the tutorial "Java Stream to List: toList() vs Collectors.toList()".
 * https://howtodoinjava.com/java8/convert-stream-to-list/
 */
public class StreamTolistVsCollectorsTolist {
    static record Fruit(String name, int price) {}
    public static void main(String[] args) throws Exception {
        {
            List<String> fruits = Stream.of("apple", "banana", "cherry").toList();                       // [apple, banana, cherry]
            show("fruits", fruits);
            List<String> fruitList = Stream.of("apple", "banana", "cherry").collect(Collectors.toList()); // [apple, banana, cherry]
            show("fruitList", fruitList);

            fruitList.add("mango");                // [apple, banana, cherry, mango]
            try { boolean added = fruits.add("mango"); show("added", added); } catch (Throwable _t) { System.out.println("added -> " + _t); }

            try { List<String> unmodifiable = Stream.of("apple", null).collect(Collectors.toUnmodifiableList()); show("unmodifiable", unmodifiable); } catch (Throwable _t) { System.out.println("unmodifiable -> " + _t); }
            List<String> withNull = Stream.of("apple", null).toList();                                     // [apple, null]
            show("withNull", withNull);
        }
        {
            List<String> sorted = Stream.of("cherry", "apple", "banana").sorted().toList();   // [apple, banana, cherry]
            show("sorted", sorted);

            String first = sorted.get(0);               // "apple"
            show("first", first);
            try { String replaced = sorted.set(0, "mango"); show("replaced", replaced); } catch (Throwable _t) { System.out.println("replaced -> " + _t); }
            try { sorted.sort(null);  } catch (Throwable _t) { System.out.println("-> " + _t); }
        }
        {
            List<String> unmodifiable = Stream.of("apple", "banana", "cherry")
                    .collect(Collectors.toUnmodifiableList());          // [apple, banana, cherry]

            try { List<String> withNull = Stream.of("apple", null, "cherry").collect(Collectors.toUnmodifiableList()); show("withNull", withNull); } catch (Throwable _t) { System.out.println("withNull -> " + _t); }

            try { boolean hasNull = unmodifiable.contains(null); show("hasNull", hasNull); } catch (Throwable _t) { System.out.println("hasNull -> " + _t); }
        }
        {
            List<String> fruitList = Stream.of("apple", "banana", "cherry")
                    .collect(Collectors.toList());                     // [apple, banana, cherry]

            fruitList.add("mango");                                // [apple, banana, cherry, mango]
            String className = fruitList.getClass().getName();     // "java.util.ArrayList"
            show("className", className);

            List<String> withNull = Stream.of("apple", null, "cherry").collect(Collectors.toList());   // [apple, null, cherry]
            show("withNull", withNull);
        }
        {
            ArrayList<String> arrayList = Stream.of("apple", "banana", "cherry")
                    .collect(Collectors.toCollection(ArrayList::new));     // [apple, banana, cherry]

            LinkedList<String> linkedList = Stream.of("apple", "banana", "cherry")
                    .collect(Collectors.toCollection(LinkedList::new));    // [apple, banana, cherry]

            linkedList.addFirst("mango");                              // [mango, apple, banana, cherry]
        }
        {
            List<Fruit> cheapFruits = Stream.of(
            new Fruit("apple", 5),
            new Fruit("banana", 3),
            new Fruit("cherry", 9),
            new Fruit("mango", 7))
                    .filter(f -> f.price() < 6)
                    .toList();                 // [Fruit[name=apple, price=5], Fruit[name=banana, price=3]]
        }
        {
            List<String> cheapNames = Stream.of(new Fruit("apple", 5), new Fruit("cherry", 9))
                    .filter(f -> f.price() < 6)
                    .map(Fruit::name)
                    .toList();                 // [apple]
        }
        {
            List<Integer> numbers = IntStream.rangeClosed(1, 5).boxed().toList();              // [1, 2, 3, 4, 5]
            show("numbers", numbers);
            List<String> labels = IntStream.rangeClosed(1, 3).mapToObj(i -> "item-" + i).toList(); // [item-1, item-2, item-3]
            show("labels", labels);
            List<Integer> fromArray = Arrays.stream(new int[]{4, 8, 15}).boxed().toList();     // [4, 8, 15]
            show("fromArray", fromArray);
        }
        {
            List<Integer> firstTen = IntStream.iterate(1, i -> i + 1)
                    .limit(10)
                    .boxed()
                    .toList();                                       // [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]

            List<Integer> evens = Stream.iterate(2, i -> i <= 10, i -> i + 2).toList();   // [2, 4, 6, 8, 10]
            show("evens", evens);
        }
        {
            String toListClass = Stream.of("apple").toList().getClass().getName();                       // "java.util.ImmutableCollections$ListN"
            show("toListClass", toListClass);
            String collectorClass = Stream.of("apple").collect(Collectors.toList()).getClass().getName();  // "java.util.ArrayList"
            show("collectorClass", collectorClass);
        }
        {
            List<Integer> parallel = IntStream.rangeClosed(1, 10).parallel().boxed().toList();   // [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
            show("parallel", parallel);
        }
        {
            Stream<String> stream = Stream.of("apple", "banana");
            List<String> first = stream.toList();    // [apple, banana]
            show("first", first);
            try { List<String> second = stream.toList(); show("second", second); } catch (Throwable _t) { System.out.println("second -> " + _t); }
        }
        {
            List<String> cart = new ArrayList<>(List.of("bread"));
            boolean changed = cart.addAll(Stream.of("milk", "eggs").toList());   // true
            show("changed", changed);
            List<String> merged = cart;                                          // [bread, milk, eggs]
            show("merged", merged);
        }
    }

    static void show(String name, Object value) {
        String text = value instanceof int[] a ? Arrays.toString(a)
        : value instanceof long[] a ? Arrays.toString(a)
        : value instanceof double[] a ? Arrays.toString(a)
        : value instanceof Object[] a ? Arrays.deepToString(a)
        : value instanceof String str ? "\"" + str + "\""
        : String.valueOf(value);
        System.out.println(name + " = " + text);
    }
}

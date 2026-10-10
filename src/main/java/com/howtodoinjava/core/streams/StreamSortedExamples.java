package com.howtodoinjava.core.streams;

import java.util.regex.*;
import java.net.*;
import java.text.*;
import java.nio.file.attribute.*;
import java.security.*;
import java.nio.charset.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.time.*;
import java.nio.file.*;
import java.math.*;
import java.io.*;

/**
 * Examples for the tutorial "Java Stream sorted(): How to Sort a Stream in Java".
 * https://howtodoinjava.com/java8/stream-sorted-method/
 */
public class StreamSortedExamples {
    static record Recipe(String name, int minutes, double rating) {}
    static record Dish(String name, int calories) implements Comparable<Dish> {
        @Override
        public int compareTo(Dish other) {
            return Integer.compare(calories, other.calories);
        }
    }
    static List<String> quickDinners(List<Recipe> recipes) {
        return recipes.stream()
                .filter(r -> r.minutes() <= 30)
                .sorted(Comparator.comparingDouble(Recipe::rating).reversed()
                .thenComparingInt(Recipe::minutes))
                .limit(3)
                .map(Recipe::name)
                .toList();
    }
    public static void main(String[] args) throws Exception {
        {
            List<Integer> scores = List.of(42, 7, 19, 3);
            List<Integer> ascending = scores.stream().sorted().toList();                             // [3, 7, 19, 42]
            show("ascending", ascending);
            List<Integer> descending = scores.stream().sorted(Comparator.reverseOrder()).toList();   // [42, 19, 7, 3]
            show("descending", descending);
            List<String> fruits = List.of("kiwi", "fig", "banana");
            List<String> byLength = fruits.stream().sorted(Comparator.comparing(String::length)).toList();   // [fig, kiwi, banana]
            show("byLength", byLength);
            List<Integer> original = scores;                                                         // [42, 7, 19, 3]
            show("original", original);
        }
        {
            List<Integer> evens = Stream.of(9, 4, 7, 2, 8).filter(n -> n % 2 == 0).sorted().toList();   // [2, 4, 8]
            show("evens", evens);
        }
        {
            List<Integer> asc = Stream.of(5, 1, 4, 2, 3).sorted().toList();                              // [1, 2, 3, 4, 5]
            show("asc", asc);
            List<Integer> desc = Stream.of(5, 1, 4, 2, 3).sorted(Comparator.reverseOrder()).toList();    // [5, 4, 3, 2, 1]
            show("desc", desc);
            List<LocalDate> dates = Stream.of(LocalDate.of(2026, 3, 1), LocalDate.of(2025, 12, 24)).sorted().toList();   // [2025-12-24, 2026-03-01]
            show("dates", dates);
        }
        {
            List<String> tags = List.of("java", "Spring", "api", "Docker");
            List<String> natural = tags.stream().sorted().toList();                                  // [Docker, Spring, api, java]
            show("natural", natural);
            List<String> ignoreCase = tags.stream().sorted(String.CASE_INSENSITIVE_ORDER).toList();  // [api, Docker, java, Spring]
            show("ignoreCase", ignoreCase);
            List<String> reversed = tags.stream().sorted(Comparator.reverseOrder()).toList();        // [java, api, Spring, Docker]
            show("reversed", reversed);
            List<String> shortFirst = tags.stream().sorted(Comparator.comparingInt(String::length)).toList();   // [api, java, Spring, Docker]
            show("shortFirst", shortFirst);
        }
        {
            int[] asc = IntStream.of(8, 3, 5).sorted().toArray();                                  // [3, 5, 8]
            show("asc", asc);
            List<Integer> desc = IntStream.of(8, 3, 5).boxed().sorted(Comparator.reverseOrder()).toList();   // [8, 5, 3]
            show("desc", desc);
        }
        {
            List<Recipe> recipes = List.of(new Recipe("lasagna", 90, 4.8), new Recipe("omelette", 10, 4.2), new Recipe("pancakes", 25, 4.5));
            List<String> byName = recipes.stream().sorted(Comparator.comparing(Recipe::name)).map(Recipe::name).toList();   // [lasagna, omelette, pancakes]
            show("byName", byName);
            List<String> quickest = recipes.stream().sorted(Comparator.comparingInt(Recipe::minutes)).map(Recipe::name).toList();   // [omelette, pancakes, lasagna]
            show("quickest", quickest);
        }
        {
            List<Recipe> recipes = List.of(new Recipe("lasagna", 90, 4.8), new Recipe("omelette", 10, 4.2), new Recipe("pancakes", 25, 4.5));
            List<String> bestFirst = recipes.stream().sorted(Comparator.comparingDouble(Recipe::rating).reversed()).map(Recipe::name).toList();   // [lasagna, pancakes, omelette]
            show("bestFirst", bestFirst);
            List<String> bestFirst2 = recipes.stream().sorted(Comparator.comparing(Recipe::rating, Comparator.reverseOrder())).map(Recipe::name).toList();   // [lasagna, pancakes, omelette]
            show("bestFirst2", bestFirst2);
        }
        {
            List<Dish> dishes = List.of(new Dish("soup", 180), new Dish("salad", 120), new Dish("curry", 450));
            List<String> lightFirst = dishes.stream().sorted().map(Dish::name).toList();     // [salad, soup, curry]
            show("lightFirst", lightFirst);
        }
        {
            List<Recipe> recipes = List.of(new Recipe("lasagna", 90, 4.8), new Recipe("omelette", 10, 4.2));
            try { List<Recipe> broken = recipes.stream().sorted().toList(); show("broken", broken); } catch (Throwable _t) { System.out.println("broken -> " + _t); }
        }
        {
            List<String> cities = Arrays.asList("Pune", null, "Delhi");
            try { List<String> crash = cities.stream().sorted().toList(); show("crash", crash); } catch (Throwable _t) { System.out.println("crash -> " + _t); }
            List<String> nullsLast = cities.stream().sorted(Comparator.nullsLast(Comparator.naturalOrder())).toList();   // [Delhi, Pune, null]
            show("nullsLast", nullsLast);
        }
        {
            List<Integer> numbers = new ArrayList<>(List.of(3, 1, 2));
            List<Integer> copy = numbers.stream().sorted().toList();   // [1, 2, 3]
            show("copy", copy);
            List<Integer> before = numbers;                            // [3, 1, 2]
            show("before", before);
            numbers.sort(null);
            List<Integer> after = numbers;                             // [1, 2, 3]
            show("after", after);
        }
        {
            List<Recipe> menu = List.of(new Recipe("lasagna", 90, 4.8), new Recipe("omelette", 10, 4.5), new Recipe("pancakes", 25, 4.5), new Recipe("salad", 15, 4.7), new Recipe("toast", 5, 3.9));
            List<String> box = quickDinners(menu);   // [salad, omelette, pancakes]
            show("box", box);
        }
        {
            Map<String, Integer> stock = Map.of("apple", 5, "banana", 2, "cherry", 9);
            List<String> lowStockFirst = stock.entrySet().stream().sorted(Map.Entry.comparingByValue()).map(Map.Entry::getKey).toList();   // [banana, apple, cherry]
            show("lowStockFirst", lowStockFirst);
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

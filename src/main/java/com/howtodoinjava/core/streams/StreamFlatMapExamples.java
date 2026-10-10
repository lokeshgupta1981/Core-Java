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
 * Examples for the tutorial "Java Stream flatMap(): Flatten Nested Lists with Examples".
 * https://howtodoinjava.com/java8/stream-flatmap-example/
 */
public class StreamFlatMapExamples {
    static record Recipe(String name, List<String> ingredients) {}
    static record Day(String name, List<Recipe> meals) {}
    public static void main(String[] args) throws Exception {
        {
            List<List<String>> baskets = List.of(List.of("apple", "kiwi"), List.of("banana"), List.of());

            List<String> fruits = baskets.stream().flatMap(List::stream).toList();                              // [apple, kiwi, banana]
            show("fruits", fruits);
            List<String> words = Stream.of("green tea", "black tea").flatMap(s -> Arrays.stream(s.split(" "))).toList();   // [green, tea, black, tea]
            show("words", words);
            List<String> found = Stream.of(Optional.of("jam"), Optional.<String>empty()).flatMap(Optional::stream).toList(); // [jam]
            show("found", found);
            int total = Stream.of(new int[] {1, 2}, new int[] {3}).flatMapToInt(Arrays::stream).sum();           // 6
            show("total", total);
        }
        {
            AtomicInteger pulled = new AtomicInteger();
            Optional<Integer> first = Stream.of(1, 2)
                    .flatMap(n -> IntStream.range(0, 1000).peek(i -> pulled.incrementAndGet()).boxed())
                    .findFirst();
            int seen = pulled.get();                        // 1
            show("seen", seen);
            List<String> rest = Stream.of("a", "b").flatMap(s -> s.equals("a") ? null : Stream.of(s)).toList();   // [b]
            show("rest", rest);
        }
        {
            List<List<Integer>> pages = List.of(List.of(1, 2, 3), List.of(4, 5), List.of(6, 7, 8));
            List<Integer> all = pages.stream().flatMap(List::stream).toList();       // [1, 2, 3, 4, 5, 6, 7, 8]
            show("all", all);
        }
        {
            String[][] grid = {{"a", "b"}, {"c", "d"}, {"e"}};
            List<String> cells = Arrays.stream(grid).flatMap(Arrays::stream).toList();   // [a, b, c, d, e]
            show("cells", cells);
        }
        {
            String text = "salt and pepper\n\n  olive oil";
            List<String> words = text.lines()
                    .flatMap(line -> Arrays.stream(line.strip().split("\\s+")))
                    .filter(w -> !w.isEmpty())
                    .toList();                              // [salt, and, pepper, olive, oil]
        }
        {
            Path file = Files.createTempFile("notes", ".txt");
            Files.writeString(file, "salt and pepper\n\n  olive oil\n");
            long wordCount;
            try (Stream<String> lines = Files.lines(file)) {
                wordCount = lines.flatMap(line -> Arrays.stream(line.strip().split("\\s+")))
                        .filter(w -> !w.isEmpty())
                        .count();
            }
            long countedWords = wordCount;                  // 5
            show("countedWords", countedWords);
            boolean deleted = Files.deleteIfExists(file);   // true
            show("deleted", deleted);
        }
        {
            List<Recipe> recipes = List.of(
            new Recipe("Pancakes", List.of("flour", "milk", "eggs")),
            new Recipe("Omelette", List.of("eggs", "cheese")));

            List<String> ingredients = recipes.stream()
                    .flatMap(r -> r.ingredients().stream())
                    .distinct()
                    .sorted()
                    .toList();                              // [cheese, eggs, flour, milk]
        }
        {
            Map<String, Integer> prices = Map.of("apple", 5, "banana", 3);
            List<Integer> known = Stream.of("apple", "kiwi", "banana")
                    .map(name -> Optional.ofNullable(prices.get(name)))
                    .flatMap(Optional::stream)
                    .toList();                              // [5, 3]
        }
        {
            List<String> rooms = List.of("A", "B");
            List<String> slots = List.of("9:00", "10:00");
            List<String> options = rooms.stream()
                    .flatMap(room -> slots.stream().map(slot -> room + " " + slot))
                    .toList();                              // [A 9:00, A 10:00, B 9:00, B 10:00]
        }
        {
            List<List<Integer>> groups = List.of(List.of(1, 2, 3), List.of(4, 5), List.of(6, 7, 8));
            int sum = groups.stream().flatMapToInt(g -> g.stream().mapToInt(Integer::intValue)).sum();   // 36
            show("sum", sum);
            long letters = Stream.of("tea", "jam").flatMapToInt(String::chars).count();                 // 6
            show("letters", letters);
        }
        {
            Recipe pancakes = new Recipe("Pancakes", List.of("flour", "milk", "eggs"));
            Recipe omelette = new Recipe("Omelette", List.of("eggs", "cheese"));
            List<Day> week = List.of(new Day("Mon", List.of(pancakes)), new Day("Tue", List.of(omelette, pancakes)));

            Map<String, Long> shoppingList = week.stream()
                    .flatMap(day -> day.meals().stream())
                    .flatMap(recipe -> recipe.ingredients().stream())
                    .collect(Collectors.groupingBy(Function.identity(), TreeMap::new, Collectors.counting()));   // {cheese=1, eggs=3, flour=2, milk=2}
        }
        {
            List<List<List<Integer>>> cube = List.of(List.of(List.of(1, 2), List.of(3)), List.of(List.of(4)));
            List<Integer> flat = cube.stream().flatMap(List::stream).flatMap(List::stream).toList();   // [1, 2, 3, 4]
            show("flat", flat);
        }
        {
            List<Recipe> menu = List.of(new Recipe("Tea", List.of("water", "tea")), new Recipe("Toast", List.of("bread")));
            Map<Integer, List<String>> bySize = menu.stream().collect(Collectors.groupingBy(r -> r.ingredients().size(), TreeMap::new, Collectors.flatMapping(r -> r.ingredients().stream(), Collectors.toList())));   // {1=[bread], 2=[water, tea]}
            show("bySize", bySize);
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

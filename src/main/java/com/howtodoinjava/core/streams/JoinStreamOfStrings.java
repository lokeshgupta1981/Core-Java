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

import java.time.format.*;

/**
 * Examples for the tutorial "Join a Stream of Strings in Java: joining() and String.join()".
 * https://howtodoinjava.com/java8/join-stream-of-strings/
 */
public class JoinStreamOfStrings {
    static record Fruit(String name, int stock) {}
    static String humanList(List<String> items) {
        if (items.size() <= 1) {
            return String.join("", items);
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < items.size(); i++) {
            if (i > 0) {
                sb.append(i == items.size() - 1 ? " and " : ", ");
            }
            sb.append(items.get(i));
        }
        return sb.toString();
    }
    static String csvCell(String value) {
        return "\"" + value.replace("\"", "\"\"") + "\"";
    }
    public static void main(String[] args) throws Exception {
        {
            List<String> fruits = List.of("apple", "banana", "cherry");

            String plain = fruits.stream().collect(Collectors.joining());                  // "applebananacherry"
            show("plain", plain);
            String csv = fruits.stream().collect(Collectors.joining(", "));                // "apple, banana, cherry"
            show("csv", csv);
            String wrapped = fruits.stream().collect(Collectors.joining(", ", "[", "]"));  // "[apple, banana, cherry]"
            show("wrapped", wrapped);
            String simple = String.join(", ", fruits);                                     // "apple, banana, cherry"
            show("simple", simple);
        }
        {
            List<String> basket = List.of("cherry", "apple", "kiwi", "banana");

            String label = basket.stream()
                    .filter(name -> name.length() > 4)
                    .sorted()
                    .map(String::toUpperCase)
                    .collect(Collectors.joining(" | "));           // "APPLE | BANANA | CHERRY"
        }
        {
            String ids = Stream.of(3, 7, 9).map(String::valueOf).collect(Collectors.joining(","));    // "3,7,9"
            show("ids", ids);
            String range = IntStream.rangeClosed(1, 5).mapToObj(Integer::toString).collect(Collectors.joining("-"));  // "1-2-3-4-5"
            show("range", range);

            List<Fruit> stock = List.of(new Fruit("apple", 5), new Fruit("banana", 3));
            String report = stock.stream()
                    .map(f -> f.name() + "=" + f.stock())
                    .collect(Collectors.joining("; "));            // "apple=5; banana=3"
        }
        {
            List<String> tags = Arrays.asList("apple", null, "cherry");

            String unsafe = tags.stream().collect(Collectors.joining(","));                              // "apple,null,cherry"
            show("unsafe", unsafe);
            String safe = tags.stream().filter(Objects::nonNull).collect(Collectors.joining(","));       // "apple,cherry"
            show("safe", safe);
            String marked = tags.stream().map(t -> Objects.toString(t, "-")).collect(Collectors.joining(","));  // "apple,-,cherry"
            show("marked", marked);
        }
        {
            List<String> none = List.of();

            String empty = none.stream().collect(Collectors.joining(","));               // ""
            show("empty", empty);
            String brackets = none.stream().collect(Collectors.joining(",", "[", "]"));  // "[]"
            show("brackets", brackets);
        }
        {
            String path = String.join("/", "usr", "local", "bin");             // "usr/local/bin"
            show("path", path);

            String[] parts = {"2026", "10", "10"};
            String date = String.join("-", parts);                              // "2026-10-10"
            show("date", date);

            List<String> fruits = List.of("apple", "banana", "cherry");
            String line = String.join(", ", fruits);                            // "apple, banana, cherry"
            show("line", line);

            Set<String> sorted = new TreeSet<>(Set.of("kiwi", "apple", "fig"));
            String ordered = String.join(",", sorted);                          // "apple,fig,kiwi"
            show("ordered", ordered);
        }
        {
            String withNull = String.join(",", "apple", null);                 // "apple,null"
            show("withNull", withNull);
            try { String noDelimiter = String.join(null, "apple", "banana"); show("noDelimiter", noDelimiter); } catch (Throwable _t) { System.out.println("noDelimiter -> " + _t); }
        }
        {
            StringJoiner joiner = new StringJoiner(", ", "{", "}");
            joiner.setEmptyValue("no fruits");
            String before = joiner.toString();                  // "no fruits"
            show("before", before);

            joiner.add("apple").add("banana");
            String after = joiner.toString();                   // "{apple, banana}"
            show("after", after);
        }
        {
            String three = humanList(List.of("apple", "banana", "cherry"));    // "apple, banana and cherry"
            show("three", three);
            String one = humanList(List.of("apple"));                            // "apple"
            show("one", one);
        }
        {
            List<String> categories = List.of("fruit", "dairy", "bakery");

            String placeholders = categories.stream().map(c -> "?").collect(Collectors.joining(", ", "(", ")"));   // "(?, ?, ?)"
            show("placeholders", placeholders);
            String sql = "SELECT name FROM product WHERE category IN " + placeholders;   // "SELECT name FROM product WHERE category IN (?, ?, ?)"
            show("sql", sql);
        }
        {
            List<String> row = List.of("apple", "red, sweet", "5");
            String csvRow = row.stream().map(v -> csvCell(v)).collect(Collectors.joining(","));    // "apple","red, sweet","5"
            show("csvRow", csvRow);
        }
        {
            String quoted = Stream.of("apple", "fig").map(s -> "'" + s + "'").collect(Collectors.joining(", "));   // "'apple', 'fig'"
            show("quoted", quoted);
        }
        {
            String reversed = String.join(",", List.of("apple", "banana", "cherry").reversed());   // "cherry,banana,apple"
            show("reversed", reversed);
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

package com.howtodoinjava.core.streams;

import java.util.regex.*;
import java.net.*;
import java.text.*;
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
 * Examples for the tutorial "Java Streams Tutorial: Every Stream Guide Grouped by Task".
 * https://howtodoinjava.com/java/stream/java-streams-guide/
 */
public class StreamTasksOverview {

    public static void main(String[] args) throws Exception {
        {
            List<String> orders = List.of("tea", "coffee", "juice", "tea", "water");

            List<String> drinks = orders.stream()              // 1. source
                    .distinct()                                // 2. intermediate operations
                    .filter(d -> d.length() > 3)
                    .map(String::toUpperCase)
                    .sorted()
                    .toList();                                 // 3. terminal operation
            String result = drinks.toString();                 // "[COFFEE, JUICE, WATER]"
            show("result", result);
            Map<String, Long> counts = orders.stream()
                    .collect(Collectors.groupingBy(d -> d, TreeMap::new, Collectors.counting()));
            String grouped = counts.toString();                // "{coffee=1, juice=1, tea=2, water=1}"
            show("grouped", grouped);
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

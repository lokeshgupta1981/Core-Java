package com.howtodoinjava.core.array;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.time.*;
import java.nio.file.*;
import java.math.*;
import java.io.*;

import java.util.regex.*;
import java.nio.charset.*;
import java.nio.*;
import java.security.*;
import java.lang.reflect.*;
import java.lang.invoke.*;

/**
 * Examples for the tutorial "Java Stream to Array and Array to Stream Conversion".
 * https://howtodoinjava.com/java/array/convert-between-stream-and-array/
 */
public class ConvertBetweenArrayAndStream {
    static <T> T[] toSortedArray(Collection<T> items, Comparator<? super T> order, IntFunction<T[]> generator) {
        return items.stream().sorted(order).toArray(generator);
    }
    public static void main(String[] args) throws Exception {
        {
            String[] hosts = {"db1", "db2", "cache"};
            String[] upper = Arrays.stream(hosts).map(String::toUpperCase).toArray(String[]::new);   // [DB1, DB2, CACHE]
            show("upper", upper);
            int[] ports = IntStream.of(8080, 9090).toArray();                                        // [8080, 9090]
            show("ports", ports);
        }
        {
            String[] fruits = {"apple", "kiwi", "fig", "plum"};
            List<String> all = Arrays.stream(fruits).toList();              // [apple, kiwi, fig, plum]
            show("all", all);
            List<String> middle = Arrays.stream(fruits, 1, 3).toList();     // [kiwi, fig]
            show("middle", middle);
            int[] marks = {40, 75, 90};
            int total = Arrays.stream(marks).sum();                         // 205
            show("total", total);
            double avg = Arrays.stream(marks).average().orElse(0);          // 68.33333333333333
            show("avg", avg);
        }
        {
            int[] ids = {3, 1, 2};
            long wrongCount = Stream.of(ids).count();             // 1, a Stream<int[]>
            show("wrongCount", wrongCount);
            long rightCount = IntStream.of(ids).count();          // 3
            show("rightCount", rightCount);
            String[] words = {"a", "b"};
            long words2 = Stream.of(words).count();               // 2, same as Arrays.stream(words)
            show("words2", words2);
        }
        {
            List<String> names = List.of("ana", "li", "raj");
            Object[] plain = names.stream().toArray();                          // [ana, li, raj]
            show("plain", plain);
            String[] typed = names.stream().toArray(String[]::new);             // [ana, li, raj]
            show("typed", typed);
            String[] lambda = names.stream().toArray(size -> new String[size]); // [ana, li, raj]
            show("lambda", lambda);
        }
        {
            Stream<Object> mixed = Stream.of("ok", 42);
            try { String[] fails = mixed.toArray(String[]::new); show("fails", fails); } catch (Throwable _t) { System.out.println("fails -> " + _t); }
        }
        {
            Stream<Integer> boxedScores = Stream.of(70, 85, 90);
            int[] raw = boxedScores.mapToInt(Integer::intValue).toArray();      // [70, 85, 90]
            show("raw", raw);
            Integer[] objects = IntStream.of(raw).boxed().toArray(Integer[]::new);   // [70, 85, 90]
            show("objects", objects);
            long[] big = LongStream.rangeClosed(1, 3).map(n -> n * 1_000_000_000L).toArray();   // [1000000000, 2000000000, 3000000000]
            show("big", big);
            double[] prices = DoubleStream.of(9.5, 4.25).toArray();             // [9.5, 4.25]
            show("prices", prices);
        }
        {
            char[] letters = {'j', 'a', 'v', 'a'};
            int[] codes = new String(letters).chars().toArray();                       // [106, 97, 118, 97]
            show("codes", codes);
            long distinct = new String(letters).chars().distinct().count();            // 3
            show("distinct", distinct);
            byte[] data = {10, -1, 7};
            int[] unsigned = IntStream.range(0, data.length).map(i -> data[i] & 0xFF).toArray();   // [10, 255, 7]
            show("unsigned", unsigned);
        }
        {
            int[][] seats = {{1, 0, 1}, {0, 0, 1}};
            int[] flat = Arrays.stream(seats).flatMapToInt(Arrays::stream).toArray();   // [1, 0, 1, 0, 0, 1]
            show("flat", flat);
            int booked = Arrays.stream(seats).flatMapToInt(Arrays::stream).sum();      // 3
            show("booked", booked);
            int[][] copy = Arrays.stream(seats).map(int[]::clone).toArray(int[][]::new);   // [[1, 0, 1], [0, 0, 1]]
            show("copy", copy);
            copy[0][0] = 9;
            int original = seats[0][0];                                                 // 1
            show("original", original);
        }
        {
            String property = " api.shop.com, cdn.shop.com,, API.shop.com ";
            String[] origins = Arrays.stream(property.split(","))
                    .map(String::strip)
                    .filter(s -> !s.isEmpty())
                    .map(s -> s.toLowerCase(Locale.ROOT))
                    .distinct()
                    .toArray(String[]::new);
            String result = Arrays.toString(origins);   // "[api.shop.com, cdn.shop.com]"
            show("result", result);
        }
        {
            int[] doubled = IntStream.rangeClosed(1, 8).parallel().map(n -> n * 2).toArray();   // [2, 4, 6, 8, 10, 12, 14, 16]
            show("doubled", doubled);
        }
        {
            String[] sortedNames = toSortedArray(List.of("raj", "ana", "li"), Comparator.naturalOrder(), String[]::new);   // [ana, li, raj]
            show("sortedNames", sortedNames);
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

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
 * Examples for the tutorial "Java Boxed Stream: IntStream boxed() to List, Set and Array".
 * https://howtodoinjava.com/java8/java8-boxed-intstream/
 */
public class BoxedStreams {
    static record Seat(int number) {}
    public static void main(String[] args) throws Exception {
        {
            List<Integer> list = IntStream.of(3, 1, 3).boxed().toList();                            // [3, 1, 3]
            show("list", list);
            Set<Integer> set = IntStream.of(3, 1, 3).boxed().collect(Collectors.toSet());           // 1 and 3, in no fixed order
            show("set", set);
            int[] primitives = IntStream.of(3, 1, 3).toArray();                                      // [3, 1, 3]
            show("primitives", primitives);
            Integer[] objects = IntStream.of(3, 1, 3).boxed().toArray(Integer[]::new);               // [3, 1, 3]
            show("objects", objects);
            List<Double> prices = DoubleStream.of(1.5, 2.0).boxed().toList();                        // [1.5, 2.0]
            show("prices", prices);
        }
        {
            Stream<Integer> ints = IntStream.range(1, 4).boxed();
            Stream<Long> longs = LongStream.of(10L, 20L).boxed();
            Stream<Double> doubles = DoubleStream.of(0.5).boxed();
            List<Integer> collected = ints.collect(Collectors.toList());                 // [1, 2, 3]
            show("collected", collected);
        }
        {
            List<Integer> same = IntStream.rangeClosed(1, 3).mapToObj(Integer::valueOf).toList();   // [1, 2, 3]
            show("same", same);
            List<String> labels = IntStream.rangeClosed(1, 3).mapToObj(i -> "Row " + i).toList();   // [Row 1, Row 2, Row 3]
            show("labels", labels);
            List<Seat> seats = IntStream.rangeClosed(1, 2).mapToObj(Seat::new).toList();             // [Seat[number=1], Seat[number=2]]
            show("seats", seats);
        }
        {
            List<Integer> fixed = IntStream.of(5, 2, 8).boxed().toList();
            List<Integer> mutable = IntStream.of(5, 2, 8).boxed().collect(Collectors.toCollection(ArrayList::new));
            mutable.add(1);
            List<Integer> grown = mutable;                                              // [5, 2, 8, 1]
            show("grown", grown);
            try { boolean added = fixed.add(1); show("added", added); } catch (Throwable _t) { System.out.println("added -> " + _t); }
        }
        {
            Set<Integer> sorted = IntStream.of(9, 3, 9, 1).boxed().collect(Collectors.toCollection(TreeSet::new));   // [1, 3, 9]
            show("sorted", sorted);
            Map<Boolean, List<Integer>> evenOdd = IntStream.rangeClosed(1, 6).boxed().collect(Collectors.partitioningBy(i -> i % 2 == 0));   // {false=[1, 3, 5], true=[2, 4, 6]}
            show("evenOdd", evenOdd);
            Map<Integer, Integer> squares = IntStream.rangeClosed(1, 3).boxed().collect(Collectors.toMap(i -> i, i -> i * i));       // {1=1, 2=4, 3=9}
            show("squares", squares);
        }
        {
            ArrayList<Integer> viaSupplier = IntStream.of(4, 6).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);   // [4, 6]
            show("viaSupplier", viaSupplier);
        }
        {
            Set<Long> ids = LongStream.of(100L, 200L, 100L).boxed().collect(Collectors.toSet());   // 100 and 200, in no fixed order
            show("ids", ids);
        }
        {
            int[] ints = IntStream.rangeClosed(1, 4).toArray();                             // [1, 2, 3, 4]
            show("ints", ints);
            long[] longs = LongStream.of(7L, 9L).toArray();                                // [7, 9]
            show("longs", longs);
            double[] doubles = DoubleStream.of(0.5, 1.5).toArray();                        // [0.5, 1.5]
            show("doubles", doubles);
            Integer[] wrapped = IntStream.rangeClosed(1, 4).boxed().toArray(Integer[]::new);   // [1, 2, 3, 4]
            show("wrapped", wrapped);
        }
        {
            int[] scores = {70, 85, 90};
            List<Integer> scoreList = Arrays.stream(scores).boxed().toList();              // [70, 85, 90]
            show("scoreList", scoreList);
            int wrongSize = Arrays.asList(scores).size();                                  // 1
            show("wrongSize", wrongSize);
        }
        {
            List<Integer> cart = List.of(5, 12, 3);
            int total = cart.stream().mapToInt(Integer::intValue).sum();                   // 20
            show("total", total);
            double average = cart.stream().mapToInt(Integer::intValue).average().orElse(0);   // 6.666666666666667
            show("average", average);
        }
        {
            List<Integer> withGaps = Arrays.asList(5, null, 3);
            try { int unsafe = withGaps.stream().mapToInt(Integer::intValue).sum(); show("unsafe", unsafe); } catch (Throwable _t) { System.out.println("unsafe -> " + _t); }
            int safe = withGaps.stream().filter(Objects::nonNull).mapToInt(Integer::intValue).sum();   // 8
            show("safe", safe);
        }
        {
            List<Integer> small = IntStream.of(100, 100).boxed().toList();
            List<Integer> large = IntStream.of(1000, 1000).boxed().toList();
            boolean smallSame = small.get(0) == small.get(1);                              // true
            show("smallSame", smallSame);
            boolean largeSame = large.get(0) == large.get(1);                              // false
            show("largeSame", largeSame);
            boolean largeEqual = large.get(0).equals(large.get(1));                        // true
            show("largeEqual", largeEqual);
        }
        {
            int[] ratings = {5, 4, 5, 3, 5, 1, 4, 9};

            Map<Integer, Long> histogram = Arrays.stream(ratings)
                    .filter(r -> r >= 1 && r <= 5)
                    .boxed()
                    .collect(Collectors.groupingBy(r -> r, TreeMap::new, Collectors.counting()));
            Map<Integer, Long> stars = histogram;               // {1=1, 3=1, 4=2, 5=3}
            show("stars", stars);
        }
        {
            List<Integer> digits = IntStream.range(0, 3).boxed().toList();   // [0, 1, 2]
            show("digits", digits);
        }
        {
            List<Character> letters = "abc".chars().mapToObj(c -> (char) c).toList();   // [a, b, c]
            show("letters", letters);
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

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
 * Examples for the tutorial "Java IntStream Guide: range(), iterate(), sum() and More".
 * https://howtodoinjava.com/java8/intstream-examples/
 */
public class IntStreamGuide {
    static boolean meetsPolicy(String password) {
        return password.length() >= 10
                && password.chars().anyMatch(Character::isDigit)
                && password.chars().anyMatch(Character::isUpperCase);
    }
    public static void main(String[] args) throws Exception {
        {
            int[] upTo4 = IntStream.range(1, 5).toArray();                              // [1, 2, 3, 4]
            show("upTo4", upTo4);
            int[] upTo5 = IntStream.rangeClosed(1, 5).toArray();                        // [1, 2, 3, 4, 5]
            show("upTo5", upTo5);
            int[] evens = IntStream.iterate(0, i -> i <= 8, i -> i + 2).toArray();      // [0, 2, 4, 6, 8]
            show("evens", evens);
            int total = IntStream.of(3, 5, 7).sum();                                    // 15
            show("total", total);
            long bigOnes = IntStream.of(3, 50, 70).filter(i -> i > 10).count();         // 2
            show("bigOnes", bigOnes);
            List<String> labels = IntStream.rangeClosed(1, 3).mapToObj(i -> "Q" + i).toList();   // [Q1, Q2, Q3]
            show("labels", labels);
        }
        {
            int[] powers = IntStream.iterate(1, i -> i * 2).limit(5).toArray();           // [1, 2, 4, 8, 16]
            show("powers", powers);
            int[] fives = IntStream.generate(() -> 5).limit(3).toArray();                  // [5, 5, 5]
            show("fives", fives);
            int[] codes = "Hi".chars().toArray();                                          // [72, 105]
            show("codes", codes);
            int[] fromArray = Arrays.stream(new int[] {9, 8, 7}, 1, 3).toArray();          // [8, 7]
            show("fromArray", fromArray);
            int[] lengths = Stream.of("fig", "apple").mapToInt(String::length).toArray();  // [3, 5]
            show("lengths", lengths);
        }
        {
            int[] dice = new Random(42).ints(5, 1, 7).toArray();
            boolean inRange = Arrays.stream(dice).allMatch(d -> d >= 1 && d <= 6);       // true
            show("inRange", inRange);
        }
        {
            List<String> fruits = List.of("apple", "banana", "cherry", "date");

            List<String> everySecond = IntStream.range(0, fruits.size())
                    .filter(i -> i % 2 == 0)
                    .mapToObj(fruits::get)
                    .toList();                                  // [apple, cherry]
        }
        {
            int[] down = IntStream.range(0, 5).map(i -> 4 - i).toArray();                  // [4, 3, 2, 1, 0]
            show("down", down);
            int[] countdown = IntStream.iterate(10, i -> i > 0, i -> i - 3).toArray();     // [10, 7, 4, 1]
            show("countdown", countdown);
        }
        {
            List<String> seats = IntStream.rangeClosed('A', 'C')
                    .boxed()
                    .flatMap(row -> IntStream.rangeClosed(1, 4).mapToObj(n -> (char) row.intValue() + "" + n))
                    .toList();
            int seatCount = seats.size();                       // 12
            show("seatCount", seatCount);
            String lastSeat = seats.getLast();                  // "C4"
            show("lastSeat", lastSeat);
        }
        {
            int[] squares = IntStream.rangeClosed(1, 4).map(i -> i * i).toArray();         // [1, 4, 9, 16]
            show("squares", squares);
            int[] unique = IntStream.of(3, 1, 3, 2, 1).distinct().sorted().toArray();      // [1, 2, 3]
            show("unique", unique);
            double[] halves = IntStream.of(1, 3).mapToDouble(i -> i / 2.0).toArray();      // [0.5, 1.5]
            show("halves", halves);
            String csv = IntStream.of(4, 8).mapToObj(String::valueOf).collect(Collectors.joining(","));   // "4,8"
            show("csv", csv);
        }
        {
            int[] belowTen = IntStream.of(2, 5, 9, 12, 3).takeWhile(i -> i < 10).toArray();   // [2, 5, 9]
            show("belowTen", belowTen);
            int[] fromTen = IntStream.of(2, 5, 9, 12, 3).dropWhile(i -> i < 10).toArray();     // [12, 3]
            show("fromTen", fromTen);
        }
        {
            int sum = IntStream.of(4, 8, 15).sum();                                        // 27
            show("sum", sum);
            OptionalInt max = IntStream.of(4, 8, 15).max();                                // OptionalInt[15]
            show("max", max);
            double avg = IntStream.of(4, 8, 15).average().orElse(0);                       // 9.0
            show("avg", avg);
            double emptyAvg = IntStream.empty().average().orElse(0);                       // 0.0
            show("emptyAvg", emptyAvg);
            int product = IntStream.rangeClosed(1, 5).reduce(1, (a, b) -> a * b);          // 120
            show("product", product);
        }
        {
            int wrapped = IntStream.of(Integer.MAX_VALUE, 1).sum();                         // -2147483648
            show("wrapped", wrapped);
            long correct = IntStream.of(Integer.MAX_VALUE, 1).asLongStream().sum();        // 2147483648
            show("correct", correct);
            try { int checked = IntStream.of(Integer.MAX_VALUE, 1).reduce(0, Math::addExact); show("checked", checked); } catch (Throwable _t) { System.out.println("checked -> " + _t); }
        }
        {
            int[] array = IntStream.range(0, 3).toArray();                                  // [0, 1, 2]
            show("array", array);
            List<Integer> list = IntStream.range(0, 3).boxed().toList();                     // [0, 1, 2]
            show("list", list);
            String text = IntStream.range(0, 3).mapToObj(Integer::toString).collect(Collectors.joining("-"));   // "0-1-2"
            show("text", text);
        }
        {
            boolean weak = meetsPolicy("applepie");             // false
            show("weak", weak);
            boolean strong = meetsPolicy("AppleBanana7");       // true
            show("strong", strong);
            long digits = "Order66Kiwi9".chars().filter(Character::isDigit).count();   // 3
            show("digits", digits);
        }
        {
            int[] byFive = IntStream.iterate(0, i -> i <= 20, i -> i + 5).toArray();   // [0, 5, 10, 15, 20]
            show("byFive", byFive);
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

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
 * Examples for the tutorial "If-Else Logic in Java Streams: filter, map and partitioningBy".
 * https://howtodoinjava.com/java8/stream-if-else-logic/
 */
public class IfElseInStreams {
    static String describe(Integer celsius) {
        return switch (celsius) {
            case Integer t when t < 0 -> "freezing";
            case Integer t when t < 25 -> "mild";
            default -> "hot";
        };
    }
    static enum Size { SMALL, MEDIUM, LARGE }
    static Size sizeOf(int grams) {
        if (grams < 100) {
            return Size.SMALL;
        } else if (grams < 1000) {
            return Size.MEDIUM;
        }
        return Size.LARGE;
    }
    static record Transaction(String description, int cents) {}
    public static void main(String[] args) throws Exception {
        {
            List<Integer> amounts = List.of(120, -40, 75, -15);
            List<Integer> credits = amounts.stream().filter(a -> a > 0).toList();                          // [120, 75]
            show("credits", credits);
            List<String> labels = amounts.stream().map(a -> a > 0 ? "credit" : "debit").toList();          // [credit, debit, credit, debit]
            show("labels", labels);
            Map<Boolean, List<Integer>> split = amounts.stream().collect(Collectors.partitioningBy(a -> a > 0));   // {false=[-40, -15], true=[120, 75]}
            show("split", split);
        }
        {
            List<Integer> numbers = List.of(-1, 1, -2, 3, 0);
            List<String> log = new ArrayList<>();
            numbers.forEach(n -> {
                if (n == 0) {
                    log.add("zero");
                } else if (n > 0) {
                    log.add("positive");
                } else {
                    log.add("negative");
                }
            });
            List<String> result = log;                                 // [negative, positive, negative, positive, zero]
            show("result", result);
        }
        {
            List<String> alerts = new ArrayList<>();
            Consumer<Integer> notifyByAmount = cents -> {
                if (cents >= 1000) {
                    alerts.add("call: " + cents);
                } else {
                    alerts.add("email: " + cents);
                }
            };
            List.of(250, 4000).forEach(notifyByAmount);
            List<String> sent = alerts;                                // [email: 250, call: 4000]
            show("sent", sent);
        }
        {
            List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6);
            Predicate<Integer> isEven = n -> n % 2 == 0;
            List<Integer> evens = numbers.stream().filter(isEven).toList();                      // [2, 4, 6]
            show("evens", evens);
            List<Integer> odds = numbers.stream().filter(isEven.negate()).toList();              // [1, 3, 5]
            show("odds", odds);
        }
        {
            List<Integer> temperatures = List.of(-5, 12, 31);
            List<String> advice = temperatures.stream().map(t -> t < 0 ? "coat" : "shirt").toList();         // [coat, shirt, shirt]
            show("advice", advice);
        }
        {
            List<Integer> temperatures = List.of(-5, 12, 31);
            List<String> words = temperatures.stream().map(t -> describe(t)).toList();      // [freezing, mild, hot]
            show("words", words);
        }
        {
            List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6);
            Map<Boolean, List<Integer>> byParity = numbers.stream().collect(Collectors.partitioningBy(n -> n % 2 == 0));
            List<Integer> evens = byParity.get(true);                  // [2, 4, 6]
            show("evens", evens);
            List<Integer> odds = byParity.get(false);                  // [1, 3, 5]
            show("odds", odds);
            Map<Boolean, Long> counts = numbers.stream().collect(Collectors.partitioningBy(n -> n > 4, Collectors.counting()));   // {false=4, true=2}
            show("counts", counts);
            Map<Boolean, List<Integer>> none = numbers.stream().collect(Collectors.partitioningBy(n -> n > 100));              // {false=[1, 2, 3, 4, 5, 6], true=[]}
            show("none", none);
        }
        {
            List<Integer> parcels = List.of(50, 400, 2500, 80, 900);
            Map<Size, List<Integer>> bySize = parcels.stream().collect(Collectors.groupingBy(g -> sizeOf(g), () -> new EnumMap<>(Size.class), Collectors.toList()));   // {SMALL=[50, 80], MEDIUM=[400, 900], LARGE=[2500]}
            show("bySize", bySize);
        }
        {
            List<Integer> orders = List.of(300, 800, 1500);
            String firstLarge = orders.stream().filter(o -> o > 1000).findFirst().map(o -> "review " + o).orElse("nothing to review");    // "review 1500"
            show("firstLarge", firstLarge);
            String noneLarge = orders.stream().filter(o -> o > 5000).findFirst().map(o -> "review " + o).orElse("nothing to review");    // "nothing to review"
            show("noneLarge", noneLarge);
            try { int mustExist = orders.stream().filter(o -> o > 5000).findFirst().orElseThrow(); show("mustExist", mustExist); } catch (Throwable _t) { System.out.println("mustExist -> " + _t); }
        }
        {
            List<Transaction> statement = List.of(new Transaction("salary", 320000), new Transaction("rent", -95000), new Transaction("groceries", -8400), new Transaction("refund", 1200), new Transaction("laptop", -129900));
            Map<Boolean, Integer> totals = statement.stream().collect(Collectors.partitioningBy(t -> t.cents() > 0, Collectors.summingInt(t -> t.cents())));
            int moneyIn = totals.get(true);                                     // 321200
            show("moneyIn", moneyIn);
            int moneyOut = totals.get(false);                                   // -233300
            show("moneyOut", moneyOut);
            List<String> review = statement.stream().filter(t -> Math.abs(t.cents()) > 100000).map(t -> t.description()).toList();   // [salary, laptop]
            show("review", review);
            List<String> lines = statement.stream().map(t -> (t.cents() > 0 ? "+ " : "- ") + t.description()).toList();             // [+ salary, - rent, - groceries, + refund, - laptop]
            show("lines", lines);
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

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
 * Examples for the tutorial "Java Stream mapMulti() with Examples and flatMap Comparison".
 * https://howtodoinjava.com/java/stream/stream-mapmulti-example/
 */
public class MapMultiExamples {
    static record Order(String status, List<String> items) {}
    static record Basket(String owner, List<Integer> prices) {}
    static enum Type { DEPOSIT, WITHDRAWAL }
    static record Transaction(String id, double amount, Type type) {}
    static record TransactionEvent(String type, double amount) {}
    public static void main(String[] args) throws Exception {
        {
            List<Integer> numbers = List.of(1, 2, 3);

            List<Integer> powers = numbers.stream()
                    .<Integer>mapMulti((num, downstream) -> {
                downstream.accept(num);
                downstream.accept(num * num);
                downstream.accept(num * num * num);
            })
                    .toList();
            List<Integer> result = powers;                  // [1, 1, 1, 2, 4, 8, 3, 9, 27]
            show("result", result);
        }
        {
            Stream<String> words = Stream.of("tea", "", "jam");
            List<String> letters = words
                    .<String>mapMulti((word, downstream) -> {
                for (char c : word.toCharArray()) {
                    downstream.accept(String.valueOf(c));
                }
            })
                    .toList();
            List<String> chars = letters;                   // [t, e, a, j, a, m]
            show("chars", chars);
        }
        {
            List<Integer> numbers = List.of(1, 2, 3, 4);

            List<Integer> evensAndTens = numbers.stream()
                    .<Integer>mapMulti((n, downstream) -> {
                if (n % 2 == 0) {
                    downstream.accept(n);
                    downstream.accept(n * 10);
                }
            })
                    .toList();
            List<Integer> withMapMulti = evensAndTens;       // [2, 20, 4, 40]
            show("withMapMulti", withMapMulti);
            List<Integer> withFlatMap = numbers.stream().filter(n -> n % 2 == 0).flatMap(n -> Stream.of(n, n * 10)).toList();   // [2, 20, 4, 40]
            show("withFlatMap", withFlatMap);
        }
        {
            List<Integer> witness = Stream.of(1, 2).<Integer>mapMulti((n, downstream) -> downstream.accept(n * 2)).toList();   // [2, 4]
            show("witness", witness);
            List<Integer> typed = Stream.of(1, 2).mapMulti((Integer n, Consumer<Integer> downstream) -> downstream.accept(n * 2)).toList();   // [2, 4]
            show("typed", typed);
        }
        {
            List<Number> readings = List.of(1, 2.5, 3, 4L, 5);

            List<Integer> ints = readings.stream()
                    .<Integer>mapMulti((n, downstream) -> {
                if (n instanceof Integer i) {
                    downstream.accept(i);
                }
            })
                    .toList();
            List<Integer> onlyInts = ints;                   // [1, 3, 5]
            show("onlyInts", onlyInts);
        }
        {
            List<String> input = List.of("10", " 25 ", "abc", "", "7");

            List<Integer> parsed = input.stream()
                    .<Integer>mapMulti((s, downstream) -> {
                try {
                    downstream.accept(Integer.parseInt(s.strip()));
                } catch (NumberFormatException e) {
                    // skip invalid values; log them in real code
                }
            })
                    .toList();
            List<Integer> valid = parsed;                    // [10, 25, 7]
            show("valid", valid);
        }
        {
            List<Order> orders = List.of(
            new Order("PAID", List.of("tea", "mug")),
            new Order("CANCELLED", List.of("jam")),
            new Order("PAID", List.of("cake", "")));

            List<String> toPack = orders.stream()
                    .<String>mapMulti((order, downstream) -> {
                if (order.status().equals("PAID")) {
                    for (String item : order.items()) {
                        if (!item.isBlank()) {
                            downstream.accept(item);
                        }
                    }
                }
            })
                    .toList();
            List<String> packList = toPack;                  // [tea, mug, cake]
            show("packList", packList);
        }
        {
            List<Optional<String>> lookups = List.of(Optional.of("tea"), Optional.empty(), Optional.of("jam"));

            List<String> found = lookups.stream().<String>mapMulti(Optional::ifPresent).toList();   // [tea, jam]
            show("found", found);
            List<String> viaFlatMap = lookups.stream().flatMap(Optional::stream).toList();           // [tea, jam]
            show("viaFlatMap", viaFlatMap);
        }
        {
            AtomicInteger emitted = new AtomicInteger();
            Optional<Integer> first = Stream.of(1, 2)
                    .<Integer>mapMulti((n, downstream) -> {
                for (int i = 0; i < 1000; i++) {
                    emitted.incrementAndGet();
                    downstream.accept(i);
                }
            })
                    .findFirst();
            int calls = emitted.get();                       // 1000
            show("calls", calls);
        }
        {
            List<Basket> baskets = List.of(new Basket("ana", List.of(3, 5)), new Basket("raj", List.of(10)));

            int total = baskets.stream()
                    .mapMultiToInt((basket, downstream) -> {
                for (int price : basket.prices()) {
                    downstream.accept(price);
                }
            })
                    .sum();
            int sum = total;                                 // 18
            show("sum", sum);
            int[] squares = IntStream.of(2, 3).mapMulti((n, downstream) -> downstream.accept(n * n)).toArray();   // [4, 9]
            show("squares", squares);
        }
        {
            List<Transaction> transactions = List.of(
            new Transaction("T1", 100, Type.DEPOSIT),
            new Transaction("T2", 50, Type.WITHDRAWAL),
            new Transaction("T3", 200, Type.WITHDRAWAL));

            List<TransactionEvent> events = transactions.stream()
                    .<TransactionEvent>mapMulti((tx, downstream) -> {
                switch (tx.type()) {
                    case DEPOSIT -> downstream.accept(new TransactionEvent("Deposit", tx.amount()));
                    case WITHDRAWAL -> {
                        downstream.accept(new TransactionEvent("Withdrawal", tx.amount()));
                        if (tx.amount() > 100) {
                            downstream.accept(new TransactionEvent("Fraud Alert", tx.amount()));
                        }
                    }
                }
            })
                    .toList();
            int eventCount = events.size();                  // 4
            show("eventCount", eventCount);
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

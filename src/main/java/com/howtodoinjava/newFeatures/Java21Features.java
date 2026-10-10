package com.howtodoinjava.newFeatures;

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
 * Examples for the tutorial "Java 21 Features (LTS) with Examples and Status in Java 25".
 * https://howtodoinjava.com/java/java-21-new-features/
 */
public class Java21Features {
    static record Shipped(String id, int days) {}
    static record Point(int x, int y) {}
    static String describe(Object o) {
        return switch (o) {
            case null -> "nothing";
            case Point(int x, int y) when x == y -> "point on the diagonal";
            case Point(int x, int y) -> "point " + x + "/" + y;
            case String s -> "text of length " + s.length();
            default -> "something else";
        };
    }
    String greeting() {
        return "Java 25";
    }
    static sealed interface Payment permits CardPayment, WalletPayment, VoucherPayment {}
    static record CardPayment(String last4, double amount) implements Payment {}
    static record WalletPayment(String provider, double amount) implements Payment {}
    static record VoucherPayment(String code) implements Payment {}
    public static void main(String[] args) throws Exception {
        {
            List<String> orders = new ArrayList<>(List.of("A-1", "A-2", "A-3"));
            String newest = orders.getLast();                                  // "A-3"
            show("newest", newest);
            List<String> latestFirst = orders.reversed();                      // [A-3, A-2, A-1]
            show("latestFirst", latestFirst);
            Object event = new Shipped("A-2", 3);
            String label = switch (event) {
                case Shipped(String id, int days) when days > 2 -> id + " is late";
                case Shipped(String id, int days) -> id + " is on time";
                default -> "unknown";
            };
            String result = label;                                             // "A-2 is late"
            show("result", result);
            Thread worker = Thread.ofVirtual().start(() -> {});
            boolean virtual = worker.isVirtual();                              // true
            show("virtual", virtual);
        }
        {
            Runnable task = () -> System.out.println("Inside Runnable");
            Thread first = Thread.startVirtualThread(task);
            Thread second = Thread.ofVirtual().name("worker-1").start(task);
            String name = second.getName();                                    // "worker-1"
            show("name", name);
            long count;
            try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
                List<Future<Integer>> futures = IntStream.range(0, 10_000)
                        .mapToObj(i -> executor.submit(() -> i))
                        .toList();
                count = futures.size();
            }
            long tasks = count;                                                // 10000
            show("tasks", tasks);
        }
        {
            ArrayList<Integer> arrayList = new ArrayList<>();
            arrayList.add(1);
            arrayList.addFirst(0);
            arrayList.addLast(2);
            String content = arrayList.toString();                             // "[0, 1, 2]"
            show("content", content);
            Integer firstItem = arrayList.getFirst();                          // 0
            show("firstItem", firstItem);
            Integer lastItem = arrayList.getLast();                            // 2
            show("lastItem", lastItem);
            List<Integer> backwards = arrayList.reversed();                    // [2, 1, 0]
            show("backwards", backwards);
        }
        {
            List<Integer> numbers = List.of(0, 1, 2);
            Integer oldFirst = numbers.get(0);                                 // 0
            show("oldFirst", oldFirst);
            Integer oldLast = numbers.get(numbers.size() - 1);                 // 2
            show("oldLast", oldLast);
        }
        {
            Object obj = new Point(1, 2);
            int sum = 0;
            if (obj instanceof Point p) {
                sum = p.x() + p.y();
            }
            int typePatternSum = sum;                                          // 3
            show("typePatternSum", typePatternSum);
        }
        {
            Object value = new Point(1, 2);
            int total = 0;
            if (value instanceof Point(int x, int y)) {
                total = x + y;
            }
            int recordPatternSum = total;                                      // 3
            show("recordPatternSum", recordPatternSum);
        }
        {
            String onDiagonal = describe(new Point(2, 2));                     // "point on the diagonal"
            show("onDiagonal", onDiagonal);
            String point = describe(new Point(1, 5));                          // "point 1/5"
            show("point", point);
            String text = describe("Lokesh");                                  // "text of length 6"
            show("text", text);
            String missing = describe(null);                                   // "nothing"
            show("missing", missing);
        }
        {
            String name = "Lokesh";
            String message = "Greetings %s!".formatted(name);                  // "Greetings Lokesh!"
            show("message", message);
            String joined = "Greetings " + name + "!";                         // "Greetings Lokesh!"
            show("joined", joined);
        }
        {
            int volume = Math.clamp(150, 0, 100);                             // 100
            show("volume", volume);
            String border = new StringBuilder().repeat("=-", 3).toString();   // "=-=-=-"
            show("border", border);
            int index = "hello".indexOf('l', 0, 3);                            // 2
            show("index", index);
            boolean emoji = Character.isEmoji(0x1F600);                        // true
            show("emoji", emoji);
            String[] parts = "a,b;c".splitWithDelimiters("[,;]", 0);           // [a, ,, b, ;, c]
            show("parts", parts);
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

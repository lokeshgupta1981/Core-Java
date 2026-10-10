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
;

/**
 * Examples for the tutorial "Java Stream concat(): Merge Streams, Append and Prepend Items".
 * https://howtodoinjava.com/java8/stream-concat-example/
 */
public class StreamConcatExamples {
    static record Product(String name, int price) {}
    public static void main(String[] args) throws Exception {
        {
            List<String> merged = Stream.concat(Stream.of("a", "b"), Stream.of("c", "d")).toList();   // [a, b, c, d]
            show("merged", merged);
            List<String> appended = Stream.concat(Stream.of("a", "b"), Stream.of("z")).toList();      // [a, b, z]
            show("appended", appended);
            List<String> prepended = Stream.concat(Stream.of("start"), Stream.of("a", "b")).toList(); // [start, a, b]
            show("prepended", prepended);
        }
        {
            boolean parallel = Stream.concat(Stream.of(1).parallel(), Stream.of(2)).isParallel();   // true
            show("parallel", parallel);

            List<String> closed = new ArrayList<>();
            Stream<String> first = Stream.of("a").onClose(() -> closed.add("first closed"));
            Stream<String> second = Stream.of("b").onClose(() -> closed.add("second closed"));
            try (Stream<String> both = Stream.concat(first, second)) {
                both.forEach(s -> {});
            }
            List<String> handlers = closed;                     // [first closed, second closed]
            show("handlers", handlers);
        }
        {
            Stream<String> left = Stream.of("a");
            Stream<String> combined = Stream.concat(left, Stream.of("b"));
            try { long reused = left.count(); show("reused", reused); } catch (Throwable _t) { System.out.println("reused -> " + _t); }
        }
        {
            Stream<Integer> first = Stream.of(1, 2);
            Stream<Integer> second = Stream.of(3, 4);
            Stream<Integer> third = Stream.of(5, 6);

            List<Integer> nested = Stream.concat(first, Stream.concat(second, third)).toList();   // [1, 2, 3, 4, 5, 6]
            show("nested", nested);
        }
        {
            List<Stream<Integer>> pages = List.of(Stream.of(1, 2), Stream.of(3, 4), Stream.of(5, 6));
            List<Integer> flat = pages.stream().flatMap(Function.identity()).toList();     // [1, 2, 3, 4, 5, 6]
            show("flat", flat);

            List<Integer> fromLists = Stream.of(List.of(1, 2), List.of(3), List.of(4, 5)).flatMap(List::stream).toList();   // [1, 2, 3, 4, 5]
            show("fromLists", fromLists);
        }
        {
            Stream<Integer> numbers = Stream.of(1, 2, 3);
            List<Integer> withTail = Stream.concat(numbers, Stream.of(4, 5)).toList();      // [1, 2, 3, 4, 5]
            show("withTail", withTail);

            Stream<Integer> more = Stream.of(1, 2, 3);
            List<Integer> withHead = Stream.concat(Stream.of(0), more).toList();            // [0, 1, 2, 3]
            show("withHead", withHead);
        }
        {
            List<String> rows = List.of("apple,5", "banana,3");
            String header = "item,qty";
            String footer = null;                               // no total line for this export
            show("footer", footer);

            List<String> lines = Stream.concat(
            Stream.concat(Stream.of(header), rows.stream()),
            Stream.ofNullable(footer)).toList();
            List<String> csv = lines;                           // [item,qty, apple,5, banana,3]
            show("csv", csv);
        }
        {
            Stream<Integer> mondayIds = Stream.of(1, 2, 3, 4);
            Stream<Integer> tuesdayIds = Stream.of(3, 4, 5);
            List<Integer> unique = Stream.concat(mondayIds, tuesdayIds).distinct().toList();   // [1, 2, 3, 4, 5]
            show("unique", unique);
        }
        {
            Stream<Product> cache = Stream.of(new Product("apple", 5), new Product("kiwi", 2));
            Stream<Product> fresh = Stream.of(new Product("apple", 6), new Product("fig", 4));

            Map<String, Product> byName = Stream.concat(fresh, cache)
                    .collect(Collectors.toMap(Product::name, p -> p, (newer, older) -> newer, LinkedHashMap::new));
            List<Product> latest = List.copyOf(byName.values());  // [Product[name=apple, price=6], Product[name=fig, price=4], Product[name=kiwi, price=2]]
            show("latest", latest);
        }
        {
            int[] ids = IntStream.concat(IntStream.range(1, 4), IntStream.of(10, 20)).toArray();   // [1, 2, 3, 10, 20]
            show("ids", ids);
            long total = LongStream.concat(LongStream.of(5), LongStream.of(7)).sum();             // 12
            show("total", total);
        }
        {
            Stream<Number> mixed = Stream.concat(Stream.of(1, 2), Stream.of(2.5));
            List<Number> values = mixed.toList();               // [1, 2, 2.5]
            show("values", values);
        }
        {
            List<String> added = Stream.concat(Stream.of("a", "b"), Stream.of("c")).toList();   // [a, b, c]
            show("added", added);
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

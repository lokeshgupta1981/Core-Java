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
 * Examples for the tutorial "Convert Iterable or Iterator to Stream in Java (StreamSupport)".
 * https://howtodoinjava.com/java8/iterable-iterator-to-stream/
 */
public class IterableToStream {
    static <T> Stream<T> streamOf(Iterable<T> iterable) {
        if (iterable instanceof Collection<T> collection) {
            return collection.stream();
        }
        return StreamSupport.stream(iterable.spliterator(), false);
    }
    static <T> Stream<T> streamOf(Iterator<T> iterator) {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, Spliterator.ORDERED), false);
    }
    public static void main(String[] args) throws Exception {
        {
            Iterable<String> iterable = List.of("apple", "banana");
            List<String> fromIterable = StreamSupport.stream(iterable.spliterator(), false).map(String::toUpperCase).toList();   // [APPLE, BANANA]
            show("fromIterable", fromIterable);

            Iterator<String> iterator = List.of("kiwi", "fig").iterator();
            Spliterator<String> split = Spliterators.spliteratorUnknownSize(iterator, Spliterator.ORDERED);
            List<String> fromIterator = StreamSupport.stream(split, false).map(String::toUpperCase).toList();          // [KIWI, FIG]
            show("fromIterator", fromIterator);
        }
        {
            Iterable<Integer> scores = () -> List.of(7, 3, 9, 4).iterator();

            List<Integer> high = StreamSupport.stream(scores.spliterator(), false)
                    .filter(s -> s > 5)
                    .sorted()
                    .toList();                                  // [7, 9]
        }
        {
            Iterable<String> names = () -> List.of("a", "b", "c").iterator();

            boolean defaultOrdered = names.spliterator().hasCharacteristics(Spliterator.ORDERED);   // false
            show("defaultOrdered", defaultOrdered);
            Spliterator<String> ordered = Spliterators.spliteratorUnknownSize(names.iterator(), Spliterator.ORDERED);
            String first = StreamSupport.stream(ordered, true).findFirst().orElseThrow();             // "a"
            show("first", first);
        }
        {
            Iterator<String> words = List.of("one", "two", "three").iterator();

            Spliterator<String> spliterator = Spliterators.spliteratorUnknownSize(words, Spliterator.ORDERED);
            Map<Integer, List<String>> byLength = StreamSupport.stream(spliterator, false)
                    .collect(Collectors.groupingBy(String::length));
            Map<Integer, List<String>> grouped = byLength;      // {3=[one, two], 5=[three]}
            show("grouped", grouped);
        }
        {
            Iterator<Integer> once = List.of(1, 2).iterator();
            long firstCount = StreamSupport.stream(Spliterators.spliteratorUnknownSize(once, 0), false).count();    // 2
            show("firstCount", firstCount);
            long secondCount = StreamSupport.stream(Spliterators.spliteratorUnknownSize(once, 0), false).count();   // 0
            show("secondCount", secondCount);
        }
        {
            Iterable<String> plain = () -> List.of("x", "y").iterator();
            List<String> viaHelper = streamOf(plain).toList();                       // [x, y]
            show("viaHelper", viaHelper);
            List<String> viaIterator = streamOf(List.of("p", "q").iterator()).toList();   // [p, q]
            show("viaIterator", viaIterator);
        }
        {
            Path dir = Files.createTempDirectory("logs");
            Files.createFile(dir.resolve("app.log"));
            Files.createFile(dir.resolve("error.log"));
            Files.createFile(dir.resolve("notes.txt"));

            List<String> logFiles;
            try (DirectoryStream<Path> entries = Files.newDirectoryStream(dir, "*.log")) {
                logFiles = StreamSupport.stream(entries.spliterator(), false)
                        .map(path -> path.getFileName().toString())
                        .sorted()
                        .toList();
            }
            List<String> found = logFiles;                      // [app.log, error.log]
            show("found", found);
        }
        {
            List<String> tokens = new Scanner("red green blue").tokens().toList();     // [red, green, blue]
            show("tokens", tokens);
            List<String> lines = "first\nsecond".lines().toList();                    // [first, second]
            show("lines", lines);
        }
        {
            Iterator<String> back = Stream.of("a", "b").iterator();
            String next = back.next();                          // "a"
            show("next", next);
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

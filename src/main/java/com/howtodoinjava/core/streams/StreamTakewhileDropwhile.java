package com.howtodoinjava.core.streams;

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
 * Examples for the tutorial "Java Stream takeWhile and dropWhile, iterate and ofNullable".
 * https://howtodoinjava.com/java9/stream-api-improvements/
 */
public class StreamTakewhileDropwhile {

    public static void main(String[] args) throws Exception {
        {
            List<Integer> readings = List.of(3, 5, 8, 12, 4, 9);

            List<Integer> taken = readings.stream().takeWhile(n -> n < 10).toList();     // [3, 5, 8]
            show("taken", taken);
            List<Integer> dropped = readings.stream().dropWhile(n -> n < 10).toList();   // [12, 4, 9]
            show("dropped", dropped);
            List<Integer> filtered = readings.stream().filter(n -> n < 10).toList();     // [3, 5, 8, 4, 9]
            show("filtered", filtered);
        }
        {
            List<Integer> powers = Stream.iterate(1, n -> n * 2)
                    .takeWhile(n -> n < 100)
                    .toList();
            String powersShown = powers.toString();      // "[1, 2, 4, 8, 16, 32, 64]"
            show("powersShown", powersShown);
        }
        {
            List<Integer> sortedPrices = List.of(5, 9, 14, 20, 35, 60);
            List<Integer> affordable = sortedPrices.stream().takeWhile(p -> p <= 20).toList();   // [5, 9, 14, 20]
            show("affordable", affordable);
        }
        {
            List<String> csv = List.of("# exported 2026-10-01", "# unit: EUR", "name,price", "tea,4", "# not a header");
            List<String> data = csv.stream().dropWhile(line -> line.startsWith("#")).toList();
            int dataLines = data.size();                 // 3
            show("dataLines", dataLines);
            String firstData = data.getFirst();          // "name,price"
            show("firstData", firstData);
        }
        {
            List<Integer> fromTen = Stream.iterate(1, n -> n + 3)
                    .dropWhile(n -> n < 10)
                    .limit(3)
                    .toList();
            String fromTenShown = fromTen.toString();    // "[10, 13, 16]"
            show("fromTenShown", fromTenShown);
        }
        {
            int sumBelow50 = IntStream.iterate(1, n -> n + 7).takeWhile(n -> n < 50).sum();   // 154
            show("sumBelow50", sumBelow50);
        }
        {
            Set<Integer> codes = Set.of(1, 2, 3, 4, 5, 6, 7);
            long unpredictable = codes.stream().takeWhile(n -> n < 5).count();   // depends on the Set iteration order
            show("unpredictable", unpredictable);
            long below5 = codes.stream().sorted().takeWhile(n -> n < 5).count();  // 4
            show("below5", below5);
        }
        {
            // for (int n = 1; n < 100; n *= 3) { ... }
            List<Integer> triples = Stream.iterate(1, n -> n < 100, n -> n * 3).toList();   // [1, 3, 9, 27, 81]
            show("triples", triples);
        }
        {
            List<Long> fibonacci = Stream.iterate(new long[] {0, 1}, f -> f[0] < 50, f -> new long[] {f[1], f[0] + f[1]})
                    .map(f -> f[0])
                    .toList();
            String fibShown = fibonacci.toString();      // "[0, 1, 1, 2, 3, 5, 8, 13, 21, 34]"
            show("fibShown", fibShown);
            List<Integer> none = Stream.iterate(200, n -> n < 100, n -> n + 1).toList();     // []
            show("none", none);
        }
        {
            long one = Stream.ofNullable("jazz").count();     // 1
            show("one", one);
            long zero = Stream.ofNullable(null).count();      // 0
            show("zero", zero);
        }
        {
            Map<String, List<String>> playlists = Map.of(
            "focus", List.of("Rain", "Waves"),
            "party", List.of("Loud"));
            List<String> keys = List.of("focus", "jazz", "party");

            List<String> songs = keys.stream()
                    .flatMap(key -> Stream.ofNullable(playlists.get(key)))
                    .flatMap(List::stream)
                    .toList();
            String songsShown = songs.toString();          // "[Rain, Waves, Loud]"
            show("songsShown", songsShown);
        }
        {
            List<String> message = List.of(
            "From: ana@example.com",
            "Subject: Dinner",
            "",
            "Hi Ben,",
            "",
            "see you at 7.");

            List<String> headers = message.stream().takeWhile(line -> !line.isEmpty()).toList();
            List<String> body = message.stream().dropWhile(line -> !line.isEmpty()).skip(1).toList();

            int headerCount = headers.size();      // 2
            show("headerCount", headerCount);
            int bodyLines = body.size();           // 3
            show("bodyLines", bodyLines);
            String greeting = body.getFirst();     // "Hi Ben,"
            show("greeting", greeting);
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

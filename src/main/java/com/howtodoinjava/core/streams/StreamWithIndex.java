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
 * Examples for the tutorial "Java Stream With Index: Iterate Over a Stream With Indices".
 * https://howtodoinjava.com/java/stream/iterate-over-stream-with-indices/
 */
public class StreamWithIndex {
    static record Indexed<T>(int index, T value) {}
    public static void main(String[] args) throws Exception {
        {
            List<String> tracks = List.of("Intro", "Blue", "Echo", "Rain", "Outro");
            List<String> numbered = IntStream.range(0, tracks.size()).mapToObj(i -> (i + 1) + ". " + tracks.get(i)).toList();   // [1. Intro, 2. Blue, 3. Echo, 4. Rain, 5. Outro]
            show("numbered", numbered);
            List<String> evenPositions = IntStream.range(0, tracks.size()).filter(i -> i % 2 == 0).mapToObj(tracks::get).toList();   // [Intro, Echo, Outro]
            show("evenPositions", evenPositions);
        }
        {
            List<String> tracks = List.of("Intro", "Blue", "Echo", "Rain", "Outro");
            List<String> lastTwo = IntStream.range(tracks.size() - 2, tracks.size()).mapToObj(tracks::get).toList();   // [Rain, Outro]
            show("lastTwo", lastTwo);
            int firstWithE = IntStream.range(0, tracks.size()).filter(i -> tracks.get(i).startsWith("E")).findFirst().orElse(-1);   // 2
            show("firstWithE", firstWithE);
            int missing = IntStream.range(0, tracks.size()).filter(i -> tracks.get(i).equals("Jazz")).findFirst().orElse(-1);     // -1
            show("missing", missing);
        }
        {
            String[] genres = {"rock", "jazz", "pop", "folk"};
            List<String> labels = IntStream.range(0, genres.length).mapToObj(i -> i + "=" + genres[i]).toList();   // [0=rock, 1=jazz, 2=pop, 3=folk]
            show("labels", labels);
            List<String> everyThird = IntStream.iterate(0, i -> i < genres.length, i -> i + 3).mapToObj(i -> genres[i]).toList();   // [rock, folk]
            show("everyThird", everyThird);
        }
        {
            List<String> tracks = List.of("Intro", "Blue", "Echo", "Rain", "Outro");
            List<Indexed<String>> pairs = IntStream.range(0, tracks.size()).mapToObj(i -> new Indexed<>(i, tracks.get(i))).toList();
            List<String> shortAtOdd = pairs.stream()
                    .filter(p -> p.index() % 2 == 1 && p.value().length() <= 4)
                    .map(p -> p.index() + ":" + p.value())
                    .toList();                     // [1:Blue, 3:Rain]
        }
        {
            List<String> tracks = List.of("Intro", "Blue", "Echo", "Rain", "Outro");
            AtomicInteger counter = new AtomicInteger();
            List<String> counted = tracks.stream().map(t -> counter.getAndIncrement() + "-" + t).toList();   // [0-Intro, 1-Blue, 2-Echo, 3-Rain, 4-Outro]
            show("counted", counted);
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

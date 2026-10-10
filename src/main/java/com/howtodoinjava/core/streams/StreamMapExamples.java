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
 * Examples for the tutorial "Java Stream map(): Transform Elements with Examples".
 * https://howtodoinjava.com/java8/stream-map-example/
 */
public class StreamMapExamples {
    static record Song(String title, String artist, int seconds) {}
    static record SongView(String title, String length) {
        static SongView from(Song song) {
            String length = song.seconds() / 60 + ":" + String.format("%02d", song.seconds() % 60);
            return new SongView(song.title(), length);
        }
    }
    public static void main(String[] args) throws Exception {
        {
            List<String> fruits = List.of("apple", "banana", "kiwi");

            List<String> upper = fruits.stream().map(String::toUpperCase).toList();     // [APPLE, BANANA, KIWI]
            show("upper", upper);
            List<Integer> lengths = fruits.stream().map(String::length).toList();      // [5, 6, 4]
            show("lengths", lengths);
            List<String> labels = fruits.stream().map(f -> f + " x2").toList();        // [apple x2, banana x2, kiwi x2]
            show("labels", labels);
            int letters = fruits.stream().mapToInt(String::length).sum();              // 15
            show("letters", letters);
        }
        {
            AtomicInteger calls = new AtomicInteger();
            Stream<String> pending = Stream.of("a", "b", "c").map(s -> {
                calls.incrementAndGet();
                return s.toUpperCase();
            });
            int before = calls.get();                      // 0
            show("before", before);
            List<String> done = pending.toList();          // [A, B, C]
            show("done", done);
            int after = calls.get();                       // 3
            show("after", after);
        }
        {
            List<String> input = List.of("1", "2", "3");
            List<Integer> numbers = input.stream().map(Integer::valueOf).toList();               // [1, 2, 3]
            show("numbers", numbers);
            try { List<Integer> broken = Stream.of("1", "two").map(Integer::valueOf).toList(); show("broken", broken); } catch (Throwable _t) { System.out.println("broken -> " + _t); }
        }
        {
            List<String> raw = List.of(" 10", "abc", "25 ", "");
            List<Integer> valid = raw.stream()
                    .map(String::strip)
                    .filter(s -> s.matches("-?\\d{1,9}"))
                    .map(Integer::valueOf)
                    .toList();                              // [10, 25]
        }
        {
            List<Song> playlist = List.of(
            new Song("Intro", "Ana", 95),
            new Song("Rain", "Raj", 241),
            new Song("Sunset", "Ana", 188));

            List<String> artists = playlist.stream().map(Song::artist).distinct().toList();     // [Ana, Raj]
            show("artists", artists);
            List<Integer> minutes = playlist.stream().map(s -> s.seconds() / 60).toList();       // [1, 4, 3]
            show("minutes", minutes);
        }
        {
            List<Song> songs = List.of(new Song("Intro", "Ana", 95), new Song("Rain", "Raj", 241), new Song("Sunset", "Ana", 188));

            List<String> longTitles = songs.stream()
                    .filter(s -> s.seconds() > 120)
                    .map(Song::title)
                    .sorted()
                    .toList();                              // [Rain, Sunset]
        }
        {
            Map<String, Integer> stock = new TreeMap<>(Map.of("apple", 5, "banana", 3));
            List<String> lines = stock.entrySet().stream().map(e -> e.getKey() + ": " + e.getValue()).toList();   // [apple: 5, banana: 3]
            show("lines", lines);
        }
        {
            List<String> scores = List.of("85", "92", "78", "90", "88");
            double average = scores.stream().mapToInt(Integer::parseInt).average().orElse(0.0);   // 86.6
            show("average", average);
            List<String> squares = IntStream.rangeClosed(1, 3).mapToObj(n -> n + "^2=" + n * n).toList();   // [1^2=1, 2^2=4, 3^2=9]
            show("squares", squares);
        }
        {
            List<Song> songs = List.of(new Song("Intro", "Ana", 95), new Song("Rain", "Raj", 241), new Song("Sunset", "Ana", 188));

            List<SongView> response = songs.stream().map(SongView::from).toList();
            SongView first = response.getFirst();          // SongView[title=Intro, length=1:35]
            show("first", first);
            int count = response.size();                   // 3
            show("count", count);
        }
        {
            Map<String, String> codes = Map.of("apple", "A1");
            List<String> found = Stream.of("apple", "kiwi").map(codes::get).filter(Objects::nonNull).toList();   // [A1]
            show("found", found);
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

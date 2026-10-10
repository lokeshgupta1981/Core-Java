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
 * Examples for the tutorial "Java Stream forEach(): Syntax, Examples and Pitfalls".
 * https://howtodoinjava.com/java8/java-stream-foreach/
 */
public class StreamForeachInternalVsExternalIteration {
    static List<String> songs = List.of("Yesterday", "Help", "Hey Jude", "Let It Be");
    public static void main(String[] args) throws Exception {
        {
            List<String> songs = List.of("Yesterday", "Help", "Hey Jude", "Let It Be");

            songs.stream().forEach(System.out::println);                                  // Yesterday, Help, Hey Jude, Let It Be
            songs.stream().filter(s -> s.length() > 8).forEach(System.out::println);      // Yesterday, Let It Be
            songs.stream().map(String::toUpperCase).forEach(System.out::println);         // YESTERDAY, HELP, HEY JUDE, LET IT BE
            songs.stream().sorted(Comparator.reverseOrder()).forEach(System.out::println); // Yesterday, Let It Be, Hey Jude, Help

            songs.parallelStream().forEach(System.out::println);          // any order, e.g. Hey Jude, Let It Be, Help, Yesterday
            songs.parallelStream().forEachOrdered(System.out::println);   // Yesterday, Help, Hey Jude, Let It Be
        }
        {
            Consumer<String> printSong = song -> System.out.println("Now playing: " + song);
            songs.stream().forEach(printSong);    // Now playing: Yesterday, Now playing: Help, ...
        }
        {
            Stream<String> stream = songs.stream();
            stream.forEach(s -> { });
            try { stream.forEach(System.out::println);  } catch (Throwable _t) { System.out.println("-> " + _t); }
        }
        {
            String[] artists = {"Beatles", "Lennon"};
            Arrays.stream(artists).forEach(System.out::println);       // Beatles, Lennon

            Map<String, Integer> plays = new LinkedHashMap<>();
            plays.put("Yesterday", 12);
            plays.put("Help", 30);

            plays.entrySet().stream()
                    .filter(e -> e.getValue() > 20)
                    .forEach(e -> System.out.println(e.getKey() + " = " + e.getValue()));   // Help = 30
        }
        {
            Map<String, Integer> counts = new TreeMap<>(Map.of("Yesterday", 12, "Help", 30));
            counts.forEach((song, count) -> System.out.println(song + " -> " + count));   // Help -> 30, Yesterday -> 12
        }
        {
            songs.stream().sorted(Comparator.reverseOrder()).forEach(System.out::println);   // Yesterday, Let It Be, Hey Jude, Help
            songs.reversed().stream().forEach(System.out::println);                          // Let It Be, Hey Jude, Help, Yesterday
        }
        {
            List<String> order = Collections.synchronizedList(new ArrayList<>());
            songs.parallelStream().forEach(order::add);             // any order, e.g. [Hey Jude, Let It Be, Help, Yesterday]

            List<String> ordered = new ArrayList<>();
            songs.parallelStream().forEachOrdered(ordered::add);    // [Yesterday, Help, Hey Jude, Let It Be]
        }
        {
            List<Integer> unsafe = new ArrayList<>();
            IntStream.range(0, 100_000).parallel().boxed().forEach(unsafe::add);
            int size = unsafe.size();          // 100000 on most runs, 66953 or 50000 on others
            show("size", size);

            List<Integer> safe = IntStream.range(0, 100_000).parallel().boxed().toList();
            int safeSize = safe.size();        // 100000
            show("safeSize", safeSize);
        }
        {
            List<String> longTitles = new ArrayList<>();
            songs.stream().filter(s -> s.length() > 8).forEach(longTitles::add);    // works, but only on a sequential stream

            List<String> titles = songs.stream().filter(s -> s.length() > 8).toList();   // [Yesterday, Let It Be]
            show("titles", titles);
        }
        {
            List<String> queue = new ArrayList<>(songs);
            try { queue.stream().forEach(s -> queue.add(s + " (live)"));  } catch (Throwable _t) { System.out.println("-> " + _t); }

            List<String> queue2 = new ArrayList<>(songs);
            List<String> live = queue2.stream().map(s -> s + " (live)").toList();
            queue2.addAll(live);                                      // [Yesterday, ..., Let It Be (live)], size 8
        }
        {
            songs.stream().forEach(s -> {
                if (s.startsWith("H")) {
                    return;                      // skips Help and Hey Jude
                }
                System.out.println(s);         // Yesterday, Let It Be
            });
        }
        {
            songs.stream().takeWhile(s -> !s.startsWith("H")).forEach(System.out::println);   // Yesterday
            boolean hasHelp = songs.stream().anyMatch(s -> s.equals("Help"));                 // true
            show("hasHelp", hasHelp);
        }
        {
            try (Writer out = Files.newBufferedWriter(Path.of("playlist.txt"))) {
                songs.stream().forEach(song -> {
                    try {
                        out.write(song + "\n");
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                });
            } catch (IOException | UncheckedIOException e) {
                System.out.println("Export failed: " + e.getMessage());
            }
            // playlist.txt: Yesterday, Help, Hey Jude, Let It Be (one per line)
        }
        {
            IntStream.range(0, songs.size())
                    .forEach(i -> System.out.println((i + 1) + ". " + songs.get(i)));   // 1. Yesterday, 2. Help, 3. Hey Jude, 4. Let It Be
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

package com.howtodoinjava.core.streams;

import java.util.concurrent.locks.*;
import java.lang.management.*;
import java.time.format.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.zip.*;
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

import java.time.temporal.*;

/**
 * Examples for the tutorial "How to Sort a Stream in Java: Numbers, Strings, Objects, Maps".
 * https://howtodoinjava.com/java8/stream-sorting/
 */
public class SortStreamGuide {
    static record Song(String title, String artist, int plays) {}
    static List<String> weeklyChart(List<Song> songs, int size) {
        return songs.stream()
                .sorted(Comparator.comparingInt(Song::plays).reversed()
                .thenComparing(Song::title))
                .limit(size)
                .map(s -> s.title() + " (" + s.plays() + ")")
                .toList();
    }
    public static void main(String[] args) throws Exception {
        {
            List<Integer> prices = List.of(30, 5, 12);
            List<Integer> lowFirst = prices.stream().sorted().toList();                              // [5, 12, 30]
            show("lowFirst", lowFirst);
            List<Integer> highFirst = prices.stream().sorted(Comparator.reverseOrder()).toList();    // [30, 12, 5]
            show("highFirst", highFirst);
            List<String> names = List.of("bob", "Alice", "carol");
            List<String> az = names.stream().sorted(String.CASE_INSENSITIVE_ORDER).toList();         // [Alice, bob, carol]
            show("az", az);
            List<Song> songs = List.of(new Song("Yellow", "Coldplay", 900), new Song("Halo", "Beyonce", 1200), new Song("Clocks", "Coldplay", 1200), new Song("Hello", "Adele", 700));
            List<String> mostPlayed = songs.stream().sorted(Comparator.comparingInt(Song::plays).reversed()).map(Song::title).toList();   // [Halo, Clocks, Yellow, Hello]
            show("mostPlayed", mostPlayed);
            List<String> withNulls = Stream.of("b", null, "a").sorted(Comparator.nullsLast(Comparator.naturalOrder())).toList();       // [a, b, null]
            show("withNulls", withNulls);
        }
        {
            List<Integer> prices = List.of(30, 5, 12, 5);
            List<Integer> asc = prices.stream().sorted().toList();                              // [5, 5, 12, 30]
            show("asc", asc);
            List<Integer> desc = prices.stream().sorted(Comparator.reverseOrder()).toList();    // [30, 12, 5, 5]
            show("desc", desc);
            List<Double> ratings = Stream.of(4.5, 3.9, 4.8).sorted().toList();                  // [3.9, 4.5, 4.8]
            show("ratings", ratings);
            List<Integer> topTwo = prices.stream().sorted(Comparator.reverseOrder()).limit(2).toList();   // [30, 12]
            show("topTwo", topTwo);
        }
        {
            int[] asc = IntStream.of(8, 3, 5).sorted().toArray();                                            // [3, 5, 8]
            show("asc", asc);
            List<Integer> desc = IntStream.of(8, 3, 5).boxed().sorted(Comparator.reverseOrder()).toList();    // [8, 5, 3]
            show("desc", desc);
        }
        {
            int[] looksFine = IntStream.of(5, 1, 9).map(i -> -i).sorted().map(i -> -i).toArray();                    // [9, 5, 1]
            show("looksFine", looksFine);
            int[] wrong = IntStream.of(5, Integer.MIN_VALUE, 9).map(i -> -i).sorted().map(i -> -i).toArray();       // [-2147483648, 9, 5]
            show("wrong", wrong);
        }
        {
            List<String> names = List.of("adam", "Zoe", "bella", "Carl");
            List<String> byCode = names.stream().sorted().toList();                                 // [Carl, Zoe, adam, bella]
            show("byCode", byCode);
            List<String> alphabetical = names.stream().sorted(String.CASE_INSENSITIVE_ORDER).toList();   // [adam, bella, Carl, Zoe]
            show("alphabetical", alphabetical);
            List<String> zToA = names.stream().sorted(String.CASE_INSENSITIVE_ORDER.reversed()).toList();   // [Zoe, Carl, bella, adam]
            show("zToA", zToA);
            List<String> shortFirst = names.stream().sorted(Comparator.comparingInt(String::length).thenComparing(String.CASE_INSENSITIVE_ORDER)).toList();   // [Zoe, adam, Carl, bella]
            show("shortFirst", shortFirst);
        }
        {
            List<String> cities = List.of("zurich", "\u00c9vian", "amsterdam");
            List<String> byCodes = cities.stream().sorted().toList();                                // [amsterdam, zurich, \u00c9vian]
            show("byCodes", byCodes);
            List<String> byLanguage = cities.stream().sorted(Collator.getInstance(Locale.FRENCH)).toList();   // [amsterdam, \u00c9vian, zurich]
            show("byLanguage", byLanguage);
        }
        {
            List<String> files = List.of("track10", "track2", "track1");
            List<String> textOrder = files.stream().sorted().toList();                                                    // [track1, track10, track2]
            show("textOrder", textOrder);
            List<String> numberOrder = files.stream().sorted(Comparator.comparingInt(f -> Integer.parseInt(f.substring(5)))).toList();   // [track1, track2, track10]
            show("numberOrder", numberOrder);
        }
        {
            List<Song> songs = List.of(new Song("Yellow", "Coldplay", 900), new Song("Halo", "Beyonce", 1200), new Song("Clocks", "Coldplay", 1200), new Song("Hello", "Adele", 700));
            List<String> byTitle = songs.stream().sorted(Comparator.comparing(Song::title)).map(Song::title).toList();         // [Clocks, Halo, Hello, Yellow]
            show("byTitle", byTitle);
            List<String> leastPlayed = songs.stream().sorted(Comparator.comparingInt(Song::plays)).map(Song::title).toList();  // [Hello, Yellow, Halo, Clocks]
            show("leastPlayed", leastPlayed);
            List<String> byArtist = songs.stream().sorted(Comparator.comparing(Song::artist).thenComparing(Song::title)).map(Song::title).toList();   // [Hello, Halo, Clocks, Yellow]
            show("byArtist", byArtist);
        }
        {
            Comparator<Song> byPlays = Comparator.comparing(Song::plays).reversed();
            Comparator<Song> byPlaysTyped = Comparator.comparing((Song s) -> s.plays()).reversed();
        }
        {
            List<Song> songs = List.of(new Song("Yellow", "Coldplay", 900), new Song("Halo", "Beyonce", 1200), new Song("Clocks", "Coldplay", 1200), new Song("Hello", "Adele", 700));
            List<String> artistAscPlaysDesc = songs.stream().sorted(Comparator.comparing(Song::artist).thenComparing(Song::plays, Comparator.reverseOrder())).map(Song::title).toList();   // [Hello, Halo, Clocks, Yellow]
            show("artistAscPlaysDesc", artistAscPlaysDesc);
            List<String> bothReversed = songs.stream().sorted(Comparator.comparing(Song::artist).thenComparingInt(Song::plays).reversed()).map(Song::title).toList();                   // [Clocks, Yellow, Halo, Hello]
            show("bothReversed", bothReversed);
            List<String> playsDescTitleAsc = songs.stream().sorted(Comparator.comparingInt(Song::plays).reversed().thenComparing(Song::title)).map(Song::title).toList();              // [Clocks, Halo, Yellow, Hello]
            show("playsDescTitleAsc", playsDescTitleAsc);
        }
        {
            List<String> titles = Arrays.asList("Halo", null, "Clocks");
            try { List<String> crash = titles.stream().sorted().toList(); show("crash", crash); } catch (Throwable _t) { System.out.println("crash -> " + _t); }
            List<String> nullsLast = titles.stream().sorted(Comparator.nullsLast(Comparator.naturalOrder())).toList();   // [Clocks, Halo, null]
            show("nullsLast", nullsLast);
            List<String> nullsFirst = titles.stream().sorted(Comparator.nullsFirst(Comparator.reverseOrder())).toList(); // [null, Halo, Clocks]
            show("nullsFirst", nullsFirst);
        }
        {
            List<Song> library = List.of(new Song("Halo", "Beyonce", 1200), new Song("Intro", null, 50), new Song("Hello", "Adele", 700));
            List<String> unknownLast = library.stream().sorted(Comparator.comparing(Song::artist, Comparator.nullsLast(Comparator.naturalOrder()))).map(Song::title).toList();   // [Hello, Halo, Intro]
            show("unknownLast", unknownLast);
        }
        {
            Map<String, Integer> plays = Map.of("Halo", 1200, "Yellow", 900, "Hello", 700);
            Map<String, Integer> topFirst = plays.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> a, LinkedHashMap::new));   // {Halo=1200, Yellow=900, Hello=700}
            show("topFirst", topFirst);
            List<String> keysByValue = plays.entrySet().stream().sorted(Map.Entry.comparingByValue()).map(Map.Entry::getKey).toList();   // [Hello, Yellow, Halo]
            show("keysByValue", keysByValue);
            Map<String, Integer> byKey = new TreeMap<>(plays);                                                                          // {Halo=1200, Hello=700, Yellow=900}
            show("byKey", byKey);
        }
        {
            List<Integer> sorted = Stream.of(3, 1, 2).sorted().toList();
            try { boolean added = sorted.add(4); show("added", added); } catch (Throwable _t) { System.out.println("added -> " + _t); }
            List<Integer> mutable = Stream.of(3, 1, 2).sorted().collect(Collectors.toCollection(ArrayList::new));   // [1, 2, 3]
            show("mutable", mutable);
            Set<Integer> unique = Stream.of(3, 1, 3, 2).sorted().collect(Collectors.toCollection(LinkedHashSet::new));   // [1, 2, 3]
            show("unique", unique);
            String[] array = Stream.of("c", "a", "b").sorted().toArray(String[]::new);                           // [a, b, c]
            show("array", array);
            String line = Stream.of("c", "a", "b").sorted().collect(Collectors.joining(", "));                   // "a, b, c"
            show("line", line);
        }
        {
            List<Song> songs = List.of(new Song("Yellow", "Coldplay", 900), new Song("Halo", "Beyonce", 1200), new Song("Clocks", "Coldplay", 1200), new Song("Hello", "Adele", 700));
            int kept = songs.stream().collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparingInt(Song::plays)))).size();   // 3
            show("kept", kept);
        }
        {
            List<Song> week = List.of(new Song("Yellow", "Coldplay", 900), new Song("Halo", "Beyonce", 1200), new Song("Clocks", "Coldplay", 1200), new Song("Hello", "Adele", 700));
            List<String> chart = weeklyChart(week, 3);   // [Clocks (1200), Halo (1200), Yellow (900)]
            show("chart", chart);
        }
        {
            List<LocalDate> latestFirst = Stream.of(LocalDate.of(2025, 11, 3), LocalDate.of(2026, 5, 1)).sorted(Comparator.reverseOrder()).toList();   // [2026-05-01, 2025-11-03]
            show("latestFirst", latestFirst);
        }
        {
            List<Integer> numbers = new ArrayList<>(List.of(3, 1, 2));
            List<Integer> copy = numbers.stream().sorted().toList();   // [1, 2, 3]
            show("copy", copy);
            numbers.sort(Comparator.reverseOrder());
            List<Integer> inPlace = numbers;                            // [3, 2, 1]
            show("inPlace", inPlace);
        }
        {
            String sortedChars = "stream".chars().sorted().collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();   // "aemrst"
            show("sortedChars", sortedChars);
        }
        {
            Set<String> tags = new HashSet<>(List.of("rock", "jazz", "pop"));
            List<String> sortedTags = tags.stream().sorted().toList();   // [jazz, pop, rock]
            show("sortedTags", sortedTags);
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

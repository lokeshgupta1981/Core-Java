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
 * Examples for the tutorial "Java Stream Distinct by Multiple Fields (with Examples)".
 * https://howtodoinjava.com/java8/stream-distinct-by-multiple-fields/
 */
public class StreamDistinctByKeyKeepFirstOrLast {
    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));   // add() returns false for a key seen before
    }
    static record Song(String title, String artist, int plays) {
        @Override
        public String toString() { return title + "/" + artist + "/" + plays; }
    }

    static List<Song> songs = List.of(
    new Song("rain", "anna", 120),
    new Song("home", "ben", 90),
    new Song("rain", "anna", 75),
    new Song("home", "carl", 60),
    new Song("home", "ben", 40),
    new Song("rain", "anna", 75));
    @SafeVarargs
    public static <T> Predicate<T> distinctByKeys(Function<? super T, ?>... keyExtractors) {
        List<Function<? super T, ?>> extractors = List.of(keyExtractors);
        Set<List<?>> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(extractors.stream().map(ke -> ke.apply(t)).toList());
    }
    static record SongKey(String title, String artist) {
        SongKey(Song song) {
            this(song.title(), song.artist());
        }

        @Override
        public String toString() { return title + "/" + artist; }
    }
    public static void main(String[] args) throws Exception {
        {
            // songs = [rain/anna/120, home/ben/90, rain/anna/75, home/carl/60, home/ben/40, rain/anna/75]

            List<Song> byList = songs.stream()
                    .filter(distinctByKey(s -> List.of(s.title(), s.artist())))
                    .toList();              // [rain/anna/120, home/ben/90, home/carl/60]

            List<Song> byRecord = songs.stream()
                    .filter(distinctByKey(SongKey::new))     // record SongKey(String title, String artist)
                    .toList();              // [rain/anna/120, home/ben/90, home/carl/60]

            List<Song> keepLast = songs.stream()
                    .collect(Collectors.collectingAndThen(
            Collectors.toMap(SongKey::new, Function.identity(), (a, b) -> b, LinkedHashMap::new),
            m -> new ArrayList<>(m.values())));  // [rain/anna/75, home/ben/40, home/carl/60]
        }
        {
            List<Song> exactCopiesRemoved = songs.stream().distinct().toList();
            // [rain/anna/120, home/ben/90, rain/anna/75, home/carl/60, home/ben/40]
        }
        {
            List<Song> distinctSongs = songs.stream()
                    .filter(distinctByKey(s -> List.of(s.title(), s.artist())))
                    .toList();              // [rain/anna/120, home/ben/90, home/carl/60]

            List<Song> byTitle = songs.stream()
                    .filter(distinctByKey(Song::title))
                    .toList();              // [rain/anna/120, home/ben/90]
        }
        {
            List<Song> distinctSongs = songs.stream()
                    .filter(distinctByKeys(Song::title, Song::artist))
                    .toList();              // [rain/anna/120, home/ben/90, home/carl/60]
        }
        {
            List<Song> distinctSongs = songs.stream()
                    .filter(distinctByKey(SongKey::new))
                    .toList();              // [rain/anna/120, home/ben/90, home/carl/60]
        }
        {
            List<Song> keepFirst = songs.stream()
                    .collect(Collectors.collectingAndThen(
            Collectors.toMap(SongKey::new, Function.identity(), (a, b) -> a, LinkedHashMap::new),
            m -> new ArrayList<>(m.values())));   // [rain/anna/120, home/ben/90, home/carl/60]

            List<Song> keepLast = songs.stream()
                    .collect(Collectors.collectingAndThen(
            Collectors.toMap(SongKey::new, Function.identity(), (a, b) -> b, LinkedHashMap::new),
            m -> new ArrayList<>(m.values())));   // [rain/anna/75, home/ben/40, home/carl/60]

            List<Song> keepMostPlayed = songs.stream()
                    .collect(Collectors.collectingAndThen(
            Collectors.toMap(SongKey::new, Function.identity(),
            BinaryOperator.maxBy(Comparator.comparingInt(Song::plays)), LinkedHashMap::new),
            m -> new ArrayList<>(m.values())));   // [rain/anna/120, home/ben/90, home/carl/60]
        }
        {
            Comparator<Song> byTitleAndArtist = Comparator.comparing(Song::title).thenComparing(Song::artist);

            List<Song> sorted = songs.stream()
                    .collect(Collectors.collectingAndThen(
            Collectors.toCollection(() -> new TreeSet<>(byTitleAndArtist)),
            ArrayList::new));   // [home/ben/90, home/carl/60, rain/anna/120]
        }
        {
            List<Song> big = IntStream.range(0, 100_000).mapToObj(i -> new Song("song" + i % 100, "anna", i)).toList();

            List<Song> sequential = big.stream().filter(distinctByKey(SongKey::new)).toList();
            Song firstKept = sequential.get(0);                                                      // song0/anna/0
            show("firstKept", firstKept);
            List<Song> parallel = big.parallelStream().filter(distinctByKey(SongKey::new)).toList();
            int keys = parallel.size();                                                              // 100
            show("keys", keys);
        }
        {
            Predicate<Song> once = distinctByKey(SongKey::new);
            long firstCount = songs.stream().filter(once).count();     // 3
            show("firstCount", firstCount);
            long secondCount = songs.stream().filter(once).count();    // 0
            show("secondCount", secondCount);
        }
        {
            List<Song> withNull = List.of(new Song("rain", null, 10), new Song("rain", null, 20));

            try { List<Song> listKey = withNull.stream().filter(distinctByKey(s -> List.of(s.title(), s.artist()))).toList(); show("listKey", listKey); } catch (Throwable _t) { System.out.println("listKey -> " + _t); }
            List<Song> arrayKey = withNull.stream().filter(distinctByKey(s -> Arrays.asList(s.title(), s.artist()))).toList(); // [rain/null/10]
            show("arrayKey", arrayKey);
            List<Song> recordKey = withNull.stream().filter(distinctByKey(SongKey::new)).toList();                             // [rain/null/10]
            show("recordKey", recordKey);
        }
        {
            List<Song> tricky = List.of(new Song("ab", "c", 1), new Song("a", "bc", 2));

            List<Song> byString = tricky.stream().filter(distinctByKey(s -> s.title() + s.artist())).toList();   // [ab/c/1]
            show("byString", byString);
            List<Song> byRecord = tricky.stream().filter(distinctByKey(SongKey::new)).toList();                  // [ab/c/1, a/bc/2]
            show("byRecord", byRecord);
        }
        {
            long distinctCount = songs.stream().filter(distinctByKey(SongKey::new)).count();   // 3
            show("distinctCount", distinctCount);

            Map<SongKey, Long> countPerKey = songs.stream().collect(Collectors.groupingBy(SongKey::new, LinkedHashMap::new, Collectors.counting()));
            // {rain/anna=3, home/ben=2, home/carl=1}

            Map<SongKey, List<Integer>> playsPerKey = songs.stream().collect(Collectors.groupingBy(SongKey::new, LinkedHashMap::new,
            Collectors.mapping(Song::plays, Collectors.toList())));
            // {rain/anna=[120, 75, 75], home/ben=[90, 40], home/carl=[60]}
        }
        {
            List<SongKey> keys = songs.stream().map(SongKey::new).distinct().toList();
            // [rain/anna, home/ben, home/carl]
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

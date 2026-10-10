package com.howtodoinjava.core.collections.list;

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
 * Examples for the tutorial "Sort ArrayList in Java: Ascending and Descending Order".
 * https://howtodoinjava.com/java/collections/arraylist/sorting-ascending-descending/
 */
public class SortArrayList {
    static record Song(String title, int plays) implements Comparable<Song> {
        @Override
        public int compareTo(Song other) {
            return Integer.compare(this.plays, other.plays);
        }
    }
    public static <T> void sort(List<T> list, Comparator<? super T> c) {
        list.sort(c);
    }
    public static void main(String[] args) throws Exception {
        {
            List<Integer> scores = new ArrayList<>(List.of(75, 42, 98, 61));
            scores.sort(Comparator.naturalOrder());                    // scores = [42, 61, 75, 98]
            scores.sort(Comparator.reverseOrder());                    // scores = [98, 75, 61, 42]
            List<Integer> copy = scores.stream().sorted().toList();    // [42, 61, 75, 98]
            show("copy", copy);
        }
        {
            Comparator<Song> byTitle = Comparator.comparing(Song::title);
            Comparator<Song> byPlaysDesc = Comparator.comparingInt(Song::plays).reversed();
        }
        {
            List<Song> songs = new ArrayList<>(List.of(
            new Song("Blue", 320), new Song("Echo", 150), new Song("Atlas", 480)));
            songs.sort(Comparator.naturalOrder());
            List<String> byPlays = songs.stream().map(Song::title).toList();    // [Echo, Blue, Atlas]
            show("byPlays", byPlays);
            songs.sort(Comparator.comparing(Song::title));
            List<String> byName = songs.stream().map(Song::title).toList();     // [Atlas, Blue, Echo]
            show("byName", byName);
        }
        {
            List<String> fruits = new ArrayList<>(List.of("banana", "apple", "cherry"));
            fruits.sort(Comparator.reverseOrder());               // fruits = [cherry, banana, apple]
            fruits.sort(Collections.reverseOrder());              // fruits = [cherry, banana, apple]
        }
        {
            List<Song> chart = new ArrayList<>(List.of(
            new Song("Blue", 320), new Song("Echo", 150), new Song("Atlas", 480)));
            chart.sort(Comparator.comparingInt(Song::plays).reversed());
            String top = chart.getFirst().title();                // "Atlas"
            show("top", top);
            List<String> titles = chart.stream().map(Song::title).toList();    // [Atlas, Blue, Echo]
            show("titles", titles);
        }
        {
            List<Integer> nums = new ArrayList<>(List.of(3, 1, 2));
            List<Integer> flipped = nums.reversed();              // [2, 1, 3], not sorted
            show("flipped", flipped);
            Collections.sort(nums);
            Collections.reverse(nums);                            // nums = [3, 2, 1]
        }
        {
            List<Song> playlist = new ArrayList<>(List.of(
            new Song("Blue", 300), new Song("Atlas", 300), new Song("Echo", 500)));
            playlist.sort(Comparator.comparingInt(Song::plays).reversed()
                    .thenComparing(Song::title));
            List<String> ranked = playlist.stream().map(Song::title).toList();   // [Echo, Atlas, Blue]
            show("ranked", ranked);
        }
        {
            List<String> names = new ArrayList<>(Arrays.asList("bob", null, "Alice", "carol"));
            names.sort(Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER));     // names = [Alice, bob, carol, null]
            List<String> plain = new ArrayList<>(List.of("bob", "Alice", "carol"));
            plain.sort(null);                                                     // plain = [Alice, bob, carol]
            List<String> upperFirst = new ArrayList<>(List.of("bob", "alice", "Carol"));
            upperFirst.sort(null);                                                // upperFirst = [Carol, alice, bob]
        }
        {
            List<Integer> ages = new ArrayList<>(List.of(37, 25, 41));
            Collections.sort(ages);                                    // ages = [25, 37, 41]
            Collections.sort(ages, Comparator.reverseOrder());         // ages = [41, 37, 25]
        }
        {
            List<Integer> fixed = List.of(3, 1, 2);
            List<Integer> safe = new ArrayList<>(fixed);
            safe.sort(null);                                           // safe = [1, 2, 3]
        }
        {
            List<Integer> points = new ArrayList<>(List.of(75, 42, 98, 61));
            List<Integer> asc = points.stream().sorted().toList();                              // [42, 61, 75, 98]
            show("asc", asc);
            List<Integer> desc = points.stream().sorted(Comparator.reverseOrder()).toList();    // [98, 75, 61, 42]
            show("desc", desc);
            List<Integer> original = points;                                                    // [75, 42, 98, 61]
            show("original", original);
        }
        {
            List<Song> library = List.of(
            new Song("Blue", 320), new Song("Echo", 150), new Song("Atlas", 480), new Song("Drift", 210));
            List<String> topTwo = library.stream()
                    .filter(s -> s.plays() > 200)
                    .sorted(Comparator.comparingInt(Song::plays).reversed())
                    .limit(2)
                    .map(Song::title)
                    .toList();
            String result = topTwo.toString();                         // "[Atlas, Blue]"
            show("result", result);
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

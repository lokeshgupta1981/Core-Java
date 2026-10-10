package com.howtodoinjava.core.collections.compare;

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
 * Examples for the tutorial "Java Comparator Interface: comparing(), thenComparing(), nulls".
 * https://howtodoinjava.com/java/collections/java-comparator/
 */
public class ComparatorExamples {
    static record Song(String title, String artist, int seconds, Integer rating) {

        @Override
        public String toString() {
            return title;
        }
    }

    static List<Song> playlist() {
        return new ArrayList<>(List.of(
        new Song("Sunrise", "Milo", 215, 4),
        new Song("Rain", "Aria", 187, null),
        new Song("Echo", "Milo", 240, 5),
        new Song("Drift", "Aria", 302, 3)));
    }
    public static void main(String[] args) throws Exception {
        {
            List<String> titles = new ArrayList<>(List.of("Yellow", "abba", "Hey Jude"));
            titles.sort(Comparator.comparing(String::length));
            List<String> byLength = titles;                     // [abba, Yellow, Hey Jude]
            show("byLength", byLength);
            titles.sort(String.CASE_INSENSITIVE_ORDER);
            List<String> ignoreCase = titles;                   // [abba, Hey Jude, Yellow]
            show("ignoreCase", ignoreCase);
            titles.sort(Comparator.reverseOrder());
            List<String> descending = titles;                   // [abba, Yellow, Hey Jude]
            show("descending", descending);
        }
        {
            List<Song> songs = playlist();
            Comparator<Song> byLength = (a, b) -> Integer.compare(a.seconds(), b.seconds());
            songs.sort(byLength);
            List<Song> shortestFirst = songs;                   // [Rain, Sunrise, Echo, Drift]
            show("shortestFirst", shortestFirst);
        }
        {
            List<Song> songs = playlist();
            songs.sort(Comparator.comparing(Song::title));
            List<Song> byTitle = songs;                         // [Drift, Echo, Rain, Sunrise]
            show("byTitle", byTitle);
            songs.sort(Comparator.comparingInt(Song::seconds));
            List<Song> bySeconds = songs;                       // [Rain, Sunrise, Echo, Drift]
            show("bySeconds", bySeconds);
            Song longest = Collections.max(songs, Comparator.comparingInt(Song::seconds));   // Drift
            show("longest", longest);
        }
        {
            List<Song> songs = playlist();
            songs.sort(Comparator.comparing(Song::artist).thenComparingInt(Song::seconds));
            List<Song> byArtistThenLength = songs;              // [Rain, Drift, Sunrise, Echo]
            show("byArtistThenLength", byArtistThenLength);
        }
        {
            List<Song> songs = playlist();
            songs.sort(Comparator.comparing(Song::artist).thenComparingInt(Song::seconds).reversed());
            List<Song> wholeChainReversed = songs;              // [Echo, Sunrise, Drift, Rain]
            show("wholeChainReversed", wholeChainReversed);
            songs.sort(Comparator.comparing(Song::artist).thenComparing(Song::seconds, Comparator.reverseOrder()));
            List<Song> longestFirstPerArtist = songs;           // [Drift, Rain, Echo, Sunrise]
            show("longestFirstPerArtist", longestFirstPerArtist);
        }
        {
            Comparator<Song> withReference = Comparator.comparing(Song::title).reversed();
            Comparator<Song> withTypedLambda = Comparator.comparing((Song s) -> s.title()).reversed();
            int sameOrder = withReference.compare(playlist().get(0), playlist().get(1));   // -1
            show("sameOrder", sameOrder);
        }
        {
            List<Song> songs = playlist();
            try { songs.sort(Comparator.comparing(Song::rating));  } catch (Throwable _t) { System.out.println("-> " + _t); }
        }
        {
            List<Song> songs = playlist();
            songs.sort(Comparator.comparing(Song::rating, Comparator.nullsLast(Comparator.reverseOrder())));
            List<Song> bestFirst = songs;                       // [Echo, Sunrise, Drift, Rain]
            show("bestFirst", bestFirst);
            List<Song> withGap = new ArrayList<>(playlist());
            withGap.add(null);
            withGap.sort(Comparator.nullsFirst(Comparator.comparing(Song::title)));
            List<Song> nullElementFirst = withGap;              // [null, Drift, Echo, Rain, Sunrise]
            show("nullElementFirst", nullElementFirst);
        }
        {
            Set<String> genres = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
            genres.addAll(List.of("Jazz", "rock", "jazz", "Blues"));
            Set<String> uniqueGenres = genres;                  // [Blues, Jazz, rock]
            show("uniqueGenres", uniqueGenres);
            PriorityQueue<Song> queue = new PriorityQueue<>(Comparator.comparingInt(Song::seconds));
            queue.addAll(playlist());
            Song next = queue.poll();                           // Rain
            show("next", next);
            Optional<Song> topRated = playlist().stream().filter(s -> s.rating() != null).max(Comparator.comparing(Song::rating));   // Optional[Echo]
            show("topRated", topRated);
        }
        {
            Map<String, Integer> plays = Map.of("Rain", 12, "Echo", 40, "Drift", 7);
            List<String> mostPlayed = plays.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).map(Map.Entry::getKey).toList();   // [Echo, Rain, Drift]
            show("mostPlayed", mostPlayed);
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

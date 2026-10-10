package com.howtodoinjava.core.basic;

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
 * Examples for the tutorial "Java var Keyword: Local Variable Type Inference Explained".
 * https://howtodoinjava.com/java10/var-local-variable-type-inference/
 */
public class VarTypeInferenceCompileTime {
    static record Song(String title, int seconds, String artist) {}
    public static void main(String[] args) throws Exception {
        {
            var count = 10;                          // 10 (int)
            show("count", count);
            var price = 2.5;                         // 2.5 (double)
            show("price", price);
            var title = "Intro";                     // "Intro" (String)
            show("title", title);
            var titles = new ArrayList<String>();    // [] (ArrayList<String>)
            show("titles", titles);
            var songs = List.of("Intro", "Rise");    // [Intro, Rise] (List<String>)
            show("songs", songs);
        }
        {
            List<Song> songs = List.of(
            new Song("Intro", 95, "Ana"),
            new Song("Rise", 320, "Ben"),
            new Song("Glow", 210, "Ana"));
            int total = 0;
            for (var song : songs) {                 // enhanced for loop variable
                total += song.seconds();
            }
            int playlistSeconds = total;             // 625
            show("playlistSeconds", playlistSeconds);
            int longest = 0;
            for (var i = 0; i < songs.size(); i++) { // basic for loop counter
                longest = Math.max(longest, songs.get(i).seconds());
            }
            int longestSeconds = longest;            // 320
            show("longestSeconds", longestSeconds);
        }
        {
            String firstLine;
            try (var reader = new BufferedReader(new StringReader("intro\nchorus"))) {
                firstLine = reader.readLine();
            }
            String result = firstLine;               // "intro"
            show("result", result);
        }
        {
            BinaryOperator<Integer> longer = (final var a, final var b) -> Math.max(a, b);
            int max = longer.apply(210, 320);        // 320
            show("max", max);
        }
        {
            var ids = new int[] {1, 2};              // [1, 2] (int[])
            show("ids", ids);
            Supplier<Integer> one = () -> 1;
            int value = one.get();                   // 1
            show("value", value);
        }
        {
            var var = 10;                            // 10 (variable named var)
            show("var", var);
            int doubled = var * 2;                   // 20
            show("doubled", doubled);
        }
        {
            var tags = new ArrayList<>();            // [] (ArrayList<Object>)
            show("tags", tags);
            tags.add("rock");
            tags.add(5);                             // compiles, no type safety
            var genres = new ArrayList<String>();    // [] (ArrayList<String>)
            show("genres", genres);
            genres.add("rock");
            int genreCount = genres.size();          // 1
            show("genreCount", genreCount);
        }
        {
            var rating = 0;
            rating += 4.5;
            int stored = rating;                     // 4
            show("stored", stored);
        }
        {
            var average = 0.0;
            average += 4.5;
            double kept = average;                   // 4.5
            show("kept", kept);
        }
        {
            List<Song> songs = List.of(
            new Song("Intro", 95, "Ana"),
            new Song("Rise", 320, "Ben"),
            new Song("Glow", 210, "Ana"));
            // 1. Explicit types
            Map<String, List<Song>> byArtist = songs.stream()
                    .collect(Collectors.groupingBy(Song::artist));
            for (Map.Entry<String, List<Song>> entry : byArtist.entrySet()) {
                String artist = entry.getKey();
            }
            int artistCount = byArtist.size();       // 2
            show("artistCount", artistCount);
            // 2. The same code with var
            var songsByArtist = songs.stream()
                    .collect(Collectors.groupingBy(Song::artist));
            for (var entry : songsByArtist.entrySet()) {
                String artist = entry.getKey();
            }
            int anaSongs = songsByArtist.get("Ana").size(); // 2
            show("anaSongs", anaSongs);
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

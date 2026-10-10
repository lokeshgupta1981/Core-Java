package com.howtodoinjava.core.string;

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
 * Examples for the tutorial "Java StringJoiner: Join Strings With a Delimiter and Prefix".
 * https://howtodoinjava.com/java/string/java8-stringjoiner-example/
 */
public class StringJoinerGuide {
    static String inClause(int count) {
        StringJoiner placeholders = new StringJoiner(", ", "(", ")");
        placeholders.setEmptyValue("(NULL)");
        for (int i = 0; i < count; i++) {
            placeholders.add("?");
        }
        return placeholders.toString();
    }
    public static void main(String[] args) throws Exception {
        {
            StringJoiner playlist = new StringJoiner(", ", "[", "]");
            playlist.add("Yellow").add("Clocks").add("Fix You");
            String result = playlist.toString();                       // "[Yellow, Clocks, Fix You]"
            show("result", result);
            int length = playlist.length();                            // 25
            show("length", length);
        }
        {
            StringJoiner plain = new StringJoiner(",");
            StringJoiner wrapped = new StringJoiner(", ", "{", "}");
            String plainEmpty = plain.toString();                      // ""
            show("plainEmpty", plainEmpty);
            String wrappedEmpty = wrapped.toString();                  // "{}"
            show("wrappedEmpty", wrappedEmpty);
            try { StringJoiner broken = new StringJoiner(null); show("broken", broken); } catch (Throwable _t) { System.out.println("broken -> " + _t); }
        }
        {
            StringJoiner genres = new StringJoiner(" | ");
            for (String genre : List.of("rock", "jazz", "pop")) {
                genres.add(genre);
            }
            String result = genres.toString();                         // "rock | jazz | pop"
            show("result", result);
        }
        {
            StringJoiner raw = new StringJoiner(",");
            raw.add("rock").add(null).add("").add("pop");
            String unfiltered = raw.toString();                        // "rock,null,,pop"
            show("unfiltered", unfiltered);

            StringJoiner clean = new StringJoiner(",");
            for (String genre : Arrays.asList("rock", null, "", "pop")) {
                if (genre != null && !genre.isBlank()) {
                    clean.add(genre);
                }
            }
            String filtered = clean.toString();                        // "rock,pop"
            show("filtered", filtered);
        }
        {
            StringJoiner songs = new StringJoiner(", ", "[", "]");
            songs.setEmptyValue("no songs");
            String before = songs.toString();                          // "no songs"
            show("before", before);
            songs.add("Clocks");
            String after = songs.toString();                           // "[Clocks]"
            show("after", after);
        }
        {
            StringJoiner withEmpty = new StringJoiner(", ", "[", "]");
            withEmpty.setEmptyValue("no songs");
            withEmpty.add("");
            String result = withEmpty.toString();                      // "[]"
            show("result", result);
        }
        {
            StringJoiner picked = new StringJoiner(", ", "[", "]");
            picked.add("Yellow").add("Clocks");
            StringJoiner suggested = new StringJoiner(" / ", "{", "}");
            suggested.add("Shiver").add("Sparks");
            String merged = picked.merge(suggested).toString();        // "[Yellow, Clocks, Shiver / Sparks]"
            show("merged", merged);
        }
        {
            List<String> titles = List.of("Yellow", "Clocks", "Fix You");
            String joined = String.join(", ", titles);                                   // "Yellow, Clocks, Fix You"
            show("joined", joined);
            String collected = titles.stream().collect(Collectors.joining(", ", "[", "]"));  // "[Yellow, Clocks, Fix You]"
            show("collected", collected);
            String upper = titles.stream().map(t -> t.toUpperCase(Locale.ROOT)).collect(Collectors.joining("/"));  // "YELLOW/CLOCKS/FIX YOU"
            show("upper", upper);
        }
        {
            String three = "SELECT * FROM song WHERE playlist_id IN " + inClause(3);   // "SELECT * FROM song WHERE playlist_id IN (?, ?, ?)"
            show("three", three);
            String none = inClause(0);                                                   // "(NULL)"
            show("none", none);
        }
        {
            String names = Stream.of("rock", null, " ", "pop").filter(s -> s != null && !s.isBlank()).collect(Collectors.joining(","));  // "rock,pop"
            show("names", names);
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

package com.howtodoinjava.core.array;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.time.*;
import java.nio.file.*;
import java.math.*;
import java.io.*;

import org.apache.commons.lang3.StringUtils;
import com.google.common.base.Joiner;
import java.util.regex.*;
import java.nio.charset.*;
import java.nio.*;
import java.security.*;
import java.lang.reflect.*;
import java.lang.invoke.*;

/**
 * Examples for the tutorial "Join String Array in Java with Comma or Any Separator".
 * https://howtodoinjava.com/java/array/join-string-array-example/
 */
public class JoinArrayItems {
    static record Song(String title, int seconds) {}
    public static void main(String[] args) throws Exception {
        {
            String[] genres = {"rock", "jazz", "pop"};
            String csv = String.join(",", genres);                                         // "rock,jazz,pop"
            show("csv", csv);
            String spaced = String.join(", ", genres);                                     // "rock, jazz, pop"
            show("spaced", spaced);
            String wrapped = Arrays.stream(genres).collect(Collectors.joining(", ", "[", "]"));   // "[rock, jazz, pop]"
            show("wrapped", wrapped);
        }
        {
            String[] genres = {"rock", "jazz", "pop"};
            String fromArray = String.join(" | ", genres);                  // "rock | jazz | pop"
            show("fromArray", fromArray);
            String fromArgs = String.join("-", "2026", "10", "10");         // "2026-10-10"
            show("fromArgs", fromArgs);
            String fromList = String.join(", ", List.of("ana", "bob"));     // "ana, bob"
            show("fromList", fromList);
        }
        {
            String[] withNull = {"rock", null, "pop"};
            String joined = String.join(",", withNull);                     // "rock,null,pop"
            show("joined", joined);
            String none = String.join(",", new String[0]);                  // ""
            show("none", none);
            try { String crash = String.join(",", (String[]) null); show("crash", crash); } catch (Throwable _t) { System.out.println("crash -> " + _t); }
        }
        {
            String[] tags = {"java", null, " ", "spring"};
            String clean = Arrays.stream(tags).filter(t -> t != null && !t.isBlank()).collect(Collectors.joining(", "));   // "java, spring"
            show("clean", clean);
        }
        {
            String[] genres = {"rock", "jazz", "pop"};
            String sorted = Arrays.stream(genres).sorted().collect(Collectors.joining(", "));   // "jazz, pop, rock"
            show("sorted", sorted);
            String json = Arrays.stream(genres).map(g -> "\"" + g + "\"").collect(Collectors.joining(",", "[", "]"));   // "[\"rock\",\"jazz\",\"pop\"]"
            show("json", json);
        }
        {
            StringJoiner names = new StringJoiner(", ", "Playing: ", ".");
            names.add("rock");
            names.add("jazz");
            String text = names.toString();   // "Playing: rock, jazz."
            show("text", text);
        }
        {
            StringJoiner empty = new StringJoiner(", ", "[", "]");
            String bare = empty.toString();                       // "[]"
            show("bare", bare);
            empty.setEmptyValue("no genres");
            String friendly = empty.toString();                   // "no genres"
            show("friendly", friendly);
        }
        {
            int[] ids = {3, 12, 7};
            String idList = Arrays.stream(ids).mapToObj(String::valueOf).collect(Collectors.joining(","));   // "3,12,7"
            show("idList", idList);
        }
        {
            Song[] songs = {new Song("Intro", 95), new Song("Outro", 120)};
            String titles = Arrays.stream(songs).map(Song::title).collect(Collectors.joining(" / "));   // "Intro / Outro"
            show("titles", titles);
        }
        {
            String[] genres = {"rock", "jazz", "pop"};
            StringBuilder sb = new StringBuilder();
            for (String g : genres) {
                if (sb.length() > 0) {
                    sb.append(", ");
                }
                sb.append(g);
            }
            String result = sb.toString();   // "rock, jazz, pop"
            show("result", result);
        }
        {
            String[] withNull = {"rock", null, "pop"};
            String commons = StringUtils.join(withNull, ",");                   // "rock,,pop"
            show("commons", commons);
            String commonsNull = StringUtils.join((String[]) null, ",");        // null
            show("commonsNull", commonsNull);
            String skipped = Joiner.on(", ").skipNulls().join(withNull);        // "rock, pop"
            show("skipped", skipped);
            String replaced = Joiner.on(", ").useForNull("?").join(withNull);   // "rock, ?, pop"
            show("replaced", replaced);
            try { String strict = Joiner.on(", ").join(withNull); show("strict", strict); } catch (Throwable _t) { System.out.println("strict -> " + _t); }
        }
        {
            String[] picked = {"rock", "jazz", "pop"};
            String placeholders = String.join(", ", Collections.nCopies(picked.length, "?"));   // "?, ?, ?"
            show("placeholders", placeholders);
            String sql = "SELECT title FROM song WHERE genre IN (" + placeholders + ")";   // "SELECT title FROM song WHERE genre IN (?, ?, ?)"
            show("sql", sql);
        }
        {
            List<Integer> scores = List.of(70, 85, 90);
            String text = scores.stream().map(String::valueOf).collect(Collectors.joining(", "));   // "70, 85, 90"
            show("text", text);
        }
        {
            String[] genres = {"rock", "jazz", "pop"};
            String head = String.join(", ", Arrays.copyOf(genres, genres.length - 1));
            String sentence = head + " and " + genres[genres.length - 1];   // "rock, jazz and pop"
            show("sentence", sentence);
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

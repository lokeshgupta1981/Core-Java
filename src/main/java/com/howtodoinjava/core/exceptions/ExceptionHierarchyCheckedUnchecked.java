package com.howtodoinjava.core.exceptions;

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
 * Examples for the tutorial "Checked vs Unchecked Exceptions in Java (With Hierarchy)".
 * https://howtodoinjava.com/java/exception-handling/checked-vs-unchecked-exceptions-in-java/
 */
public class ExceptionHierarchyCheckedUnchecked {
    static List<String> loadOrEmpty(Path file) {
        try {
            return Files.readAllLines(file);
        } catch (IOException e) {
            return List.of();                       // missing or unreadable file
        }
    }
    static List<String> loadTracks(Path file) throws IOException {
        return Files.readAllLines(file);
    }
    static record Track(String title, int seconds) {
        Track {
            Objects.requireNonNull(title, "title");
            if (seconds < 0) {
                throw new IllegalArgumentException("seconds must be >= 0: " + seconds);
            }
        }
    }
    static String readQuietly(Path file) {
        try {
            return Files.readString(file);
        } catch (IOException e) {
            throw new UncheckedIOException("Cannot read " + file, e);
        }
    }
    public static void main(String[] args) throws Exception {
        {
            String error = "none";
            try {
                String text = Files.readString(Path.of("missing.m3u"));  // checked: needs try or throws
            } catch (IOException e) {
                error = e.getClass().getSimpleName();
            }
            try { String caught = error; show("caught", caught); } catch (Throwable _t) { System.out.println("caught -> " + _t); }
            try { int seconds = Integer.parseInt("3:45"); show("seconds", seconds); } catch (Throwable _t) { System.out.println("seconds -> " + _t); }
        }
        {
            List<String> tracks = loadOrEmpty(Path.of("missing.m3u"));   // []
            show("tracks", tracks);
        }
        {
            try { List<String> loaded = loadTracks(Path.of("missing.m3u")); show("loaded", loaded); } catch (Throwable _t) { System.out.println("loaded -> " + _t); }
        }
        {
            List<String> queue = List.of("intro", "chorus");
            try { String third = queue.get(2); show("third", third); } catch (Throwable _t) { System.out.println("third -> " + _t); }
            String title = null;
            try { int length = title.length(); show("length", length); } catch (Throwable _t) { System.out.println("length -> " + _t); }
            Object track = "intro";
            try { Integer id = (Integer) track; show("id", id); } catch (Throwable _t) { System.out.println("id -> " + _t); }
        }
        {
            Track ok = new Track("intro", 225);           // Track[title=intro, seconds=225]
            show("ok", ok);
            try { Track bad = new Track("outro", -5); show("bad", bad); } catch (Throwable _t) { System.out.println("bad -> " + _t); }
        }
        {
            List<String> names = List.of("a.m3u", "b.m3u");
            try { List<String> files = names.stream().map(Path::of).map(file -> readQuietly(file)).toList(); show("files", files); } catch (Throwable _t) { System.out.println("files -> " + _t); }
        }
        {
            Thread worker = new Thread(() -> { });
            try { worker.stop();  } catch (Throwable _t) { System.out.println("-> " + _t); }
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

package com.howtodoinjava.newFeatures;

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
 * Examples for the tutorial "Java Unnamed Variables and Patterns: Using _ (Java 22+)".
 * https://howtodoinjava.com/java/unnamed-patterns-and-variables/
 */
public class UnnamedVariablesAllowedPlaces {
    static record Track(String title, String artist, int seconds) {}
    static record Span(String name, List<String> log) implements AutoCloseable {
        Span {
            log.add("start " + name);
        }

        @Override
        public void close() {
            log.add("end " + name);
        }
    }
    static sealed interface Media permits Song, Podcast, Audiobook {}
    static record Song(String title, String artist, int seconds) implements Media {}
    static record Podcast(String show, int episode, int seconds) implements Media {}
    static record Audiobook(String title, String narrator, int seconds) implements Media {}
    static record PlayRow(String user, String track, String plays) {}
    public static void main(String[] args) throws Exception {
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

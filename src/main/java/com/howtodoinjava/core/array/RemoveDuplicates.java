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

import java.util.regex.*;
import java.nio.charset.*;
import java.nio.*;
import java.security.*;
import java.lang.reflect.*;
import java.lang.invoke.*;

/**
 * Examples for the tutorial "Remove Duplicates from an Array in Java: Find, Count, Remove".
 * https://howtodoinjava.com/java/array/array-remove-duplicate-elements/
 */
public class RemoveDuplicates {
    static Set<Integer> findDuplicates(int[] array) {
        Set<Integer> seen = new HashSet<>();
        Set<Integer> duplicates = new LinkedHashSet<>();
        for (int value : array) {
            if (!seen.add(value)) {
                duplicates.add(value);
            }
        }
        return duplicates;
    }
    static OptionalInt firstRepeated(int[] array) {
        Set<Integer> seen = new HashSet<>();
        for (int value : array) {
            if (!seen.add(value)) {
                return OptionalInt.of(value);
            }
        }
        return OptionalInt.empty();
    }
    static int dedupeSorted(int[] sorted) {
        if (sorted.length == 0) {
            return 0;
        }
        int write = 1;
        for (int read = 1; read < sorted.length; read++) {
            if (sorted[read] != sorted[write - 1]) {
                sorted[write++] = sorted[read];
            }
        }
        return write;
    }
    static record Seat(String row, int number) {}
    public static void main(String[] args) throws Exception {
        {
            int[] views = {3, 7, 3, 9, 7, 3};
            int[] distinct = Arrays.stream(views).distinct().toArray();   // [3, 7, 9]
            show("distinct", distinct);
            Map<Integer, Long> counts = Arrays.stream(views).boxed().collect(Collectors.groupingBy(v -> v, LinkedHashMap::new, Collectors.counting()));   // {3=3, 7=2, 9=1}
            show("counts", counts);
            List<Integer> duplicates = counts.entrySet().stream().filter(e -> e.getValue() > 1).map(Map.Entry::getKey).toList();   // [3, 7]
            show("duplicates", duplicates);
        }
        {
            Set<Integer> repeated = findDuplicates(new int[]{3, 7, 3, 9, 7, 3});   // [3, 7]
            show("repeated", repeated);
            Set<Integer> clean = findDuplicates(new int[]{1, 2, 3});              // []
            show("clean", clean);
        }
        {
            OptionalInt first = firstRepeated(new int[]{5, 1, 4, 1, 5});   // OptionalInt[1]
            show("first", first);
            OptionalInt none = firstRepeated(new int[]{5, 1, 4});          // OptionalInt.empty
            show("none", none);
        }
        {
            int[] seats = {12, 4, 12, 7, 4, 12};
            Map<Integer, Integer> freq = new TreeMap<>();
            for (int seat : seats) {
                freq.merge(seat, 1, Integer::sum);
            }
            Map<Integer, Integer> bySeat = freq;   // {4=2, 7=1, 12=3}
            show("bySeat", bySeat);
        }
        {
            String[] colors = {"red", "blue", "red", "green", "blue", "red"};
            Map<String, Long> colorCounts = Arrays.stream(colors).collect(Collectors.groupingBy(c -> c, LinkedHashMap::new, Collectors.counting()));   // {red=3, blue=2, green=1}
            show("colorCounts", colorCounts);
            long duplicateValues = colorCounts.values().stream().filter(n -> n > 1).count();   // 2
            show("duplicateValues", duplicateValues);
            long extraCopies = colors.length - colorCounts.size();                              // 3
            show("extraCopies", extraCopies);
            List<String> onlyOnce = colorCounts.entrySet().stream().filter(e -> e.getValue() == 1).map(Map.Entry::getKey).toList();   // [green]
            show("onlyOnce", onlyOnce);
        }
        {
            int[] scores = {40, 10, 40, 30, 10};
            int[] uniqueScores = Arrays.stream(scores).distinct().toArray();                    // [40, 10, 30]
            show("uniqueScores", uniqueScores);
            int[] sortedUnique = Arrays.stream(scores).distinct().sorted().toArray();           // [10, 30, 40]
            show("sortedUnique", sortedUnique);
            String[] names = {"Ann", "Bob", "Ann"};
            String[] uniqueNames = Arrays.stream(names).distinct().toArray(String[]::new);       // [Ann, Bob]
            show("uniqueNames", uniqueNames);
        }
        {
            String[] cities = {"Pune", "Agra", "Pune", "Goa"};
            String[] inOrder = new LinkedHashSet<>(Arrays.asList(cities)).toArray(String[]::new);   // [Pune, Agra, Goa]
            show("inOrder", inOrder);
            String[] sorted = new TreeSet<>(Arrays.asList(cities)).toArray(String[]::new);          // [Agra, Goa, Pune]
            show("sorted", sorted);
        }
        {
            int[] marks = {2, 2, 5, 7, 7, 7, 9};
            int newLength = dedupeSorted(marks);             // 4
            show("newLength", newLength);
            int[] uniqueMarks = Arrays.copyOf(marks, newLength);   // [2, 5, 7, 9]
            show("uniqueMarks", uniqueMarks);
            int emptyLength = dedupeSorted(new int[0]);      // 0
            show("emptyLength", emptyLength);
        }
        {
            Seat[] booked = {new Seat("A", 1), new Seat("B", 4), new Seat("A", 1)};
            Seat[] uniqueSeats = Arrays.stream(booked).distinct().toArray(Seat[]::new);   // [Seat[row=A, number=1], Seat[row=B, number=4]]
            show("uniqueSeats", uniqueSeats);
            boolean hasDuplicate = uniqueSeats.length != booked.length;                   // true
            show("hasDuplicate", hasDuplicate);
        }
        {
            String[] langs = {"Java", "java", "Kotlin", "JAVA"};
            Set<String> ignoreCase = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
            ignoreCase.addAll(Arrays.asList(langs));
            Set<String> result = ignoreCase;   // [Java, Kotlin]
            show("result", result);
        }
        {
            String[] signups = {"ann@mail.com", "Bob@mail.com ", "ANN@mail.com", "bob@mail.com", "cid@mail.com"};
            String[] normalized = Arrays.stream(signups).map(s -> s.strip().toLowerCase(Locale.ROOT)).toArray(String[]::new);
            String[] recipients = Arrays.stream(normalized).distinct().toArray(String[]::new);   // [ann@mail.com, bob@mail.com, cid@mail.com]
            show("recipients", recipients);
            Set<String> repeatedSignups = new LinkedHashSet<>();
            Set<String> seenSignups = new HashSet<>();
            for (String email : normalized) {
                if (!seenSignups.add(email)) {
                    repeatedSignups.add(email);
                }
            }
            Set<String> report = repeatedSignups;   // [ann@mail.com, bob@mail.com]
            show("report", report);
        }
        {
            int[] ids = {4, 8, 15, 8};
            boolean anyDup = Arrays.stream(ids).distinct().count() != ids.length;   // true
            show("anyDup", anyDup);
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

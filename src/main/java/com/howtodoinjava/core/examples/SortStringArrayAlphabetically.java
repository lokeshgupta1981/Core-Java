package com.howtodoinjava.core.examples;

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
import java.text.Collator;

/**
 * Examples for the tutorial "Sort a String Array Alphabetically in Java".
 * https://howtodoinjava.com/java-examples/sort-string-array-alphabetically/
 */
public class SortStringArrayAlphabetically {
    static void insertionSort(String[] values) {
        for (int i = 1; i < values.length; i++) {
            String current = values[i];
            int j = i - 1;
            while (j >= 0 && values[j].compareToIgnoreCase(current) > 0) {
                values[j + 1] = values[j];
                j--;
            }
            values[j + 1] = current;
        }
    }
    public static void main(String[] args) throws Exception {
        {
            String[] cities = {"paris", "Oslo", "berlin", "Zurich", "Amsterdam"};
            Arrays.sort(cities);
            String natural = Arrays.toString(cities);           // "[Amsterdam, Oslo, Zurich, berlin, paris]"
            show("natural", natural);
            Arrays.sort(cities, String.CASE_INSENSITIVE_ORDER);
            String ignoreCase = Arrays.toString(cities);        // "[Amsterdam, berlin, Oslo, paris, Zurich]"
            show("ignoreCase", ignoreCase);
            Arrays.sort(cities, String.CASE_INSENSITIVE_ORDER.reversed());
            String reversed = Arrays.toString(cities);          // "[Zurich, paris, Oslo, berlin, Amsterdam]"
            show("reversed", reversed);
        }
        {
            String[] tags = {"java", "Spring", "API", "docker", "Kafka"};
            Arrays.sort(tags, String.CASE_INSENSITIVE_ORDER);
            String sorted = Arrays.toString(tags);               // "[API, docker, java, Kafka, Spring]"
            show("sorted", sorted);
        }
        {
            String[] names = {"oslo", "OSLO", "Oslo"};
            Arrays.sort(names, String.CASE_INSENSITIVE_ORDER.thenComparing(Comparator.naturalOrder()));
            String tieBreak = Arrays.toString(names);            // "[OSLO, Oslo, oslo]"
            show("tieBreak", tieBreak);
        }
        {
            String[] cities = {"paris", "Oslo", "berlin", "Zurich", "Amsterdam"};
            Arrays.sort(cities, Comparator.reverseOrder());
            String naturalDesc = Arrays.toString(cities);        // "[paris, berlin, Zurich, Oslo, Amsterdam]"
            show("naturalDesc", naturalDesc);
            Arrays.sort(cities, String.CASE_INSENSITIVE_ORDER.reversed());
            String ignoreCaseDesc = Arrays.toString(cities);     // "[Zurich, paris, Oslo, berlin, Amsterdam]"
            show("ignoreCaseDesc", ignoreCaseDesc);
        }
        {
            String aalesund = "\u00c5lesund";
            String[] towns = {"paris", "Oslo", "berlin", "Zurich", "Amsterdam", aalesund};
            Arrays.sort(towns);
            int naturalIndex = List.of(towns).indexOf(aalesund);          // 5, after Zurich
            show("naturalIndex", naturalIndex);
            Arrays.sort(towns, Collator.getInstance(Locale.ENGLISH));
            int englishIndex = List.of(towns).indexOf(aalesund);          // 0, before Amsterdam
            show("englishIndex", englishIndex);
            String rest = String.join(", ", List.of(towns).subList(1, 6)); // "Amsterdam, berlin, Oslo, paris, Zurich"
            show("rest", rest);
            Arrays.sort(towns, Collator.getInstance(Locale.forLanguageTag("sv")));
            int swedishIndex = List.of(towns).indexOf(aalesund);          // 5, A-ring sorts after Z in Swedish
            show("swedishIndex", swedishIndex);
        }
        {
            Collator primary = Collator.getInstance(Locale.ENGLISH);
            primary.setStrength(Collator.PRIMARY);
            int sameLetters = primary.compare("\u00c5lesund", "Alesund");     // 0
            show("sameLetters", sameLetters);
            int sameIgnoringCase = primary.compare("Oslo", "oslo");           // 0
            show("sameIgnoringCase", sameIgnoringCase);
        }
        {
            String[] entered = {"paris", "Oslo", "berlin"};
            String[] sortedCopy = Stream.of(entered).sorted(String.CASE_INSENSITIVE_ORDER).toArray(String[]::new);   // [berlin, Oslo, paris]
            show("sortedCopy", sortedCopy);
            String original = Arrays.toString(entered);           // "[paris, Oslo, berlin]"
            show("original", original);
        }
        {
            List<String> list = new ArrayList<>(List.of("paris", "Oslo", "berlin"));
            list.sort(String.CASE_INSENSITIVE_ORDER);
            String sortedList = list.toString();                  // "[berlin, Oslo, paris]"
            show("sortedList", sortedList);
        }
        {
            String[] withNull = {"Oslo", null, "Amsterdam"};
            try { Arrays.sort(withNull);  } catch (Throwable _t) { System.out.println("-> " + _t); }
        }
        {
            String[] imported = {"Oslo", null, "amsterdam"};
            Arrays.sort(imported, Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER));
            String safe = Arrays.toString(imported);              // "[amsterdam, Oslo, null]"
            show("safe", safe);
        }
        {
            String[] options = {"Select a city", "paris", "Oslo", "berlin"};
            Arrays.sort(options, 1, options.length, String.CASE_INSENSITIVE_ORDER);
            String menu = Arrays.toString(options);               // "[Select a city, berlin, Oslo, paris]"
            show("menu", menu);
        }
        {
            String[] words = {"tram", "ox", "camel", "ant", "bee"};
            Arrays.sort(words, Comparator.comparingInt(String::length).thenComparing(Comparator.naturalOrder()));
            String byLength = Arrays.toString(words);             // "[ox, ant, bee, tram, camel]"
            show("byLength", byLength);
        }
        {
            String[] files = {"file10", "file2", "file1"};
            Arrays.sort(files);
            String plain = Arrays.toString(files);                // "[file1, file10, file2]"
            show("plain", plain);
            Arrays.sort(files, Comparator.comparingInt(String::length).thenComparing(Comparator.naturalOrder()));
            String numeric = Arrays.toString(files);              // "[file1, file2, file10]"
            show("numeric", numeric);
        }
        {
            Comparator<String> contactOrder = Comparator.nullsLast(Collator.getInstance(Locale.ENGLISH));
            String[] contacts = {"zoe", null, "Ana", "\u00c9lodie", "emma", "Bruno"};
            Arrays.sort(contacts, contactOrder);
            int elodieIndex = Arrays.asList(contacts).indexOf("\u00c9lodie");     // 2, between Bruno and emma
            show("elodieIndex", elodieIndex);
            String last = contacts[contacts.length - 1];                         // null
            show("last", last);
        }
        {
            String[] cities = {"paris", "Oslo", "berlin"};
            insertionSort(cities);
            String manual = Arrays.toString(cities);              // "[berlin, Oslo, paris]"
            show("manual", manual);
        }
        {
            char[] letters = "Kotlin".toCharArray();
            Arrays.sort(letters);
            String sortedChars = new String(letters);             // "Kilnot"
            show("sortedChars", sortedChars);
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

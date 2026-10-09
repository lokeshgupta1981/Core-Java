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

import org.apache.commons.lang3.ArrayUtils;
import java.util.regex.*;
import java.nio.charset.*;
import java.nio.*;
import java.security.*;
import java.lang.reflect.*;
import java.lang.invoke.*;

/**
 * Examples for the tutorial "Check if an Array Contains a Value in Java (int and String)".
 * https://howtodoinjava.com/java/array/array-contains-example/
 */
public class ArrayContains {
    static record Room(String type, int beds) {}
    static <T> int indexOf(T[] array, T value) {
        if (array == null) {
            return -1;
        }
        for (int i = 0; i < array.length; i++) {
            if (Objects.equals(array[i], value)) {
                return i;
            }
        }
        return -1;
    }
    static <T> boolean contains(T[] array, T value) {
        return indexOf(array, value) >= 0;
    }
    public static void main(String[] args) throws Exception {
        {
            String[] tags = {"java", "spring", "kafka"};
            boolean hasSpring = Arrays.asList(tags).contains("spring");      // true
            show("hasSpring", hasSpring);
            int kafkaAt = Arrays.asList(tags).indexOf("kafka");              // 2
            show("kafkaAt", kafkaAt);
            int[] ports = {80, 443, 8080};
            boolean hasHttps = Arrays.stream(ports).anyMatch(p -> p == 443);  // true
            show("hasHttps", hasHttps);
        }
        {
            String[] tags = {"java", null, "kafka"};
            boolean found = Arrays.asList(tags).contains(new String("java"));   // true
            show("found", found);
            boolean hasNull = Arrays.asList(tags).contains(null);               // true
            show("hasNull", hasNull);
            int missing = Arrays.asList(tags).indexOf("maven");                 // -1
            show("missing", missing);
            Room[] rooms = {new Room("twin", 2), new Room("suite", 4)};
            boolean hasSuite = Arrays.asList(rooms).contains(new Room("suite", 4));   // true
            show("hasSuite", hasSuite);
        }
        {
            String[] codes = {"a", "b"};
            try { boolean viaListOf = List.of(codes).contains(null); show("viaListOf", viaListOf); } catch (Throwable _t) { System.out.println("viaListOf -> " + _t); }
        }
        {
            int[] ports = {80, 443, 8080};
            boolean wrong = Arrays.asList(ports).contains(443);            // false
            show("wrong", wrong);
            int size = Arrays.asList(ports).size();                        // 1
            show("size", size);
            boolean right = Arrays.stream(ports).anyMatch(p -> p == 443);  // true
            show("right", right);
            boolean boxed = Arrays.stream(ports).boxed().toList().contains(443);   // true
            show("boxed", boxed);
        }
        {
            char[] grade = {'A', 'B', 'C'};
            boolean hasB = new String(grade).indexOf('B') >= 0;                        // true
            show("hasB", hasB);
            byte[] flags = {1, 0, 4};
            boolean hasFour = IntStream.range(0, flags.length).anyMatch(i -> flags[i] == 4);   // true
            show("hasFour", hasFour);
            double[] rates = {0.5, 1.25};
            boolean hasRate = Arrays.stream(rates).anyMatch(r -> r == 1.25);           // true
            show("hasRate", hasRate);
        }
        {
            String[] beds = {"single", null, "double"};
            boolean hasDouble = contains(beds, "double");   // true
            show("hasDouble", hasDouble);
            int nullAt = indexOf(beds, null);              // 1
            show("nullAt", nullAt);
            boolean none = contains(null, "single");        // false
            show("none", none);
        }
        {
            String[] tags = {"Java", "Spring", "Kafka"};
            boolean ignoreCase = Arrays.stream(tags).anyMatch(t -> t.equalsIgnoreCase("spring"));   // true
            show("ignoreCase", ignoreCase);
            Room[] rooms = {new Room("twin", 2), new Room("suite", 4)};
            boolean family = Arrays.stream(rooms).anyMatch(r -> r.beds() >= 4);                     // true
            show("family", family);
            OptionalInt firstBig = IntStream.range(0, rooms.length).filter(i -> rooms[i].beds() >= 4).findFirst();   // OptionalInt[1]
            show("firstBig", firstBig);
        }
        {
            String[] tags = {"Java", "Spring", "Kafka"};
            List<String> required = List.of("Java", "Kafka");
            boolean all = Arrays.asList(tags).containsAll(required);   // true
            show("all", all);
        }
        {
            int[] years = {2019, 2021, 2023, 2025};
            int idx = Arrays.binarySearch(years, 2023);   // 2
            show("idx", idx);
            int gap = Arrays.binarySearch(years, 2022);   // -3, would go at index 2
            show("gap", gap);
            boolean has2022 = gap >= 0;                   // false
            show("has2022", has2022);
        }
        {
            int[] codes = {40, 10, 30, 20};
            int unsorted = Arrays.binarySearch(codes, 40);   // -5, wrong, 40 is at index 0
            show("unsorted", unsorted);
            int[] sorted = codes.clone();
            Arrays.sort(sorted);
            int ok = Arrays.binarySearch(sorted, 40);        // 3
            show("ok", ok);
        }
        {
            String[] allowed = {"png", "jpg", "pdf", "png"};
            Set<String> allowedSet = new HashSet<>(Arrays.asList(allowed));
            boolean okPdf = allowedSet.contains("pdf");   // true
            show("okPdf", okPdf);
            boolean okExe = allowedSet.contains("exe");   // false
            show("okExe", okExe);
        }
        {
            String[] allowed = {"png", "jpg", "pdf", "png"};
            try { Set<String> strict = Set.of(allowed); show("strict", strict); } catch (Throwable _t) { System.out.println("strict -> " + _t); }
        }
        {
            int[] ports = {80, 443, 8080};
            boolean viaCommons = ArrayUtils.contains(ports, 8080);   // true
            show("viaCommons", viaCommons);
            boolean nullArray = ArrayUtils.contains((int[]) null, 80);   // false
            show("nullArray", nullArray);
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

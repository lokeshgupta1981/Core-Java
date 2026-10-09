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
 * Examples for the tutorial "Create a Subarray in Java: copyOfRange(), arraycopy, Streams".
 * https://howtodoinjava.com/java/array/copy-array-range/
 */
public class ArrayCopyRange {
    static <T> T[] page(T[] results, int pageNumber, int pageSize) {
        int from = Math.min(pageNumber * pageSize, results.length);
        int to = Math.min(from + pageSize, results.length);
        return Arrays.copyOfRange(results, from, to);
    }
    public static void main(String[] args) throws Exception {
        {
            String[] steps = {"wash", "chop", "boil", "fry", "serve"};
            String[] middle = Arrays.copyOfRange(steps, 1, 4);                    // [chop, boil, fry]
            show("middle", middle);
            String[] firstTwo = Arrays.copyOf(steps, 2);                          // [wash, chop]
            show("firstTwo", firstTwo);
            String[] lastTwo = Arrays.stream(steps, 3, 5).toArray(String[]::new); // [fry, serve]
            show("lastTwo", lastTwo);
            String[] target = new String[2];
            System.arraycopy(steps, 2, target, 0, 2);
            String copied = Arrays.toString(target);                              // "[boil, fry]"
            show("copied", copied);
        }
        {
            int start = 1;
            int endInclusive = 3;
            String[] picked = Arrays.copyOfRange(new String[]{"a", "b", "c", "d", "e"}, start, endInclusive + 1);   // [b, c, d]
            show("picked", picked);
            int size = picked.length;   // 3
            show("size", size);
        }
        {
            int[] scores = {90, 75, 60, 85};
            int[] tail = Arrays.copyOfRange(scores, 2, 4);     // [60, 85]
            show("tail", tail);
            int[] padded = Arrays.copyOfRange(scores, 2, 6);   // [60, 85, 0, 0]
            show("padded", padded);
            int[] empty = Arrays.copyOfRange(scores, 4, 4);    // []
            show("empty", empty);
            try { int[] reversed = Arrays.copyOfRange(scores, 3, 1); show("reversed", reversed); } catch (Throwable _t) { System.out.println("reversed -> " + _t); }
            try { int[] outside = Arrays.copyOfRange(scores, 5, 6); show("outside", outside); } catch (Throwable _t) { System.out.println("outside -> " + _t); }
        }
        {
            int[] ranks = {4, 8, 15, 16, 23};
            int[] topThree = Arrays.copyOf(ranks, 3);     // [4, 8, 15]
            show("topThree", topThree);
            int[] longer = Arrays.copyOf(ranks, 7);       // [4, 8, 15, 16, 23, 0, 0]
            show("longer", longer);
        }
        {
            int[] source = {1, 2, 3, 4, 5, 6};
            int[] buffer = new int[5];
            System.arraycopy(source, 2, buffer, 1, 3);        // srcPos 2, destPos 1, length 3
            String filled = Arrays.toString(buffer);          // "[0, 3, 4, 5, 0]"
            show("filled", filled);
        }
        {
            int[] marks = {40, 82, 55, 91, 67, 73};
            int[] passed = Arrays.stream(marks, 1, 5).filter(m -> m >= 60).toArray();             // [82, 91, 67]
            show("passed", passed);
            int[] byIndex = IntStream.range(2, 4).map(i -> marks[i]).toArray();                    // [55, 91]
            show("byIndex", byIndex);
            double avg = Arrays.stream(marks, 0, 3).average().orElse(0);                           // 59.0
            show("avg", avg);
        }
        {
            String[] team = {"Lokesh", "Alex", "Maria", "Sam"};
            String[] pair = ArrayUtils.subarray(team, 1, 3);       // [Alex, Maria]
            show("pair", pair);
            String[] clamped = ArrayUtils.subarray(team, -2, 99);  // [Lokesh, Alex, Maria, Sam]
            show("clamped", clamped);
            String[] none = ArrayUtils.subarray(team, 3, 1);       // []
            show("none", none);
            String[] noTeam = null;
            String[] nothing = ArrayUtils.subarray(noTeam, 0, 2);  // null
            show("nothing", nothing);
        }
        {
            int[] queue = {5, 3, 9, 1, 7};
            Arrays.sort(queue, 1, 4);
            String partSorted = Arrays.toString(queue);       // "[5, 1, 3, 9, 7]"
            show("partSorted", partSorted);
            String[] tags = {"java", "array", "copy"};
            List<String> view = Arrays.asList(tags).subList(1, 3);   // [array, copy]
            show("view", view);
            view.set(1, "slice");
            String original = tags[2];                         // "slice"
            show("original", original);
        }
        {
            String[] hits = {"r1", "r2", "r3", "r4", "r5", "r6", "r7"};
            String[] page0 = page(hits, 0, 3);   // [r1, r2, r3]
            show("page0", page0);
            String[] page2 = page(hits, 2, 3);   // [r7]
            show("page2", page2);
            String[] page5 = page(hits, 5, 3);   // []
            show("page5", page5);
        }
        {
            int[] temps = {18, 21, 19, 24, 22};
            int n = 3;
            int[] lastN = Arrays.copyOfRange(temps, Math.max(0, temps.length - n), temps.length);   // [19, 24, 22]
            show("lastN", lastN);
        }
        {
            String[] cities = {"Pune", "Delhi", "Goa", "Agra"};
            List<String> south = List.of(Arrays.copyOfRange(cities, 2, 4));            // [Goa, Agra]
            show("south", south);
            List<Integer> nums = Arrays.stream(new int[]{1, 2, 3, 4}, 1, 3).boxed().toList();   // [2, 3]
            show("nums", nums);
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

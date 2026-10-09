package com.howtodoinjava.java25.array;

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
 * Examples for the tutorial "Split an Array in Java: At an Index, in Halves or in Chunks".
 * https://howtodoinjava.com/java/array/split-arrays/
 */
public class SplitArray {
    static int[][] splitAt(int[] array, int index) {
        Objects.checkIndex(index, array.length + 1);   // allows 0..length
        int[] left = Arrays.copyOfRange(array, 0, index);
        int[] right = Arrays.copyOfRange(array, index, array.length);
        return new int[][]{left, right};
    }
    static int[][] chunk(int[] array, int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("size must be positive: " + size);
        }
        int count = (array.length + size - 1) / size;   // rounds up
        int[][] chunks = new int[count][];
        for (int i = 0; i < count; i++) {
            int start = i * size;
            chunks[i] = Arrays.copyOfRange(array, start, Math.min(start + size, array.length));
        }
        return chunks;
    }
    static <T> List<T[]> chunk(T[] array, int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("size must be positive: " + size);
        }
        List<T[]> chunks = new ArrayList<>();
        for (int start = 0; start < array.length; start += size) {
            chunks.add(Arrays.copyOfRange(array, start, Math.min(start + size, array.length)));
        }
        return chunks;
    }
    static int[][] splitInto(int[] array, int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n must be positive: " + n);
        }
        int[][] parts = new int[n][];
        for (int i = 0; i < n; i++) {
            int start = (int) ((long) i * array.length / n);
            int end = (int) ((long) (i + 1) * array.length / n);
            parts[i] = Arrays.copyOfRange(array, start, end);
        }
        return parts;
    }
    public static void main(String[] args) throws Exception {
        {
            int[] readings = {12, 15, 11, 18, 20, 17, 14};
            int[] firstPart = Arrays.copyOfRange(readings, 0, 3);                 // [12, 15, 11]
            show("firstPart", firstPart);
            int[] secondPart = Arrays.copyOfRange(readings, 3, readings.length);  // [18, 20, 17, 14]
            show("secondPart", secondPart);
        }
        {
            int[] data = {12, 15, 11, 18};
            int[][] parts = splitAt(data, 1);     // [[12], [15, 11, 18]]
            show("parts", parts);
            int[][] edge = splitAt(data, 4);      // [[12, 15, 11, 18], []]
            show("edge", edge);
            try { int[][] bad = splitAt(data, 5); show("bad", bad); } catch (Throwable _t) { System.out.println("bad -> " + _t); }
        }
        {
            int[] odd = {1, 2, 3, 4, 5};
            int mid = odd.length / 2;                              // 2, rounds down
            show("mid", mid);
            int[] smallFirst = Arrays.copyOfRange(odd, 0, mid);    // [1, 2]
            show("smallFirst", smallFirst);
            int midUp = (odd.length + 1) / 2;                      // 3, rounds up
            show("midUp", midUp);
            int[] bigFirst = Arrays.copyOfRange(odd, 0, midUp);    // [1, 2, 3]
            show("bigFirst", bigFirst);
            int[] rest = Arrays.copyOfRange(odd, midUp, odd.length);   // [4, 5]
            show("rest", rest);
        }
        {
            int[] ids = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
            int[][] chunks = chunk(ids, 4);         // [[1, 2, 3, 4], [5, 6, 7, 8], [9, 10]]
            show("chunks", chunks);
            int[][] whole = chunk(ids, 50);         // [[1, 2, 3, 4, 5, 6, 7, 8, 9, 10]]
            show("whole", whole);
            int[][] none = chunk(new int[0], 3);    // []
            show("none", none);
        }
        {
            String[] guests = {"Ann", "Bob", "Cid", "Dev", "Eva"};
            List<String[]> tables = chunk(guests, 2);
            int tableCount = tables.size();                     // 3
            show("tableCount", tableCount);
            String lastTable = Arrays.toString(tables.get(2));  // "[Eva]"
            show("lastTable", lastTable);
        }
        {
            String[] letters = {"a", "b", "c", "d", "e"};
            List<List<String>> windows = Arrays.stream(letters).gather(Gatherers.windowFixed(2)).toList();   // [[a, b], [c, d], [e]]
            show("windows", windows);
            int[] nums = {1, 2, 3, 4};
            List<List<Integer>> pairs = Arrays.stream(nums).boxed().gather(Gatherers.windowFixed(3)).toList();   // [[1, 2, 3], [4]]
            show("pairs", pairs);
        }
        {
            int[] tasks = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
            int[][] three = splitInto(tasks, 3);            // [[1, 2, 3], [4, 5, 6], [7, 8, 9, 10]]
            show("three", three);
            int[][] many = splitInto(new int[]{1, 2}, 3);   // [[], [1], [2]]
            show("many", many);
        }
        {
            int[] temps = {12, 15, 11, 18, 20};
            int lateSum = Arrays.stream(temps, 3, 5).sum();     // 38
            show("lateSum", lateSum);
            String[] week = {"mon", "tue", "wed", "thu"};
            List<String> midweek = Arrays.asList(week).subList(1, 3);   // [tue, wed]
            show("midweek", midweek);
            midweek.set(0, "TUE");
            String changed = week[1];                            // "TUE", the view writes through
            show("changed", changed);
        }
        {
            int[] userIds = {101, 102, 103, 104, 105, 106, 107, 108, 109, 110};
            List<String> requests = Arrays.stream(chunk(userIds, 4))
                    .map(batch -> "ids=" + Arrays.stream(batch).mapToObj(String::valueOf).collect(Collectors.joining(",")))
                    .toList();
            int requestCount = requests.size();     // 3
            show("requestCount", requestCount);
            String lastRequest = requests.get(2);   // "ids=109,110"
            show("lastRequest", lastRequest);
        }
        {
            int[] values = {3, 8, 5, 6, 2};
            Map<Boolean, List<Integer>> byParity = Arrays.stream(values).boxed().collect(Collectors.partitioningBy(v -> v % 2 == 0));
            List<Integer> evens = byParity.get(true);    // [8, 6, 2]
            show("evens", evens);
            List<Integer> odds = byParity.get(false);    // [3, 5]
            show("odds", odds);
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

package com.howtodoinjava.algorithms;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.time.*;
import java.nio.file.*;
import java.math.*;
import java.io.*;


/**
 * Examples for the tutorial "Jump Search in Java".
 * https://howtodoinjava.com/algorithm/jump-search-algorithm/
 */
public class JumpSearchAlgorithm {
    static int jumpSearch(int[] a, int target) {
        if (a == null || a.length == 0) {
            return -1;
        }
        int n = a.length;
        int step = (int) Math.sqrt(n);
        int prev = 0;
        int end = step;
        while (a[Math.min(end, n) - 1] < target) {      // 1. jump block by block
            prev = end;
            end += step;
            if (prev >= n) {
                return -1;                  // past the last block
            }
        }
        for (int i = prev; i < Math.min(end, n); i++) { // 2. scan the block
            if (a[i] == target) {
                return i;
            }
        }
        return -1;
    }
    static <T extends Comparable<? super T>> int jumpSearch(T[] a, T target) {
        int n = a.length;
        if (n == 0 || target == null) {
            return -1;
        }
        int step = (int) Math.sqrt(n);
        int prev = 0;
        int end = step;
        while (a[Math.min(end, n) - 1].compareTo(target) < 0) {
            prev = end;
            end += step;
            if (prev >= n) {
                return -1;
            }
        }
        for (int i = prev; i < Math.min(end, n); i++) {
            if (a[i].compareTo(target) == 0) {
                return i;
            }
        }
        return -1;
    }
    public static void main(String[] args) throws Exception {
        {
            int[] prices = {2, 4, 7, 9, 12, 15, 18, 21, 25, 28, 31, 34, 38, 41, 45, 49};
            int found = jumpSearch(prices, 31);     // 10
            show("found", found);
            int missing = jumpSearch(prices, 30);   // -1
            show("missing", missing);
        }
        {
            int[] sorted = {3, 6, 9, 12};
            int first = jumpSearch(sorted, 3);      // 0
            show("first", first);
            int last = jumpSearch(sorted, 12);      // 3
            show("last", last);
            int tooBig = jumpSearch(sorted, 99);    // -1
            show("tooBig", tooBig);
            int empty = jumpSearch(new int[0], 3);  // -1
            show("empty", empty);
        }
        {
            LocalDate[] releases = {
                LocalDate.of(2024, 3, 19), LocalDate.of(2024, 9, 17), LocalDate.of(2025, 3, 18), LocalDate.of(2025, 9, 16)
            };
            int index = jumpSearch(releases, LocalDate.of(2025, 3, 18));   // 2
            show("index", index);
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

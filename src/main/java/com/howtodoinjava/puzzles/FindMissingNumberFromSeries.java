package com.howtodoinjava.puzzles;

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
 * Examples for the tutorial "Find the Missing Number in an Array in Java (Sum, XOR, BitSet)".
 * https://howtodoinjava.com/java/puzzles/find-missing-number-from-series/
 */
public class FindMissingNumberFromSeries {
    static int findMissing(int[] a) {
        Objects.requireNonNull(a, "array");
        long n = a.length + 1L;
        long expected = n * (n + 1) / 2;
        long actual = 0;
        for (int value : a) {
            actual += value;
        }
        return (int) (expected - actual);
    }
    static int findMissingXor(int[] a) {
        int result = 0;
        for (int i = 0; i < a.length; i++) {
            result ^= (i + 1) ^ a[i];
        }
        return result ^ (a.length + 1);
    }
    static int findMissingSorted(int[] a) {
        int low = 0;
        int high = a.length - 1;
        while (low <= high) {
            int mid = (low + high) >>> 1;
            if (a[mid] == mid + 1) {
                low = mid + 1;              // gap is to the right
            } else {
                high = mid - 1;             // gap is here or to the left
            }
        }
        return low + 1;
    }
    static List<Integer> findAllMissing(int[] a, int low, int high) {
        BitSet seen = new BitSet(high - low + 1);
        for (int value : a) {
            if (value < low || value > high) {
                throw new IllegalArgumentException("Out of range: " + value);
            }
            seen.set(value - low);
        }
        List<Integer> missing = new ArrayList<>();
        for (int i = seen.nextClearBit(0); i <= high - low; i = seen.nextClearBit(i + 1)) {
            missing.add(i + low);
        }
        return missing;
    }
    static long findMissingInRange(int[] a, long low, long high) {
        long count = high - low + 1;
        long expected = (low + high) * count / 2;
        long actual = 0;
        for (int value : a) {
            actual += value;
        }
        return expected - actual;
    }
    public static void main(String[] args) throws Exception {
        {
            int[] tickets = {1, 2, 4, 5, 6};
            int missing = findMissing(tickets);                     // 3
            show("missing", missing);
            int lastGone = findMissing(new int[] {1, 2, 3});        // 4
            show("lastGone", lastGone);
            int unsorted = findMissing(new int[] {5, 1, 2, 4});     // 3
            show("unsorted", unsorted);
        }
        {
            int fromEmpty = findMissing(new int[0]);   // 1
            show("fromEmpty", fromEmpty);
            try { int nullInput = findMissing(null); show("nullInput", nullInput); } catch (Throwable _t) { System.out.println("nullInput -> " + _t); }
        }
        {
            int bigN = 46_341;
            int wrongSum = bigN * (bigN + 1) / 2;          // -1073716337
            show("wrongSum", wrongSum);
            long rightSum = bigN * (bigN + 1L) / 2;        // 1073767311
            show("rightSum", rightSum);
        }
        {
            int xorMissing = findMissingXor(new int[] {1, 2, 4, 5, 6});   // 3
            show("xorMissing", xorMissing);
            int xorUnsorted = findMissingXor(new int[] {5, 1, 4, 3});     // 2
            show("xorUnsorted", xorUnsorted);
            int cancel = 7 ^ 7;                                           // 0
            show("cancel", cancel);
        }
        {
            int sortedGap = findMissingSorted(new int[] {1, 2, 3, 5, 6, 7, 8});   // 4
            show("sortedGap", sortedGap);
            int sortedLast = findMissingSorted(new int[] {1, 2, 3});              // 4
            show("sortedLast", sortedLast);
            int sortedFirst = findMissingSorted(new int[] {2, 3, 4});             // 1
            show("sortedFirst", sortedFirst);
        }
        {
            List<Integer> gaps = findAllMissing(new int[] {1, 5, 2, 8, 9}, 1, 10);   // [3, 4, 6, 7, 10]
            show("gaps", gaps);
            List<Integer> noGaps = findAllMissing(new int[] {3, 1, 2}, 1, 3);         // []
            show("noGaps", noGaps);
            try { List<Integer> bad = findAllMissing(new int[] {1, 42}, 1, 10); show("bad", bad); } catch (Throwable _t) { System.out.println("bad -> " + _t); }
        }
        {
            int[] invoices = {1001, 1002, 1004, 1005};
            long gapInvoice = findMissingInRange(invoices, 1001, 1005);   // 1003
            show("gapInvoice", gapInvoice);
        }
        {
            int fromDuplicate = findMissing(new int[] {1, 1, 3});   // 5, but 2 and 4 are missing
            show("fromDuplicate", fromDuplicate);
        }
        {
            int[] nums = {3, 1, 4};
            long n = nums.length + 1L;
            long streamMissing = n * (n + 1) / 2 - Arrays.stream(nums).asLongStream().sum();   // 2
            show("streamMissing", streamMissing);
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

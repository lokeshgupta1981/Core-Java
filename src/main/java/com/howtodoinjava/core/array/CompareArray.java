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
 * Examples for the tutorial "Check If Two Arrays Are Equal in Java (equals, deepEquals)".
 * https://howtodoinjava.com/java/array/checking-arrays-are-equal/
 */
public class CompareArray {
    static class Point {
        final int x;
        final int y;
        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
    static boolean sameElements(int[] a, int[] b) {
        if (a == null || b == null || a.length != b.length) {
            return a == b;
        }
        int[] x = a.clone();
        int[] y = b.clone();
        Arrays.sort(x);
        Arrays.sort(y);
        return Arrays.equals(x, y);
    }
    static record Sample(String name, int[] values) {}
    static record Reading(String sensor, int[] values) {
        @Override
        public boolean equals(Object o) {
            return o instanceof Reading r && sensor.equals(r.sensor) && Arrays.equals(values, r.values);
        }
        @Override
        public int hashCode() {
            return 31 * sensor.hashCode() + Arrays.hashCode(values);
        }
    }
    public static void main(String[] args) throws Exception {
        {
            int[] first = {1, 2, 3};
            int[] second = {1, 2, 3};
            boolean sameObject = first == second;                  // false
            show("sameObject", sameObject);
            boolean viaEquals = first.equals(second);              // false, same as ==
            show("viaEquals", viaEquals);
            boolean sameContent = Arrays.equals(first, second);    // true
            show("sameContent", sameContent);
            int[][] gridA = {{1, 2}, {3}};
            int[][] gridB = {{1, 2}, {3}};
            boolean nested = Arrays.deepEquals(gridA, gridB);      // true
            show("nested", nested);
        }
        {
            int[] cached = {4, 5};
            int[] alias = cached;
            int[] copy = cached.clone();
            boolean sameRef = cached == alias;              // true
            show("sameRef", sameRef);
            boolean copyRef = cached == copy;               // false
            show("copyRef", copyRef);
            boolean copyContent = Arrays.equals(cached, copy);   // true
            show("copyContent", copyContent);
        }
        {
            boolean order = Arrays.equals(new int[] {1, 2}, new int[] {2, 1});        // false
            show("order", order);
            boolean length = Arrays.equals(new int[] {1, 2}, new int[] {1, 2, 0});    // false
            show("length", length);
            boolean bothNull = Arrays.equals((int[]) null, (int[]) null);             // true
            show("bothNull", bothNull);
            boolean oneNull = Arrays.equals(new int[0], null);                        // false
            show("oneNull", oneNull);
            boolean nullItems = Arrays.equals(new String[] {"a", null}, new String[] {"a", null});   // true
            show("nullItems", nullItems);
            boolean nan = Arrays.equals(new double[] {Double.NaN}, new double[] {Double.NaN});       // true
            show("nan", nan);
            boolean zeros = Arrays.equals(new double[] {0.0}, new double[] {-0.0});                  // false
            show("zeros", zeros);
        }
        {
            byte[] pngSignature = {(byte) 0x89, 'P', 'N', 'G'};
            byte[] upload = {(byte) 0x89, 'P', 'N', 'G', 13, 10, 26, 10};
            boolean isPng = Arrays.equals(upload, 0, 4, pngSignature, 0, 4);   // true
            show("isPng", isPng);
        }
        {
            byte[] expectedMac = {7, 1, 9};
            byte[] receivedMac = {7, 1, 9};
            boolean valid = MessageDigest.isEqual(expectedMac, receivedMac);   // true
            show("valid", valid);
        }
        {
            int[][] seatsA = {{1, 0}, {0, 1}};
            int[][] seatsB = {{1, 0}, {0, 1}};
            boolean shallow = Arrays.equals(seatsA, seatsB);        // false, rows compared by reference
            show("shallow", shallow);
            boolean deep = Arrays.deepEquals(seatsA, seatsB);       // true
            show("deep", deep);
            Object[] mixedA = {"id", new int[] {7}};
            Object[] mixedB = {"id", new int[] {7}};
            boolean mixed = Arrays.deepEquals(mixedA, mixedB);      // true
            show("mixed", mixed);
        }
        {
            String[] tagsA = {"Java", "SQL"};
            String[] tagsB = {"java", "sql"};
            boolean exact = Arrays.equals(tagsA, tagsB);                                    // false
            show("exact", exact);
            boolean ignoreCase = Arrays.equals(tagsA, tagsB, String.CASE_INSENSITIVE_ORDER);   // true
            show("ignoreCase", ignoreCase);
        }
        {
            Point[] routeA = {new Point(0, 0), new Point(2, 3)};
            Point[] routeB = {new Point(0, 0), new Point(2, 3)};
            boolean byEquals = Arrays.equals(routeA, routeB);      // false, no equals() override
            show("byEquals", byEquals);
            Comparator<Point> byXY = Comparator.<Point>comparingInt(p -> p.x).thenComparingInt(p -> p.y);
            boolean byFields = Arrays.equals(routeA, routeB, byXY);   // true
            show("byFields", byFields);
        }
        {
            int[] expected = {10, 20, 30, 40};
            int[] actual = {10, 20, 35, 40};
            int firstDiff = Arrays.mismatch(expected, actual);                     // 2
            show("firstDiff", firstDiff);
            int same = Arrays.mismatch(expected, expected.clone());                // -1
            show("same", same);
            int prefix = Arrays.mismatch(new int[] {1, 2}, new int[] {1, 2, 3});   // 2
            show("prefix", prefix);
        }
        {
            int[] v1 = {1, 10, 0};
            int[] v2 = {1, 9, 3};
            int versions = Arrays.compare(v1, v2);                          // 1, v1 is greater
            show("versions", versions);
            int shorter = Arrays.compare(new int[] {1, 2}, new int[] {1, 2, 0});   // -1
            show("shorter", shorter);
            int equal = Arrays.compare(new int[] {5}, new int[] {5});              // 0
            show("equal", equal);
        }
        {
            boolean anyOrder = sameElements(new int[] {3, 1, 2}, new int[] {1, 2, 3});   // true
            show("anyOrder", anyOrder);
            boolean counts = sameElements(new int[] {1, 1, 2}, new int[] {1, 2, 2});     // false
            show("counts", counts);
        }
        {
            Sample s1 = new Sample("temp", new int[] {20, 21});
            Sample s2 = new Sample("temp", new int[] {20, 21});
            boolean recordsEqual = s1.equals(s2);          // false
            show("recordsEqual", recordsEqual);
        }
        {
            boolean fixed = new Reading("t1", new int[] {5}).equals(new Reading("t1", new int[] {5}));   // true
            show("fixed", fixed);
            Set<Reading> unique = new HashSet<>(List.of(new Reading("t1", new int[] {5})));
            boolean found = unique.contains(new Reading("t1", new int[] {5}));                         // true
            show("found", found);
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

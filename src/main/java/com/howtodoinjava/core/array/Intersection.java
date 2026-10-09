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
 * Examples for the tutorial "Intersection of Two Arrays in Java, With and Without Duplicates".
 * https://howtodoinjava.com/java/array/intersection-between-arrays/
 */
public class Intersection {
    static int[] intersect(int[] a, int[] b) {
        Set<Integer> lookup = new HashSet<>();
        for (int v : b) {
            lookup.add(v);
        }
        return Arrays.stream(a)
                .filter(lookup::contains)
                .distinct()
                .toArray();
    }
    static int[] intersectWithRepeats(int[] a, int[] b) {
        Map<Integer, Integer> counts = new HashMap<>();
        for (int v : b) {
            counts.merge(v, 1, Integer::sum);
        }
        int[] out = new int[Math.min(a.length, b.length)];
        int k = 0;
        for (int v : a) {
            Integer left = counts.get(v);
            if (left != null && left > 0) {
                counts.put(v, left - 1);
                out[k++] = v;
            }
        }
        return Arrays.copyOf(out, k);
    }
    static int[] intersectSorted(int[] a, int[] b) {
        int[] out = new int[Math.min(a.length, b.length)];
        int i = 0, j = 0, k = 0;
        while (i < a.length && j < b.length) {
            if (a[i] < b[j]) {
                i++;
            } else if (a[i] > b[j]) {
                j++;
            } else {
                if (k == 0 || out[k - 1] != a[i]) {
                    out[k++] = a[i];          // skip repeats
                }
                i++;
                j++;
            }
        }
        return Arrays.copyOf(out, k);
    }
    public static void main(String[] args) throws Exception {
        {
            int[] a = {4, 1, 7, 4, 9};
            int[] b = {9, 4, 2, 4};
            Set<Integer> inB = Arrays.stream(b).boxed().collect(Collectors.toSet());
            int[] common = Arrays.stream(a).filter(inB::contains).distinct().toArray();   // [4, 9]
            show("common", common);
        }
        {
            String[] aliceFollows = {"raj", "li", "tom", "ana"};
            String[] bobFollows = {"ana", "sam", "li"};
            Set<String> mutual = new LinkedHashSet<>(Arrays.asList(aliceFollows));
            boolean changed = mutual.retainAll(new HashSet<>(Arrays.asList(bobFollows)));   // true
            show("changed", changed);
            String[] both = mutual.toArray(String[]::new);                                  // [li, ana]
            show("both", both);
        }
        {
            int[] scoresA = {70, 85, 90, 85};
            int[] scoresB = {85, 60, 70};
            int[] shared = intersect(scoresA, scoresB);              // [70, 85]
            show("shared", shared);
            int[] nothing = intersect(scoresA, new int[]{1, 2});     // []
            show("nothing", nothing);
            int[] sortedShared = IntStream.of(intersect(new int[]{9, 3, 5}, new int[]{5, 9})).sorted().toArray();   // [5, 9]
            show("sortedShared", sortedShared);
        }
        {
            int[] repeats = intersectWithRepeats(new int[]{4, 1, 7, 4, 9}, new int[]{9, 4, 2, 4});   // [4, 4, 9]
            show("repeats", repeats);
            int[] oneMatch = intersectWithRepeats(new int[]{2, 2, 2}, new int[]{2});              // [2]
            show("oneMatch", oneMatch);
        }
        {
            int[] sortedCommon = intersectSorted(new int[]{1, 4, 4, 7, 9}, new int[]{2, 4, 4, 9});   // [4, 9]
            show("sortedCommon", sortedCommon);
            int[] noOverlap = intersectSorted(new int[]{1, 2, 3}, new int[]{4, 5, 6});           // []
            show("noOverlap", noOverlap);
        }
        {
            Integer[] booked = {12, 13, 14};
            Integer[] requested = {11, 12};
            boolean clash = !Collections.disjoint(Arrays.asList(booked), Arrays.asList(requested));   // true
            show("clash", clash);
            Set<Integer> bookedSet = Set.of(12, 13, 14);
            boolean free = Arrays.stream(new int[]{20, 21}).noneMatch(bookedSet::contains);           // true
            show("free", free);
        }
        {
            String[] mon = {"ana", "li", "raj"};
            String[] tue = {"li", "raj", "tom"};
            String[] wed = {"raj", "li"};
            Set<String> everyDay = new LinkedHashSet<>(Arrays.asList(mon));
            boolean c1 = everyDay.retainAll(new HashSet<>(Arrays.asList(tue)));   // true
            show("c1", c1);
            boolean c2 = everyDay.retainAll(new HashSet<>(Arrays.asList(wed)));   // false, nothing removed
            show("c2", c2);
            String days = everyDay.toString();                                    // "[li, raj]"
            show("days", days);
        }
        {
            String[] monday = {"ana", "li", "raj"};
            Set<String> notTue = new LinkedHashSet<>(Arrays.asList(monday));
            boolean r = notTue.removeAll(Set.of("li", "raj", "tom"));   // true
            show("r", r);
            String diff = notTue.toString();                            // "[ana]"
            show("diff", diff);
        }
        {
            String[] team = {"ana", "li", "raj"};
            Set<String> guests = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
            Collections.addAll(guests, "Li", "TOM");
            String[] caseless = Arrays.stream(team).filter(guests::contains).toArray(String[]::new);   // [li]
            show("caseless", caseless);
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

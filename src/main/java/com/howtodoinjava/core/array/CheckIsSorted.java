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
import com.google.common.collect.Comparators;
import java.util.regex.*;
import java.nio.charset.*;
import java.nio.*;
import java.security.*;
import java.lang.reflect.*;
import java.lang.invoke.*;

/**
 * Examples for the tutorial "Check if an Array Is Sorted in Java (Ascending, Descending)".
 * https://howtodoinjava.com/java/array/check-sorted-array/
 */
public class CheckIsSorted {
    static boolean isSorted(int[] a) {
        if (a == null) {
            return true;
        }
        for (int i = 0; i < a.length - 1; i++) {
            if (a[i] > a[i + 1]) {
                return false;               // first out-of-order pair
            }
        }
        return true;
    }
    static <T> boolean isSorted(T[] a, Comparator<? super T> order) {
        if (a == null) {
            return true;
        }
        for (int i = 0; i < a.length - 1; i++) {
            if (order.compare(a[i], a[i + 1]) > 0) {
                return false;
            }
        }
        return true;
    }
    static <T extends Comparable<? super T>> boolean isSorted(T[] a) {
        return isSorted(a, Comparator.naturalOrder());
    }
    static record Release(String version, LocalDate date) {}
    static int[] sortedCopy(int[] a) {
        int[] copy = a.clone();
        Arrays.sort(copy);
        return copy;
    }
    static boolean isSortedRecursive(int[] a, int n) {
        if (n <= 1) {
            return true;
        }
        return a[n - 2] <= a[n - 1] && isSortedRecursive(a, n - 1);
    }
    public static void main(String[] args) throws Exception {
        {
            int[] builds = {3, 7, 7, 12};
            boolean ok = isSorted(builds);              // true
            show("ok", ok);
            int[] shuffled = {3, 12, 7};
            boolean notOk = isSorted(shuffled);         // false
            show("notOk", notOk);
        }
        {
            boolean empty = isSorted(new int[0]);       // true
            show("empty", empty);
            boolean one = isSorted(new int[]{42});      // true
            show("one", one);
            boolean tail = isSorted(new int[]{1, 2, 3, 5, 4});   // false
            show("tail", tail);
        }
        {
            int[] builds = {3, 7, 7, 12};
            boolean ascending = IntStream.range(0, builds.length - 1).noneMatch(i -> builds[i] > builds[i + 1]);    // true
            show("ascending", ascending);
            boolean descending = IntStream.range(0, builds.length - 1).noneMatch(i -> builds[i] < builds[i + 1]);   // false
            show("descending", descending);
            boolean strictly = IntStream.range(0, builds.length - 1).noneMatch(i -> builds[i] >= builds[i + 1]);    // false
            show("strictly", strictly);
        }
        {
            Release[] releases = {new Release("2.1", LocalDate.of(2026, 9, 1)), new Release("2.0", LocalDate.of(2026, 6, 15)), new Release("1.9", LocalDate.of(2026, 3, 2))};
            boolean newestFirst = isSorted(releases, Comparator.comparing(Release::date).reversed());   // true
            show("newestFirst", newestFirst);
            boolean oldestFirst = isSorted(releases, Comparator.comparing(Release::date));              // false
            show("oldestFirst", oldestFirst);
            Integer[] boxed = {3, 7, 7, 12};
            boolean natural = isSorted(boxed);                                                          // true
            show("natural", natural);
        }
        {
            Integer[] withNull = {null, 3, 7};
            boolean nullsFirst = isSorted(withNull, Comparator.nullsFirst(Comparator.naturalOrder()));   // true
            show("nullsFirst", nullsFirst);
            try { boolean crash = isSorted(withNull); show("crash", crash); } catch (Throwable _t) { System.out.println("crash -> " + _t); }
        }
        {
            String[] names = {"apple", "Banana", "cherry"};
            boolean byCode = isSorted(names);                                    // false, "B" < "a"
            show("byCode", byCode);
            boolean ignoreCase = isSorted(names, String.CASE_INSENSITIVE_ORDER); // true
            show("ignoreCase", ignoreCase);
        }
        {
            int[] shuffled = {40, 10, 30, 20};
            int lost = Arrays.binarySearch(shuffled, 40);   // -5, although 40 is at index 0
            show("lost", lost);
        }
        {
            int[] timestamps = {1700, 1650, 1800};
            int[] searchable = isSorted(timestamps) ? timestamps : sortedCopy(timestamps);
            int at = Arrays.binarySearch(searchable, 1700);   // 1
            show("at", at);
        }
        {
            int[] builds = {3, 7, 7, 12};
            boolean commons = ArrayUtils.isSorted(builds);                                   // true
            show("commons", commons);
            String[] names = {"apple", "Banana", "cherry"};
            boolean commonsCase = ArrayUtils.isSorted(names, String.CASE_INSENSITIVE_ORDER); // true
            show("commonsCase", commonsCase);
            boolean commonsNull = ArrayUtils.isSorted((int[]) null);                         // true
            show("commonsNull", commonsNull);
        }
        {
            List<Integer> buildList = List.of(3, 7, 7, 12);
            boolean inOrder = Comparators.isInOrder(buildList, Comparator.naturalOrder());           // true
            show("inOrder", inOrder);
            boolean strict = Comparators.isInStrictOrder(buildList, Comparator.naturalOrder());      // false
            show("strict", strict);
            String[] names = {"apple", "Banana", "cherry"};
            boolean guavaArray = Comparators.isInOrder(Arrays.asList(names), String.CASE_INSENSITIVE_ORDER);   // true
            show("guavaArray", guavaArray);
        }
        {
            int[] builds = {3, 7, 7, 12};
            boolean rec = isSortedRecursive(builds, builds.length);   // true
            show("rec", rec);
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

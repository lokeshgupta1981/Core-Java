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
 * Examples for the tutorial "Reverse an Array in Java: In Place, Copy and List.reversed()".
 * https://howtodoinjava.com/java/array/reverse-an-array/
 */
public class ReverseArray {
    static void reverse(int[] a) {
        if (a == null) {
            return;
        }
        for (int left = 0, right = a.length - 1; left < right; left++, right--) {
            int tmp = a[left];
            a[left] = a[right];
            a[right] = tmp;
        }
    }
    static <T> void reverse(T[] a) {
        if (a == null) {
            return;
        }
        for (int left = 0, right = a.length - 1; left < right; left++, right--) {
            T tmp = a[left];
            a[left] = a[right];
            a[right] = tmp;
        }
    }
    public static void main(String[] args) throws Exception {
        {
            int[] playlist = {11, 22, 33, 44, 55};
            reverse(playlist);
            int[] reversed = playlist;                // [55, 44, 33, 22, 11]
            show("reversed", reversed);
        }
        {
            int[] even = {1, 2, 3, 4, 5, 6};
            reverse(even);
            int[] evenResult = even;                  // [6, 5, 4, 3, 2, 1]
            show("evenResult", evenResult);
            int[] single = {7};
            reverse(single);
            int[] singleResult = single;              // [7]
            show("singleResult", singleResult);
        }
        {
            String[] steps = {"wash", "cut", "cook"};
            reverse(steps);
            String[] undoOrder = steps;               // [cook, cut, wash]
            show("undoOrder", undoOrder);
        }
        {
            int[] scores = {40, 65, 80, 95};
            int[] topFirst = new int[scores.length];
            for (int i = 0; i < scores.length; i++) {
                topFirst[i] = scores[scores.length - 1 - i];
            }
            int[] display = topFirst;                 // [95, 80, 65, 40]
            show("display", display);
            int[] original = scores;                  // [40, 65, 80, 95]
            show("original", original);
        }
        {
            int[] scores = {40, 65, 80, 95};
            int[] desc = IntStream.range(0, scores.length).map(i -> scores[scores.length - 1 - i]).toArray();   // [95, 80, 65, 40]
            show("desc", desc);
            String[] names = {"ana", "ben", "cy"};
            String[] back = IntStream.range(0, names.length).mapToObj(i -> names[names.length - 1 - i]).toArray(String[]::new);   // [cy, ben, ana]
            show("back", back);
        }
        {
            String[] tags = {"java", "spring", "sql"};
            Collections.reverse(Arrays.asList(tags));
            String[] result = tags;                   // [sql, spring, java]
            show("result", result);
        }
        {
            int[] nums = {1, 2, 3};
            List<int[]> wrapped = Arrays.asList(nums);
            int size = wrapped.size();                 // 1
            show("size", size);
            Collections.reverse(wrapped);
            int[] unchanged = nums;                    // [1, 2, 3]
            show("unchanged", unchanged);
        }
        {
            Integer[] orders = {101, 102, 103};
            List<Integer> newestFirst = Arrays.asList(orders).reversed();   // [103, 102, 101]
            show("newestFirst", newestFirst);
            Integer first = newestFirst.get(0);                              // 103
            show("first", first);
            newestFirst.set(0, 999);
            Integer[] backing = orders;                                      // [101, 102, 999]
            show("backing", backing);
        }
        {
            int[] codes = {1, 2, 3, 4, 5};
            ArrayUtils.reverse(codes);
            int[] all = codes;                         // [5, 4, 3, 2, 1]
            show("all", all);
            int[] part = {1, 2, 3, 4, 5};
            ArrayUtils.reverse(part, 1, 4);
            int[] middle = part;                       // [1, 4, 3, 2, 5]
            show("middle", middle);
        }
        {
            int[] marks = {70, 95, 82};
            Arrays.sort(marks);
            reverse(marks);
            int[] descending = marks;                  // [95, 82, 70]
            show("descending", descending);
            Integer[] boxed = {70, 95, 82};
            Arrays.sort(boxed, Collections.reverseOrder());
            Integer[] desc = boxed;                    // [95, 82, 70]
            show("desc", desc);
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

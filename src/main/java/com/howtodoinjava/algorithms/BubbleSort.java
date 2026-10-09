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
 * Examples for the tutorial "Bubble Sort in Java".
 * https://howtodoinjava.com/algorithm/bubble-sort-java-example/
 */
public class BubbleSort {
    static void bubbleSortBasic(int[] a) {
        int n = a.length;
        for (int pass = 0; pass < n - 1; pass++) {
            for (int j = 0; j < n - 1 - pass; j++) {
                if (a[j] > a[j + 1]) {
                    int tmp = a[j];
                    a[j] = a[j + 1];
                    a[j + 1] = tmp;
                }
            }
        }
    }
    static void bubbleSort(int[] a) {
        if (a == null) {
            return;
        }
        for (int pass = 0; pass < a.length - 1; pass++) {
            boolean swapped = false;
            for (int j = 0; j < a.length - 1 - pass; j++) {
                if (a[j] > a[j + 1]) {
                    int tmp = a[j];
                    a[j] = a[j + 1];
                    a[j + 1] = tmp;
                    swapped = true;
                }
            }
            if (!swapped) {
                break;                      // already sorted
            }
        }
    }
    static <T> void bubbleSort(T[] a, Comparator<? super T> cmp) {
        for (int pass = 0; pass < a.length - 1; pass++) {
            boolean swapped = false;
            for (int j = 0; j < a.length - 1 - pass; j++) {
                if (cmp.compare(a[j], a[j + 1]) > 0) {
                    T tmp = a[j];
                    a[j] = a[j + 1];
                    a[j + 1] = tmp;
                    swapped = true;
                }
            }
            if (!swapped) {
                break;
            }
        }
    }
    static record Booking(String name, int seatClass) {}
    public static void main(String[] args) throws Exception {
        {
            int[] readings = {4, 1, 3, 2, 6, 5};
            bubbleSort(readings);
            int[] sorted = readings;                // [1, 2, 3, 4, 5, 6]
            show("sorted", sorted);
        }
        {
            int[] inOrder = {1, 2, 3, 4};
            bubbleSort(inOrder);
            int[] unchanged = inOrder;              // [1, 2, 3, 4], one pass
            show("unchanged", unchanged);
            int[] reversed = {4, 3, 2, 1};
            bubbleSortBasic(reversed);
            int[] basicResult = reversed;           // [1, 2, 3, 4]
            show("basicResult", basicResult);
        }
        {
            String[] fruits = {"pear", "apple", "kiwi"};
            bubbleSort(fruits, Comparator.naturalOrder());
            String[] byName = fruits;               // [apple, kiwi, pear]
            show("byName", byName);
            Integer[] marks = {6, 3, 8, 2, 5};
            bubbleSort(marks, Comparator.reverseOrder());
            Integer[] descending = marks;           // [8, 6, 5, 3, 2]
            show("descending", descending);
        }
        {
            Booking[] bookings = {new Booking("Ann", 2), new Booking("Bob", 1), new Booking("Cid", 2), new Booking("Dan", 1)};
            bubbleSort(bookings, Comparator.comparingInt(Booking::seatClass));
            String order = Arrays.stream(bookings).map(Booking::name).collect(Collectors.joining(", "));   // "Bob, Dan, Ann, Cid"
            show("order", order);
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

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
 * Examples for the tutorial "Merge Sort in Java".
 * https://howtodoinjava.com/algorithm/merge-sort-java-example/
 */
public class MergeSort {
    static void mergeSort(int[] a) {
        if (a == null || a.length < 2) {
            return;
        }
        int[] buffer = new int[a.length];
        sort(a, buffer, 0, a.length - 1);
    }

    static void sort(int[] a, int[] buffer, int low, int high) {
        if (low >= high) {
            return;
        }
        int mid = low + (high - low) / 2;   // no int overflow
        sort(a, buffer, low, mid);
        sort(a, buffer, mid + 1, high);
        merge(a, buffer, low, mid, high);
    }

    static void merge(int[] a, int[] buffer, int low, int mid, int high) {
        System.arraycopy(a, low, buffer, low, high - low + 1);
        int i = low;                        // front of the left half
        int j = mid + 1;                    // front of the right half
        int k = low;                        // next output position
        while (i <= mid && j <= high) {
            if (buffer[i] <= buffer[j]) {   // <= keeps it stable
                a[k++] = buffer[i++];
            } else {
                a[k++] = buffer[j++];
            }
        }
        while (i <= mid) {
            a[k++] = buffer[i++];
        }
        while (j <= high) {
            a[k++] = buffer[j++];
        }
    }
    static record Pickup(String customer, int slot) {}
    static <T> void mergeSort(T[] a, Comparator<? super T> cmp) {
        if (a == null || a.length < 2) {
            return;
        }
        T[] buffer = a.clone();
        sort(a, buffer, cmp, 0, a.length - 1);
    }

    static <T> void sort(T[] a, T[] buffer, Comparator<? super T> cmp, int low, int high) {
        if (low >= high) {
            return;
        }
        int mid = low + (high - low) / 2;
        sort(a, buffer, cmp, low, mid);
        sort(a, buffer, cmp, mid + 1, high);
        System.arraycopy(a, low, buffer, low, high - low + 1);
        int i = low, j = mid + 1, k = low;
        while (i <= mid && j <= high) {
            a[k++] = cmp.compare(buffer[i], buffer[j]) <= 0 ? buffer[i++] : buffer[j++];
        }
        while (i <= mid) {
            a[k++] = buffer[i++];
        }
        while (j <= high) {
            a[k++] = buffer[j++];
        }
    }
    static void mergeSortBottomUp(int[] a) {
        if (a == null || a.length < 2) {
            return;
        }
        int n = a.length;
        int[] buffer = new int[n];
        for (int width = 1; width < n; width *= 2) {
            for (int low = 0; low < n - width; low += 2 * width) {
                int mid = low + width - 1;
                int high = Math.min(low + 2 * width - 1, n - 1);
                merge(a, buffer, low, mid, high);
            }
        }
    }
    static void sortFast(int[] a, int[] buffer, int low, int high) {
        if (high - low < 16) {
            for (int i = low + 1; i <= high; i++) {     // insertion sort
                int value = a[i];
                int j = i - 1;
                while (j >= low && a[j] > value) {
                    a[j + 1] = a[j];
                    j--;
                }
                a[j + 1] = value;
            }
            return;
        }
        int mid = low + (high - low) / 2;
        sortFast(a, buffer, low, mid);
        sortFast(a, buffer, mid + 1, high);
        if (a[mid] <= a[mid + 1]) {
            return;                         // already in order
        }
        merge(a, buffer, low, mid, high);
    }
    public static void main(String[] args) throws Exception {
        {
            int[] deliveryMinutes = {5, 2, 7, 1, 8, 3, 6, 4};
            mergeSort(deliveryMinutes);
            int[] sorted = deliveryMinutes;         // [1, 2, 3, 4, 5, 6, 7, 8]
            show("sorted", sorted);
        }
        {
            int[] none = {};
            mergeSort(none);
            int[] stillNone = none;                 // []
            show("stillNone", stillNone);
            int[] repeats = {4, 1, 4, 1, 2};
            mergeSort(repeats);
            int[] sortedRepeats = repeats;          // [1, 1, 2, 4, 4]
            show("sortedRepeats", sortedRepeats);
        }
        {
            Pickup[] pickups = {
                new Pickup("Mia", 3), new Pickup("Noah", 1), new Pickup("Liam", 3), new Pickup("Emma", 1)
            };
            mergeSort(pickups, Comparator.comparingInt(Pickup::slot));
            String order = Arrays.stream(pickups).map(Pickup::customer).collect(Collectors.joining(", "));   // "Noah, Emma, Mia, Liam"
            show("order", order);
        }
        {
            int[] times = {9, 3, 7, 1, 5};
            mergeSortBottomUp(times);
            int[] bottomUp = times;                 // [1, 3, 5, 7, 9]
            show("bottomUp", bottomUp);
        }
        {
            int[] arrivals = new Random(7).ints(100, 0, 1_000).toArray();
            int[] expected = arrivals.clone();
            Arrays.sort(expected);
            sortFast(arrivals, new int[arrivals.length], 0, arrivals.length - 1);
            boolean same = Arrays.equals(expected, arrivals);   // true
            show("same", same);
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

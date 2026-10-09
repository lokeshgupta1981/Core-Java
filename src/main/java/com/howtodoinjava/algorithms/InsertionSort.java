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
 * Examples for the tutorial "Insertion Sort in Java".
 * https://howtodoinjava.com/algorithm/insertion-sort-java-example/
 */
public class InsertionSort {
    static void insertionSort(int[] a) {
        if (a == null) {
            return;
        }
        for (int i = 1; i < a.length; i++) {
            int key = a[i];
            int j = i - 1;
            while (j >= 0 && a[j] > key) {
                a[j + 1] = a[j];            // shift right
                j--;
            }
            a[j + 1] = key;
        }
    }
    static <T> void insertionSort(T[] a, Comparator<? super T> cmp) {
        for (int i = 1; i < a.length; i++) {
            T key = a[i];
            int j = i - 1;
            while (j >= 0 && cmp.compare(a[j], key) > 0) {
                a[j + 1] = a[j];
                j--;
            }
            a[j + 1] = key;
        }
    }
    static record Ticket(String id, int priority) {}
    static void binaryInsertionSort(int[] a) {
        for (int i = 1; i < a.length; i++) {
            int key = a[i];
            int low = 0;
            int high = i;
            while (low < high) {
                int mid = (low + high) >>> 1;
                if (key < a[mid]) {
                    high = mid;
                } else {
                    low = mid + 1;          // after equal keys, stable
                }
            }
            System.arraycopy(a, low, a, low + 1, i - low);
            a[low] = key;
        }
    }
    public static void main(String[] args) throws Exception {
        {
            int[] responseMs = {8, 3, 5, 1, 9, 2};
            insertionSort(responseMs);
            int[] sorted = responseMs;              // [1, 2, 3, 5, 8, 9]
            show("sorted", sorted);
        }
        {
            int[] empty = {};
            insertionSort(empty);
            int[] stillEmpty = empty;               // []
            show("stillEmpty", stillEmpty);
            int[] withDupes = {2, 5, 2, 1};
            insertionSort(withDupes);
            int[] sortedDupes = withDupes;          // [1, 2, 2, 5]
            show("sortedDupes", sortedDupes);
        }
        {
            Ticket[] tickets = {new Ticket("t1", 3), new Ticket("t2", 1), new Ticket("t3", 3), new Ticket("t4", 2)};
            insertionSort(tickets, Comparator.comparingInt(Ticket::priority));
            String order = Arrays.stream(tickets).map(Ticket::id).collect(Collectors.joining(", "));   // "t2, t4, t1, t3"
            show("order", order);
        }
        {
            int[] almostSorted = IntStream.range(0, 1_000_000).toArray();
            Random random = new Random(42);
            for (int k = 0; k < 100; k++) {                 // swap 100 neighbor pairs
                int i = random.nextInt(almostSorted.length - 1);
                int tmp = almostSorted[i];
                almostSorted[i] = almostSorted[i + 1];
                almostSorted[i + 1] = tmp;
            }
            insertionSort(almostSorted);
            boolean inOrder = almostSorted[500] < almostSorted[501];   // true
            show("inOrder", inOrder);
        }
        {
            int[] latencies = {8, 3, 5, 1, 9, 2};
            binaryInsertionSort(latencies);
            int[] binarySorted = latencies;         // [1, 2, 3, 5, 8, 9]
            show("binarySorted", binarySorted);
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

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
 * Examples for the tutorial "Selection Sort in Java".
 * https://howtodoinjava.com/algorithm/selection-sort-java-example/
 */
public class SelectionSort {
    static void selectionSort(int[] a) {
        if (a == null) {
            return;
        }
        for (int i = 0; i < a.length - 1; i++) {
            int min = i;
            for (int j = i + 1; j < a.length; j++) {
                if (a[j] < a[min]) {
                    min = j;
                }
            }
            if (min != i) {                 // skip useless swaps
                int tmp = a[i];
                a[i] = a[min];
                a[min] = tmp;
            }
        }
    }
    static <T> void selectionSort(T[] a, Comparator<? super T> cmp) {
        for (int i = 0; i < a.length - 1; i++) {
            int min = i;
            for (int j = i + 1; j < a.length; j++) {
                if (cmp.compare(a[j], a[min]) < 0) {
                    min = j;
                }
            }
            if (min != i) {
                T tmp = a[i];
                a[i] = a[min];
                a[min] = tmp;
            }
        }
    }
    static record Task(String name, int priority) {}
    static <T> void stableSelectionSort(T[] a, Comparator<? super T> cmp) {
        for (int i = 0; i < a.length - 1; i++) {
            int min = i;
            for (int j = i + 1; j < a.length; j++) {
                if (cmp.compare(a[j], a[min]) < 0) {
                    min = j;
                }
            }
            T value = a[min];
            System.arraycopy(a, i, a, i + 1, min - i);   // shift right
            a[i] = value;
        }
    }
    public static void main(String[] args) throws Exception {
        {
            int[] pages = {7, 4, 9, 1, 5};
            selectionSort(pages);
            int[] sorted = pages;                   // [1, 4, 5, 7, 9]
            show("sorted", sorted);
        }
        {
            int[] one = {42};
            selectionSort(one);
            int[] single = one;                     // [42]
            show("single", single);
            int[] repeated = {3, 1, 3, 1};
            selectionSort(repeated);
            int[] sortedRepeated = repeated;        // [1, 1, 3, 3]
            show("sortedRepeated", sortedRepeated);
        }
        {
            String[] cities = {"Pune", "Agra", "Delhi"};
            selectionSort(cities, Comparator.naturalOrder());
            String[] alphabetical = cities;         // [Agra, Delhi, Pune]
            show("alphabetical", alphabetical);
            Integer[] heights = {7, 4, 9, 1, 5};
            selectionSort(heights, Comparator.reverseOrder());
            Integer[] tallestFirst = heights;       // [9, 7, 5, 4, 1]
            show("tallestFirst", tallestFirst);
        }
        {
            Task[] tasks = {new Task("write", 2), new Task("test", 2), new Task("deploy", 1)};
            selectionSort(tasks, Comparator.comparingInt(Task::priority));
            String order = Arrays.stream(tasks).map(Task::name).collect(Collectors.joining(", "));   // "deploy, test, write"
            show("order", order);
        }
        {
            Task[] queue = {new Task("write", 2), new Task("test", 2), new Task("deploy", 1)};
            stableSelectionSort(queue, Comparator.comparingInt(Task::priority));
            String stableOrder = Arrays.stream(queue).map(Task::name).collect(Collectors.joining(", "));   // "deploy, write, test"
            show("stableOrder", stableOrder);
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

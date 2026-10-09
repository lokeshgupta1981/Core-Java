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
 * Examples for the tutorial "Java Algorithms".
 * https://howtodoinjava.com/java-algorithms-implementations/
 */
public class JavaAlgorithms {

    public static void main(String[] args) throws Exception {
        {
            int[] scores = {6, 4, 1, 8, 9, 2, 5};
            Arrays.sort(scores);                                  // Dual-Pivot Quicksort
            int[] sorted = scores;                                // [1, 2, 4, 5, 6, 8, 9]
            show("sorted", sorted);
            int index = Arrays.binarySearch(scores, 8);           // 5
            show("index", index);
            List<String> names = new ArrayList<>(List.of("Rita", "Amit", "Lokesh"));
            Collections.sort(names);                              // TimSort, stable
            List<String> sortedNames = names;                     // [Amit, Lokesh, Rita]
            show("sortedNames", sortedNames);
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

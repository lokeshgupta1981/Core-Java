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
 * Examples for the tutorial "Java Arrays: Memory, Rules, Arrays Class and All Tutorials".
 * https://howtodoinjava.com/java/array/java-arrays/
 */
public class JavaArrays {

    public static void main(String[] args) throws Exception {
        {
            int[] scores = {72, 95, 61, 88};
            int first = scores[0];                  // 72
            show("first", first);
            int count = scores.length;              // 4
            show("count", count);
            String text = Arrays.toString(scores);  // "[72, 95, 61, 88]"
            show("text", text);
            Arrays.sort(scores);
            String sorted = Arrays.toString(scores);   // "[61, 72, 88, 95]"
            show("sorted", sorted);
        }
        {
            int[] powers = new int[5];
            powers[0] = 1;
            powers[1] = 2;
            powers[2] = 4;
            String unset = Arrays.toString(powers);   // "[1, 2, 4, 0, 0]"
            show("unset", unset);
            String[] names = new String[2];
            String empty = Arrays.toString(names);    // "[null, null]"
            show("empty", empty);
        }
        {
            int[] small = new int[3];
            try { int outside = small[3]; show("outside", outside); } catch (Throwable _t) { System.out.println("outside -> " + _t); }
        }
        {
            int[][] triangle = new int[3][];
            triangle[0] = new int[1];
            triangle[1] = new int[2];
            triangle[2] = new int[3];
            int lastRowLength = triangle[2].length;   // 3
            show("lastRowLength", lastRowLength);
            String grid = Arrays.deepToString(triangle);   // "[[0], [0, 0], [0, 0, 0]]"
            show("grid", grid);
        }
        {
            int[] ids = {40, 10, 30, 20};
            int[] bigger = Arrays.copyOf(ids, 6);    // [40, 10, 30, 20, 0, 0]
            show("bigger", bigger);
            Arrays.sort(ids);
            int position = Arrays.binarySearch(ids, 30);   // 2
            show("position", position);
            boolean same = Arrays.equals(ids, new int[]{10, 20, 30, 40});   // true
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

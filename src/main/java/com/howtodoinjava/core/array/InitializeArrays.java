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
 * Examples for the tutorial "Declare and Initialize an Array in Java (With Examples)".
 * https://howtodoinjava.com/java/array/initializing-array/
 */
public class InitializeArrays {
    static String[] defaultRoles() {
        return new String[] {"viewer", "editor"};
    }
    public static void main(String[] args) throws Exception {
        {
            int[] scores = new int[3];                       // [0, 0, 0]
            show("scores", scores);
            int[] primes = {2, 3, 5, 7};                     // [2, 3, 5, 7]
            show("primes", primes);
            String[] days = new String[] {"Mon", "Tue"};     // [Mon, Tue]
            show("days", days);
            int[] squares = new int[5];
            Arrays.setAll(squares, i -> i * i);
            int[] filled = squares;                          // [0, 1, 4, 9, 16]
            show("filled", filled);
            int[][] grid = new int[2][3];                    // [[0, 0, 0], [0, 0, 0]]
            show("grid", grid);
        }
        {
            int[] marks;          // preferred
            int marks2[];         // C-style, legal but discouraged
            int[] a, b;           // a and b are both int[]
            int c[], d;           // c is int[], but d is a plain int
        }
        {
            int months = 12;
            double[] monthlySales = new double[months];
            monthlySales[0] = 1250.5;
            int count = monthlySales.length;                     // 12
            show("count", count);
            double march = monthlySales[2];                      // 0.0
            show("march", march);
            try { double bad = monthlySales[12]; show("bad", bad); } catch (Throwable _t) { System.out.println("bad -> " + _t); }
            try { int[] negative = new int[-1]; show("negative", negative); } catch (Throwable _t) { System.out.println("negative -> " + _t); }
        }
        {
            boolean[] seatsTaken = new boolean[4];    // [false, false, false, false]
            show("seatsTaken", seatsTaken);
            String[] names = new String[2];           // [null, null]
            show("names", names);
            Integer[] boxed = new Integer[2];         // [null, null]
            show("boxed", boxed);
        }
        {
            String[] status = {"active", "paused", "closed"};      // length 3
            show("status", status);
            int[] weights = new int[] {10, 20, 30,};                // trailing comma allowed
            show("weights", weights);
            char[] vowels = {'a', 'e', 'i', 'o', 'u'};              // [a, e, i, o, u]
            show("vowels", vowels);
        }
        {
            String[] status = new String[3];
            status = new String[] {"active", "paused"};           // [active, paused]
            String[] roles = defaultRoles();                      // [viewer, editor]
            show("roles", roles);
            var levels = new int[] {1, 2, 3};                     // [1, 2, 3]
            show("levels", levels);
        }
        {
            int[] retries = new int[5];
            Arrays.fill(retries, 3);
            int[] all = retries;                              // [3, 3, 3, 3, 3]
            show("all", all);
            int[] partial = new int[6];
            Arrays.fill(partial, 1, 4, 9);
            int[] range = partial;                            // [0, 9, 9, 9, 0, 0]
            show("range", range);
            int[] even = new int[4];
            Arrays.setAll(even, i -> i * 2);
            int[] evens = even;                               // [0, 2, 4, 6]
            show("evens", evens);
        }
        {
            StringBuilder[] shared = new StringBuilder[2];
            Arrays.fill(shared, new StringBuilder());
            shared[0].append("x");
            String second = shared[1].toString();                  // "x", same object
            show("second", second);
            StringBuilder[] separate = new StringBuilder[2];
            Arrays.setAll(separate, i -> new StringBuilder());
            separate[0].append("x");
            String other = separate[1].toString();                 // ""
            show("other", other);
        }
        {
            String[] labels = new String[3];
            for (int i = 0; i < labels.length; i++) {
                labels[i] = "Table " + (i + 1);
            }
            String[] tables = labels;                              // [Table 1, Table 2, Table 3]
            show("tables", tables);
        }
        {
            int[] oneToFive = IntStream.rangeClosed(1, 5).toArray();          // [1, 2, 3, 4, 5]
            show("oneToFive", oneToFive);
            int[] firstFive = IntStream.range(0, 5).toArray();                // [0, 1, 2, 3, 4]
            show("firstFive", firstFive);
            double[] prices = DoubleStream.of(9.5, 12.0).toArray();           // [9.5, 12.0]
            show("prices", prices);
            String[] zeros = Stream.generate(() -> "0").limit(3).toArray(String[]::new);   // [0, 0, 0]
            show("zeros", zeros);
            String[] upper = Stream.of("tea", "jam").map(String::toUpperCase).toArray(String[]::new);   // [TEA, JAM]
            show("upper", upper);
        }
        {
            int[] base = {4, 5};
            int[] longer = Arrays.copyOf(base, 4);                 // [4, 5, 0, 0]
            show("longer", longer);
        }
        {
            int[][] board = new int[3][3];                       // [[0, 0, 0], [0, 0, 0], [0, 0, 0]]
            show("board", board);
            int[][] matrix = {{1, 2, 3}, {4, 5, 6}};               // [[1, 2, 3], [4, 5, 6]]
            show("matrix", matrix);
            int rows = matrix.length;                              // 2
            show("rows", rows);
            int cols = matrix[0].length;                           // 3
            show("cols", cols);
            int[][] triangle = new int[3][];
            triangle[0] = new int[] {1};
            triangle[1] = new int[] {1, 1};
            triangle[2] = new int[] {1, 2, 1};
            int[][] jagged = triangle;                             // [[1], [1, 1], [1, 2, 1]]
            show("jagged", jagged);
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

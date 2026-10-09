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
 * Examples for the tutorial "Recursion in Java".
 * https://howtodoinjava.com/algorithm/what-is-recursion/
 */
public class RecursionExamples {
    static long factorial(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("n must not be negative");
        }
        if (n <= 1) {
            return 1;                       // base case
        }
        return n * factorial(n - 1);        // recursive case
    }
    static String reverse(String s) {
        if (s == null || s.isEmpty()) {
            return s;
        }
        return reverse(s.substring(1)) + s.charAt(0);
    }
    static int binarySearch(int[] a, int key, int low, int high) {
        if (low > high) {
            return -1;                      // not found
        }
        int mid = low + (high - low) / 2;
        if (a[mid] == key) {
            return mid;
        }
        return a[mid] < key
        ? binarySearch(a, key, mid + 1, high)
        : binarySearch(a, key, low, mid - 1);
    }
    static record Category(String name, int items, List<Category> children) {}
    static int countItems(Category c) {
        int total = c.items();
        for (Category child : c.children()) {
            total += countItems(child);
        }
        return total;
    }
    static long fib(int n) {
        if (n < 2) {
            return n;
        }
        return fib(n - 1) + fib(n - 2);
    }
    static long fibMemo(int n, Map<Integer, Long> memo) {
        if (n < 2) {
            return n;
        }
        Long cached = memo.get(n);
        if (cached != null) {
            return cached;
        }
        long value = fibMemo(n - 1, memo) + fibMemo(n - 2, memo);
        memo.put(n, value);
        return value;
    }
    static boolean isEven(int n) {
        return n == 0 || isOdd(n - 1);
    }

    static boolean isOdd(int n) {
        return n != 0 && isEven(n - 1);
    }
    public static void main(String[] args) throws Exception {
        {
            long f4 = factorial(4);                 // 24
            show("f4", f4);
            long f20 = factorial(20);               // 2432902008176640000
            show("f20", f20);
            long f0 = factorial(0);                 // 1
            show("f0", f0);
        }
        {
            String reversed = reverse("java");     // "avaj"
            show("reversed", reversed);
            String empty = reverse("");             // ""
            show("empty", empty);
        }
        {
            int[] years = {2015, 2018, 2020, 2023, 2026};
            int found = binarySearch(years, 2023, 0, years.length - 1);     // 3
            show("found", found);
            int missing = binarySearch(years, 2019, 0, years.length - 1);   // -1
            show("missing", missing);
        }
        {
            Category menu = new Category("Menu", 0, List.of(
            new Category("Drinks", 2, List.of(new Category("Tea", 3, List.of()), new Category("Juice", 4, List.of()))),
            new Category("Snacks", 5, List.of())));
            int allItems = countItems(menu);        // 14
            show("allItems", allItems);
        }
        {
            long fib30 = fib(30);                   // 832040
            show("fib30", fib30);
            long fib90 = fibMemo(90, new HashMap<>());   // 2880067194370816120
            show("fib90", fib90);
        }
        {
            boolean even = isEven(10);              // true
            show("even", even);
            boolean odd = isOdd(7);                 // true
            show("odd", odd);
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

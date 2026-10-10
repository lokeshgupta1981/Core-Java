package com.howtodoinjava.core.examples;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.time.*;
import java.nio.file.*;
import java.math.*;
import java.io.*;

import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.stream.*;
import java.util.function.*;
import java.io.*;
import java.nio.file.*;
import java.nio.charset.*;
import java.time.*;
import java.math.*;

/**
 * Examples for the tutorial "Swap Two Numbers in Java With and Without a Temp Variable".
 * https://howtodoinjava.com/java-examples/swap-two-numbers/
 */
public class SwapTwoNumbersTempVariable {
    static void swap(int[] values, int i, int j) {
        int temp = values[i];
        values[i] = values[j];
        values[j] = temp;
    }
    static void swapInts(int first, int second) {
        int temp = first;
        first = second;
        second = temp;
    }
    static record Pair(int first, int second) {
        Pair swapped() {
            return new Pair(second, first);
        }
    }
    static record PriceRange(int min, int max) {
        PriceRange {
            if (min > max) {
                int temp = min;
                min = max;
                max = temp;
            }
        }
    }
    public static void main(String[] args) throws Exception {
        {
            int a = 100;
            int b = 200;
            int temp = a;                                  // temp = 100
            show("temp", temp);
            a = b;
            b = temp;
            String result = "a = " + a + ", b = " + b;     // "a = 200, b = 100"
            show("result", result);
        }
        {
            int[] scores = {42, 17, 8};
            swap(scores, 0, 2);
            String swapped = Arrays.toString(scores);      // "[8, 17, 42]"
            show("swapped", swapped);
        }
        {
            int a = 100;
            int b = 200;
            a = a + b;                                     // a = 300
            b = a - b;                                     // b = 300 - 200 = 100
            a = a - b;                                     // a = 300 - 100 = 200
            String result = "a = " + a + ", b = " + b;     // "a = 200, b = 100"
            show("result", result);
        }
        {
            int x = Integer.MAX_VALUE;
            int y = 1;
            x = x + y;                                     // x = -2147483648, overflow
            y = x - y;                                     // y = 2147483647
            x = x - y;                                     // x = 1
            String wrapped = x + " " + y;                  // "1 2147483647"
            show("wrapped", wrapped);
        }
        {
            double small = 0.1;
            double large = 1.0E20;
            small = small + large;                         // 1.0E20, the 0.1 is rounded away
            large = small - large;                         // 0.0
            small = small - large;                         // 1.0E20
            String broken = small + " " + large;           // "1.0E20 0.0"
            show("broken", broken);
        }
        {
            int a = 5;                                     // 5 = 0101
            show("a", a);
            int b = 9;                                     // 9 = 1001
            show("b", b);
            a = a ^ b;                                     // a = 1100 (12)
            b = a ^ b;                                     // b = 1100 ^ 1001 = 0101 (5)
            a = a ^ b;                                     // a = 1100 ^ 0101 = 1001 (9)
            String result = "a = " + a + ", b = " + b;     // "a = 9, b = 5"
            show("result", result);
        }
        {
            int[] marks = {7, 3};
            int i = 0;
            int j = 0;
            marks[i] = marks[i] ^ marks[j];
            marks[j] = marks[j] ^ marks[i];
            marks[i] = marks[i] ^ marks[j];
            int lost = marks[0];                           // 0
            show("lost", lost);
        }
        {
            int a = 100;
            int b = 200;
            b = a + b - (a = b);                           // 300 - 200
            String result = "a = " + a + ", b = " + b;     // "a = 200, b = 100"
            show("result", result);
        }
        {
            int a = 100;
            int b = 200;
            swapInts(a, b);
            String unchanged = "a = " + a + ", b = " + b;  // "a = 100, b = 200"
            show("unchanged", unchanged);
        }
        {
            Pair pair = new Pair(100, 200).swapped();      // Pair[first=200, second=100]
            show("pair", pair);
        }
        {
            PriceRange typed = new PriceRange(500, 100);       // PriceRange[min=100, max=500]
            show("typed", typed);
            PriceRange correct = new PriceRange(100, 500);     // PriceRange[min=100, max=500]
            show("correct", correct);
        }
        {
            int a = 0;
            int b = 5;
            a = a * b;                                     // a = 0
            b = a / b;                                     // b = 0
            try { int failed = a / b; show("failed", failed); } catch (Throwable _t) { System.out.println("failed -> " + _t); }
        }
        {
            String first = "tea";
            String second = "coffee";
            first = first + second;                                         // "teacoffee"
            second = first.substring(0, first.length() - second.length());  // "tea"
            first = first.substring(second.length());                       // "coffee"
            String result = first + " " + second;                           // "coffee tea"
            show("result", result);
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

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
 * Examples for the tutorial "Pronic Number in Java: Check n(n+1) With or Without a Loop".
 * https://howtodoinjava.com/java-examples/check-pronic-number/
 */
public class PronicNumberOblongRectangle {
    static boolean isPronicLoop(int number) {
        for (long i = 0; i * (i + 1) <= number; i++) {
            if (i * (i + 1) == number) {
                return true;
            }
        }
        return false;
    }
    static boolean isPronic(long number) {
        if (number < 0) {
            return false;
        }
        long root = (long) Math.sqrt(number);
        return root * (root + 1) == number;
    }
    static OptionalInt pronicIndex(int x) {
        if (x < 0) {
            return OptionalInt.empty();
        }
        long t = 4L * x + 1;
        long root = (long) Math.sqrt(t);
        return root * root == t ? OptionalInt.of((int) (root - 1) / 2) : OptionalInt.empty();
    }
    static int teamsForDoubleRoundRobin(int fixtures) {
        OptionalInt n = pronicIndex(fixtures);
        if (n.isEmpty() || fixtures == 0) {
            throw new IllegalArgumentException("Not a double round-robin schedule: " + fixtures + " fixtures");
        }
        return n.getAsInt() + 1;
    }
    public static void main(String[] args) throws Exception {
        {
            int number = 56;
            int root = (int) Math.sqrt(number);             // 7
            show("root", root);
            boolean pronic = root * (root + 1) == number;   // true
            show("pronic", pronic);
        }
        {
            boolean p1 = isPronicLoop(0);      // true
            show("p1", p1);
            boolean p2 = isPronicLoop(42);     // true
            show("p2", p2);
            boolean p3 = isPronicLoop(24);     // false
            show("p3", p3);
        }
        {
            boolean q1 = isPronic(56);                         // true
            show("q1", q1);
            boolean q2 = isPronic(57);                         // false
            show("q2", q2);
            boolean q3 = isPronic(-6);                         // false
            show("q3", q3);
            boolean q4 = isPronic(3_037_000_499L * 3_037_000_500L);   // true
            show("q4", q4);
            boolean q5 = isPronic(Long.MAX_VALUE);             // false
            show("q5", q5);
        }
        {
            OptionalInt i56 = pronicIndex(56);     // OptionalInt[7]
            show("i56", i56);
            OptionalInt i30 = pronicIndex(30);     // OptionalInt[5]
            show("i30", i30);
            OptionalInt i50 = pronicIndex(50);     // OptionalInt.empty
            show("i50", i50);
        }
        {
            List<Integer> firstTen = IntStream.range(0, 10).map(n -> n * (n + 1)).boxed().toList();   // [0, 2, 6, 12, 20, 30, 42, 56, 72, 90]
            show("firstTen", firstTen);
            List<Long> upTo500 = LongStream.iterate(1, n -> n * (n + 1) <= 500, n -> n + 1).map(n -> n * (n + 1)).boxed().toList();   // [2, 6, 12, 20, 30, 42, 56, 72, 90, 110, 132, 156, 182, 210, 240, 272, 306, 342, 380, 420, 462]
            show("upTo500", upTo500);
        }
        {
            int teams20 = teamsForDoubleRoundRobin(380);    // 20
            show("teams20", teams20);
            int teams18 = teamsForDoubleRoundRobin(306);    // 18
            show("teams18", teams18);
            try { int broken = teamsForDoubleRoundRobin(379); show("broken", broken); } catch (Throwable _t) { System.out.println("broken -> " + _t); }
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

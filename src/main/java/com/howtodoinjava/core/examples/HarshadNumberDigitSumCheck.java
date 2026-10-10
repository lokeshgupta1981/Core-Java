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
 * Examples for the tutorial "Harshad Number in Java: Niven Number Check With Examples".
 * https://howtodoinjava.com/java-examples/check-harshad-number/
 */
public class HarshadNumberDigitSumCheck {
    static boolean isHarshadUnsafe(int number) {
        int temp = number;
        int digitSum = 0;
        while (temp > 0) {
            digitSum += temp % 10;
            temp /= 10;
        }
        return number % digitSum == 0;
    }
    static boolean isHarshad(long number) {
        if (number <= 0) {
            return false;
        }
        long digitSum = 0;
        for (long temp = number; temp > 0; temp /= 10) {
            digitSum += temp % 10;
        }
        return number % digitSum == 0;
    }
    static boolean isHarshadText(long number) {
        if (number <= 0) {
            return false;
        }
        int digitSum = Long.toString(number).chars().map(c -> c - '0').sum();
        return number % digitSum == 0;
    }
    static boolean isHarshad(long number, int base) {
        if (number <= 0 || base < 2) {
            return false;
        }
        long digitSum = 0;
        for (long temp = number; temp > 0; temp /= base) {
            digitSum += temp % base;
        }
        return number % digitSum == 0;
    }
    static record DrawResult(long receipt, long digitSum, boolean wins) {}
    static DrawResult luckyDraw(long receipt) {
        if (receipt <= 0) {
            throw new IllegalArgumentException("Invalid receipt number: " + receipt);
        }
        long digitSum = 0;
        for (long t = receipt; t > 0; t /= 10) {
            digitSum += t % 10;
        }
        return new DrawResult(receipt, digitSum, receipt % digitSum == 0);
    }
    public static void main(String[] args) throws Exception {
        {
            int number = 156;
            int digitSum = String.valueOf(number).chars().map(c -> c - '0').sum();   // 12
            show("digitSum", digitSum);
            boolean harshad = number % digitSum == 0;                               // true
            show("harshad", harshad);
        }
        {
            boolean h40 = isHarshadUnsafe(40);     // true
            show("h40", h40);
            boolean h47 = isHarshadUnsafe(47);     // false
            show("h47", h47);
            try { boolean h0 = isHarshadUnsafe(0); show("h0", h0); } catch (Throwable _t) { System.out.println("h0 -> " + _t); }
        }
        {
            boolean safe0 = isHarshad(0);                         // false
            show("safe0", safe0);
            boolean safeNeg = isHarshad(-18);                     // false
            show("safeNeg", safeNeg);
            boolean h1729 = isHarshad(1729);                      // true
            show("h1729", h1729);
            boolean bigOne = isHarshad(1_000_000_000_000_000L);   // true
            show("bigOne", bigOne);
        }
        {
            boolean t156 = isHarshadText(156);    // true
            show("t156", t156);
            boolean t19 = isHarshadText(19);      // false
            show("t19", t19);
        }
        {
            String binary = Long.toString(12, 2);       // "1100"
            show("binary", binary);
            boolean base2 = isHarshad(12, 2);           // true
            show("base2", base2);
            boolean base10 = isHarshad(11, 10);         // false
            show("base10", base10);
            boolean base3 = isHarshad(11, 3);           // false
            show("base3", base3);
        }
        {
            List<Long> allHarshad = LongStream.rangeClosed(1, 1_000).filter(n -> IntStream.rangeClosed(2, (int) Math.max(2, n)).allMatch(b -> isHarshad(n, b))).boxed().toList();   // [1, 2, 4, 6]
            show("allHarshad", allHarshad);
        }
        {
            List<Long> upTo50 = LongStream.rangeClosed(1, 50).filter(n -> isHarshad(n)).boxed().toList();   // [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 12, 18, 20, 21, 24, 27, 30, 36, 40, 42, 45, 48, 50]
            show("upTo50", upTo50);
            long countTo100 = LongStream.rangeClosed(1, 100).filter(n -> isHarshad(n)).count();     // 33
            show("countTo100", countTo100);
            long countTo1000 = LongStream.rangeClosed(1, 1_000).filter(n -> isHarshad(n)).count();  // 213
            show("countTo1000", countTo1000);
        }
        {
            DrawResult win = luckyDraw(20_250_304);     // DrawResult[receipt=20250304, digitSum=16, wins=true]
            show("win", win);
            DrawResult lose = luckyDraw(20_250_311);    // DrawResult[receipt=20250311, digitSum=14, wins=false]
            show("lose", lose);
            try { DrawResult bad = luckyDraw(0); show("bad", bad); } catch (Throwable _t) { System.out.println("bad -> " + _t); }
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

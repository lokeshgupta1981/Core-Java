package com.howtodoinjava.core.basic;

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
import java.math.BigInteger;

/**
 * Examples for the tutorial "Java Math.abs() vs Math.absExact(): Overflow Explained".
 * https://howtodoinjava.com/java/basics/java-math-abs-vs-absexact/
 */
public class MathAbsIntegerMinValueOverflow {
    static long distanceOrReject(int position) {
        try {
            return Math.absExact(position);
        } catch (ArithmeticException e) {
            throw new IllegalArgumentException("Position out of range: " + position, e);
        }
    }
    static int absExactLegacy(int a) {
        if (a == Integer.MIN_VALUE) {
            throw new ArithmeticException("Overflow to represent absolute value of Integer.MIN_VALUE");
        }
        return Math.abs(a);
    }
    public static void main(String[] args) throws Exception {
        {
            int distance = Math.abs(-15);                         // 15
            show("distance", distance);
            double depth = Math.abs(-10.56);                      // 10.56
            show("depth", depth);
            int broken = Math.abs(Integer.MIN_VALUE);             // -2147483648
            show("broken", broken);
            int safe = Math.absExact(-15);                        // 15
            show("safe", safe);
            try { int checked = Math.absExact(Integer.MIN_VALUE); show("checked", checked); } catch (Throwable _t) { System.out.println("checked -> " + _t); }
        }
        {
            double doubleValue = Math.abs(-10.56);          // 10.56
            show("doubleValue", doubleValue);
            float floatValue = Math.abs(-7.8f);             // 7.8
            show("floatValue", floatValue);
            int intValue = Math.abs(-15);                   // 15
            show("intValue", intValue);
            long longValue = Math.abs(-123456789L);         // 123456789
            show("longValue", longValue);
            byte small = -128;
            int widened = Math.abs(small);                  // 128
            show("widened", widened);
        }
        {
            double negativeZero = Math.abs(-0.0);                       // 0.0
            show("negativeZero", negativeZero);
            double infinity = Math.abs(Double.NEGATIVE_INFINITY);       // Infinity
            show("infinity", infinity);
            double notANumber = Math.abs(Double.NaN);                   // NaN
            show("notANumber", notANumber);
        }
        {
            int intMinValue = Integer.MIN_VALUE;
            long longMinValue = Long.MIN_VALUE;

            int absInt = Math.abs(intMinValue);         // -2147483648
            show("absInt", absInt);
            long absLong = Math.abs(longMinValue);      // -9223372036854775808
            show("absLong", absLong);
        }
        {
            int high = Integer.MAX_VALUE;
            int low = -10;
            int wrongGap = Math.abs(high - low);              // 2147483639
            show("wrongGap", wrongGap);
            long rightGap = Math.abs((long) high - low);      // 2147483657
            show("rightGap", rightGap);
        }
        {
            int fine = Math.absExact(-2_000_000_000);                 // 2000000000
            show("fine", fine);
            try { int intFails = Math.absExact(Integer.MIN_VALUE); show("intFails", intFails); } catch (Throwable _t) { System.out.println("intFails -> " + _t); }
            try { long longFails = Math.absExact(Long.MIN_VALUE); show("longFails", longFails); } catch (Throwable _t) { System.out.println("longFails -> " + _t); }
        }
        {
            long widenedAbs = Math.abs((long) Integer.MIN_VALUE);          // 2147483648
            show("widenedAbs", widenedAbs);
            BigInteger bigAbs = BigInteger.valueOf(Long.MIN_VALUE).abs();   // 9223372036854775808
            show("bigAbs", bigAbs);
            try { int negated = Math.negateExact(Integer.MIN_VALUE); show("negated", negated); } catch (Throwable _t) { System.out.println("negated -> " + _t); }
        }
        {
            int hash = "polygenelubricants".hashCode();         // -2147483648
            show("hash", hash);
            int badSegment = Math.abs(hash) % 10;                 // -8
            show("badSegment", badSegment);
        }
        {
            int keyHash = "polygenelubricants".hashCode();      // -2147483648
            show("keyHash", keyHash);
            int segment = Math.floorMod(keyHash, 10);             // 2
            show("segment", segment);
            int masked = (keyHash & 0x7fffffff) % 10;             // 0
            show("masked", masked);
        }
        {
            int legacyResult = absExactLegacy(-42);                       // 42
            show("legacyResult", legacyResult);
            try { int legacyFails = absExactLegacy(Integer.MIN_VALUE); show("legacyFails", legacyFails); } catch (Throwable _t) { System.out.println("legacyFails -> " + _t); }
        }
        {
            int value = -25;
            int manual = value < 0 ? -value : value;        // 25
            show("manual", manual);
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

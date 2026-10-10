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

/**
 * Examples for the tutorial "Java Math.divideExact(): Detect Integer Division Overflow".
 * https://howtodoinjava.com/java/basics/math-divide-exact/
 */
public class MathDivideexactOverflow {
    static long normalize(long amountInCents, int direction) {
        try {
            return Math.divideExact(amountInCents, (long) direction);
        } catch (ArithmeticException e) {
            throw new IllegalArgumentException(
            "Invalid row, amount=" + amountInCents + ", direction=" + direction, e);
        }
    }
    static int divideExactLegacy(int x, int y) {
        if (x == Integer.MIN_VALUE && y == -1) {
            throw new ArithmeticException("integer overflow");
        }
        return x / y;
    }
    public static void main(String[] args) throws Exception {
        {
            int quotient = 100 / -1;                                  // -100
            show("quotient", quotient);
            int exact = Math.divideExact(100, -1);                    // -100
            show("exact", exact);
            int wrapped = Integer.MIN_VALUE / -1;                     // -2147483648
            show("wrapped", wrapped);
            try { int checked = Math.divideExact(Integer.MIN_VALUE, -1); show("checked", checked); } catch (Throwable _t) { System.out.println("checked -> " + _t); }
        }
        {
            int result = 100 / -1;   // -100
            show("result", result);
        }
        {
            int maxInteger = Integer.MAX_VALUE;   //  2147483647
            show("maxInteger", maxInteger);
            int minInteger = Integer.MIN_VALUE;   // -2147483648
            show("minInteger", minInteger);

            int quotient = minInteger / -1;       // -2147483648
            show("quotient", quotient);
        }
        {
            long y = Long.MIN_VALUE;    // -9223372036854775808
            show("y", y);
            long longQuotient = y / -1; // -9223372036854775808
            show("longQuotient", longQuotient);
        }
        {
            int quotientExactFine = Math.divideExact(4, -1);                // -4
            show("quotientExactFine", quotientExactFine);
            int truncated = Math.divideExact(-7, 2);                         // -3
            show("truncated", truncated);
            try { int quotientExactEx = Math.divideExact(Integer.MIN_VALUE, -1); show("quotientExactEx", quotientExactEx); } catch (Throwable _t) { System.out.println("quotientExactEx -> " + _t); }
            try { long longEx = Math.divideExact(Long.MIN_VALUE, -1L); show("longEx", longEx); } catch (Throwable _t) { System.out.println("longEx -> " + _t); }
            try { int byZero = Math.divideExact(7, 0); show("byZero", byZero); } catch (Throwable _t) { System.out.println("byZero -> " + _t); }
        }
        {
            int floor = Math.floorDivExact(-7, 2);                         // -4
            show("floor", floor);
            int ceil = Math.ceilDivExact(7, 2);                            // 4
            show("ceil", ceil);
            try { int floorEx = Math.floorDivExact(Integer.MIN_VALUE, -1); show("floorEx", floorEx); } catch (Throwable _t) { System.out.println("floorEx -> " + _t); }
        }
        {
            int pages = Math.ceilDivExact(45, 10);                         // 5
            show("pages", pages);
        }
        {
            int remainder = Integer.MIN_VALUE % -1;                        // 0
            show("remainder", remainder);
            int floorRemainder = Math.floorMod(-7, 2);                     // 1
            show("floorRemainder", floorRemainder);
        }
        {
            double infinite = 10.0 / 0;                                   // Infinity
            show("infinite", infinite);
            double notANumber = 0.0 / 0;                                   // NaN
            show("notANumber", notANumber);
            long widened = (long) Integer.MIN_VALUE / -1;                  // 2147483648
            show("widened", widened);
        }
        {
            long credit = normalize(1250, 1);                              // 1250
            show("credit", credit);
            long debit = normalize(1250, -1);                              // -1250
            show("debit", debit);
            try { long corrupt = normalize(Long.MIN_VALUE, -1); show("corrupt", corrupt); } catch (Throwable _t) { System.out.println("corrupt -> " + _t); }
            try { long noDirection = normalize(1250, 0); show("noDirection", noDirection); } catch (Throwable _t) { System.out.println("noDirection -> " + _t); }
        }
        {
            int legacy = divideExactLegacy(-9, 3);                         // -3
            show("legacy", legacy);
            try { int legacyEx = divideExactLegacy(Integer.MIN_VALUE, -1); show("legacyEx", legacyEx); } catch (Throwable _t) { System.out.println("legacyEx -> " + _t); }
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

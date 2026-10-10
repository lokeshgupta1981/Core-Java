package com.howtodoinjava.core.basic;

import java.util.regex.*;
import java.net.*;
import java.text.*;
import java.nio.file.attribute.*;
import java.security.*;
import java.nio.charset.*;
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
 * Examples for the tutorial "Detect Integer Overflow in Java with Math Exact Methods".
 * https://howtodoinjava.com/java8/java-8-exact-airthmetic-operations-supported-in-math-class/
 */
public class IntegerOverflowDetection {
    static record CartLine(int priceCents, int quantity) {}
    static long cartTotalCents(List<CartLine> lines) {
        long total = 0;
        for (CartLine line : lines) {
            long lineTotal = Math.multiplyExact((long) line.priceCents(), line.quantity());
            total = Math.addExact(total, lineTotal);
        }
        return total;
    }
    static String checkout(List<CartLine> lines) {
        try {
            return "Total: " + cartTotalCents(lines) + " cents";
        } catch (ArithmeticException e) {
            return "Rejected: order total is too large";
        }
    }
    static boolean multiplyFitsInInt(int a, int b) {
        long wide = (long) a * b;
        return wide == (int) wide;
    }
    static boolean addFitsInLong(long a, long b) {
        return b > 0 ? a <= Long.MAX_VALUE - b : a >= Long.MIN_VALUE - b;
    }
    public static void main(String[] args) throws Exception {
        {
            int max = Integer.MAX_VALUE;                // 2147483647
            show("max", max);
            int wrapped = max + 1;                      // -2147483648, no error
            show("wrapped", wrapped);
            long widened = (long) max + 1;              // 2147483648
            show("widened", widened);
            try { int checked = Math.addExact(max, 1); show("checked", checked); } catch (Throwable _t) { System.out.println("checked -> " + _t); }
        }
        {
            int priceCents = 49_999;
            int quantity = 50_000;
            int total = priceCents * quantity;          // -1795017296, a negative order total
            show("total", total);
        }
        {
            try { int sum = Math.addExact(2_100_000_000, 100_000_000); show("sum", sum); } catch (Throwable _t) { System.out.println("sum -> " + _t); }
            long longSum = Math.addExact(2_100_000_000L, 100_000_000L);  // 2200000000
            show("longSum", longSum);
            try { int below = Math.subtractExact(Integer.MIN_VALUE, 1); show("below", below); } catch (Throwable _t) { System.out.println("below -> " + _t); }
            try { int square = Math.multiplyExact(46_341, 46_341); show("square", square); } catch (Throwable _t) { System.out.println("square -> " + _t); }
            try { int next = Math.incrementExact(Integer.MAX_VALUE); show("next", next); } catch (Throwable _t) { System.out.println("next -> " + _t); }
            try { int flipped = Math.negateExact(Integer.MIN_VALUE); show("flipped", flipped); } catch (Throwable _t) { System.out.println("flipped -> " + _t); }
            long bytes = Math.multiplyExact(3_000_000_000L, 4);          // 12000000000
            show("bytes", bytes);
            try { long huge = Math.multiplyExact(Long.MAX_VALUE, 2L); show("huge", huge); } catch (Throwable _t) { System.out.println("huge -> " + _t); }
        }
        {
            String bulk = checkout(List.of(new CartLine(49_999, 50_000)));                   // "Total: 2499950000 cents"
            show("bulk", bulk);
            String absurd = checkout(List.of(new CartLine(Integer.MAX_VALUE, Integer.MAX_VALUE),
            new CartLine(Integer.MAX_VALUE, Integer.MAX_VALUE),
            new CartLine(Integer.MAX_VALUE, Integer.MAX_VALUE)));
        }
        {
            long fileSize = 3_000_000_000L;
            int truncated = (int) fileSize;                         // -1294967296
            show("truncated", truncated);
            try { int exact = Math.toIntExact(fileSize); show("exact", exact); } catch (Throwable _t) { System.out.println("exact -> " + _t); }
            int clamped = Math.clamp(fileSize, 0, Integer.MAX_VALUE);  // 2147483647
            show("clamped", clamped);
        }
        {
            int total = 0;
            total += 3_000_000_000L;                                // compiles, adds a long to an int
            int afterAdd = total;                                   // -1294967296
            show("afterAdd", afterAdd);

            byte level = 127;
            level++;                                                // wraps, like level = (byte) (level + 1)
            byte afterIncrement = level;                            // -128
            show("afterIncrement", afterIncrement);
        }
        {
            int quotient = Integer.MIN_VALUE / -1;                  // -2147483648
            show("quotient", quotient);
            int absolute = Math.abs(Integer.MIN_VALUE);             // -2147483648
            show("absolute", absolute);
            try { int safeQuotient = Math.divideExact(Integer.MIN_VALUE, -1); show("safeQuotient", safeQuotient); } catch (Throwable _t) { System.out.println("safeQuotient -> " + _t); }
            try { int safeAbsolute = Math.absExact(Integer.MIN_VALUE); show("safeAbsolute", safeAbsolute); } catch (Throwable _t) { System.out.println("safeAbsolute -> " + _t); }
        }
        {
            int hash = "polygenelubricants".hashCode();            // -2147483648
            show("hash", hash);
            int brokenIndex = Math.abs(hash) % 10;                  // -8
            show("brokenIndex", brokenIndex);
            int safeIndex = Math.floorMod(hash, 10);                // 2
            show("safeIndex", safeIndex);
        }
        {
            boolean fits = multiplyFitsInInt(46_340, 46_340);             // true
            show("fits", fits);
            boolean tooBig = multiplyFitsInInt(46_341, 46_341);           // false
            show("tooBig", tooBig);
            boolean longFits = addFitsInLong(Long.MAX_VALUE - 5, 5);       // true
            show("longFits", longFits);
            boolean longTooBig = addFitsInLong(Long.MAX_VALUE - 5, 6);     // false
            show("longTooBig", longTooBig);
        }
        {
            int low = 1_500_000_000;
            int high = 2_000_000_000;
            int brokenMid = (low + high) / 2;                       // -397483648
            show("brokenMid", brokenMid);
            int safeMid = low + (high - low) / 2;                   // 1750000000
            show("safeMid", safeMid);
            int shiftMid = (low + high) >>> 1;                      // 1750000000
            show("shiftMid", shiftMid);
        }
        {
            int days = 25;
            int brokenMillis = days * 24 * 60 * 60 * 1000;          // -2134967296
            show("brokenMillis", brokenMillis);
            long millis = days * 24L * 60 * 60 * 1000;              // 2160000000
            show("millis", millis);
            long fromDuration = Duration.ofDays(days).toMillis();   // 2160000000
            show("fromDuration", fromDuration);
        }
        {
            BigInteger factorial = BigInteger.ONE;
            for (int i = 2; i <= 25; i++) {
                factorial = factorial.multiply(BigInteger.valueOf(i));
            }
            String value = factorial.toString();                    // "15511210043330985984000000"
            show("value", value);
            try { long asLong = factorial.longValueExact(); show("asLong", asLong); } catch (Throwable _t) { System.out.println("asLong -> " + _t); }
        }
        {
            double tooLarge = Double.MAX_VALUE * 2;                 // Infinity
            show("tooLarge", tooLarge);
            boolean finite = Double.isFinite(tooLarge);             // false
            show("finite", finite);
            double tooSmall = Double.MIN_VALUE / 2;                 // 0.0
            show("tooSmall", tooSmall);
            double growth = Math.exp(1000);                         // Infinity
            show("growth", growth);
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

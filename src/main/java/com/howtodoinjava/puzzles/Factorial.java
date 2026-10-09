package com.howtodoinjava.puzzles;

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
 * Examples for the tutorial "Factorial Program in Java: Loop, Recursion, Streams, BigInteger".
 * https://howtodoinjava.com/java/puzzles/calculate-factorial-in-java/
 */
public class Factorial {
    static long factorial(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("n must not be negative: " + n);
        }
        long result = 1;
        for (int i = 2; i <= n; i++) {
            result = Math.multiplyExact(result, i);
        }
        return result;
    }
    static long factorialRecursive(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("n must not be negative: " + n);
        }
        if (n == 0) {
            return 1;                       // base case
        }
        return Math.multiplyExact(n, factorialRecursive(n - 1));
    }
    static long oldRecursive(long n) {
        return n == 1 ? 1 : n * oldRecursive(n - 1);
    }
    static long factorialStream(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("n must not be negative: " + n);
        }
        return LongStream.rangeClosed(1, n).reduce(1, Math::multiplyExact);
    }
    static BigInteger bigFactorial(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("n must not be negative: " + n);
        }
        BigInteger result = BigInteger.ONE;
        for (int i = 2; i <= n; i++) {
            result = result.multiply(BigInteger.valueOf(i));
        }
        return result;
    }
    static final class FactorialTable {
        private static final long[] VALUES = new long[21];

        static {
            VALUES[0] = 1;
            for (int i = 1; i < VALUES.length; i++) {
                VALUES[i] = VALUES[i - 1] * i;
            }
        }

        static long of(int n) {
            if (n < 0 || n >= VALUES.length) {
                throw new IllegalArgumentException("n must be 0 to 20: " + n);
            }
            return VALUES[n];
        }
    }
    static long choose(int n, int k) {
        long result = 1;
        for (int i = 1; i <= k; i++) {
            result = result * (n - k + i) / i;   // always an exact division
        }
        return result;
    }
    public static void main(String[] args) throws Exception {
        {
            long five = factorial(5);          // 120
            show("five", five);
            long zero = factorial(0);          // 1
            show("zero", zero);
            long twenty = factorial(20);       // 2432902008176640000
            show("twenty", twenty);
            try { long tooBig = factorial(21); show("tooBig", tooBig); } catch (Throwable _t) { System.out.println("tooBig -> " + _t); }
        }
        {
            long wrapped = 2432902008176640000L * 21;   // -4249290049419214848
            show("wrapped", wrapped);
            int intWrapped = 479001600 * 13;            // 1932053504, the real 13! is 6227020800
            show("intWrapped", intWrapped);
        }
        {
            long one = factorial(1);           // 1
            show("one", one);
            long ten = factorial(10);          // 3628800
            show("ten", ten);
            try { long negative = factorial(-3); show("negative", negative); } catch (Throwable _t) { System.out.println("negative -> " + _t); }
        }
        {
            long rec4 = factorialRecursive(4);      // 24
            show("rec4", rec4);
            long rec0 = factorialRecursive(0);      // 1
            show("rec0", rec0);
        }
        {
            try { long crash = oldRecursive(0); show("crash", crash); } catch (Throwable _t) { System.out.println("crash -> " + _t); }
        }
        {
            long stream6 = factorialStream(6);      // 720
            show("stream6", stream6);
            long stream0 = factorialStream(0);      // 1
            show("stream0", stream0);
        }
        {
            BigInteger f25 = bigFactorial(25);                  // 15511210043330985984000000
            show("f25", f25);
            int digits100 = bigFactorial(100).toString().length();   // 158
            show("digits100", digits100);
            int digits1000 = bigFactorial(1000).toString().length(); // 2568
            show("digits1000", digits1000);
            BigInteger viaStream = LongStream.rangeClosed(1, 25).mapToObj(BigInteger::valueOf).reduce(BigInteger.ONE, BigInteger::multiply);   // 15511210043330985984000000
            show("viaStream", viaStream);
        }
        {
            long table15 = FactorialTable.of(15);   // 1307674368000
            show("table15", table15);
            try { long table21 = FactorialTable.of(21); show("table21", table21); } catch (Throwable _t) { System.out.println("table21 -> " + _t); }
        }
        {
            long playlists = factorial(10);         // 3628800
            show("playlists", playlists);
            long lottery = choose(49, 6);           // 13983816
            show("lottery", lottery);
            try { long naive = factorial(49); show("naive", naive); } catch (Throwable _t) { System.out.println("naive -> " + _t); }
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

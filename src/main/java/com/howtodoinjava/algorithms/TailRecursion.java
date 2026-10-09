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
 * Examples for the tutorial "Tail Recursion vs Non-Tail Recursion in Java".
 * https://howtodoinjava.com/algorithm/tail-vs-non-tail-recursion/
 */
public class TailRecursion {
    static long sum(int n) {
        return n == 0 ? 0 : n + sum(n - 1);           // non-tail: adds after the call
    }

    static long sumTail(int n, long acc) {
        return n == 0 ? acc : sumTail(n - 1, acc + n); // tail: the call is the last action
    }
    static void countDown(int n, List<Integer> out) {
        if (n == 0) {
            return;
        }
        out.add(n);
        countDown(n - 1, out);              // tail call, nothing runs after it
    }

    static void countUp(int n, List<Integer> out) {
        if (n == 0) {
            return;
        }
        countUp(n - 1, out);                // not a tail call
        out.add(n);                         // runs while the frames return
    }
    static long factorialTail(int n, long acc) {
        if (n < 0) {
            throw new IllegalArgumentException("n must not be negative");
        }
        return n <= 1 ? acc : factorialTail(n - 1, acc * n);
    }
    static long fibTail(int n, long previous, long current) {
        return n == 0 ? previous : fibTail(n - 1, current, previous + current);
    }
    static long sumLoop(int n) {
        long acc = 0;
        while (n != 0) {                    // base case as the loop condition
            acc = acc + n;                  // same as sumTail(n - 1, acc + n)
            n = n - 1;
        }
        return acc;
    }
    @FunctionalInterface
    static interface TailCall<T> {
        TailCall<T> next();

        default boolean isDone() {
            return false;
        }

        default T result() {
            throw new UnsupportedOperationException("not done yet");
        }

        default T invoke() {
            return Stream.iterate(this, TailCall::next)
                    .filter(TailCall::isDone)
                    .findFirst()
                    .orElseThrow()
                    .result();
        }
    }
    static <T> TailCall<T> done(T value) {
        return new TailCall<>() {
            public TailCall<T> next() {
                throw new UnsupportedOperationException("already done");
            }

            public boolean isDone() {
                return true;
            }

            public T result() {
                return value;
            }
        };
    }

    static TailCall<Long> sumTrampoline(int n, long acc) {
        return n == 0 ? done(acc) : () -> sumTrampoline(n - 1, acc + n);
    }
    public static void main(String[] args) throws Exception {
        {
            long a = sum(1_000);                    // 500500
            show("a", a);
            long b = sumTail(1_000, 0);             // 500500
            show("b", b);
            try { long c = sum(100_000); show("c", c); } catch (Throwable _t) { System.out.println("c -> " + _t); }
            try { long d = sumTail(100_000, 0); show("d", d); } catch (Throwable _t) { System.out.println("d -> " + _t); }
        }
        {
            List<Integer> down = new ArrayList<>();
            countDown(3, down);
            List<Integer> descending = down;        // [3, 2, 1]
            show("descending", descending);
            List<Integer> up = new ArrayList<>();
            countUp(3, up);
            List<Integer> ascending = up;           // [1, 2, 3]
            show("ascending", ascending);
        }
        {
            long f5 = factorialTail(5, 1);          // 120
            show("f5", f5);
            long f20 = factorialTail(20, 1);        // 2432902008176640000
            show("f20", f20);
        }
        {
            long fib10 = fibTail(10, 0, 1);         // 55
            show("fib10", fib10);
            long fib50 = fibTail(50, 0, 1);         // 12586269025
            show("fib50", fib50);
        }
        {
            long big = sumLoop(100_000);            // 5000050000
            show("big", big);
        }
        {
            long million = sumTrampoline(1_000_000, 0).invoke();    // 500000500000
            show("million", million);
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

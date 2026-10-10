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
 * Examples for the tutorial "Deficient Number in Java: Check, Deficiency and Examples".
 * https://howtodoinjava.com/java-examples/check-deficient-number/
 */
public class DeficientPerfectAbundantNumbers {
    static boolean isDeficientSimple(int n) {
        if (n < 1) {
            throw new IllegalArgumentException("Deficient numbers are positive: " + n);
        }
        int sum = 0;
        for (int d = 1; d <= n / 2; d++) {
            if (n % d == 0) {
                sum += d;
            }
        }
        return sum < n;
    }
    static long properDivisorSum(long n) {
        if (n < 1) {
            throw new IllegalArgumentException("Expected a positive number: " + n);
        }
        if (n == 1) {
            return 0;
        }
        long sum = 1;                          // 1 divides every n > 1
        for (long i = 2; i * i <= n; i++) {
            if (n % i == 0) {
                long pair = n / i;
                sum += (pair == i) ? i : i + pair;
            }
        }
        return sum;
    }
    static boolean isDeficient(long n) {
        return properDivisorSum(n) < n;
    }
    static enum NumberClass { DEFICIENT, PERFECT, ABUNDANT }
    static NumberClass classify(long n) {
        int cmp = Long.compare(properDivisorSum(n), n);
        return cmp < 0 ? NumberClass.DEFICIENT
        : cmp == 0 ? NumberClass.PERFECT
        : NumberClass.ABUNDANT;
    }
    static int[] aliquotSums(int limit) {
        int[] sums = new int[limit + 1];
        for (int d = 1; d <= limit / 2; d++) {
            for (int m = 2 * d; m <= limit; m += d) {
                sums[m] += d;
            }
        }
        return sums;
    }
    static String factsCard(String input) {
        long n;
        try {
            n = Long.parseLong(input.strip());
        } catch (NumberFormatException e) {
            return "Please type a whole number";
        }
        if (n < 1) {
            return "Please type a number greater than 0";
        }
        long sum = properDivisorSum(n);
        return switch (classify(n)) {
            case DEFICIENT -> n + " is deficient by " + (n - sum);
            case PERFECT -> n + " is perfect";
            case ABUNDANT -> n + " is abundant by " + (sum - n);
        };
    }
    public static void main(String[] args) throws Exception {
        {
            int n = 21;
            int divisorSum = IntStream.rangeClosed(1, n / 2).filter(d -> n % d == 0).sum();   // 11
            show("divisorSum", divisorSum);
            boolean deficient = divisorSum < n;                                                // true
            show("deficient", deficient);
            int deficiency = n - divisorSum;                                                   // 10
            show("deficiency", deficiency);
        }
        {
            boolean a = isDeficientSimple(21);    // true
            show("a", a);
            boolean b = isDeficientSimple(20);    // false
            show("b", b);
            boolean c = isDeficientSimple(1);     // true
            show("c", c);
            try { boolean d = isDeficientSimple(0); show("d", d); } catch (Throwable _t) { System.out.println("d -> " + _t); }
        }
        {
            long s21 = properDivisorSum(21);                     // 11
            show("s21", s21);
            long s100 = properDivisorSum(100);                   // 117
            show("s100", s100);
            boolean big = isDeficient(1_000_000_000_000L);       // false
            show("big", big);
            boolean bigPrime = isDeficient(999_999_999_989L);    // true
            show("bigPrime", bigPrime);
        }
        {
            NumberClass k21 = classify(21);      // DEFICIENT
            show("k21", k21);
            NumberClass k28 = classify(28);      // PERFECT
            show("k28", k28);
            NumberClass k12 = classify(12);      // ABUNDANT
            show("k12", k12);
            NumberClass k945 = classify(945);    // ABUNDANT
            show("k945", k945);
        }
        {
            int[] sums = aliquotSums(20_000);
            long deficientCount = IntStream.rangeClosed(1, 20_000).filter(i -> sums[i] < i).count();    // 15043
            show("deficientCount", deficientCount);
            long perfectCount = IntStream.rangeClosed(1, 20_000).filter(i -> sums[i] == i).count();     // 4
            show("perfectCount", perfectCount);
            long abundantCount = IntStream.rangeClosed(1, 20_000).filter(i -> sums[i] > i).count();     // 4953
            show("abundantCount", abundantCount);
        }
        {
            String card1 = factsCard(" 21 ");      // "21 is deficient by 10"
            show("card1", card1);
            String card2 = factsCard("496");       // "496 is perfect"
            show("card2", card2);
            String card3 = factsCard("12");        // "12 is abundant by 4"
            show("card3", card3);
            String card4 = factsCard("abc");       // "Please type a whole number"
            show("card4", card4);
            String card5 = factsCard("-5");        // "Please type a number greater than 0"
            show("card5", card5);
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

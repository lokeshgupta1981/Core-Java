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
 * Examples for the tutorial "Happy Number in Java: HashSet and Floyd Cycle Detection".
 * https://howtodoinjava.com/java-examples/happy-unhappy-numbers/
 */
public class HappyNumberUnhappyCycle {
    static int digitSquareSum(int n) {
        int sum = 0;
        while (n > 0) {
            int digit = n % 10;
            sum += digit * digit;
            n /= 10;
        }
        return sum;
    }
    static boolean isHappy(int number) {
        if (number < 1) {
            return false;
        }
        Set<Integer> seen = new HashSet<>();
        int n = number;
        while (n != 1 && seen.add(n)) {
            n = digitSquareSum(n);
        }
        return n == 1;
    }
    static boolean isHappyFloyd(int number) {
        if (number < 1) {
            return false;
        }
        int slow = number;
        int fast = digitSquareSum(number);
        while (fast != 1 && slow != fast) {
            slow = digitSquareSum(slow);
            fast = digitSquareSum(digitSquareSum(fast));
        }
        return fast == 1;
    }
    static boolean isHappyShortcut(int number) {
        int n = number;
        while (n > 1 && n != 4) {
            n = digitSquareSum(n);
        }
        return n == 1;
    }
    static Optional<String> resolve(Map<String, String> redirects, String path) {
        Set<String> visited = new HashSet<>();
        String current = path;
        while (redirects.containsKey(current)) {
            if (!visited.add(current)) {
                return Optional.empty();           // loop found
            }
            current = redirects.get(current);
        }
        return Optional.of(current);
    }
    public static void main(String[] args) throws Exception {
        {
            int step1 = 1 * 1 + 9 * 9;       // 82
            show("step1", step1);
            int step2 = 8 * 8 + 2 * 2;       // 68
            show("step2", step2);
            int step3 = 6 * 6 + 8 * 8;       // 100
            show("step3", step3);
            int step4 = 1 * 1 + 0 * 0 + 0 * 0;   // 1
            show("step4", step4);
        }
        {
            boolean h19 = isHappy(19);      // true
            show("h19", h19);
            boolean h7 = isHappy(7);        // true
            show("h7", h7);
            boolean h28 = isHappy(28);      // true
            show("h28", h28);
            boolean h20 = isHappy(20);      // false
            show("h20", h20);
            boolean h0 = isHappy(0);        // false
            show("h0", h0);
        }
        {
            boolean f19 = isHappyFloyd(19);    // true
            show("f19", f19);
            boolean f20 = isHappyFloyd(20);    // false
            show("f20", f20);
            boolean f1 = isHappyFloyd(1);      // true
            show("f1", f1);
        }
        {
            boolean s97 = isHappyShortcut(97);   // true
            show("s97", s97);
            boolean s58 = isHappyShortcut(58);   // false
            show("s58", s58);
            boolean s0 = isHappyShortcut(0);     // false
            show("s0", s0);
        }
        {
            List<Integer> upTo50 = IntStream.rangeClosed(1, 50).filter(n -> isHappy(n)).boxed().toList();   // [1, 7, 10, 13, 19, 23, 28, 31, 32, 44, 49]
            show("upTo50", upTo50);
            long countTo100 = IntStream.rangeClosed(1, 100).filter(n -> isHappyShortcut(n)).count();   // 20
            show("countTo100", countTo100);
            List<Integer> happyPrimes = IntStream.rangeClosed(2, 100).filter(n -> isHappy(n) && BigInteger.valueOf(n).isProbablePrime(20)).boxed().toList();   // [7, 13, 19, 23, 31, 79, 97]
            show("happyPrimes", happyPrimes);
        }
        {
            Map<String, String> redirects = Map.of("/sale", "/offers", "/offers", "/deals", "/deals", "/offers");
            Optional<String> loop = resolve(redirects, "/sale");                        // Optional.empty
            show("loop", loop);
            Optional<String> fine = resolve(Map.of("/sale", "/offers"), "/sale");     // Optional[/offers]
            show("fine", fine);
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

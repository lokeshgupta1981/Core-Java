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
import java.security.SecureRandom;

/**
 * Examples for the tutorial "Prime Numbers From 2 to N in Java (Sieve of Eratosthenes)".
 * https://howtodoinjava.com/java-examples/find-prime-numbers-2-to-n/
 */
public class SieveOfEratosthenesJava {
    static List<Integer> primesUpTo(int n) {
        if (n < 2) {
            return List.of();
        }
        boolean[] composite = new boolean[n + 1];
        List<Integer> primes = new ArrayList<>();
        for (int i = 2; i <= n; i++) {
            if (!composite[i]) {
                primes.add(i);
                for (long multiple = (long) i * i; multiple <= n; multiple += i) {
                    composite[(int) multiple] = true;
                }
            }
        }
        return primes;
    }
    static boolean isPrime(int n) {
        if (n < 2) {
            return false;                          // 0, 1 and negative numbers
        }
        if (n % 2 == 0) {
            return n == 2;
        }
        for (int d = 3; d <= n / d; d += 2) {
            if (n % d == 0) {
                return false;
            }
        }
        return true;
    }
    static boolean isPrime6k(long n) {
        if (n < 4) {
            return n >= 2;                         // 2 and 3
        }
        if (n % 2 == 0 || n % 3 == 0) {
            return false;
        }
        for (long d = 5; d <= n / d; d += 6) {
            if (n % d == 0 || n % (d + 2) == 0) {
                return false;
            }
        }
        return true;
    }
    static BitSet sieve(int n) {
        BitSet primes = new BitSet(n + 1);
        primes.set(2, n + 1);                      // every number is a candidate
        for (int i = 2; (long) i * i <= n; i = primes.nextSetBit(i + 1)) {
            for (int multiple = i * i; multiple <= n && multiple > 0; multiple += i) {
                primes.clear(multiple);
            }
        }
        return primes;
    }
    static List<Integer> firstPrimes(int count) {
        if (count < 6) {
            return primesUpTo(11).subList(0, count);
        }
        double ln = Math.log(count);
        int limit = (int) (count * (ln + Math.log(ln))) + 1;
        return primesUpTo(limit).subList(0, count);
    }
    static int nextPrime(int capacity) {
        int candidate = Math.max(capacity, 2);
        while (!isPrime(candidate)) {
            candidate++;
        }
        return candidate;
    }
    public static void main(String[] args) throws Exception {
        {
            List<Integer> primes = primesUpTo(30);         // [2, 3, 5, 7, 11, 13, 17, 19, 23, 29]
            show("primes", primes);
            List<Integer> none = primesUpTo(1);            // []
            show("none", none);
        }
        {
            boolean two = isPrime(2);                      // true
            show("two", two);
            boolean one = isPrime(1);                      // false
            show("one", one);
            boolean ninetySeven = isPrime(97);             // true
            show("ninetySeven", ninetySeven);
            boolean ninetyOne = isPrime(91);               // false, 7 * 13
            show("ninetyOne", ninetyOne);
            boolean maxInt = isPrime(Integer.MAX_VALUE);   // true
            show("maxInt", maxInt);
        }
        {
            boolean billionAndSeven = isPrime6k(1_000_000_007L);     // true
            show("billionAndSeven", billionAndSeven);
            boolean composite = isPrime6k(600_851_475_143L);         // false
            show("composite", composite);
        }
        {
            List<Integer> upTo50 = IntStream.rangeClosed(2, 50)
                    .filter(n -> isPrime(n))
                    .boxed()
                    .toList();
            String result = upTo50.toString();             // "[2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47]"
            show("result", result);
        }
        {
            int below1000 = primesUpTo(1000).size();              // 168
            show("below1000", below1000);
            int belowMillion = primesUpTo(1_000_000).size();      // 78498
            show("belowMillion", belowMillion);
            int largest = primesUpTo(1_000_000).getLast();        // 999983
            show("largest", largest);
        }
        {
            int count = sieve(1_000_000).cardinality();             // 78498
            show("count", count);
            String small = sieve(30).toString();                      // "{2, 3, 5, 7, 11, 13, 17, 19, 23, 29}"
            show("small", small);
        }
        {
            List<Integer> firstTen = IntStream.iterate(2, i -> i + 1)
                    .filter(i -> isPrime(i))
                    .limit(10)
                    .boxed()
                    .toList();
            String ten = firstTen.toString();              // "[2, 3, 5, 7, 11, 13, 17, 19, 23, 29]"
            show("ten", ten);
        }
        {
            List<Integer> firstFive = firstPrimes(5);                     // [2, 3, 5, 7, 11]
            show("firstFive", firstFive);
            int thousandth = firstPrimes(1000).getLast();                  // 7919
            show("thousandth", thousandth);
            int tenThousandth = firstPrimes(10_000).getLast();             // 104729
            show("tenThousandth", tenThousandth);
        }
        {
            BigInteger mersenne = BigInteger.TWO.pow(61).subtract(BigInteger.ONE);   // 2305843009213693951
            show("mersenne", mersenne);
            boolean isPrime = mersenne.isProbablePrime(50);                           // true
            show("isPrime", isPrime);
            BigInteger next = BigInteger.valueOf(1_000_000).nextProbablePrime();     // 1000003
            show("next", next);
        }
        {
            BigInteger keyPrime = BigInteger.probablePrime(1024, new SecureRandom());
            int bits = keyPrime.bitLength();                                         // 1024
            show("bits", bits);
        }
        {
            int buckets = nextPrime(1000);                 // 1009
            show("buckets", buckets);
            int small = nextPrime(1);                      // 2
            show("small", small);
        }
        {
            List<Integer> upTo100 = primesUpTo(100);
            int howMany = upTo100.size();                  // 25
            show("howMany", howMany);
            List<Integer> lastFour = upTo100.subList(21, 25);                // [79, 83, 89, 97]
            show("lastFour", lastFour);
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

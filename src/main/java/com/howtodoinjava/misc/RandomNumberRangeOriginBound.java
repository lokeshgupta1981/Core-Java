package com.howtodoinjava.misc;

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
import java.util.random.*;
import java.security.*;

/**
 * Examples for the tutorial "Generate a Random Number in a Range in Java (Min to Max)".
 * https://howtodoinjava.com/java/generate-random-numbers-in-range/
 */
public class RandomNumberRangeOriginBound {
    static int randomBetween(RandomGenerator generator, int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("min " + min + " is greater than max " + max);
        }
        return (int) generator.nextLong(min, (long) max + 1);
    }
    static int randomExcluding(RandomGenerator generator, int min, int max, int excluded) {
        int value = generator.nextInt(min, max);
        return value >= excluded ? value + 1 : value;
    }
    public static void main(String[] args) throws Exception {
        {
            int dice = ThreadLocalRandom.current().nextInt(1, 7);                 // 1 to 6
            show("dice", dice);
            int percent = RandomGenerator.getDefault().nextInt(0, 101);           // 0 to 100
            show("percent", percent);
            double price = ThreadLocalRandom.current().nextDouble(5.0, 10.0);     // 5.0 to just under 10.0
            show("price", price);
            long orderNo = RandomGenerator.getDefault().nextLong(1_000L, 10_000L);  // 1000 to 9999
            show("orderNo", orderNo);
            int java8Dice = 1 + new Random().nextInt(6);                           // 1 to 6, works on Java 8
            show("java8Dice", java8Dice);
        }
        {
            int roll = randomBetween(ThreadLocalRandom.current(), 1, 6);                 // 1 to 6
            show("roll", roll);
            int anyPositive = randomBetween(ThreadLocalRandom.current(), 0, Integer.MAX_VALUE);  // 0 to 2147483647
            show("anyPositive", anyPositive);
            try { int overflow = ThreadLocalRandom.current().nextInt(0, Integer.MAX_VALUE + 1); show("overflow", overflow); } catch (Throwable _t) { System.out.println("overflow -> " + _t); }
            try { int reversed = ThreadLocalRandom.current().nextInt(10, 5); show("reversed", reversed); } catch (Throwable _t) { System.out.println("reversed -> " + _t); }
        }
        {
            Random random = new Random();
            int fromRandom = random.nextInt(180 - 60 + 1) + 60;          // 60 to 180
            show("fromRandom", fromRandom);
            int fromMath = 60 + (int) (Math.random() * (180 - 60 + 1));  // 60 to 180
            show("fromMath", fromMath);
            int wrongFormula = 60 + (int) (Math.random() * 180);         // 60 to 239, a common bug
            show("wrongFormula", wrongFormula);
        }
        {
            RandomGenerator generator = RandomGenerator.getDefault();
            double temperature = generator.nextDouble(-5.0, 35.0);       // -5.0 to just under 35.0
            show("temperature", temperature);
            float discount = generator.nextFloat(0.05f, 0.30f);          // 0.05 to just under 0.30
            show("discount", discount);
            long fileSize = generator.nextLong(1L, 10_000_000_000L);     // 1 to 9999999999
            show("fileSize", fileSize);
            int offset = generator.nextInt(-10, 11);                     // -10 to 10
            show("offset", offset);
        }
        {
            long cents = ThreadLocalRandom.current().nextLong(100, 10_000);    // 100 to 9999
            show("cents", cents);
            BigDecimal price = BigDecimal.valueOf(cents, 2);                    // 1.00 to 99.99
            show("price", price);
        }
        {
            int[] rolls = new Random(42).ints(5, 1, 7).toArray();              // [3, 4, 1, 3, 1]
            show("rolls", rolls);
            double[] weights = new Random(42).doubles(3, 0.5, 2.0).toArray();    // 3 values from 0.5 to just under 2.0
            show("weights", weights);
        }
        {
            List<Integer> questionIds = IntStream.rangeClosed(1, 40)
                    .boxed()
                    .collect(Collectors.toCollection(ArrayList::new));
            Collections.shuffle(questionIds, new Random(3));
            List<Integer> quiz = List.copyOf(questionIds.subList(0, 5));         // [4, 36, 22, 18, 6]
            show("quiz", quiz);
        }
        {
            int nextTip = randomExcluding(ThreadLocalRandom.current(), 1, 6, 3);     // 1 to 6, never 3
            show("nextTip", nextTip);
            boolean neverThree = IntStream.range(0, 10_000)
                    .map(i -> randomExcluding(ThreadLocalRandom.current(), 1, 6, 3))
                    .noneMatch(v -> v == 3);
            boolean checked = neverThree;                                             // true
            show("checked", checked);
        }
        {
            SecureRandom secure = new SecureRandom();
            int code = secure.nextInt(100_000, 1_000_000);                // 100000 to 999999
            show("code", code);
            String padded = String.format("%06d", secure.nextInt(1_000_000));  // 000000 to 999999
            show("padded", padded);
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

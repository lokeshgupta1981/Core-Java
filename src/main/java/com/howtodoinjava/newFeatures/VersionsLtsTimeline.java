package com.howtodoinjava.newFeatures;

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
import java.util.random.RandomGenerator;
import java.util.random.RandomGeneratorFactory;

/**
 * Examples for the tutorial "Java New Features by Version: Java 8 to Java 27".
 * https://howtodoinjava.com/java/new-features/
 */
public class VersionsLtsTimeline {
    static record Dish(String name, int price) {}                         // record, Java 16
    static sealed interface Order permits DineIn, Takeaway {}             // sealed, Java 17
    static record DineIn(int table) implements Order {}
    static record Takeaway(String customer) implements Order {}
    static class Item {
        final String name;

        Item(String name) {
            this.name = name;
        }
    }
    public static void main(String[] args) throws Exception {
        {
            Object value = 42;
            String described = value instanceof Integer i ? "int " + i : "other";   // "int 42"
            show("described", described);
        }
        {
            RandomGenerator dice = RandomGeneratorFactory.of("L64X128MixRandom").create(42);
            int roll = dice.nextInt(1, 7);                                     // 1
            show("roll", roll);
            String algorithm = RandomGeneratorFactory.getDefault().name();    // "L32X64MixRandom"
            show("algorithm", algorithm);
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

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
import static java.lang.Integer.*;
import static java.lang.Long.MAX_VALUE;
import static java.lang.Math.PI;
import static java.lang.Math.max;
import static java.lang.String.format;
import static java.util.Comparator.comparing;
import static java.util.Map.entry;
import static java.util.stream.Collectors.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.stream.*;
import java.util.function.*;
import java.io.*;
import java.nio.file.*;
import java.nio.charset.*;
import java.time.*;
import java.math.*;
import org.junit.jupiter.api.Test;

/**
 * Examples for the tutorial "Static Import in Java: Syntax, Rules and When to Use It".
 * https://howtodoinjava.com/java/basics/static-import-declarations-in-java/
 */
public class StaticImportNameResolution {
    static String format(Object value) {
        return "[" + value + "]";
    }
    @Test
    void largerScoreWins() {
        assertEquals(7, max(3, 7));
        assertThrows(NumberFormatException.class, () -> Integer.parseInt("seven"));
    }
    public static void main(String[] args) throws Exception {
        {
            int larger = max(3, 7);                 // 7
            show("larger", larger);
            double circle = PI * 2;                 // 6.283185307179586
            show("circle", circle);
        }
        {
            Map.Entry<String, Integer> apples = entry("apple", 5);              // apple=5
            show("apples", apples);
            String joined = Stream.of("red", "blue").collect(joining(", "));     // "red, blue"
            show("joined", joined);
            Map<Integer, Long> byLength = Stream.of("fig", "kiwi", "pear").collect(groupingBy(String::length, counting()));   // {3=1, 4=2}
            show("byLength", byLength);
            List<String> sorted = Stream.of("kiwi", "fig", "banana").sorted(comparing(String::length)).toList();   // [fig, kiwi, banana]
            show("sorted", sorted);
        }
        {
            long limit = MAX_VALUE;                 // 9223372036854775807, from Long
            show("limit", limit);
            int smallest = MIN_VALUE;               // -2147483648, from Integer
            show("smallest", smallest);
        }
        {
            String label = format(5);               // "[5]", the class's own method
            show("label", label);
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

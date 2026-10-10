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

/**
 * Examples for the tutorial "Java Tuple: Records, javatuples Library and Examples".
 * https://howtodoinjava.com/java/java-tuples/
 */
public class TupleVsRecord {
    static record Tuple<A, B>(A first, B second) {}
    static record PriceRange(double min, double max) {}
    static PriceRange priceRange(List<Double> prices) {
        DoubleSummaryStatistics stats = prices.stream()
                .mapToDouble(Double::doubleValue)
                .summaryStatistics();
        return new PriceRange(stats.getMin(), stats.getMax());
    }
    public static void main(String[] args) throws Exception {
        {
            Tuple<Integer, String> fruit = new Tuple<>(1, "apple");      // Tuple[first=1, second=apple]
            show("fruit", fruit);
            Integer number = fruit.first();                                 // 1
            show("number", number);
            String label = fruit.second();                                  // "apple"
            show("label", label);
            boolean same = fruit.equals(new Tuple<>(1, "apple"));           // true
            show("same", same);
        }
        {
            PriceRange range = priceRange(List.of(4.5, 2.0, 9.25));      // PriceRange[min=2.0, max=9.25]
            show("range", range);
            double lowest = range.min();                                    // 2.0
            show("lowest", lowest);
            double highest = range.max();                                   // 9.25
            show("highest", highest);
        }
        {
            Map.Entry<String, Integer> entry = Map.entry("Sajal", 12);    // Sajal=12
            show("entry", entry);
            String key = entry.getKey();                                    // "Sajal"
            show("key", key);
            try { Map.Entry<String, Integer> bad = Map.entry("Sajal", null); show("bad", bad); } catch (Throwable _t) { System.out.println("bad -> " + _t); }
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

package com.howtodoinjava.core.array;

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
 * Examples for the tutorial "Find Max and Min in an Array in Java (Loop, Stream, Objects)".
 * https://howtodoinjava.com/java/array/find-max-min-arrays/
 */
public class FindMaxMin {
    static record Range(int min, int max) {}
    static Range minMax(int[] values) {
        if (values == null || values.length == 0) {
            throw new IllegalArgumentException("values must not be empty");
        }
        int min = values[0];
        int max = values[0];
        for (int i = 1; i < values.length; i++) {
            if (values[i] > max) {
                max = values[i];
            } else if (values[i] < min) {
                min = values[i];
            }
        }
        return new Range(min, max);
    }
    static record Reading(String city, double celsius) {}
    public static void main(String[] args) throws Exception {
        {
            int[] temps = {18, 24, 9, 31, 15};
            int max = Arrays.stream(temps).max().orElseThrow();              // 31
            show("max", max);
            int min = Arrays.stream(temps).min().orElseThrow();              // 9
            show("min", min);
            IntSummaryStatistics stats = Arrays.stream(temps).summaryStatistics();
            int high = stats.getMax();                                       // 31
            show("high", high);
            int low = stats.getMin();                                        // 9
            show("low", low);
        }
        {
            int[] temps = {18, 24, 9, 31, 15};
            Range today = minMax(temps);            // Range[min=9, max=31]
            show("today", today);
            Range single = minMax(new int[]{7});    // Range[min=7, max=7]
            show("single", single);
            try { Range none = minMax(new int[0]); show("none", none); } catch (Throwable _t) { System.out.println("none -> " + _t); }
        }
        {
            int[] noReadings = {};
            OptionalInt maybeMax = Arrays.stream(noReadings).max();   // OptionalInt.empty
            show("maybeMax", maybeMax);
            int fallback = Arrays.stream(noReadings).max().orElse(0); // 0
            show("fallback", fallback);
            try { int boom = Arrays.stream(noReadings).max().getAsInt(); show("boom", boom); } catch (Throwable _t) { System.out.println("boom -> " + _t); }
        }
        {
            IntSummaryStatistics empty = Arrays.stream(new int[0]).summaryStatistics();
            int emptyMin = empty.getMin();          // 2147483647
            show("emptyMin", emptyMin);
            int emptyMax = empty.getMax();          // -2147483648
            show("emptyMax", emptyMax);
            long count = empty.getCount();          // 0
            show("count", count);
        }
        {
            Reading[] readings = {new Reading("Oslo", 4.5), new Reading("Rome", 21.0), new Reading("Pune", 33.5)};
            Reading hottest = Arrays.stream(readings).max(Comparator.comparingDouble(Reading::celsius)).orElseThrow();   // Reading[city=Pune, celsius=33.5]
            show("hottest", hottest);
            Reading coldest = Collections.min(Arrays.asList(readings), Comparator.comparingDouble(Reading::celsius));     // Reading[city=Oslo, celsius=4.5]
            show("coldest", coldest);
        }
        {
            Integer[] boxed = {18, 24, 9, 31, 15};
            Integer top = Collections.max(Arrays.asList(boxed));                 // 31
            show("top", top);
            String[] cities = {"Rome", "Oslo", "Pune"};
            String first = Collections.min(Arrays.asList(cities));               // "Oslo"
            show("first", first);
            int[] temps = {18, 24, 9, 31, 15};
            Integer viaBoxed = Collections.min(Arrays.stream(temps).boxed().toList());   // 9
            show("viaBoxed", viaBoxed);
        }
        {
            int[] hourly = {18, 24, 9, 31, 15};
            int maxAt = IntStream.range(0, hourly.length).reduce((i, j) -> hourly[j] > hourly[i] ? j : i).orElse(-1);   // 3
            show("maxAt", maxAt);
            int minAt = IntStream.range(0, hourly.length).reduce((i, j) -> hourly[j] < hourly[i] ? j : i).orElse(-1);   // 2
            show("minAt", minAt);
        }
        {
            double[] sensor = {21.5, Double.NaN, 25.0};
            double streamMax = Arrays.stream(sensor).max().orElseThrow();                                  // NaN
            show("streamMax", streamMax);
            double cleanMax = Arrays.stream(sensor).filter(d -> !Double.isNaN(d)).max().orElseThrow();     // 25.0
            show("cleanMax", cleanMax);
            double mathMax = Math.max(21.5, Double.NaN);                                                   // NaN
            show("mathMax", mathMax);
        }
        {
            int[] temps = {18, 24, 9, 31, 15};
            int[] sorted = temps.clone();
            Arrays.sort(sorted);
            int sortedMin = sorted[0];                    // 9
            show("sortedMin", sortedMin);
            int sortedMax = sorted[sorted.length - 1];    // 31
            show("sortedMax", sortedMax);
        }
        {
            int[][] grid = {{18, 24}, {9, 31, 15}};
            IntSummaryStatistics all = Arrays.stream(grid).flatMapToInt(Arrays::stream).summaryStatistics();
            int gridMax = all.getMax();             // 31
            show("gridMax", gridMax);
            int gridMin = all.getMin();             // 9
            show("gridMin", gridMin);
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

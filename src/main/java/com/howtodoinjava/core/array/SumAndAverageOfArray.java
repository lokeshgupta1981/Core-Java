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
 * Examples for the tutorial "Sum and Average of an Array in Java (Loop, Stream, Overflow)".
 * https://howtodoinjava.com/java/array/sum-and-average-of-array/
 */
public class SumAndAverageOfArray {
    static OptionalDouble averageRating(int[] ratings) {
        if (ratings == null) {
            return OptionalDouble.empty();
        }
        return Arrays.stream(ratings).average();
    }
    static record Workout(String day, int minutes) {}
    public static void main(String[] args) throws Exception {
        {
            int[] visits = {4200, 8100, 6500, 9000, 7300};
            long total = Arrays.stream(visits).asLongStream().sum();        // 35100
            show("total", total);
            double average = Arrays.stream(visits).average().orElse(0.0);   // 7020.0
            show("average", average);
        }
        {
            int[] visits = {4200, 8100, 6500, 9000, 7300};
            long total = 0;
            for (int v : visits) {
                total += v;
            }
            double average = (double) total / visits.length;   // 7020.0
            show("average", average);
        }
        {
            int[] ratings = {4, 5, 5, 4};
            int sum = Arrays.stream(ratings).sum();             // 18
            show("sum", sum);
            int wrongAvg = sum / ratings.length;                 // 4
            show("wrongAvg", wrongAvg);
            double lateCast = (double) (sum / ratings.length);   // 4.0
            show("lateCast", lateCast);
            double rightAvg = (double) sum / ratings.length;     // 4.5
            show("rightAvg", rightAvg);
        }
        {
            int[] visits = {4200, 8100, 6500, 9000, 7300};
            int sum = Arrays.stream(visits).sum();                         // 35100
            show("sum", sum);
            OptionalDouble avg = Arrays.stream(visits).average();          // OptionalDouble[7020.0]
            show("avg", avg);
            IntSummaryStatistics stats = Arrays.stream(visits).summaryStatistics();
            long statsSum = stats.getSum();                               // 35100
            show("statsSum", statsSum);
            double statsAvg = stats.getAverage();                         // 7020.0
            show("statsAvg", statsAvg);
        }
        {
            Integer[] scores = {12, null, 30};
            int safeSum = Arrays.stream(scores).filter(Objects::nonNull).mapToInt(Integer::intValue).sum();   // 42
            show("safeSum", safeSum);
            try { int crash = Arrays.stream(scores).mapToInt(Integer::intValue).sum(); show("crash", crash); } catch (Throwable _t) { System.out.println("crash -> " + _t); }
        }
        {
            short[] temps = {21, 23, 19};
            int tempSum = IntStream.range(0, temps.length).map(i -> temps[i]).sum();   // 63
            show("tempSum", tempSum);
            long[] bytesSent = {3_000_000_000L, 2_500_000_000L};
            long sent = Arrays.stream(bytesSent).sum();                                // 5500000000
            show("sent", sent);
            double[] km = {5.2, 10.0, 3.3};
            double kmAvg = Arrays.stream(km).average().orElse(0.0);                    // 6.166666666666667
            show("kmAvg", kmAvg);
        }
        {
            int[] watchMillis = {2_000_000_000, 2_000_000_000};
            int wrapped = Arrays.stream(watchMillis).sum();                     // -294967296
            show("wrapped", wrapped);
            long correct = Arrays.stream(watchMillis).asLongStream().sum();     // 4000000000
            show("correct", correct);
            long viaStats = Arrays.stream(watchMillis).summaryStatistics().getSum();   // 4000000000
            show("viaStats", viaStats);
        }
        {
            int[] watchMillis = {2_000_000_000, 2_000_000_000};
            try { int strict = Arrays.stream(watchMillis).reduce(0, Math::addExact); show("strict", strict); } catch (Throwable _t) { System.out.println("strict -> " + _t); }
        }
        {
            int[] none = {};
            double nan = (double) 0 / none.length;                      // NaN
            show("nan", nan);
            OptionalDouble empty = Arrays.stream(none).average();       // OptionalDouble.empty
            show("empty", empty);
            double zero = Arrays.stream(none).summaryStatistics().getAverage();   // 0.0
            show("zero", zero);
            try { long boom = 0L / none.length; show("boom", boom); } catch (Throwable _t) { System.out.println("boom -> " + _t); }
        }
        {
            OptionalDouble noReviews = averageRating(null);                // OptionalDouble.empty
            show("noReviews", noReviews);
            String label = noReviews.isPresent() ? "%.1f".formatted(noReviews.getAsDouble()) : "No ratings yet";   // "No ratings yet"
            show("label", label);
            String shown = "%.1f".formatted(averageRating(new int[]{4, 5, 5}).orElse(0));   // "4.7"
            show("shown", shown);
        }
        {
            double[] cart = {0.10, 0.20};
            double doubleTotal = Arrays.stream(cart).sum();   // 0.30000000000000004
            show("doubleTotal", doubleTotal);
            BigDecimal[] prices = {new BigDecimal("0.10"), new BigDecimal("0.20"), new BigDecimal("19.99")};
            BigDecimal total = Arrays.stream(prices).reduce(BigDecimal.ZERO, BigDecimal::add);   // 20.29
            show("total", total);
            BigDecimal avg = total.divide(BigDecimal.valueOf(prices.length), 2, RoundingMode.HALF_UP);   // 6.76
            show("avg", avg);
        }
        {
            int[] visits = {4200, 8100, 6500, 9000, 7300};
            int firstThree = Arrays.stream(visits, 0, 3).sum();                          // 18800
            show("firstThree", firstThree);
            double busyDaysAvg = Arrays.stream(visits).filter(v -> v >= 7000).average().orElse(0.0);   // 8133.333333333333
            show("busyDaysAvg", busyDaysAvg);
            long busyDays = Arrays.stream(visits).filter(v -> v >= 7000).count();       // 3
            show("busyDays", busyDays);
        }
        {
            int[][] weeks = {{4200, 8100}, {6500, 9000, 7300}};
            int grandTotal = Arrays.stream(weeks).flatMapToInt(Arrays::stream).sum();             // 35100
            show("grandTotal", grandTotal);
            int[] perWeek = Arrays.stream(weeks).mapToInt(w -> Arrays.stream(w).sum()).toArray(); // [12300, 22800]
            show("perWeek", perWeek);
        }
        {
            Workout[] log = {new Workout("mon", 30), new Workout("wed", 45), new Workout("fri", 20)};
            int minutes = Arrays.stream(log).collect(Collectors.summingInt(Workout::minutes));          // 95
            show("minutes", minutes);
            double avgMinutes = Arrays.stream(log).collect(Collectors.averagingInt(Workout::minutes));  // 31.666666666666668
            show("avgMinutes", avgMinutes);
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

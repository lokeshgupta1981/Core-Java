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
 * Examples for the tutorial "Average of N Numbers in Java: Loop, Array, List and Streams".
 * https://howtodoinjava.com/java-examples/calculate-average-of-n-numbers/
 */
public class AverageOfNNumbersFlow {
    static OptionalDouble readAverage(Scanner scanner) {
        scanner.useLocale(Locale.ROOT);
        double sum = 0;
        int count = 0;
        while (scanner.hasNext()) {
            if (scanner.hasNextDouble()) {
                sum += scanner.nextDouble();
                count++;
            } else {
                scanner.next();                        // skip a token that is not a number
            }
        }
        return count == 0 ? OptionalDouble.empty() : OptionalDouble.of(sum / count);
    }
    static final class MovingAverage {
        private final int window;
        private final Deque<Long> values = new ArrayDeque<>();
        private long sum;

        MovingAverage(int window) {
            this.window = window;
        }

        double add(long value) {
            values.addLast(value);
            sum += value;
            if (values.size() > window) {
                sum -= values.removeFirst();
            }
            return (double) sum / values.size();
        }
    }
    public static void main(String[] args) throws Exception {
        {
            double[] ratings = {4.5, 3.0, 5.0, 4.0};
            double sum = 0;
            for (double rating : ratings) {
                sum += rating;
            }
            double average = sum / ratings.length;                                  // 4.125
            show("average", average);
            double streamAverage = Arrays.stream(ratings).average().orElse(0.0);    // 4.125
            show("streamAverage", streamAverage);
        }
        {
            int total = 7;
            int count = 2;
            double wrong = total / count;                 // 3.0
            show("wrong", wrong);
            double right = (double) total / count;        // 3.5
            show("right", right);
        }
        {
            OptionalDouble result = readAverage(new Scanner("10.3 20.6 abc 30.9 5 10"));
            String shown = String.format(Locale.ROOT, "%.2f", result.orElse(0.0));    // "15.36"
            show("shown", shown);
            OptionalDouble none = readAverage(new Scanner("abc"));                      // OptionalDouble.empty
            show("none", none);
        }
        {
            int[] marks = {72, 85, 90, 64, 78};
            long sum = 0;
            for (int mark : marks) {
                sum += mark;
            }
            double average = (double) sum / marks.length;                       // 77.8
            show("average", average);
            double streamAverage = Arrays.stream(marks).average().orElse(0);    // 77.8
            show("streamAverage", streamAverage);
        }
        {
            double[] none = {};
            double emptySum = 0;
            double nanAverage = emptySum / none.length;                     // NaN
            show("nanAverage", nanAverage);
            OptionalDouble emptyAverage = Arrays.stream(none).average();    // OptionalDouble.empty
            show("emptyAverage", emptyAverage);
            try { int intAverage = 0 / none.length; show("intAverage", intAverage); } catch (Throwable _t) { System.out.println("intAverage -> " + _t); }
        }
        {
            int[] views = {2_000_000_000, 2_000_000_000};
            int intSum = views[0] + views[1];                                // -294967296
            show("intSum", intSum);
            int streamSum = Arrays.stream(views).sum();                      // -294967296
            show("streamSum", streamSum);
            long longSum = Arrays.stream(views).asLongStream().sum();        // 4000000000
            show("longSum", longSum);
            double viewAverage = Arrays.stream(views).average().orElse(0);   // 2.0E9
            show("viewAverage", viewAverage);
        }
        {
            List<Double> temperatures = new ArrayList<>(List.of(21.5, 23.0, 19.5, 22.0));
            double average = temperatures.stream().mapToDouble(Double::doubleValue).average().orElse(Double.NaN);   // 21.5
            show("average", average);
            double viaCollector = temperatures.stream().collect(Collectors.averagingDouble(t -> t));    // 21.5
            show("viaCollector", viaCollector);
        }
        {
            List<Double> noReadings = List.of();
            double collectorEmpty = noReadings.stream().collect(Collectors.averagingDouble(t -> t));    // 0.0
            show("collectorEmpty", collectorEmpty);
            boolean hasAverage = noReadings.stream().mapToDouble(t -> t).average().isPresent();       // false
            show("hasAverage", hasAverage);
        }
        {
            List<Double> temperatures = List.of(21.5, 23.0, 19.5, 22.0);
            DoubleSummaryStatistics stats = temperatures.stream().mapToDouble(Double::doubleValue).summaryStatistics();
            double min = stats.getMin();             // 19.5
            show("min", min);
            double max = stats.getMax();             // 23.0
            show("max", max);
            double mean = stats.getAverage();        // 21.5
            show("mean", mean);
        }
        {
            List<BigDecimal> orders = List.of(new BigDecimal("19.99"), new BigDecimal("5.49"), new BigDecimal("12.00"));
            BigDecimal total = orders.stream().reduce(BigDecimal.ZERO, BigDecimal::add);                  // 37.48
            show("total", total);
            BigDecimal count = BigDecimal.valueOf(orders.size());
            BigDecimal averageOrder = total.divide(count, 2, RoundingMode.HALF_EVEN);                     // 12.49
            show("averageOrder", averageOrder);
            try { BigDecimal unrounded = total.divide(count); show("unrounded", unrounded); } catch (Throwable _t) { System.out.println("unrounded -> " + _t); }
        }
        {
            MovingAverage latency = new MovingAverage(3);
            double first = latency.add(120);         // 120.0
            show("first", first);
            double second = latency.add(80);         // 100.0
            show("second", second);
            double third = latency.add(100);         // 100.0
            show("third", third);
            double fourth = latency.add(300);        // 160.0
            show("fourth", fourth);
        }
        {
            double avg = 15.3563;
            String text = String.format(Locale.ROOT, "%.2f", avg);                            // "15.36"
            show("text", text);
            double rounded = Math.round(avg * 100) / 100.0;                                    // 15.36
            show("rounded", rounded);
            BigDecimal exact = BigDecimal.valueOf(avg).setScale(2, RoundingMode.HALF_UP);      // 15.36
            show("exact", exact);
        }
        {
            double[] salaries = {3200, 3500, 3900, 4100, 25000};
            double meanSalary = Arrays.stream(salaries).average().orElse(0);                    // 7940.0
            show("meanSalary", meanSalary);
            double[] sorted = salaries.clone();
            Arrays.sort(sorted);
            double medianSalary = sorted[sorted.length / 2];                                    // 3900.0
            show("medianSalary", medianSalary);
        }
        {
            double[] scores = {70, 80, 90};
            double[] weights = {1, 1, 2};
            double weightedSum = 0;
            double weightTotal = 0;
            for (int i = 0; i < scores.length; i++) {
                weightedSum += scores[i] * weights[i];
                weightTotal += weights[i];
            }
            double grade = weightedSum / weightTotal;                                           // 82.5
            show("grade", grade);
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

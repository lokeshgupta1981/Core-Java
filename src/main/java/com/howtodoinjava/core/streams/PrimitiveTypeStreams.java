package com.howtodoinjava.core.streams;

import java.util.regex.*;
import java.net.*;
import java.text.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.time.*;
import java.nio.file.*;
import java.math.*;
import java.io.*;

/**
 * Examples for the tutorial "Primitive Type Streams in Java: IntStream, LongStream and More".
 * https://howtodoinjava.com/java/stream/primitive-type-streams/
 */
public class PrimitiveTypeStreams {
    static record Walk(String day, int steps, double km) {}
    public static void main(String[] args) throws Exception {
        {
            int[] steps = {4200, 8100, 6500, 12000, 9300};

            int total = Arrays.stream(steps).sum();                         // 40100
            show("total", total);
            double avg = Arrays.stream(steps).average().orElse(0);          // 8020.0
            show("avg", avg);
            int best = Arrays.stream(steps).max().orElse(0);                // 12000
            show("best", best);
            long goalDays = Arrays.stream(steps).filter(s -> s >= 8000).count();   // 3
            show("goalDays", goalDays);
            List<Integer> days = IntStream.rangeClosed(1, 5).boxed().toList();     // [1, 2, 3, 4, 5]
            show("days", days);
            IntSummaryStatistics stats = Arrays.stream(steps).summaryStatistics();
            String summary = stats.toString();   // "IntSummaryStatistics{count=5, sum=40100, min=4200, average=8020.000000, max=12000}"
            show("summary", summary);
        }
        {
            List<Integer> ratings = List.of(4, 5, 3);
            int sumBoxed = ratings.stream().reduce(0, Integer::sum);          // 12, unboxes and boxes each step
            show("sumBoxed", sumBoxed);
            int sumPrimitive = ratings.stream().mapToInt(Integer::intValue).sum();   // 12, unboxes once per element
            show("sumPrimitive", sumPrimitive);
        }
        {
            int[] ints = IntStream.of(1, 2, 3).toArray();                  // [1, 2, 3]
            show("ints", ints);
            long[] longs = LongStream.of(10L, 20L).toArray();               // [10, 20]
            show("longs", longs);
            double[] doubles = DoubleStream.of(1.5, 2.5).toArray();         // [1.5, 2.5]
            show("doubles", doubles);
            long none = IntStream.empty().count();                          // 0
            show("none", none);
        }
        {
            int[] open = IntStream.range(1, 5).toArray();                  // [1, 2, 3, 4]
            show("open", open);
            int[] closed = IntStream.rangeClosed(1, 5).toArray();           // [1, 2, 3, 4, 5]
            show("closed", closed);
            long empty = IntStream.range(5, 5).count();                     // 0
            show("empty", empty);
            long big = LongStream.rangeClosed(1, 3_000_000_000L).count();   // 3000000000
            show("big", big);
        }
        {
            int[] everyThird = IntStream.iterate(0, i -> i < 10, i -> i + 3).toArray();   // [0, 3, 6, 9]
            show("everyThird", everyThird);
            int[] countdown = IntStream.iterate(3, i -> i > 0, i -> i - 1).toArray();     // [3, 2, 1]
            show("countdown", countdown);
            int[] zeros = IntStream.generate(() -> 0).limit(3).toArray();                 // [0, 0, 0]
            show("zeros", zeros);
        }
        {
            int[] heartRates = {72, 88, 95, 110};
            IntStream rates = Arrays.stream(heartRates);
            int peak = rates.max().orElse(0);                               // 110
            show("peak", peak);
            double avgMiddle = Arrays.stream(heartRates, 1, 3).average().orElse(0);   // 91.5
            show("avgMiddle", avgMiddle);
            long[] ids = {101L, 102L};
            long idSum = Arrays.stream(ids).sum();                          // 203
            show("idSum", idSum);
            double[] kms = {2.5, 3.0};
            double distance = Arrays.stream(kms).sum();                     // 5.5
            show("distance", distance);
        }
        {
            List<Walk> week = List.of(
            new Walk("Mon", 4200, 3.1),
            new Walk("Tue", 8100, 6.0),
            new Walk("Wed", 6500, 4.8));

            int totalSteps = week.stream().mapToInt(Walk::steps).sum();               // 18800
            show("totalSteps", totalSteps);
            double totalKm = week.stream().mapToDouble(Walk::km).sum();               // 13.899999999999999
            show("totalKm", totalKm);
            long asLong = week.stream().mapToLong(Walk::steps).sum();                 // 18800
            show("asLong", asLong);
            List<Integer> numbers = List.of(1, 2, 3);
            int fromBoxed = numbers.stream().mapToInt(Integer::intValue).sum();       // 6
            show("fromBoxed", fromBoxed);
        }
        {
            long digits = "Room 204B".chars().filter(Character::isDigit).count();   // 3
            show("digits", digits);
            String upper = "run".chars()
                    .map(Character::toUpperCase)
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString();                                            // "RUN"
            int[] dice = new Random(7).ints(3, 1, 7).toArray();             // [5, 3, 4]
            show("dice", dice);
        }
        {
            int max = IntStream.of(10, 18, 12).max().orElse(0);              // 18
            show("max", max);
            double average = IntStream.of(1, 2, 3, 4, 5).average().orElse(0);   // 3.0
            show("average", average);
            int sum = IntStream.range(1, 10).sum();                           // 45
            show("sum", sum);
            OptionalInt noMax = IntStream.empty().max();                      // OptionalInt.empty
            show("noMax", noMax);
            try { int unsafe = IntStream.empty().max().getAsInt(); show("unsafe", unsafe); } catch (Throwable _t) { System.out.println("unsafe -> " + _t); }
        }
        {
            int overflow = IntStream.of(Integer.MAX_VALUE, 1).sum();                 // -2147483648
            show("overflow", overflow);
            long correct = IntStream.of(Integer.MAX_VALUE, 1).asLongStream().sum();   // 2147483648
            show("correct", correct);
            long viaStats = IntStream.of(Integer.MAX_VALUE, 1).summaryStatistics().getSum();   // 2147483648
            show("viaStats", viaStats);
        }
        {
            IntSummaryStatistics stats = IntStream.of(10, 18, 12, 70, 5).summaryStatistics();
            int min = stats.getMin();                                        // 5
            show("min", min);
            int max = stats.getMax();                                        // 70
            show("max", max);
            double avg = stats.getAverage();                                 // 23.0
            show("avg", avg);
            long sum = stats.getSum();                                       // 115
            show("sum", sum);
            long count = stats.getCount();                                   // 5
            show("count", count);
        }
        {
            Stream<Integer> boxedInts = IntStream.of(1, 2, 3).boxed();
            List<Integer> list = IntStream.of(1, 2, 3).boxed().toList();      // [1, 2, 3]
            show("list", list);
            List<Long> longList = LongStream.of(5L, 6L).boxed().toList();     // [5, 6]
            show("longList", longList);
            List<String> labels = IntStream.rangeClosed(1, 3).mapToObj(i -> "Day " + i).toList();   // [Day 1, Day 2, Day 3]
            show("labels", labels);
            int[] array = IntStream.of(3, 1, 2).sorted().toArray();           // [1, 2, 3]
            show("array", array);
        }
        {
            double half = IntStream.of(1, 2).asDoubleStream().map(d -> d / 2).sum();   // 1.5
            show("half", half);
            long[] widened = IntStream.of(7, 8).asLongStream().toArray();              // [7, 8]
            show("widened", widened);
            int[] truncated = DoubleStream.of(2.9, 3.1).mapToInt(d -> (int) d).toArray();   // [2, 3]
            show("truncated", truncated);
            int[] rounded = DoubleStream.of(2.9, 3.1).mapToLong(Math::round).mapToInt(Math::toIntExact).toArray();   // [3, 3]
            show("rounded", rounded);
        }
        {
            List<Walk> week = List.of(
            new Walk("Mon", 4200, 3.1), new Walk("Tue", 8100, 6.0),
            new Walk("Wed", 6500, 4.8), new Walk("Thu", 12000, 8.9),
            new Walk("Fri", 9300, 7.0));

            IntSummaryStatistics steps = week.stream().collect(Collectors.summarizingInt(Walk::steps));
            long totalSteps = steps.getSum();                                // 40100
            show("totalSteps", totalSteps);
            double dailyAverage = steps.getAverage();                        // 8020.0
            show("dailyAverage", dailyAverage);
            int goalDays = (int) week.stream().mapToInt(Walk::steps).filter(s -> s >= 8000).count();   // 3
            show("goalDays", goalDays);
            String bestDay = week.stream()
                    .max(Comparator.comparingInt(Walk::steps))
                    .map(Walk::day)
                    .orElse("none");                                         // "Thu"
            double kmRounded = Math.round(week.stream().mapToDouble(Walk::km).sum() * 10) / 10.0;   // 29.8
            show("kmRounded", kmRounded);
            List<String> chart = IntStream.range(0, week.size())
                    .mapToObj(i -> (i + 1) + ". " + week.get(i).day() + " " + week.get(i).steps())
                    .toList();                                               // [1. Mon 4200, 2. Tue 8100, 3. Wed 6500, 4. Thu 12000, 5. Fri 9300]
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

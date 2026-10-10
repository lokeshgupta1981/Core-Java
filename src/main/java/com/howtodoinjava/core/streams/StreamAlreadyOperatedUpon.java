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
 * Examples for the tutorial "Stream Has Already Been Operated Upon or Closed: Causes and Fix".
 * https://howtodoinjava.com/java/stream/stream-has-already-been-operated-upon-or-closed/
 */
public class StreamAlreadyOperatedUpon {
    static Stream<Integer> scores() {
        return Stream.of(123, 234, 11, 57, 60, -4);
    }
    static List<Integer> readAmounts(Path csv) throws IOException {
        try (Stream<String> lines = Files.lines(csv)) {
            return lines.skip(1)
                    .map(line -> line.split(",")[1].strip())
                    .map(Integer::parseInt)
                    .toList();
        }
    }
    public static void main(String[] args) throws Exception {
        {
            Stream<Integer> numbers = Stream.of(123, 234, 11, 57, 60, -4);
            List<Integer> evens = numbers.filter(n -> n % 2 == 0).toList();   // [234, 60, -4]
            show("evens", evens);
            try { List<Integer> odds = numbers.filter(n -> n % 2 != 0).toList(); show("odds", odds); } catch (Throwable _t) { System.out.println("odds -> " + _t); }

            List<Integer> source = List.of(123, 234, 11, 57, 60, -4);
            List<Integer> evenNumbers = source.stream().filter(n -> n % 2 == 0).toList();   // [234, 60, -4]
            show("evenNumbers", evenNumbers);
            List<Integer> oddNumbers = source.stream().filter(n -> n % 2 != 0).toList();    // [123, 11, 57]
            show("oddNumbers", oddNumbers);
        }
        {
            Stream<Integer> doubled = Stream.of(123, 234, 11, 57, 60, -4).map(n -> n * 2);
            List<Integer> big = doubled.filter(n -> n > 100).toList();        // [246, 468, 114, 120]
            show("big", big);
            try { List<Integer> small = doubled.filter(n -> n <= 100).toList(); show("small", small); } catch (Throwable _t) { System.out.println("small -> " + _t); }
        }
        {
            long count = scores().count();                                   // 6
            show("count", count);
            Stream<Integer> cached = scores();
            long positives = cached.filter(n -> n > 0).count();              // 5
            show("positives", positives);
            try { long negatives = cached.filter(n -> n < 0).count(); show("negatives", negatives); } catch (Throwable _t) { System.out.println("negatives -> " + _t); }
        }
        {
            Stream<Integer> iterated = Stream.of(123, 234);
            Iterator<Integer> it = iterated.iterator();
            try { long afterIterator = iterated.count(); show("afterIterator", afterIterator); } catch (Throwable _t) { System.out.println("afterIterator -> " + _t); }

            Stream<Integer> closed = Stream.of(123, 234);
            closed.close();
            try { long afterClose = closed.count(); show("afterClose", afterClose); } catch (Throwable _t) { System.out.println("afterClose -> " + _t); }
        }
        {
            List<Integer> source = List.of(123, 234, 11, 57, 60, -4);

            List<Integer> positiveNumbers = source.stream().filter(n -> n > 0).toList();          // [123, 234, 11, 57, 60]
            show("positiveNumbers", positiveNumbers);
            long evenCount = positiveNumbers.stream().filter(n -> n % 2 == 0).count();             // 2
            show("evenCount", evenCount);
            int largest = positiveNumbers.stream().max(Integer::compare).orElseThrow();            // 234
            show("largest", largest);
        }
        {
            Supplier<Stream<Integer>> streamSupplier = () -> Stream.of(123, 234, 11, 57, 60, -4);

            List<Integer> evenOnes = streamSupplier.get().filter(n -> n % 2 == 0).toList();   // [234, 60, -4]
            show("evenOnes", evenOnes);
            List<Integer> oddOnes = streamSupplier.get().filter(n -> n % 2 != 0).toList();    // [123, 11, 57]
            show("oddOnes", oddOnes);
        }
        {
            List<Integer> source = List.of(123, 234, 11, 57, 60, -4);

            Map<Boolean, List<Integer>> byParity = source.stream().collect(Collectors.partitioningBy(n -> n % 2 == 0));   // {false=[123, 11, 57], true=[234, 60, -4]}
            show("byParity", byParity);
            IntSummaryStatistics stats = source.stream().mapToInt(Integer::intValue).summaryStatistics();               // IntSummaryStatistics{count=6, sum=481, min=-4, average=80.166667, max=234}
            show("stats", stats);
            int range = source.stream().collect(Collectors.teeing(
            Collectors.minBy(Integer::compare),
            Collectors.maxBy(Integer::compare),
            (min, max) -> max.orElseThrow() - min.orElseThrow()));                                                // 238
        }
        {
            List<Integer> amounts = List.of(120, 75, 300, 40);
            long rows = amounts.size();                                           // 4
            show("rows", rows);
            int total = amounts.stream().mapToInt(Integer::intValue).sum();       // 535
            show("total", total);
            List<Integer> large = amounts.stream().filter(a -> a > 100).toList(); // [120, 300]
            show("large", large);
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

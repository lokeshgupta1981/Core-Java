package com.howtodoinjava.core.streams;

import java.util.regex.*;
import java.net.*;
import java.text.*;
import java.nio.file.attribute.*;
import java.security.*;
import java.nio.charset.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.time.*;
import java.nio.file.*;
import java.math.*;
import java.io.*;

import java.time.format.*;
;

/**
 * Examples for the tutorial "Get the Last Element of a Stream in Java: reduce() and getLast()".
 * https://howtodoinjava.com/java8/java-stream-get-last-element/
 */
public class StreamLastElement {

    public static void main(String[] args) throws Exception {
        {
            List<Integer> readings = List.of(18, 21, 19, 24);
            Integer latest = readings.getLast();                                                                   // 24
            show("latest", latest);
            Optional<Integer> lastCool = readings.stream().filter(r -> r < 20).reduce((first, second) -> second);   // Optional[19]
            show("lastCool", lastCool);
            Optional<Integer> lastHot = readings.stream().filter(r -> r > 30).reduce((first, second) -> second);    // Optional.empty
            show("lastHot", lastHot);
        }
        {
            List<Integer> readings = List.of(18, 21, 19, 24);
            Integer last = readings.getLast();                                           // 24
            show("last", last);
            Integer oldStyle = readings.get(readings.size() - 1);                        // 24
            show("oldStyle", oldStyle);
            String newestTag = new LinkedHashSet<>(List.of("v1", "v2", "v3")).getLast(); // "v3"
            show("newestTag", newestTag);
            Integer highest = new TreeSet<>(readings).getLast();                         // 24
            show("highest", highest);
        }
        {
            List<Integer> noReadings = List.of();
            try { Integer boom = noReadings.getLast(); show("boom", boom); } catch (Throwable _t) { System.out.println("boom -> " + _t); }
            Optional<Integer> safeLast = noReadings.isEmpty() ? Optional.empty() : Optional.of(noReadings.getLast());   // Optional.empty
            show("safeLast", safeLast);
        }
        {
            List<Integer> readings = List.of(18, 21, 19, 24);
            Optional<Integer> last = readings.stream().reduce((first, second) -> second);                      // Optional[24]
            show("last", last);
            int lastOrDefault = readings.stream().filter(r -> r > 30).reduce((a, b) -> b).orElse(-1);          // -1
            show("lastOrDefault", lastOrDefault);
            try { Integer lastOrThrow = Stream.<Integer>empty().reduce((a, b) -> b).orElseThrow(); show("lastOrThrow", lastOrThrow); } catch (Throwable _t) { System.out.println("lastOrThrow -> " + _t); }
            OptionalInt lastInt = IntStream.rangeClosed(1, 5).reduce((a, b) -> b);                              // OptionalInt[5]
            show("lastInt", lastInt);
        }
        {
            List<Integer> readings = List.of(18, 21, 19, 24);
            List<Integer> cool = readings.stream().filter(r -> r < 22).toList();                                   // [18, 21, 19]
            show("cool", cool);
            Integer lastCool = cool.getLast();                                                                     // 19
            show("lastCool", lastCool);
        }
        {
            List<Integer> readings = List.of(18, 21, 19, 24);
            Optional<Integer> viaSkip = readings.stream().skip(readings.size() - 1).findFirst();             // Optional[24]
            show("viaSkip", viaSkip);
            List<Integer> empty = List.of();
            try { Optional<Integer> broken = empty.stream().skip(empty.size() - 1).findFirst(); show("broken", broken); } catch (Throwable _t) { System.out.println("broken -> " + _t); }
            Optional<Integer> guarded = empty.stream().skip(Math.max(0, empty.size() - 1)).findFirst();       // Optional.empty
            show("guarded", guarded);
        }
        {
            Integer lastEven = Stream.iterate(0, n -> n + 2).limit(100).reduce((a, b) -> b).orElse(-1);                // 198
            show("lastEven", lastEven);
            Integer lastPowerBelow1000 = Stream.iterate(1, n -> n * 2).takeWhile(n -> n < 1000).reduce((a, b) -> b).orElseThrow();   // 512
            show("lastPowerBelow1000", lastPowerBelow1000);
        }
        {
            List<Integer> readings = List.of(18, 21, 19, 24);
            int n = 2;
            List<Integer> lastTwo = readings.subList(Math.max(0, readings.size() - n), readings.size());   // [19, 24]
            show("lastTwo", lastTwo);
            List<Integer> newestFirst = readings.reversed().stream().limit(n).toList();                     // [24, 19]
            show("newestFirst", newestFirst);
        }
        {
            String log = """
            10:01 INFO started
            10:05 ERROR disk full
            10:07 INFO retry
            10:09 ERROR timeout
            10:10 INFO done
            """;
            String lastError = log.lines().filter(line -> line.contains("ERROR")).reduce((a, b) -> b).orElse("no errors");   // "10:09 ERROR timeout"
            show("lastError", lastError);
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

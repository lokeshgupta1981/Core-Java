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

/**
 * Examples for the tutorial "Java Stream peek(): How It Works and When It Is Skipped".
 * https://howtodoinjava.com/java8/java-stream-peek-example/
 */
public class StreamPeekExamples {
    static record Reading(String sensor, double celsius) {}
    public static void main(String[] args) throws Exception {
        {
            List<String> seen = new ArrayList<>();
            List<String> loud = Stream.of("hum", "beep", "buzz", "hi").filter(w -> w.length() > 3).peek(seen::add).map(String::toUpperCase).toList();   // [BEEP, BUZZ]
            show("loud", loud);
            List<String> passedFilter = seen;                                  // [beep, buzz]
            show("passedFilter", passedFilter);
            List<String> counted = new ArrayList<>();
            long total = Stream.of("hum", "beep").peek(counted::add).count();  // 2
            show("total", total);
            List<String> peekedBeforeCount = counted;                          // [], count() skipped the pipeline
            show("peekedBeforeCount", peekedBeforeCount);
        }
        {
            Stream.of(3, 8, 1).peek(n -> System.out.println("read " + n)).map(n -> n * 10).forEach(n -> System.out.println("got " + n));
        }
        {
            List<Integer> mapped = new ArrayList<>();
            long size = List.of(5, 6, 7).stream().peek(mapped::add).map(n -> n * 2).count();       // 3
            show("size", size);
            List<Integer> afterMap = mapped;                                                       // []
            show("afterMap", afterMap);
            List<Integer> filtered = new ArrayList<>();
            long bigOnes = List.of(5, 6, 7).stream().peek(filtered::add).filter(n -> n > 5).count();   // 2
            show("bigOnes", bigOnes);
            List<Integer> afterFilter = filtered;                                                  // [5, 6, 7]
            show("afterFilter", afterFilter);
        }
        {
            List<Integer> pulled = new ArrayList<>();
            Optional<Integer> firstEven = Stream.of(3, 5, 6, 8, 9).peek(pulled::add).filter(n -> n % 2 == 0).findFirst();   // Optional[6]
            show("firstEven", firstEven);
            List<Integer> visited = pulled;                                                                                 // [3, 5, 6]
            show("visited", visited);
            List<Integer> generated = new ArrayList<>();
            List<Integer> firstTwo = Stream.iterate(1, n -> n + 1).peek(generated::add).limit(2).toList();                  // [1, 2]
            show("firstTwo", firstTwo);
            List<Integer> created = generated;                                                                              // [1, 2]
            show("created", created);
        }
        {
            List<String> events = new ArrayList<>();
            List<Integer> sortedNumbers = Stream.of(3, 1, 2).peek(n -> events.add("in " + n)).sorted().peek(n -> events.add("out " + n)).toList();   // [1, 2, 3]
            show("sortedNumbers", sortedNumbers);
            List<String> order = events;                                                                                                             // [in 3, in 1, in 2, out 1, out 2, out 3]
            show("order", order);
        }
        {
            List<StringBuilder> names = List.of(new StringBuilder("ann"), new StringBuilder("bob"));
            long changed = names.stream().peek(sb -> sb.setCharAt(0, 'A')).count();       // 2
            show("changed", changed);
            String firstName = names.getFirst().toString();                                // "ann", peek() never ran
            show("firstName", firstName);
        }
        {
            List<String> capitalized = Stream.of("ann", "bob").map(s -> Character.toUpperCase(s.charAt(0)) + s.substring(1)).toList();   // [Ann, Bob]
            show("capitalized", capitalized);
        }
        {
            Queue<Integer> seenInParallel = new ConcurrentLinkedQueue<>();
            List<Integer> doubled = IntStream.rangeClosed(1, 6).boxed().parallel().peek(seenInParallel::add).map(n -> n * 2).toList();   // [2, 4, 6, 8, 10, 12]
            show("doubled", doubled);
            int seenCount = seenInParallel.size();                                                                                       // 6, in an order that varies
            show("seenCount", seenCount);
        }
        {
            System.Logger log = System.getLogger("sensor-import");
            Predicate<String> wellFormed = Pattern.compile("\\w+,-?\\d+(\\.\\d+)?").asMatchPredicate();
            List<String> upload = List.of("s1,21.5", "s2,abc", "s3,-4.0");
            List<Reading> readings = upload.stream()
                    .filter(wellFormed)
                    .peek(line -> log.log(System.Logger.Level.DEBUG, "valid line {0}", line))
                    .map(line -> line.split(","))
                    .map(parts -> new Reading(parts[0], Double.parseDouble(parts[1])))
                    .toList();
            List<Reading> imported = readings;                     // [Reading[sensor=s1, celsius=21.5], Reading[sensor=s3, celsius=-4.0]]
            show("imported", imported);
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

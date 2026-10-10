package com.howtodoinjava.core.streams;

import java.util.concurrent.locks.*;
import java.lang.management.*;
import java.time.format.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.zip.*;
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

import java.time.temporal.*;

/**
 * Examples for the tutorial "Java Stream Reuse: How to Consume a Stream Multiple Times".
 * https://howtodoinjava.com/java8/java-stream-reuse/
 */
public class StreamReuse {
    static record Hit(String path, int status, int millis) {
        static Hit parse(String line) {
            String[] parts = line.strip().split("\\s+");
            return new Hit(parts[0], Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
        }
    }
    static record Report(long requests, double errorRate, String slowestPath) {}
    static Report report(BufferedReader reader) {
        List<Hit> hits = reader.lines().filter(line -> !line.isBlank()).map(Hit::parse).toList();
        long errors = hits.stream().filter(h -> h.status() >= 400).count();
        String slowest = hits.stream().max(Comparator.comparingInt(Hit::millis)).map(Hit::path).orElse("none");
        double rate = hits.isEmpty() ? 0 : 100.0 * errors / hits.size();
        return new Report(hits.size(), rate, slowest);
    }
    public static void main(String[] args) throws Exception {
        {
            List<Hit> hits = List.of(new Hit("/home", 200, 40), new Hit("/cart", 500, 900), new Hit("/home", 200, 60), new Hit("/login", 404, 15));
            Stream<Hit> once = hits.stream();
            long first = once.count();                                                     // 4
            show("first", first);
            try { long second = once.count(); show("second", second); } catch (Throwable _t) { System.out.println("second -> " + _t); }
            Supplier<Stream<Hit>> source = hits::stream;
            long errors = source.get().filter(h -> h.status() >= 400).count();             // 2
            show("errors", errors);
            List<Integer> times = hits.stream().map(Hit::millis).toList();
            int slowest = times.stream().max(Integer::compare).orElse(0);                  // 900
            show("slowest", slowest);
            double errorRate = hits.stream().collect(Collectors.teeing(Collectors.counting(), Collectors.filtering(h -> h.status() >= 400, Collectors.counting()), (all, bad) -> 100.0 * bad / all));   // 50.0
            show("errorRate", errorRate);
        }
        {
            List<Hit> hits = List.of(new Hit("/home", 200, 40), new Hit("/cart", 500, 900), new Hit("/home", 200, 60), new Hit("/login", 404, 15));
            Supplier<Stream<Hit>> hitStream = hits::stream;
            long requests = hitStream.get().count();                                       // 4
            show("requests", requests);
            OptionalDouble avgMillis = hitStream.get().mapToInt(Hit::millis).average();    // OptionalDouble[253.75]
            show("avgMillis", avgMillis);
            Supplier<IntStream> millis = () -> hits.stream().mapToInt(Hit::millis);
            int fastest = millis.get().min().orElse(0);                                    // 15
            show("fastest", fastest);
        }
        {
            List<Hit> hits = List.of(new Hit("/home", 200, 40), new Hit("/cart", 500, 900), new Hit("/home", 200, 60), new Hit("/login", 404, 15));
            Function<Integer, Stream<Hit>> withStatus = status -> hits.stream().filter(h -> h.status() == status);
            long ok = withStatus.apply(200).count();                                       // 2
            show("ok", ok);
            long notFound = withStatus.apply(404).count();                                 // 1
            show("notFound", notFound);
        }
        {
            List<Hit> hits = List.of(new Hit("/home", 200, 40), new Hit("/cart", 500, 900), new Hit("/home", 200, 60), new Hit("/login", 404, 15));
            Stream<Hit> stored = hits.stream();
            Supplier<Stream<Hit>> broken = () -> stored;
            long firstCall = broken.get().count();                                         // 4
            show("firstCall", firstCall);
            try { long secondCall = broken.get().count(); show("secondCall", secondCall); } catch (Throwable _t) { System.out.println("secondCall -> " + _t); }
        }
        {
            List<String> raw = List.of("/home 200 40", "/cart 500 900", "/home 200 60", "/login 404 15");
            List<Hit> parsed = raw.stream().map(Hit::parse).toList();
            long pages = parsed.stream().map(Hit::path).distinct().count();                // 3
            show("pages", pages);
            long slow = parsed.stream().filter(h -> h.millis() > 100).count();             // 1
            show("slow", slow);
            int totalMillis = parsed.stream().mapToInt(Hit::millis).sum();                 // 1015
            show("totalMillis", totalMillis);
        }
        {
            List<Hit> hits = List.of(new Hit("/home", 200, 40), new Hit("/cart", 500, 900), new Hit("/home", 200, 60), new Hit("/login", 404, 15));
            Predicate<Hit> isError = h -> h.status() >= 400;
            UnaryOperator<Stream<Hit>> slowErrors = s -> s.filter(isError).filter(h -> h.millis() > 100);
            Collector<Hit, ?, Map<Integer, Long>> perStatus = Collectors.groupingBy(Hit::status, TreeMap::new, Collectors.counting());
            long slowErrorCount = slowErrors.apply(hits.stream()).count();                 // 1
            show("slowErrorCount", slowErrorCount);
            Map<Integer, Long> statusCounts = hits.stream().collect(perStatus);            // {200=2, 404=1, 500=1}
            show("statusCounts", statusCounts);
            Map<Integer, Long> errorCounts = hits.stream().filter(isError).collect(perStatus);   // {404=1, 500=1}
            show("errorCounts", errorCounts);
        }
        {
            List<Hit> hits = List.of(new Hit("/home", 200, 40), new Hit("/cart", 500, 900), new Hit("/home", 200, 60), new Hit("/login", 404, 15));
            IntSummaryStatistics stats = hits.stream().mapToInt(Hit::millis).summaryStatistics();   // IntSummaryStatistics{count=4, sum=1015, min=15, average=253.750000, max=900}
            show("stats", stats);
            Map<Boolean, Long> errorSplit = hits.stream().collect(Collectors.partitioningBy(h -> h.status() >= 400, Collectors.counting()));   // {false=2, true=2}
            show("errorSplit", errorSplit);
            Map<String, Long> perPath = hits.stream().collect(Collectors.groupingBy(Hit::path, TreeMap::new, Collectors.counting()));       // {/cart=1, /home=2, /login=1}
            show("perPath", perPath);
            String range = hits.stream().collect(Collectors.teeing(Collectors.minBy(Comparator.comparingInt(Hit::millis)), Collectors.maxBy(Comparator.comparingInt(Hit::millis)), (min, max) -> min.orElseThrow().path() + " to " + max.orElseThrow().path()));   // "/login to /cart"
            show("range", range);
        }
        {
            BufferedReader upload = new BufferedReader(new StringReader("/home 200 40\n/cart 500 900\n/home 200 60\n/login 404 15"));
            Supplier<Stream<String>> lines = upload::lines;
            long firstCount = lines.get().count();                                         // 4
            show("firstCount", firstCount);
            long secondCount = lines.get().count();                                        // 0
            show("secondCount", secondCount);
        }
        {
            Report daily = report(new BufferedReader(new StringReader("/home 200 40\n/cart 500 900\n/home 200 60\n/login 404 15")));   // Report[requests=4, errorRate=50.0, slowestPath=/cart]
            show("daily", daily);
            Report empty = report(new BufferedReader(new StringReader("")));                                                               // Report[requests=0, errorRate=0.0, slowestPath=none]
            show("empty", empty);
        }
        {
            byte[] data = new ByteArrayInputStream("log".getBytes(StandardCharsets.UTF_8)).readAllBytes();
            int firstPass = new ByteArrayInputStream(data).readAllBytes().length;        // 3
            show("firstPass", firstPass);
            int secondPass = new ByteArrayInputStream(data).readAllBytes().length;       // 3
            show("secondPass", secondPass);
        }
        {
            Stream.Builder<String> builder = Stream.<String>builder().add("/home").add("/cart");
            long built = builder.build().count();                                         // 2
            show("built", built);
            try { Stream<String> again = builder.build(); show("again", again); } catch (Throwable _t) { System.out.println("again -> " + _t); }
        }
        {
            Iterable<String> paths = List.of("/home", "/cart");
            long firstRun = StreamSupport.stream(paths.spliterator(), false).count();    // 2
            show("firstRun", firstRun);
            long secondRun = StreamSupport.stream(paths.spliterator(), false).count();   // 2
            show("secondRun", secondRun);
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

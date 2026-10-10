package com.howtodoinjava.core.collections.map;

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
 * Examples for the tutorial "Java Map computeIfAbsent(): Multimaps, Caches and null Rules".
 * https://howtodoinjava.com/java/collections/hashmap/map-computeifabsent/
 */
public class ComputeIfAbsentExamples {
    static long fib(int n, Map<Integer, Long> cache) {
        if (n < 2) {
            return n;
        }
        return cache.computeIfAbsent(n, k -> fib(k - 1, cache) + fib(k - 2, cache));
    }
    static long fibSafe(int n, Map<Integer, Long> cache) {
        if (n < 2) {
            return n;
        }
        Long known = cache.get(n);
        if (known != null) {
            return known;
        }
        long value = fibSafe(n - 1, cache) + fibSafe(n - 2, cache);
        cache.put(n, value);
        return value;
    }
    public static void main(String[] args) throws Exception {
        {
            Map<String, List<String>> postsByTag = new HashMap<>();
            postsByTag.computeIfAbsent("java", tag -> new ArrayList<>()).add("Records");
            postsByTag.computeIfAbsent("java", tag -> new ArrayList<>()).add("Streams");
            List<String> javaPosts = postsByTag.get("java");                 // [Records, Streams]
            show("javaPosts", javaPosts);

            Map<Integer, Integer> squares = new HashMap<>(Map.of(3, 9));
            Integer cached = squares.computeIfAbsent(3, n -> n * n);          // 9 (function not called)
            show("cached", cached);
            Integer computed = squares.computeIfAbsent(4, n -> n * n);        // 16 (computed and stored)
            show("computed", computed);
            Integer nothing = squares.computeIfAbsent(5, n -> null);          // null (nothing stored)
            show("nothing", nothing);
        }
        {
            AtomicInteger calls = new AtomicInteger();
            Function<String, Double> loadRate = code -> {
                calls.incrementAndGet();                                     // remote call in a real app
                return code.equals("USD") ? 1.08 : 0.86;
            };

            Map<String, Double> rates = new HashMap<>();
            double first = rates.computeIfAbsent("USD", loadRate);           // 1.08 (loaded)
            show("first", first);
            double second = rates.computeIfAbsent("USD", loadRate);          // 1.08 (from the map)
            show("second", second);
            double pound = rates.computeIfAbsent("GBP", loadRate);           // 0.86 (loaded)
            show("pound", pound);
            int remoteCalls = calls.get();                                   // 2
            show("remoteCalls", remoteCalls);
        }
        {
            List<String> titles = List.of("Records", "Streams", "Sealed classes", "Spring Boot");
            Map<Character, List<String>> byLetter = new TreeMap<>();
            for (String title : titles) {
                byLetter.computeIfAbsent(title.charAt(0), letter -> new ArrayList<>()).add(title);
            }
            Map<Character, List<String>> index = byLetter;   // {R=[Records], S=[Streams, Sealed classes, Spring Boot]}
            show("index", index);
        }
        {
            List<String> titles = List.of("Records", "Streams", "Sealed classes", "Spring Boot");
            Map<Character, List<String>> byLetter = titles.stream().collect(Collectors.groupingBy(t -> t.charAt(0), TreeMap::new, Collectors.toList()));   // {R=[Records], S=[Streams, Sealed classes, Spring Boot]}
            show("byLetter", byLetter);
        }
        {
            Map<String, Integer> stock = new HashMap<>(Map.of("apple", 5));
            try { Integer value = stock.computeIfAbsent("apple", null); show("value", value); } catch (Throwable _t) { System.out.println("value -> " + _t); }
        }
        {
            Map<String, Integer> stock = new HashMap<>();
            Integer none = stock.computeIfAbsent("kiwi", k -> null);         // null
            show("none", none);
            boolean stored = stock.containsKey("kiwi");                       // false
            show("stored", stored);

            stock.put("plum", null);
            Integer replaced = stock.computeIfAbsent("plum", k -> 7);        // 7 (null value counts as absent)
            show("replaced", replaced);
        }
        {
            Map<String, Integer> stock = new HashMap<>();
            try { int unboxed = stock.computeIfAbsent("fig", k -> null); show("unboxed", unboxed); } catch (Throwable _t) { System.out.println("unboxed -> " + _t); }
            Integer safe = stock.computeIfAbsent("fig", k -> null);          // null
            show("safe", safe);
        }
        {
            Map<String, Integer> stock = new HashMap<>();
            try { Integer failed = stock.computeIfAbsent("kiwi", k -> Integer.parseInt("n/a")); show("failed", failed); } catch (Throwable _t) { System.out.println("failed -> " + _t); }
            boolean present = stock.containsKey("kiwi");                                     // false
            show("present", present);
        }
        {
            try { long broken = fib(30, new HashMap<>()); show("broken", broken); } catch (Throwable _t) { System.out.println("broken -> " + _t); }
        }
        {
            long fifty = fibSafe(50, new HashMap<>());                       // 12586269025
            show("fifty", fifty);
        }
        {
            Map<String, List<String>> postsByTag = new HashMap<>();
            try { boolean added = postsByTag.putIfAbsent("java", new ArrayList<>()).add("Records"); show("added", added); } catch (Throwable _t) { System.out.println("added -> " + _t); }
        }
        {
            Map<String, List<String>> postsByTag = new HashMap<>();
            boolean added = postsByTag.computeIfAbsent("java", tag -> new ArrayList<>()).add("Records");   // true
            show("added", added);
            List<String> posts = postsByTag.get("java");                                                 // [Records]
            show("posts", posts);
        }
        {
            Map<String, Integer> visits = new HashMap<>();
            visits.put("home", null);
            Integer skipped = visits.computeIfPresent("home", (k, v) -> v + 1);           // null (not called)
            show("skipped", skipped);
            Integer firstVisit = visits.compute("home", (k, v) -> v == null ? 1 : v + 1);  // 1
            show("firstVisit", firstVisit);
            Integer nextVisit = visits.compute("home", (k, v) -> v == null ? 1 : v + 1);   // 2
            show("nextVisit", nextVisit);
            Integer removed = visits.computeIfPresent("home", (k, v) -> null);            // null (entry removed)
            show("removed", removed);
            boolean exists = visits.containsKey("home");                                   // false
            show("exists", exists);
        }
        {
            ConcurrentMap<String, AtomicInteger> hits = new ConcurrentHashMap<>();
            try (ExecutorService pool = Executors.newVirtualThreadPerTaskExecutor()) {
                for (int i = 0; i < 1000; i++) {
                    pool.submit(() -> hits.computeIfAbsent("/home", url -> new AtomicInteger()).incrementAndGet());
                }
            }
            int total = hits.get("/home").get();                             // 1000
            show("total", total);
        }
        {
            Map<String, Integer> limits = new HashMap<>();
            int readOnly = limits.getOrDefault("guest", 10);                 // 10 (map unchanged)
            show("readOnly", readOnly);
            int stored = limits.computeIfAbsent("guest", k -> 10);           // 10 (map now has guest=10)
            show("stored", stored);
            int size = limits.size();                                        // 1
            show("size", size);
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

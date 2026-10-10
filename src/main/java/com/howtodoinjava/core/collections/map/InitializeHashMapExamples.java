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
 * Examples for the tutorial "Initialize HashMap in Java: Capacity, Load Factor, newHashMap".
 * https://howtodoinjava.com/java/collections/hashmap/initialize-hashmap-examples/
 */
public class InitializeHashMapExamples {
    static record Setting(String name, int value) {}
    static record Price(String sku, long cents) {}
    public static void main(String[] args) throws Exception {
        {
            Map<String, Integer> empty = new HashMap<>();                       // {}, 16 buckets on the first put
            show("empty", empty);
            Map<String, Integer> presized = HashMap.newHashMap(100);            // {}, holds 100 entries without a resize
            show("presized", presized);
            Map<String, Integer> tuned = new HashMap<>(64, 0.5f);               // {}, 64 buckets, resizes after 32 entries
            show("tuned", tuned);
            Map<String, Integer> config = new HashMap<>(Map.of("timeout", 30, "retries", 3));
            Integer previous = config.put("port", 8080);                        // null, the copy is mutable
            show("previous", previous);
            int size = config.size();                                           // 3
            show("size", size);
            Map<String, Integer> lengths = Stream.of("timeout", "port")
                    .collect(Collectors.toMap(k -> k, String::length, (a, b) -> a, HashMap::new));
            Integer portLength = lengths.get("port");                           // 4
            show("portLength", portLength);
        }
        {
            Map<String, Integer> settings = new HashMap<>();
            Integer old = settings.put("timeout", 30);           // null
            show("old", old);
            Integer timeout = settings.get("timeout");           // 30
            show("timeout", timeout);
        }
        {
            Map<String, Integer> ten = new HashMap<>(10);                       // 16 buckets, resizes on the 13th entry
            show("ten", ten);
            Map<String, Integer> sparse = new HashMap<>(64, 0.5f);              // 64 buckets, resizes on the 33rd entry
            show("sparse", sparse);
            try { Map<String, Integer> negative = new HashMap<>(-1); show("negative", negative); } catch (Throwable _t) { System.out.println("negative -> " + _t); }
            try { Map<String, Integer> zeroFactor = new HashMap<>(16, 0f); show("zeroFactor", zeroFactor); } catch (Throwable _t) { System.out.println("zeroFactor -> " + _t); }
        }
        {
            Map<String, Integer> forTen = HashMap.newHashMap(10);               // holds 10 entries without a resize
            show("forTen", forTen);
            try { Map<String, Integer> invalid = HashMap.newHashMap(-5); show("invalid", invalid); } catch (Throwable _t) { System.out.println("invalid -> " + _t); }
        }
        {
            Map<String, Integer> limits = HashMap.newHashMap(2);
            Integer first = limits.put("uploadMb", 25);          // null
            show("first", first);
            Integer second = limits.put("users", 500);           // null
            show("second", second);
            Integer users = limits.get("users");                 // 500
            show("users", users);
        }
        {
            Map<String, Integer> defaults = new HashMap<>(Map.of("timeout", 30, "retries", 3));
            Integer timeout = defaults.get("timeout");             // 30
            show("timeout", timeout);
            Integer old = defaults.put("timeout", 60);             // 30, the copy can change
            show("old", old);
            Map<String, Integer> many = new HashMap<>(Map.ofEntries(
            Map.entry("timeout", 30),
            Map.entry("retries", 3),
            Map.entry("port", 8080)));
            int count = many.size();                               // 3
            show("count", count);
        }
        {
            try { Map<String, Integer> duplicate = Map.of("timeout", 30, "timeout", 60); show("duplicate", duplicate); } catch (Throwable _t) { System.out.println("duplicate -> " + _t); }
            Map<String, Integer> readOnly = Map.of("timeout", 30);
            try { Integer changed = readOnly.put("timeout", 60); show("changed", changed); } catch (Throwable _t) { System.out.println("changed -> " + _t); }
        }
        {
            List<Setting> rows = List.of(new Setting("timeout", 30), new Setting("retries", 3));
            Map<String, Integer> byName = rows.stream().collect(Collectors.toMap(Setting::name, Setting::value));
            Integer retries = byName.get("retries");               // 3
            show("retries", retries);
            Integer added = byName.put("port", 8080);              // null, the result is mutable
            show("added", added);
        }
        {
            List<Setting> withDuplicates = List.of(new Setting("timeout", 30), new Setting("timeout", 45));
            try { Map<String, Integer> failing = withDuplicates.stream().collect(Collectors.toMap(Setting::name, Setting::value)); show("failing", failing); } catch (Throwable _t) { System.out.println("failing -> " + _t); }
            HashMap<String, Integer> lastWins = withDuplicates.stream()
                    .collect(Collectors.toMap(Setting::name, Setting::value, (oldValue, newValue) -> newValue, HashMap::new));
            Integer timeout = lastWins.get("timeout");                                  // 45
            show("timeout", timeout);
        }
        {
            List<Setting> withDuplicates = List.of(new Setting("timeout", 30), new Setting("timeout", 45));
            Map<String, List<Integer>> grouped = withDuplicates.stream()
                    .collect(Collectors.groupingBy(Setting::name, Collectors.mapping(Setting::value, Collectors.toList())));
            List<Integer> timeouts = grouped.get("timeout");                            // [30, 45]
            show("timeouts", timeouts);
        }
        {
            List<Price> rows = List.of(new Price("tea", 450), new Price("mug", 1200));
            Map<String, Long> prices = HashMap.newHashMap(rows.size());
            rows.forEach(row -> prices.put(row.sku(), row.cents()));
            Long mug = prices.get("mug");                         // 1200
            show("mug", mug);
            Long updated = prices.put("tea", 480L);               // 450, price change event
            show("updated", updated);
        }
        {
            Map<String, Integer> ports = new HashMap<>(Map.of("http", 80, "https", 443));
            Integer https = ports.get("https");                    // 443
            show("https", https);
        }
        {
            Map<String, Integer> nullable = new HashMap<>(Map.of("timeout", 30));
            Integer none = nullable.put("proxy", null);              // null, HashMap accepts null values
            show("none", none);
            boolean hasProxy = nullable.containsKey("proxy");        // true
            show("hasProxy", hasProxy);
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

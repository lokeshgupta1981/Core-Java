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

import java.math.BigDecimal;

/**
 * Examples for the tutorial "Compare Two Maps in Java: equals(), Keys, Values and Diff".
 * https://howtodoinjava.com/java/collections/hashmap/compare-two-hashmaps/
 */
public class CompareTwoMaps {
    static <K> boolean equalArrayValues(Map<K, String[]> first, Map<K, String[]> second) {
        if (first.size() != second.size()) {
            return false;
        }
        return first.entrySet().stream().allMatch(e -> second.containsKey(e.getKey()) && Arrays.equals(e.getValue(), second.get(e.getKey())));
    }
    static record Change(String before, String after) {}
    public static void main(String[] args) throws Exception {
        {
            Map<String, Integer> limits = Map.of("free", 10, "pro", 100);
            Map<String, Integer> sameLimits = new TreeMap<>(Map.of("pro", 100, "free", 10));
            Map<String, Integer> otherLimits = Map.of("free", 10, "pro", 50);

            boolean equal = limits.equals(sameLimits);                              // true
            show("equal", equal);
            boolean different = limits.equals(otherLimits);                         // false
            show("different", different);
        }
        {
            Map<String, String[]> hostsA = Map.of("eu", new String[] {"fra", "ams"});
            Map<String, String[]> hostsB = Map.of("eu", new String[] {"fra", "ams"});
            boolean byEquals = hostsA.equals(hostsB);                               // false (array identity)
            show("byEquals", byEquals);
        }
        {
            Map<String, String[]> hostsA = Map.of("eu", new String[] {"fra", "ams"});
            Map<String, String[]> hostsB = Map.of("eu", new String[] {"fra", "ams"});
            boolean byContent = equalArrayValues(hostsA, hostsB);                   // true
            show("byContent", byContent);
        }
        {
            Map<String, String> v1 = Map.of("timeout", "30", "retries", "3");
            Map<String, String> v2 = Map.of("timeout", "60", "retries", "3");
            Map<String, String> v3 = Map.of("timeout", "30", "retries", "3", "cache", "on");

            boolean sameKeys = v1.keySet().equals(v2.keySet());                     // true
            show("sameKeys", sameKeys);
            boolean moreKeys = v1.keySet().equals(v3.keySet());                     // false
            show("moreKeys", moreKeys);
            boolean covers = v3.keySet().containsAll(v1.keySet());                  // true
            show("covers", covers);
        }
        {
            Map<String, String> v1 = Map.of("timeout", "30", "retries", "3", "theme", "light");
            Map<String, String> v2 = Map.of("timeout", "60", "retries", "3", "cache", "on");

            Set<String> onlyInV1 = new TreeSet<>(v1.keySet());
            onlyInV1.removeAll(v2.keySet());
            Set<String> removedKeys = onlyInV1;                                     // [theme]
            show("removedKeys", removedKeys);

            Set<String> onlyInV2 = new TreeSet<>(v2.keySet());
            onlyInV2.removeAll(v1.keySet());
            Set<String> addedKeys = onlyInV2;                                       // [cache]
            show("addedKeys", addedKeys);

            Set<String> common = new TreeSet<>(v1.keySet());
            common.retainAll(v2.keySet());
            Set<String> commonKeys = common;                                        // [retries, timeout]
            show("commonKeys", commonKeys);
        }
        {
            Map<String, Integer> stockA = Map.of("x1", 5, "x2", 5, "x3", 8);
            Map<String, Integer> stockB = Map.of("y1", 8, "y2", 5, "y3", 5);
            Map<String, Integer> stockC = Map.of("z1", 5, "z2", 8, "z3", 8);

            Map<Integer, Long> countsA = stockA.values().stream().collect(Collectors.groupingBy(v -> v, Collectors.counting()));
            boolean sameAsB = countsA.equals(stockB.values().stream().collect(Collectors.groupingBy(v -> v, Collectors.counting())));   // true
            show("sameAsB", sameAsB);
            boolean sameAsC = countsA.equals(stockC.values().stream().collect(Collectors.groupingBy(v -> v, Collectors.counting())));   // false
            show("sameAsC", sameAsC);
        }
        {
            Map<String, Integer> stockA = Map.of("x1", 5, "x2", 5, "x3", 8);
            Map<String, Integer> stockC = Map.of("z1", 5, "z2", 8, "z3", 8);
            boolean sameDistinct = new HashSet<>(stockA.values()).equals(new HashSet<>(stockC.values()));   // true
            show("sameDistinct", sameDistinct);
        }
        {
            Map<String, String> v1 = Map.of("timeout", "30", "retries", "3", "theme", "light");
            Map<String, String> v2 = Map.of("timeout", "60", "retries", "3", "cache", "on");
            Map<String, Change> changes = v1.keySet().stream().filter(v2::containsKey).filter(k -> !Objects.equals(v1.get(k), v2.get(k))).collect(Collectors.toMap(k -> k, k -> new Change(v1.get(k), v2.get(k)), (a, b) -> a, TreeMap::new));   // {timeout=Change[before=30, after=60]}
            show("changes", changes);
        }
        {
            Map<String, Integer> columnsA = new LinkedHashMap<>();
            columnsA.put("name", 20);
            columnsA.put("email", 30);
            Map<String, Integer> columnsB = new LinkedHashMap<>();
            columnsB.put("email", 30);
            columnsB.put("name", 20);

            boolean unordered = columnsA.equals(columnsB);                          // true
            show("unordered", unordered);
            boolean ordered = new ArrayList<>(columnsA.entrySet()).equals(new ArrayList<>(columnsB.entrySet()));   // false
            show("ordered", ordered);
        }
        {
            Map<String, BigDecimal> priceA = Map.of("pen", new BigDecimal("1.0"));
            Map<String, BigDecimal> priceB = Map.of("pen", new BigDecimal("1.00"));
            boolean byEquals = priceA.equals(priceB);                               // false
            show("byEquals", byEquals);
            boolean byValue = priceA.keySet().equals(priceB.keySet()) && priceA.entrySet().stream().allMatch(e -> e.getValue().compareTo(priceB.get(e.getKey())) == 0);   // true
            show("byValue", byValue);
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

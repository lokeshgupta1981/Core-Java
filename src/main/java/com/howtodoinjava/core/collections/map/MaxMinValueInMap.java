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
 * Examples for the tutorial "Max Value in a Java Map: Find the Largest, Smallest and Key".
 * https://howtodoinjava.com/java/collections/hashmap/smallest-largest-value-in-map/
 */
public class MaxMinValueInMap {
    static record Product(String name, double price) {}
    public static void main(String[] args) throws Exception {
        {
            Map<String, Integer> stock = Map.of("apple", 5, "banana", 3, "cherry", 8);

            Optional<Map.Entry<String, Integer>> maxEntry = stock.entrySet().stream().max(Map.Entry.comparingByValue());   // Optional[cherry=8]
            show("maxEntry", maxEntry);
            Optional<Map.Entry<String, Integer>> minEntry = stock.entrySet().stream().min(Map.Entry.comparingByValue());   // Optional[banana=3]
            show("minEntry", minEntry);

            String maxKey = maxEntry.map(Map.Entry::getKey).orElse("none");     // "cherry"
            show("maxKey", maxKey);
            int maxValue = maxEntry.map(Map.Entry::getValue).orElse(0);         // 8
            show("maxValue", maxValue);
            int minValue = Collections.min(stock.values());                     // 3
            show("minValue", minValue);
        }
        {
            Map<String, Integer> stock = Map.of("apple", 5, "banana", 3, "cherry", 8);
            Optional<Map.Entry<String, Integer>> maxEntry = stock.entrySet().stream().max(Map.Entry.comparingByValue());   // Optional[cherry=8]
            show("maxEntry", maxEntry);
            String maxKey = maxEntry.map(Map.Entry::getKey).orElse("none");     // "cherry"
            show("maxKey", maxKey);
            String minKey = stock.entrySet().stream().min(Map.Entry.comparingByValue()).map(Map.Entry::getKey).orElse("none");   // "banana"
            show("minKey", minKey);
        }
        {
            Map<String, Integer> stock = Map.of("apple", 5, "banana", 3, "cherry", 8);
            Optional<Integer> maxStock = stock.values().stream().max(Comparator.naturalOrder());   // Optional[8]
            show("maxStock", maxStock);
            int maxOrZero = stock.values().stream().mapToInt(Integer::intValue).max().orElse(0);   // 8
            show("maxOrZero", maxOrZero);
        }
        {
            Map<String, Integer> empty = new HashMap<>();
            String noKey = empty.entrySet().stream().max(Map.Entry.comparingByValue()).map(Map.Entry::getKey).orElse("none");   // "none"
            show("noKey", noKey);
            try { Map.Entry<String, Integer> crash = empty.entrySet().stream().max(Map.Entry.comparingByValue()).get(); show("crash", crash); } catch (Throwable _t) { System.out.println("crash -> " + _t); }
        }
        {
            Map<String, Integer> stock = Map.of("apple", 5, "banana", 3, "cherry", 8);
            int maxValue = Collections.max(stock.values());                                                  // 8
            show("maxValue", maxValue);
            int minValue = Collections.min(stock.values());                                                  // 3
            show("minValue", minValue);
            Map.Entry<String, Integer> maxEntry = Collections.max(stock.entrySet(), Map.Entry.comparingByValue());   // cherry=8
            show("maxEntry", maxEntry);
            Map.Entry<String, Integer> minEntry = Collections.min(stock.entrySet(), Map.Entry.comparingByValue());   // banana=3
            show("minEntry", minEntry);
        }
        {
            Map<String, Integer> empty = new HashMap<>();
            try { int failed = Collections.max(empty.values()); show("failed", failed); } catch (Throwable _t) { System.out.println("failed -> " + _t); }
            int safeMax = empty.isEmpty() ? 0 : Collections.max(empty.values());     // 0
            show("safeMax", safeMax);
        }
        {
            Map<String, Integer> stock = Map.of("apple", 5, "banana", 3, "cherry", 8);
            Map.Entry<String, Integer> maxEntry = null;
            Map.Entry<String, Integer> minEntry = null;
            for (Map.Entry<String, Integer> entry : stock.entrySet()) {
                if (maxEntry == null || entry.getValue() > maxEntry.getValue()) {
                    maxEntry = entry;
                }
                if (minEntry == null || entry.getValue() < minEntry.getValue()) {
                    minEntry = entry;
                }
            }
            String largest = maxEntry.toString();                                    // "cherry=8"
            show("largest", largest);
            String smallest = minEntry.toString();                                   // "banana=3"
            show("smallest", smallest);
        }
        {
            Map<String, Integer> stock = Map.of("apple", 5, "banana", 3, "cherry", 8);
            IntSummaryStatistics stats = stock.values().stream().mapToInt(Integer::intValue).summaryStatistics();
            int max = stats.getMax();                                                // 8
            show("max", max);
            int min = stats.getMin();                                                // 3
            show("min", min);
            long sum = stats.getSum();                                               // 16
            show("sum", sum);
        }
        {
            Map<String, Integer> scores = new LinkedHashMap<>();
            scores.put("anna", 8);
            scores.put("ben", 3);
            scores.put("carl", 8);

            Optional<Map.Entry<String, Integer>> firstTop = scores.entrySet().stream().max(Map.Entry.comparingByValue());   // Optional[anna=8]
            show("firstTop", firstTop);
            int topScore = Collections.max(scores.values());                                                                 // 8
            show("topScore", topScore);
            List<String> winners = scores.entrySet().stream().filter(e -> e.getValue() == topScore).map(Map.Entry::getKey).toList();   // [anna, carl]
            show("winners", winners);
        }
        {
            Map<String, Integer> visits = new LinkedHashMap<>();
            visits.put("home", 1000);
            visits.put("blog", 1000);
            Integer topVisits = Collections.max(visits.values());
            List<String> wrong = visits.entrySet().stream().filter(e -> e.getValue() == topVisits).map(Map.Entry::getKey).toList();        // [home]
            show("wrong", wrong);
            List<String> right = visits.entrySet().stream().filter(e -> e.getValue().equals(topVisits)).map(Map.Entry::getKey).toList();   // [home, blog]
            show("right", right);
        }
        {
            Map<String, Integer> stock = Map.of("apple", 5, "banana", 3, "cherry", 8);
            TreeMap<String, Integer> sorted = new TreeMap<>(stock);
            Map.Entry<String, Integer> first = sorted.firstEntry();                  // apple=5
            show("first", first);
            Map.Entry<String, Integer> last = sorted.lastEntry();                    // cherry=8
            show("last", last);
            String maxKey = Collections.max(stock.keySet());                         // "cherry"
            show("maxKey", maxKey);
        }
        {
            Map<String, Integer> stock = Map.of("apple", 5, "banana", 3, "cherry", 8);
            List<String> topTwo = stock.entrySet().stream().sorted(Map.Entry.<String, Integer>comparingByValue().reversed()).limit(2).map(Map.Entry::getKey).toList();   // [cherry, apple]
            show("topTwo", topTwo);
        }
        {
            Map<String, Integer> withNull = new HashMap<>();
            withNull.put("apple", 5);
            withNull.put("banana", null);
            withNull.put("cherry", 8);
            try { Optional<Map.Entry<String, Integer>> crash = withNull.entrySet().stream().max(Map.Entry.comparingByValue()); show("crash", crash); } catch (Throwable _t) { System.out.println("crash -> " + _t); }
            Optional<Map.Entry<String, Integer>> skipNulls = withNull.entrySet().stream().filter(e -> e.getValue() != null).max(Map.Entry.comparingByValue());   // Optional[cherry=8]
            show("skipNulls", skipNulls);
            Optional<Map.Entry<String, Integer>> nullsFirst = withNull.entrySet().stream().max(Map.Entry.comparingByValue(Comparator.nullsFirst(Comparator.naturalOrder())));   // Optional[cherry=8]
            show("nullsFirst", nullsFirst);
        }
        {
            Map<String, Product> products = Map.of("p1", new Product("apple", 1.5), "p2", new Product("cherry", 4.0), "p3", new Product("banana", 0.5));
            Optional<Map.Entry<String, Product>> priciest = products.entrySet().stream().max(Comparator.comparingDouble(e -> e.getValue().price()));   // Optional[p2=Product[name=cherry, price=4.0]]
            show("priciest", priciest);
            String priciestId = priciest.map(Map.Entry::getKey).orElse("none");     // "p2"
            show("priciestId", priciestId);
        }
        {
            Map<String, Integer> stock = Map.of("apple", 5, "banana", 3, "cherry", 8);
            Comparator<Map.Entry<String, Integer>> byValue = Map.Entry.comparingByValue();
            String range = stock.entrySet().stream().collect(Collectors.teeing(Collectors.minBy(byValue), Collectors.maxBy(byValue), (lo, hi) -> lo.map(Map.Entry::getKey).orElse("-") + " to " + hi.map(Map.Entry::getKey).orElse("-")));   // "banana to cherry"
            show("range", range);
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

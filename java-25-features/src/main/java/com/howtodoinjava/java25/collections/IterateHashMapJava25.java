package com.howtodoinjava.java25.collections;

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
 * Examples for the tutorial "Iterate HashMap in Java: Fastest Way With JMH Results".
 * https://howtodoinjava.com/java/collections/hashmap/performance-comparison-of-different-ways-to-iterate-over-hashmap/
 */
public class IterateHashMapJava25 {
    static record ReportLine(String sku, int units, boolean reorder) {}
    public static void main(String[] args) throws Exception {
        {
            Map<String, Integer> stock = new HashMap<>(Map.of("tea", 5, "mug", 2, "pot", 3));
            int a = 0;
            for (Map.Entry<String, Integer> e : stock.entrySet()) { a += e.getValue(); }
            int viaEntrySet = a;                                                      // 10
            show("viaEntrySet", viaEntrySet);
            int[] b = {0};
            stock.forEach((sku, units) -> b[0] += units);
            int viaForEach = b[0];                                                    // 10
            show("viaForEach", viaForEach);
            int viaValues = stock.values().stream().mapToInt(Integer::intValue).sum(); // 10
            show("viaValues", viaValues);
            int c = 0;
            for (String sku : stock.keySet()) { c += stock.get(sku); }
            int viaKeySet = c;                                                        // 10, one extra get() per key
            show("viaKeySet", viaKeySet);
        }
        {
            Map<String, Integer> stock = new HashMap<>(Map.of("tea", 5, "mug", 0));
            List<String> soldOut = new ArrayList<>();
            for (Map.Entry<String, Integer> entry : stock.entrySet()) {
                if (entry.getValue() == 0) {
                    soldOut.add(entry.getKey());
                }
            }
            List<String> result = soldOut;                     // [mug]
            show("result", result);
        }
        {
            Map<String, Integer> stock = new HashMap<>(Map.of("tea", 5, "mug", 2));
            int units = 0;
            for (var entry : stock.entrySet()) {
                units += entry.getValue();
            }
            int total = units;                                 // 7
            show("total", total);
            int[] sum = {0};
            stock.forEach((_, u) -> sum[0] += u);
            int viaUnnamed = sum[0];                           // 7
            show("viaUnnamed", viaUnnamed);
        }
        {
            Map<String, Integer> stock = new HashMap<>(Map.of("tea", 5, "mug", 2));
            StringBuilder csv = new StringBuilder();
            stock.forEach((sku, units) -> csv.append(sku).append(',').append(units).append('\n'));
            int lines = csv.toString().lines().toList().size();         // 2
            show("lines", lines);
        }
        {
            Map<String, Integer> stock = new HashMap<>(Map.of("tea", 5, "mug", 2));
            Set<String> skus = new TreeSet<>(stock.keySet());          // [mug, tea]
            show("skus", skus);
            int units = 0;
            for (int u : stock.values()) {
                units += u;
            }
            int total = units;                                         // 7
            show("total", total);
        }
        {
            Map<String, Integer> stock = new HashMap<>(Map.of("tea", 5, "mug", 0, "pot", 0));
            Iterator<Map.Entry<String, Integer>> it = stock.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, Integer> entry = it.next();
                if (entry.getValue() == 0) {
                    it.remove();
                }
            }
            int left = stock.size();                                   // 1
            show("left", left);
        }
        {
            Map<String, Integer> stock = new HashMap<>(Map.of("tea", 5, "mug", 2, "pot", 0));
            List<String> lowStock = stock.entrySet().stream()
                    .filter(e -> e.getValue() < 3)
                    .map(Map.Entry::getKey)
                    .sorted()
                    .toList();
            List<String> result = lowStock;                            // [mug, pot]
            show("result", result);
        }
        {
            Map<String, Integer> stock = new HashMap<>(Map.of("tea", 5, "mug", 0, "pot", 3));
            try { stock.keySet().forEach(sku -> { if (stock.get(sku) == 0) stock.remove(sku); });  } catch (Throwable _t) { System.out.println("-> " + _t); }
        }
        {
            Map<String, Integer> stock = new HashMap<>(Map.of("tea", 5, "mug", 0, "pot", 0));
            boolean removed = stock.values().removeIf(units -> units == 0);      // true
            show("removed", removed);
            int left = stock.size();                                             // 1
            show("left", left);
            Map<String, Integer> prices = new HashMap<>(Map.of("tea", 450, "mug", 1200));
            prices.replaceAll((sku, cents) -> cents + 50);
            Integer mug = prices.get("mug");                                     // 1250
            show("mug", mug);
        }
        {
            Map<String, Integer> stock = new HashMap<>(Map.of("tea", 5, "mug", 2));
            for (Map.Entry<String, Integer> entry : stock.entrySet()) {
                entry.setValue(entry.getValue() * 2);
            }
            Integer tea = stock.get("tea");                            // 10
            show("tea", tea);
        }
        {
            Map<String, Integer> stock = new HashMap<>(Map.of("tea", 5, "mug", 2, "pot", 3));
            List<String> byKey = stock.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .map(e -> e.getKey() + "=" + e.getValue())
                    .toList();                                         // [mug=2, pot=3, tea=5]
            List<String> keys = byKey;                                 // [mug=2, pot=3, tea=5]
            show("keys", keys);
            LinkedHashMap<String, Integer> ordered = new LinkedHashMap<>();
            Integer p1 = ordered.put("tea", 5);                        // null
            show("p1", p1);
            Integer p2 = ordered.put("mug", 2);                        // null
            show("p2", p2);
            String last = ordered.reversed().firstEntry().getKey();    // "mug"
            show("last", last);
        }
        {
            Map<String, Integer> stock = new HashMap<>(Map.of("tea", 40, "mug", 3, "pot", 0, "cup", 12));
            List<ReportLine> report = stock.entrySet().stream()
                    .filter(e -> e.getValue() > 0)
                    .sorted(Map.Entry.comparingByKey())
                    .map(e -> new ReportLine(e.getKey(), e.getValue(), e.getValue() < 10))
                    .toList();
            int lines = report.size();                                  // 3
            show("lines", lines);
            String first = report.getFirst().sku();                     // "cup"
            show("first", first);
            boolean mugReorder = report.get(1).reorder();               // true
            show("mugReorder", mugReorder);
        }
        {
            Map<String, Integer> stock = new HashMap<>(Map.of("tea", 5, "mug", 0));
            boolean removed = stock.entrySet().removeIf(e -> e.getValue() == 0);   // true
            show("removed", removed);
        }
        {
            TreeMap<String, Integer> sorted = new TreeMap<>(Map.of("tea", 5, "mug", 2, "pot", 3));
            String firstKey = sorted.reversed().firstKey();            // "tea"
            show("firstKey", firstKey);
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

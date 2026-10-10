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
 * Examples for the tutorial "ConcurrentHashMap vs synchronizedMap: Synchronize a HashMap".
 * https://howtodoinjava.com/java/collections/hashmap/synchronize-hashmap/
 */
public class ConcurrentVsSynchronizedMap {
    static boolean reserve(ConcurrentHashMap<String, Integer> stock, String sku) {
        boolean[] ok = {false};
        stock.computeIfPresent(sku, (key, units) -> {
            if (units == 0) {
                return 0;
            }
            ok[0] = true;
            return units - 1;
        });
        return ok[0];
    }
    public static void main(String[] args) throws Exception {
        {
            Map<String, Integer> wrapped = Collections.synchronizedMap(new HashMap<>());
            ConcurrentHashMap<String, Integer> concurrent = new ConcurrentHashMap<>();
            Integer w1 = wrapped.merge("tea", 1, Integer::sum);          // 1, one lock for the whole map
            show("w1", w1);
            Integer c1 = concurrent.merge("tea", 1, Integer::sum);       // 1, locks only the bucket of "tea"
            show("c1", c1);
            Integer w2 = wrapped.put("note", null);                      // null, HashMap accepts null values
            show("w2", w2);
            try { Integer c2 = concurrent.put("note", null); show("c2", c2); } catch (Throwable _t) { System.out.println("c2 -> " + _t); }
        }
        {
            Map<String, Integer> unsafe = new HashMap<>();
            try (ExecutorService pool = Executors.newVirtualThreadPerTaskExecutor()) {
                for (int i = 0; i < 1_000; i++) {
                    pool.submit(() -> unsafe.merge("views", 1, Integer::sum));
                }
            }
            Integer views = unsafe.get("views");                         // often below 1000, varies per run
            show("views", views);
        }
        {
            Map<String, Integer> stock = Collections.synchronizedMap(new HashMap<>());
            Integer before = stock.put("tea", 5);                        // null
            show("before", before);
            Integer units = stock.get("tea");                            // 5
            show("units", units);
            Integer missing = stock.getOrDefault("mug", 0);              // 0
            show("missing", missing);
        }
        {
            ConcurrentHashMap<String, Integer> stock = new ConcurrentHashMap<>();
            Integer added = stock.putIfAbsent("tea", 5);                 // null
            show("added", added);
            Integer kept = stock.putIfAbsent("tea", 9);                  // 5, the existing value stays
            show("kept", kept);
            Integer reserved = stock.compute("tea", (sku, n) -> n - 1);  // 4
            show("reserved", reserved);
            long count = stock.mappingCount();                           // 1
            show("count", count);
        }
        {
            Map<String, Integer> stock = Collections.synchronizedMap(new HashMap<>());
            if (!stock.containsKey("tea")) {     // thread A checks
                stock.put("tea", 5);             // thread B may have put "tea" in between
            }
        }
        {
            Map<String, Integer> wrapped = Collections.synchronizedMap(new HashMap<>());
            ConcurrentHashMap<String, Integer> concurrent = new ConcurrentHashMap<>();
            Integer w = wrapped.putIfAbsent("tea", 5);                   // null, atomic on the wrapper too
            show("w", w);
            Integer c = concurrent.putIfAbsent("tea", 5);                // null
            show("c", c);
            Integer sold = concurrent.merge("tea", -1, Integer::sum);    // 4
            show("sold", sold);
        }
        {
            Map<String, Integer> shelf = Collections.synchronizedMap(new HashMap<>(Map.of("tea", 5, "teaGift", 0)));
            synchronized (shelf) {
                shelf.merge("tea", -1, Integer::sum);
                shelf.merge("teaGift", 1, Integer::sum);
            }
            Integer tea = shelf.get("tea");                              // 4
            show("tea", tea);
            Integer gifts = shelf.get("teaGift");                        // 1
            show("gifts", gifts);
        }
        {
            Map<String, Integer> stock = Collections.synchronizedMap(new HashMap<>(Map.of("tea", 5, "mug", 2)));
            int total = 0;
            synchronized (stock) {               // lock the wrapper, not the keySet() or values() view
                for (int units : stock.values()) {
                    total += units;
                }
            }
            int sum = total;                                             // 7
            show("sum", sum);
        }
        {
            Map<String, Integer> stock = Collections.synchronizedMap(new HashMap<>(Map.of("tea", 5, "mug", 0)));
            boolean removed = stock.values().removeIf(units -> units == 0);   // true
            show("removed", removed);
            int left = stock.size();                                          // 1
            show("left", left);
        }
        {
            ConcurrentHashMap<String, Integer> stock = new ConcurrentHashMap<>(Map.of("tea", 5, "mug", 0, "pot", 0));
            for (String sku : stock.keySet()) {
                stock.remove(sku, 0);            // removes only if the value is still 0
            }
            int left = stock.size();                                     // 1
            show("left", left);
        }
        {
            ConcurrentHashMap<String, Integer> stock = new ConcurrentHashMap<>(Map.of("lamp", 1));
            boolean first = reserve(stock, "lamp");                      // true
            show("first", first);
            boolean second = reserve(stock, "lamp");                     // false, sold out
            show("second", second);
            boolean unknown = reserve(stock, "desk");                    // false, no such product
            show("unknown", unknown);
            Integer lamps = stock.get("lamp");                           // 0
            show("lamps", lamps);
        }
        {
            Map<String, Integer> existing = new HashMap<>(Map.of("tea", 5));
            Map<String, Integer> wrapped = Collections.synchronizedMap(existing);   // same entries, one lock
            show("wrapped", wrapped);
            ConcurrentHashMap<String, Integer> copy = new ConcurrentHashMap<>(existing);
            Integer tea = copy.get("tea");                                          // 5
            show("tea", tea);
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

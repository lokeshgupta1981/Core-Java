package com.howtodoinjava.core.collections.map;

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
 * Examples for the tutorial "Java ConcurrentMap: Atomic Methods and Implementations".
 * https://howtodoinjava.com/java/collections/java-concurrentmap/
 */
public class ConcurrentMapAtomicOps {
    static double loadRate(String currency) {
        return currency.equals("EUR") ? 0.92 : 1.0;           // stands in for a remote call
    }
    static void track(String endpoint) {
        HITS.computeIfAbsent(endpoint, k -> new LongAdder()).increment();
    }

    static final ConcurrentMap<String, LongAdder> HITS = new ConcurrentHashMap<>();
    public static void main(String[] args) throws Exception {
        {
            ConcurrentMap<String, Integer> views = new ConcurrentHashMap<>();
            views.put("home", 1);

            Integer existing = views.putIfAbsent("home", 5);                      // 1, no change
            show("existing", existing);
            Integer added = views.putIfAbsent("cart", 2);                         // null, key added
            show("added", added);
            int home = views.merge("home", 1, Integer::sum);                      // 2
            show("home", home);
            int cart = views.compute("cart", (k, v) -> v == null ? 1 : v * 10);   // 20
            show("cart", cart);
            boolean replaced = views.replace("cart", 20, 0);                      // true
            show("replaced", replaced);
            boolean removed = views.remove("home", 99);                           // false, value is 2
            show("removed", removed);
            int missing = views.getOrDefault("login", 0);                         // 0
            show("missing", missing);
        }
        {
            ConcurrentMap<String, Integer> racy = new ConcurrentHashMap<>();
            try (ExecutorService pool = Executors.newFixedThreadPool(4)) {
                for (int i = 0; i < 10_000; i++) {
                    pool.submit(() -> {
                        Integer v = racy.get("home");               // two threads can read the same value
                        racy.put("home", v == null ? 1 : v + 1);
                    });
                }
            }
            int lost = racy.get("home");                            // often less than 10000, differs per run
            show("lost", lost);
        }
        {
            ConcurrentMap<String, Integer> safe = new ConcurrentHashMap<>();
            try (ExecutorService pool = Executors.newFixedThreadPool(4)) {
                for (int i = 0; i < 10_000; i++) {
                    pool.submit(() -> safe.merge("home", 1, Integer::sum));
                }
            }
            int counted = safe.get("home");                         // 10000
            show("counted", counted);
        }
        {
            ConcurrentMap<String, Integer> seats = new ConcurrentHashMap<>(Map.of("show", 1));
            Integer left = seats.computeIfPresent("show", (k, v) -> v > 1 ? v - 1 : null);   // null, entry removed
            show("left", left);
            boolean stillThere = seats.containsKey("show");                                 // false
            show("stillThere", stillThere);
        }
        {
            ConcurrentMap<String, Double> rates = new ConcurrentHashMap<>();
            double eur = rates.computeIfAbsent("EUR", k -> loadRate(k));    // 0.92, loaded once
            show("eur", eur);
            double again = rates.computeIfAbsent("EUR", k -> 99.0);          // 0.92, function not called
            show("again", again);
        }
        {
            ConcurrentMap<String, Integer> stock = new ConcurrentHashMap<>();
            try { stock.put("apple", null);  } catch (Throwable _t) { System.out.println("-> " + _t); }
        }
        {
            ConcurrentMap<String, Optional<Integer>> prices = new ConcurrentHashMap<>();
            prices.put("apple", Optional.empty());                  // stored, means "no price yet"
            int price = prices.get("apple").orElse(0);              // 0
            show("price", price);
        }
        {
            ConcurrentMap<String, Integer> plain = new ConcurrentHashMap<>();                  // 16 bins, load factor 0.75
            show("plain", plain);
            ConcurrentMap<String, Integer> sized = new ConcurrentHashMap<>(1_000);             // sized for 1000 mappings
            show("sized", sized);
            ConcurrentMap<String, Integer> copied = new ConcurrentHashMap<>(Map.of("a", 1));   // copies a map
            show("copied", copied);
            Set<String> users = ConcurrentHashMap.newKeySet();                                 // a concurrent Set
            show("users", users);
            boolean addedUser = users.add("lokesh");                                           // true
            show("addedUser", addedUser);
        }
        {
            ConcurrentNavigableMap<Integer, String> scores = new ConcurrentSkipListMap<>();
            scores.put(70, "bob");
            scores.put(95, "lokesh");
            scores.put(82, "alex");
            String best = scores.lastEntry().getValue();            // "lokesh"
            show("best", best);
            SortedMap<Integer, String> passed = scores.tailMap(80); // {82=alex, 95=lokesh}
            show("passed", passed);
        }
        {
            ConcurrentMap<String, Integer> carts = new ConcurrentHashMap<>(Map.of("a", 1, "b", 0, "c", 3));
            for (String user : carts.keySet()) {
                carts.remove("b");                                  // no ConcurrentModificationException
            }
            int total = carts.values().stream().mapToInt(Integer::intValue).sum();   // 4
            show("total", total);
            long nonEmpty = carts.entrySet().stream().filter(e -> e.getValue() > 0).count();   // 2
            show("nonEmpty", nonEmpty);
        }
        {
            ConcurrentHashMap<String, Integer> stockLevels = new ConcurrentHashMap<>(Map.of("apple", 5, "pear", 0, "plum", 7));
            long count = stockLevels.mappingCount();                                        // 3
            show("count", count);
            Integer sum = stockLevels.reduceValues(1_000, Integer::sum);                    // 12
            show("sum", sum);
            String outOfStock = stockLevels.search(1_000, (k, v) -> v == 0 ? k : null);     // "pear"
            show("outOfStock", outOfStock);
        }
        {
            track("/orders");
            track("/orders");
            track("/login");
            long orders = HITS.get("/orders").sum();                // 2
            show("orders", orders);
            long login = HITS.get("/login").sum();                  // 1
            show("login", login);
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

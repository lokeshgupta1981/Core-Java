package com.howtodoinjava.concurrency;

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

/**
 * Examples for the tutorial "ConcurrentHashMap in Java: Internals, Atomic Methods, Pitfalls".
 * https://howtodoinjava.com/java/multi-threading/best-practices-for-using-concurrenthashmap/
 */
public class ConcurrentHashMapGuide {
    static class PriceCache {
        private final ConcurrentHashMap<String, Double> prices = new ConcurrentHashMap<>();
        private final AtomicInteger loads = new AtomicInteger();

        double price(String product) {
            return prices.computeIfAbsent(product, this::loadPrice);
        }

        private double loadPrice(String product) {
            loads.incrementAndGet();                 // stands in for a database call
            return product.length() * 1.5;
        }

        int loads() {
            return loads.get();
        }
    }
    public static void main(String[] args) throws Exception {
        {
            ConcurrentHashMap<String, Integer> views = new ConcurrentHashMap<>();
            views.put("home", 1);
            Integer existing = views.putIfAbsent("home", 5);          // 1, no change
            show("existing", existing);
            Integer homeViews = views.merge("home", 1, Integer::sum); // 2
            show("homeViews", homeViews);
            Integer cartViews = views.computeIfAbsent("cart", k -> 0); // 0
            show("cartViews", cartViews);
            Integer cartAfter = views.compute("cart", (k, v) -> v + 10); // 10
            show("cartAfter", cartAfter);
            boolean replaced = views.replace("cart", 10, 11);         // true
            show("replaced", replaced);
            Integer helpViews = views.getOrDefault("help", 0);        // 0
            show("helpViews", helpViews);
            long entries = views.mappingCount();                      // 2
            show("entries", entries);
            Integer total = views.reduceValues(1, Integer::sum);      // 13
            show("total", total);
        }
        {
            ConcurrentHashMap<String, Integer> pageViews = new ConcurrentHashMap<>();
            ConcurrentHashMap<String, Integer> safeViews = new ConcurrentHashMap<>();
            Runnable visit = () -> {
                for (int i = 0; i < 100_000; i++) {
                    Integer current = pageViews.get("home");             // check
                    pageViews.put("home", current == null ? 1 : current + 1);   // then act
                    safeViews.merge("home", 1, Integer::sum);            // one atomic step
                }
            };
            Thread visitor1 = Thread.ofPlatform().start(visit);
            Thread visitor2 = Thread.ofPlatform().start(visit);
            visitor1.join();
            visitor2.join();
            int racyCount = pageViews.get("home");                       // less than 200000 in most runs
            show("racyCount", racyCount);
            int mergedCount = safeViews.get("home");                     // 200000
            show("mergedCount", mergedCount);
        }
        {
            PriceCache cache = new PriceCache();
            try (ExecutorService shoppers = Executors.newFixedThreadPool(4)) {
                for (int i = 0; i < 1_000; i++) {
                    shoppers.submit(() -> cache.price("apple"));
                }
            }
            double applePrice = cache.price("apple");        // 7.5
            show("applePrice", applePrice);
            int loaderCalls = cache.loads();                 // 1
            show("loaderCalls", loaderCalls);
        }
        {
            ConcurrentHashMap<String, Integer> stock = new ConcurrentHashMap<>();
            Integer missing = stock.get("kiwi");                 // null, key is absent
            show("missing", missing);
            Integer withDefault = stock.getOrDefault("kiwi", 0); // 0
            show("withDefault", withDefault);
            try { Integer nullValue = stock.put("kiwi", null); show("nullValue", nullValue); } catch (Throwable _t) { System.out.println("nullValue -> " + _t); }
            try { Integer nullKey = stock.put(null, 5); show("nullKey", nullKey); } catch (Throwable _t) { System.out.println("nullKey -> " + _t); }
        }
        {
            ConcurrentHashMap<String, Integer> carts = new ConcurrentHashMap<>(Map.of("anna", 0, "ben", 2, "carl", 0));
            for (Map.Entry<String, Integer> cart : carts.entrySet()) {
                if (cart.getValue() == 0) {
                    carts.remove(cart.getKey());             // no ConcurrentModificationException
                }
            }
            int activeCarts = carts.size();                  // 1
            show("activeCarts", activeCarts);
        }
        {
            ConcurrentHashMap<String, Integer> inventory = new ConcurrentHashMap<>(Map.of("apple", 5, "banana", 3, "cherry", 12));
            Integer totalUnits = inventory.reduceValues(1, Integer::sum);                 // 20
            show("totalUnits", totalUnits);
            Integer largest = inventory.reduceValues(Long.MAX_VALUE, Integer::max);       // 12
            show("largest", largest);
            String lowStock = inventory.search(1, (fruit, units) -> units < 4 ? fruit : null);   // "banana"
            show("lowStock", lowStock);
            long fruitKinds = inventory.mappingCount();                                   // 3
            show("fruitKinds", fruitKinds);
        }
        {
            Set<String> onlineUsers = ConcurrentHashMap.newKeySet();
            boolean added = onlineUsers.add("lokesh");           // true
            show("added", added);
            boolean addedAgain = onlineUsers.add("lokesh");      // false
            show("addedAgain", addedAgain);
            boolean removed = onlineUsers.remove("lokesh");      // true
            show("removed", removed);
        }
        {
            ConcurrentHashMap<String, Integer> scores = new ConcurrentHashMap<>();
            Set<String> players = scores.keySet(0);
            players.add("alex");
            Integer alexScore = scores.get("alex");              // 0
            show("alexScore", alexScore);
        }
        {
            ConcurrentHashMap<String, Set<String>> tagsByPost = new ConcurrentHashMap<>();
            tagsByPost.computeIfAbsent("post-1", k -> ConcurrentHashMap.newKeySet()).add("java");
            tagsByPost.computeIfAbsent("post-1", k -> ConcurrentHashMap.newKeySet()).add("threads");
            int tagCount = tagsByPost.get("post-1").size();      // 2
            show("tagCount", tagCount);
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

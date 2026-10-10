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
 * Examples for the tutorial "Java Hashtable: Why It Is Legacy and What to Use Instead".
 * https://howtodoinjava.com/java/collections/hashtable-class/
 */
public class HashtableExamples {

    public static void main(String[] args) throws Exception {
        {
            Hashtable<String, Integer> legacy = new Hashtable<>();
            legacy.put("visits", 10);
            Integer visits = legacy.get("visits");                                // 10
            show("visits", visits);
            try { Integer missing = legacy.put("clicks", null); show("missing", missing); } catch (Throwable _t) { System.out.println("missing -> " + _t); }
            Map<String, Integer> local = new HashMap<>(legacy);                   // single-threaded replacement
            show("local", local);
            ConcurrentMap<String, Integer> shared = new ConcurrentHashMap<>(legacy); // replacement for shared state
            show("shared", shared);
            shared.merge("visits", 1, Integer::sum);
            Integer updated = shared.get("visits");                               // 11
            show("updated", updated);
        }
        {
            Hashtable<String, Integer> sessions = new Hashtable<>();
            sessions.put("anna", 3);
            sessions.put("bob", 1);
            Integer annaCount = sessions.get("anna");                  // 3
            show("annaCount", annaCount);
            boolean hasBobKey = sessions.containsKey("bob");           // true
            show("hasBobKey", hasBobKey);
            boolean hasValue3 = sessions.contains(3);                  // true, checks values
            show("hasValue3", hasValue3);
            Integer removed = sessions.remove("bob");                  // 1
            show("removed", removed);
            Enumeration<String> names = sessions.keys();
            String firstName = names.nextElement();                    // "anna"
            show("firstName", firstName);
            try { Integer nullKey = sessions.put(null, 5); show("nullKey", nullKey); } catch (Throwable _t) { System.out.println("nullKey -> " + _t); }
        }
        {
            Hashtable<String, Integer> hits = new Hashtable<>();
            // not atomic: two threads can both read 7 and both write 8
            Integer current = hits.getOrDefault("home", 0);
            hits.put("home", current + 1);
            Integer homeHits = hits.get("home");                       // 1 in a single thread
            show("homeHits", homeHits);
        }
        {
            ConcurrentHashMap<String, Integer> pageHits = new ConcurrentHashMap<>();
            try (ExecutorService pool = Executors.newFixedThreadPool(2)) {
                for (int t = 0; t < 2; t++) {
                    pool.submit(() -> {
                        for (int i = 0; i < 10_000; i++) {
                            pageHits.merge("home", 1, Integer::sum);
                        }
                    });
                }
            }
            Integer total = pageHits.get("home");                      // 20000
            show("total", total);
        }
        {
            Hashtable<String, Integer> table = new Hashtable<>(Map.of("a", 1, "b", 2));
            Iterator<String> iterator = table.keySet().iterator();
            table.put("c", 3);
            try { String next = iterator.next(); show("next", next); } catch (Throwable _t) { System.out.println("next -> " + _t); }
            Enumeration<String> keys = table.keys();
            table.put("d", 4);
            boolean more = keys.hasMoreElements();                     // true, no exception
            show("more", more);
        }
        {
            Map<String, String> hashMap = new HashMap<>();
            hashMap.put(null, "guest");
            hashMap.put("theme", null);
            int hashMapSize = hashMap.size();                          // 2
            show("hashMapSize", hashMapSize);
            Map<String, String> hashtable = new Hashtable<>();
            try { String nullValue = hashtable.put("theme", null); show("nullValue", nullValue); } catch (Throwable _t) { System.out.println("nullValue -> " + _t); }
        }
        {
            ConcurrentMap<String, Integer> registry = new ConcurrentHashMap<>();
            registry.put("anna", 3);
            registry.putIfAbsent("anna", 9);
            Integer anna = registry.get("anna");                       // 3
            show("anna", anna);
            Set<String> users = registry.keySet();                     // [anna]
            show("users", users);
            boolean hasThree = registry.containsValue(3);              // true
            show("hasThree", hasThree);
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

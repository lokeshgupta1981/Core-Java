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
 * Examples for the tutorial "Java LinkedHashMap: Insertion Order, Access Order and LRU Cache".
 * https://howtodoinjava.com/java/collections/linkedhashmap/
 */
public class LinkedHashMapExamples {
    static class LruCache<K, V> extends LinkedHashMap<K, V> {
        private final int maxEntries;

        LruCache(int maxEntries) {
            super(16, 0.75f, true);
            if (maxEntries < 1) {
                throw new IllegalArgumentException("maxEntries must be at least 1");
            }
            this.maxEntries = maxEntries;
        }

        @Override
        protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
            return size() > maxEntries;
        }
    }
    public static void main(String[] args) throws Exception {
        {
            LinkedHashMap<String, Integer> stock = new LinkedHashMap<>();
            stock.put("apple", 5);
            stock.put("banana", 3);
            stock.put("cherry", 8);
            Set<String> keys = stock.keySet();                    // [apple, banana, cherry]
            show("keys", keys);
            Integer oldApple = stock.put("apple", 6);             // 5, apple keeps its position
            show("oldApple", oldApple);
            Map.Entry<String, Integer> first = stock.firstEntry(); // apple=6
            show("first", first);
            Map.Entry<String, Integer> last = stock.lastEntry();   // cherry=8
            show("last", last);
            SequencedMap<String, Integer> reversed = stock.reversed(); // {cherry=8, banana=3, apple=6}
            show("reversed", reversed);
        }
        {
            LinkedHashMap<String, Integer> columns = new LinkedHashMap<>();
            columns.put("id", 1);
            columns.put("name", 2);
            columns.put("email", 3);
            columns.put("id", 10);
            String afterUpdate = columns.toString();       // "{id=10, name=2, email=3}"
            show("afterUpdate", afterUpdate);
            columns.remove("name");
            columns.put("name", 2);
            String afterReinsert = columns.toString();     // "{id=10, email=3, name=2}"
            show("afterReinsert", afterReinsert);
            columns.putLast("id", 10);
            String afterPutLast = columns.toString();      // "{email=3, name=2, id=10}"
            show("afterPutLast", afterPutLast);
        }
        {
            LinkedHashMap<String, Integer> queue = new LinkedHashMap<>();
            queue.put("anna", 1);
            queue.put("bob", 2);
            queue.put("carl", 3);
            Map.Entry<String, Integer> next = queue.pollFirstEntry();        // anna=1
            show("next", next);
            queue.putFirst("dina", 0);
            String order = queue.toString();                                  // "{dina=0, bob=2, carl=3}"
            show("order", order);
            String lastKey = queue.sequencedKeySet().getLast();               // "carl"
            show("lastKey", lastKey);
            List<String> backwards = List.copyOf(queue.reversed().keySet());  // [carl, bob, dina]
            show("backwards", backwards);
        }
        {
            LinkedHashMap<String, Integer> views = new LinkedHashMap<>(16, 0.75f, true);
            views.put("phone", 1);
            views.put("laptop", 2);
            views.put("watch", 3);
            Integer laptop = views.get("laptop");                  // 2, laptop moves to the end
            show("laptop", laptop);
            boolean hasPhone = views.containsKey("phone");         // true, order unchanged
            show("hasPhone", hasPhone);
            Integer tablet = views.getOrDefault("tablet", 0);      // 0, no entry, order unchanged
            show("tablet", tablet);
            String accessOrder = views.toString();                 // "{phone=1, watch=3, laptop=2}"
            show("accessOrder", accessOrder);
        }
        {
            LruCache<String, Double> rates = new LruCache<>(2);
            rates.put("USD", 1.0);
            rates.put("EUR", 0.92);
            Double usd = rates.get("USD");                    // 1.0
            show("usd", usd);
            rates.put("GBP", 0.79);
            String cached = rates.toString();                 // "{USD=1.0, GBP=0.79}"
            show("cached", cached);
            boolean hasEur = rates.containsKey("EUR");        // false
            show("hasEur", hasEur);
        }
        {
            LinkedHashMap<String, Integer> presized = LinkedHashMap.newLinkedHashMap(100);   // holds 100 entries without resizing
            show("presized", presized);
            LinkedHashMap<String, Integer> byCapacity = new LinkedHashMap<>(128);           // table capacity 128
            show("byCapacity", byCapacity);
            LinkedHashMap<String, Integer> lru = new LinkedHashMap<>(16, 0.75f, true);      // access order
            show("lru", lru);
            LinkedHashMap<String, Integer> copy = new LinkedHashMap<>(Map.of("a", 1));      // copies the entries
            show("copy", copy);
            int copied = copy.size();                                                        // 1
            show("copied", copied);
        }
        {
            Map<String, Integer> shared = Collections.synchronizedMap(new LinkedHashMap<>());
            shared.put("first", 1);
            shared.put("second", 2);
            List<String> snapshot;
            synchronized (shared) {
                snapshot = List.copyOf(shared.keySet());
            }
            List<String> safeCopy = snapshot;                // [first, second]
            show("safeCopy", safeCopy);
        }
        {
            Map<String, Integer> scores = Map.of("anna", 7, "bob", 9, "carl", 5);
            LinkedHashMap<String, Integer> byScore = scores.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue())
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> a, LinkedHashMap::new));
            String sorted = byScore.toString();          // "{carl=5, anna=7, bob=9}"
            show("sorted", sorted);
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

package com.howtodoinjava.core.streams;

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
 * Examples for the tutorial "Filter a Map by Keys and Values Using Java Streams".
 * https://howtodoinjava.com/java/stream/filter-map-keys-values-both/
 */
public class FilterMapByKeysAndValues {
    static record Item(int qty, double price) {}
    static <K, V> Map<K, V> byKey(Map<K, V> map, Predicate<? super K> keyTest) {
        return map.entrySet().stream()
                .filter(e -> keyTest.test(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
    static <K, V> Map<K, V> byValue(Map<K, V> map, Predicate<? super V> valueTest) {
        return map.entrySet().stream()
                .filter(e -> valueTest.test(e.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
    static <K, V> Map<K, V> byEntry(Map<K, V> map, BiPredicate<? super K, ? super V> test) {
        return map.entrySet().stream()
                .filter(e -> test.test(e.getKey(), e.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
    static LinkedHashMap<String, Boolean> enabledBetaFlags(Map<String, Boolean> flags) {
        return flags.entrySet().stream()
                .filter(e -> e.getKey().startsWith("beta.") && Boolean.TRUE.equals(e.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
        (a, b) -> a, LinkedHashMap::new));
    }
    public static void main(String[] args) throws Exception {
        {
            Map<String, Integer> stock = Map.of("apple", 5, "banana", 0, "cherry", 12, "lime", 8, "mango", 3);

            Map<String, Integer> shortNames = stock.entrySet().stream()
                    .filter(e -> e.getKey().length() <= 4)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));   // {lime=8}

            Map<String, Integer> plenty = stock.entrySet().stream()
                    .filter(e -> e.getValue() > 10)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));   // {cherry=12}

            Map<String, Integer> both = stock.entrySet().stream()
                    .filter(e -> e.getKey().startsWith("m") && e.getValue() > 0)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));   // {mango=3}
        }
        {
            Map<String, Integer> stock = Map.of("apple", 5, "banana", 0, "cherry", 12, "lime", 8, "mango", 3);

            Map<String, Integer> fromAtoC = stock.entrySet().stream()
                    .filter(e -> e.getKey().compareTo("c") < 0)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            int count = fromAtoC.size();                        // 2
            show("count", count);
            boolean hasBanana = fromAtoC.containsKey("banana"); // true
            show("hasBanana", hasBanana);
        }
        {
            TreeMap<String, Integer> sorted = new TreeMap<>(Map.of("apple", 5, "banana", 0, "cherry", 12, "lime", 8));
            SortedMap<String, Integer> beforeC = sorted.headMap("c");   // {apple=5, banana=0}
            show("beforeC", beforeC);
        }
        {
            Map<String, Integer> stock = Map.of("apple", 5, "banana", 0, "cherry", 12, "lime", 8, "mango", 3);

            Map<String, Integer> inStock = stock.entrySet().stream()
                    .filter(e -> e.getValue() > 0)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            int itemsInStock = inStock.size();                  // 4
            show("itemsInStock", itemsInStock);
            boolean soldOut = inStock.containsKey("banana");    // false
            show("soldOut", soldOut);
        }
        {
            Map<String, Item> shelf = Map.of("apple", new Item(5, 0.5), "cherry", new Item(12, 4.0), "lime", new Item(8, 0.3));

            Map<String, Item> pricey = shelf.entrySet().stream()
                    .filter(e -> e.getValue().price() > 1.0)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));   // {cherry=Item[qty=12, price=4.0]}
        }
        {
            Map<String, Integer> stock = Map.of("apple", 5, "banana", 0, "cherry", 12, "lime", 8, "mango", 3);

            Map<String, Integer> longAndPlenty = stock.entrySet().stream()
                    .filter(e -> e.getKey().length() > 4 && e.getValue() > 4)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            Set<String> names = new TreeSet<>(longAndPlenty.keySet());   // [apple, cherry]
            show("names", names);

            Map<String, Integer> limeOrEmpty = stock.entrySet().stream()
                    .filter(e -> e.getKey().equals("lime") || e.getValue() == 0)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            Set<String> picked = new TreeSet<>(limeOrEmpty.keySet());    // [banana, lime]
            show("picked", picked);
        }
        {
            Map<String, Integer> stock = Map.of("apple", 5, "banana", 0, "cherry", 12, "lime", 8, "mango", 3);

            Map<String, Integer> mFruits = byKey(stock, k -> k.startsWith("m"));               // {mango=3}
            show("mFruits", mFruits);
            Map<String, Integer> big = byValue(stock, v -> v >= 12);                           // {cherry=12}
            show("big", big);
            Map<String, Integer> mix = byEntry(stock, (k, v) -> k.contains("i") && v > 5);     // {lime=8}
            show("mix", mix);
            Map<String, Integer> chained = byValue(byKey(stock, k -> k.length() == 5), v -> v > 3);   // {apple=5}
            show("chained", chained);
        }
        {
            Map<String, Integer> menu = new LinkedHashMap<>();
            menu.put("soup", 4);
            menu.put("salad", 0);
            menu.put("pasta", 9);
            menu.put("cake", 6);

            LinkedHashMap<String, Integer> available = menu.entrySet().stream()
                    .filter(e -> e.getValue() > 0)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
            (a, b) -> a, LinkedHashMap::new));      // {soup=4, pasta=9, cake=6}
            Map.Entry<String, Integer> first = available.firstEntry();   // soup=4
            show("first", first);
        }
        {
            Map<String, Integer> cart = new HashMap<>(Map.of("apple", 5, "banana", 0, "cherry", 12, "lime", 0));

            boolean changed = cart.values().removeIf(qty -> qty == 0);      // true
            show("changed", changed);
            boolean noBanana = !cart.containsKey("banana");                // true
            show("noBanana", noBanana);
            cart.keySet().removeIf(name -> name.startsWith("ch"));
            cart.entrySet().removeIf(e -> e.getKey().equals("apple") && e.getValue() < 3);
            Map<String, Integer> left = new TreeMap<>(cart);               // {apple=5}
            show("left", left);
        }
        {
            Map<String, Integer> fixed = Map.of("apple", 5, "banana", 0);
            try { boolean removed = fixed.values().removeIf(qty -> qty == 0); show("removed", removed); } catch (Throwable _t) { System.out.println("removed -> " + _t); }
        }
        {
            Map<String, Integer> stock = Map.of("apple", 5, "banana", 0, "cherry", 12, "lime", 8, "mango", 3);

            List<String> soldOut = stock.entrySet().stream()
                    .filter(e -> e.getValue() == 0)
                    .map(Map.Entry::getKey)
                    .toList();                                      // [banana]
            List<Integer> bigCounts = stock.values().stream().filter(v -> v > 6).sorted().toList();   // [8, 12]
            show("bigCounts", bigCounts);
            List<String> withA = stock.keySet().stream().filter(k -> k.contains("a")).sorted().toList();   // [apple, banana, mango]
            show("withA", withA);
        }
        {
            Map<String, Integer> counts = new HashMap<>();
            counts.put("apple", 5);
            counts.put("kiwi", null);

            try { Map<String, Integer> all = counts.entrySet().stream().filter(e -> true).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)); show("all", all); } catch (Throwable _t) { System.out.println("all -> " + _t); }
        }
        {
            Map<String, Integer> counts = new HashMap<>();
            counts.put("apple", 5);
            counts.put("kiwi", null);

            Map<String, Integer> nonNull = counts.entrySet().stream()
                    .filter(e -> e.getValue() != null)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));   // {apple=5}
            Map<String, Integer> keepNulls = counts.entrySet().stream()
                    .filter(e -> e.getKey().length() == 4)
                    .collect(HashMap::new, (m, e) -> m.put(e.getKey(), e.getValue()), HashMap::putAll);   // {kiwi=null}
        }
        {
            Map<String, Boolean> flags = new LinkedHashMap<>();
            flags.put("beta.search", true);
            flags.put("checkout.v2", true);
            flags.put("beta.dark-mode", false);
            flags.put("beta.export", true);
            flags.put("beta.chat", null);

            LinkedHashMap<String, Boolean> shown = enabledBetaFlags(flags);   // {beta.search=true, beta.export=true}
            show("shown", shown);
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

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
 * Examples for the tutorial "TreeMap vs HashMap in Java: Differences and When to Use Each".
 * https://howtodoinjava.com/java/collections/java-treemap-vs-hashmap/
 */
public class TreeMapVsHashMap {

    public static void main(String[] args) throws Exception {
        {
            HashMap<String, Integer> hashMap = new HashMap<>(Map.of("pear", 3, "apple", 5, "fig", 7));
            TreeMap<String, Integer> treeMap = new TreeMap<>(hashMap);
            Integer fromHash = hashMap.get("fig");                 // 7
            show("fromHash", fromHash);
            Integer fromTree = treeMap.get("fig");                 // 7
            show("fromTree", fromTree);
            Set<String> hashKeys = hashMap.keySet();               // order depends on hash codes
            show("hashKeys", hashKeys);
            Set<String> treeKeys = treeMap.keySet();               // [apple, fig, pear]
            show("treeKeys", treeKeys);
            String smallest = treeMap.firstKey();                  // "apple"
            show("smallest", smallest);
            try { Integer nullKey = treeMap.put(null, 0); show("nullKey", nullKey); } catch (Throwable _t) { System.out.println("nullKey -> " + _t); }
        }
        {
            HashMap<String, String> settings = new HashMap<>();
            settings.put(null, "default");
            settings.put(null, "fallback");
            String nullEntry = settings.get(null);                       // "fallback"
            show("nullEntry", nullEntry);
            TreeMap<String, String> sorted = new TreeMap<>();
            sorted.put("theme", null);
            boolean nullValueOk = sorted.containsKey("theme");           // true
            show("nullValueOk", nullValueOk);
            try { String failed = sorted.put(null, "default"); show("failed", failed); } catch (Throwable _t) { System.out.println("failed -> " + _t); }
        }
        {
            TreeMap<String, Integer> prices = new TreeMap<>(Map.of("apple", 5, "fig", 7, "pear", 3, "plum", 4));
            String first = prices.firstKey();                            // "apple"
            show("first", first);
            String last = prices.lastKey();                              // "plum"
            show("last", last);
            Map.Entry<String, Integer> firstEntry = prices.firstEntry(); // apple=5
            show("firstEntry", firstEntry);
            String beforePear = prices.lowerKey("pear");                 // "fig"
            show("beforePear", beforePear);
            SortedMap<String, Integer> fromP = prices.tailMap("p");      // {pear=3, plum=4}
            show("fromP", fromP);
            Map.Entry<String, Integer> polled = prices.pollLastEntry();  // plum=4
            show("polled", polled);
            String remaining = prices.toString();                        // "{apple=5, fig=7, pear=3}"
            show("remaining", remaining);
        }
        {
            Map<Integer, String> source = Map.of(10, "ten", 2, "two", 13, "thirteen", 5, "five");
            TreeMap<Integer, String> ascending = new TreeMap<>(source);
            String up = ascending.toString();                            // "{2=two, 5=five, 10=ten, 13=thirteen}"
            show("up", up);
            TreeMap<Integer, String> descending = new TreeMap<>(Comparator.reverseOrder());
            descending.putAll(source);
            String down = descending.toString();                         // "{13=thirteen, 10=ten, 5=five, 2=two}"
            show("down", down);
        }
        {
            HashMap<Integer, String> hashMap = new HashMap<>();
            hashMap.put(10, "value1");
            hashMap.put(10, "value2");
            String inHashMap = hashMap.toString();                       // "{10=value2}"
            show("inHashMap", inHashMap);
            TreeMap<Integer, String> treeMap = new TreeMap<>();
            treeMap.put(10, "value1");
            treeMap.put(10, "value2");
            String inTreeMap = treeMap.toString();                       // "{10=value2}"
            show("inTreeMap", inTreeMap);
        }
        {
            HashMap<BigDecimal, String> hashPrices = new HashMap<>();
            hashPrices.put(new BigDecimal("1.0"), "first");
            hashPrices.put(new BigDecimal("1.00"), "second");
            int hashSize = hashPrices.size();                            // 2
            show("hashSize", hashSize);
            TreeMap<BigDecimal, String> treePrices = new TreeMap<>();
            treePrices.put(new BigDecimal("1.0"), "first");
            treePrices.put(new BigDecimal("1.00"), "second");
            String treeContent = treePrices.toString();                  // "{1.0=second}"
            show("treeContent", treeContent);
        }
        {
            TreeMap<String, String> dictionary = new TreeMap<>(Map.of("apple", "a fruit", "banana", "a long fruit", "cherry", "a small fruit"));
            String definition = dictionary.get("banana");                // "a long fruit"
            show("definition", definition);
            String previous = dictionary.lowerKey("banana");             // "apple"
            show("previous", previous);
            String next = dictionary.higherKey("banana");                // "cherry"
            show("next", next);
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

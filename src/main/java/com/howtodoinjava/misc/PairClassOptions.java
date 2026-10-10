package com.howtodoinjava.misc;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.time.*;
import java.nio.file.*;
import java.math.*;
import java.io.*;

import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.stream.*;
import java.util.function.*;
import java.io.*;
import java.nio.file.*;
import java.nio.charset.*;
import java.time.*;
import java.math.*;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.MutablePair;

/**
 * Examples for the tutorial "Pair in Java: Map.entry, Records, Commons Lang and Vavr".
 * https://howtodoinjava.com/java/pairs-in-java/
 */
public class PairClassOptions {
    static record FruitCount(String fruit, int count) {}
    static record WordCount(String word, long count) {}
    static List<WordCount> topWords(String text, int limit) {
        Map<String, Long> counts = Arrays.stream(text.toLowerCase().split("\\W+"))
                .collect(Collectors.groupingBy(w -> w, Collectors.counting()));
        return counts.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed()
                .thenComparing(Map.Entry.comparingByKey()))
                .limit(limit)
                .map(e -> new WordCount(e.getKey(), e.getValue()))
                .toList();
    }
    public static void main(String[] args) throws Exception {
        {
            Map.Entry<String, Integer> entry = Map.entry("apple", 5);         // apple=5
            show("entry", entry);
            String fruit = entry.getKey();                                     // "apple"
            show("fruit", fruit);
            Integer count = entry.getValue();                                  // 5
            show("count", count);
            boolean same = entry.equals(Map.entry("apple", 5));                // true
            show("same", same);
            try { Integer old = entry.setValue(6); show("old", old); } catch (Throwable _t) { System.out.println("old -> " + _t); }
            try { Map.Entry<String, Integer> empty = Map.entry("apple", null); show("empty", empty); } catch (Throwable _t) { System.out.println("empty -> " + _t); }
        }
        {
            Map.Entry<String, Integer> mutable = new AbstractMap.SimpleEntry<>("apple", 5);
            Integer previous = mutable.setValue(6);                            // 5
            show("previous", previous);
            Integer current = mutable.getValue();                              // 6
            show("current", current);
            Map.Entry<String, Integer> fixed = new AbstractMap.SimpleImmutableEntry<>("apple", null);  // apple=null
            show("fixed", fixed);
            try { Integer rejected = fixed.setValue(7); show("rejected", rejected); } catch (Throwable _t) { System.out.println("rejected -> " + _t); }
        }
        {
            List<WordCount> top = topWords("good plot, good cast, slow plot, good end", 2);  // [WordCount[word=good, count=3], WordCount[word=plot, count=2]]
            show("top", top);
            String first = top.get(0).word();                                  // "good"
            show("first", first);
        }
        {
            javafx.util.Pair<String, Integer> fx = new javafx.util.Pair<>("apple", 5);  // apple=5
            show("fx", fx);
            String key = fx.getKey();                                          // "apple"
            show("key", key);
            Integer value = fx.getValue();                                     // 5
            show("value", value);
        }
        {
            Pair<String, Integer> pair = Pair.of("apple", 5);                // (apple,5)
            show("pair", pair);
            String left = pair.getLeft();                                      // "apple"
            show("left", left);
            Integer right = pair.getRight();                                   // 5
            show("right", right);
            String key = pair.getKey();                                        // "apple"
            show("key", key);
            boolean same = pair.equals(Pair.of("apple", 5));                   // true
            show("same", same);
            int order = pair.compareTo(Pair.of("banana", 3));                  // -1
            show("order", order);
        }
        {
            ImmutablePair<String, Integer> fixed = ImmutablePair.of("apple", 5);
            try { Integer rejected = fixed.setValue(6); show("rejected", rejected); } catch (Throwable _t) { System.out.println("rejected -> " + _t); }
            MutablePair<String, Integer> basket = MutablePair.of("apple", 5);
            basket.setRight(basket.getRight() + 3);
            Integer total = basket.getRight();                                 // 8
            show("total", total);
        }
        {
            List<Map.Entry<String, Integer>> basket = List.of(Map.entry("apple", 5), Map.entry("banana", 3), Map.entry("apple", 2));
            int apples = basket.stream().filter(e -> e.getKey().equals("apple")).mapToInt(Map.Entry::getValue).sum();  // 7
            show("apples", apples);
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

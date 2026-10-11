package com.howtodoinjava.core.collections;

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

/**
 * Examples for the tutorial "Create a Java List with a Single Element (List.of, singletonList)".
 * https://howtodoinjava.com/java/collections/create-list-with-single-element/
 */
public class SingleElementListDecision {

    public static void main(String[] args) throws Exception {
        {
            List<String> viaListOf = List.of("apple");                          // [apple]
            show("viaListOf", viaListOf);
            List<String> viaSingleton = Collections.singletonList("apple");     // [apple]
            show("viaSingleton", viaSingleton);
            List<String> viaAsList = Arrays.asList("apple");                    // [apple]
            show("viaAsList", viaAsList);
            List<String> growable = new ArrayList<>(List.of("apple"));          // [apple]
            show("growable", growable);
            List<String> viaStream = Stream.of("apple").toList();               // [apple]
            show("viaStream", viaStream);
        }
        {
            List<String> fruits = List.of("apple");
            String first = fruits.get(0);                       // "apple"
            show("first", first);
            try { boolean added = fruits.add("banana"); show("added", added); } catch (Throwable _t) { System.out.println("added -> " + _t); }
        }
        {
            String missing = null;
            try { List<String> nullList = List.of(missing); show("nullList", nullList); } catch (Throwable _t) { System.out.println("nullList -> " + _t); }
            try { boolean hasNull = List.of("apple").contains(null); show("hasNull", hasNull); } catch (Throwable _t) { System.out.println("hasNull -> " + _t); }
        }
        {
            List<String> single = Collections.singletonList("apple");     // [apple]
            show("single", single);
            List<String> withNull = Collections.singletonList(null);       // [null]
            show("withNull", withNull);
            boolean hasNull = single.contains(null);                       // false
            show("hasNull", hasNull);
            try { String replaced = single.set(0, "banana"); show("replaced", replaced); } catch (Throwable _t) { System.out.println("replaced -> " + _t); }
        }
        {
            List<String> fixed = Arrays.asList("apple");
            String old = fixed.set(0, "banana");               // "apple"
            show("old", old);
            String now = fixed.get(0);                         // "banana"
            show("now", now);
            try { boolean grown = fixed.add("cherry"); show("grown", grown); } catch (Throwable _t) { System.out.println("grown -> " + _t); }
        }
        {
            List<String> cart = new ArrayList<>(List.of("pizza"));
            cart.add("salad");                                  // cart = [pizza, salad]
            List<String> cart2 = new ArrayList<>();
            cart2.add("pizza");                                 // cart2 = [pizza]
        }
        {
            List<String> collected = Stream.of("pizza").collect(Collectors.toCollection(ArrayList::new));    // [pizza]
            show("collected", collected);
            List<String> readOnly = Stream.of("pizza").toList();                                            // [pizza]
            show("readOnly", readOnly);
        }
        {
            Set<String> oneTag = Set.of("vegan");                                    // [vegan]
            show("oneTag", oneTag);
            Set<String> oneTagOld = Collections.singleton("vegan");                    // [vegan]
            show("oneTagOld", oneTagOld);
            Map<String, Integer> oneStock = Map.of("apple", 5);                        // {apple=5}
            show("oneStock", oneStock);
            Map<String, Integer> oneStockOld = Collections.singletonMap("apple", 5);   // {apple=5}
            show("oneStockOld", oneStockOld);
        }
        {
            List<String> placeholders = Collections.nCopies(3, "?");           // [?, ?, ?]
            show("placeholders", placeholders);
            String sql = String.join(", ", placeholders);                     // "?, ?, ?"
            show("sql", sql);
        }
        {
            boolean same = List.of("apple").equals(Collections.singletonList("apple"));    // true
            show("same", same);
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

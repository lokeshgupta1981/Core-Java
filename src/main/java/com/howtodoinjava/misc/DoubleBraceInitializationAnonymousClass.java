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

/**
 * Examples for the tutorial "Double Brace Initialization in Java and Its Alternatives".
 * https://howtodoinjava.com/java/double-brace-initialization-in-java/
 */
public class DoubleBraceInitializationAnonymousClass {
    static class FruitSet extends HashSet<String> {
        {
            add("apple");
            add("banana");
        }
    }
    static class PantryService {
        private final List<String> stockHistory = new ArrayList<>(List.of("apple", "banana"));

        Map<String, Integer> reorderLimits() {
            return new HashMap<>() {{
                    put("apple", 5);
                    put("banana", 3);
            }};
        }

        static Map<String, Integer> reorderLimitsStatic() {
            return new HashMap<>() {{
                    put("apple", 5);
            }};
        }
    }
    static String serialize(Object value) {
        try (ObjectOutputStream out = new ObjectOutputStream(new ByteArrayOutputStream())) {
            out.writeObject(value);
            return "ok";
        } catch (IOException e) {
            return e.getClass().getSimpleName();
        }
    }
    public static void main(String[] args) throws Exception {
        {
            Set<String> fruits = new HashSet<>() {{
                    add("apple");
                    add("banana");
            }};
            boolean hasApple = fruits.contains("apple");                // true
            show("hasApple", hasApple);
            boolean isAnonymous = fruits.getClass().isAnonymousClass(); // true
            show("isAnonymous", isAnonymous);
            boolean isPlainHashSet = fruits.getClass() == HashSet.class; // false
            show("isPlainHashSet", isPlainHashSet);
            Set<String> modern = Set.of("apple", "banana");
            boolean sameContent = fruits.equals(modern);                // true
            show("sameContent", sameContent);
        }
        {
            Set<String> named = new FruitSet();
            boolean sameAsBraces = named.equals(Set.of("apple", "banana"));  // true
            show("sameAsBraces", sameAsBraces);
        }
        {
            Map<String, Integer> limits = new PantryService().reorderLimits();
            String hiddenField = limits.getClass().getDeclaredFields()[0].getName();               // "this$0"
            show("hiddenField", hiddenField);
            String fieldType = limits.getClass().getDeclaredFields()[0].getType().getSimpleName(); // "PantryService"
            show("fieldType", fieldType);
            int staticFields = PantryService.reorderLimitsStatic().getClass().getDeclaredFields().length; // 0
            show("staticFields", staticFields);
        }
        {
            try { String braces = serialize(new PantryService().reorderLimits()); show("braces", braces); } catch (Throwable _t) { System.out.println("braces -> " + _t); }
            String plain = serialize(new HashMap<>(Map.of("apple", 5)));     // "ok"
            show("plain", plain);
        }
        {
            List<String> fruitList = List.of("apple", "banana");          // [apple, banana]
            show("fruitList", fruitList);
            Map<String, Integer> stock = Map.of("apple", 5, "banana", 3);
            int apples = stock.get("apple");                               // 5
            show("apples", apples);
            Map<String, Integer> manyItems = Map.ofEntries(
            Map.entry("apple", 5),
            Map.entry("banana", 3),
            Map.entry("cherry", 7));
            int itemCount = manyItems.size();                              // 3
            show("itemCount", itemCount);
            try { boolean added = fruitList.add("cherry"); show("added", added); } catch (Throwable _t) { System.out.println("added -> " + _t); }
            try { Set<String> duplicates = Set.of("apple", "apple"); show("duplicates", duplicates); } catch (Throwable _t) { System.out.println("duplicates -> " + _t); }
        }
        {
            List<String> shoppingList = new ArrayList<>(List.of("apple", "banana"));
            shoppingList.add("cherry");
            List<String> grown = shoppingList;                             // [apple, banana, cherry]
            show("grown", grown);
            Set<String> sorted = Stream.of("banana", "apple").collect(Collectors.toCollection(TreeSet::new)); // [apple, banana]
            show("sorted", sorted);
            Set<String> ordered = new LinkedHashSet<>();
            Collections.addAll(ordered, "banana", "apple");
            Set<String> insertionOrder = ordered;                          // [banana, apple]
            show("insertionOrder", insertionOrder);
        }
        {
            // Before: Java 7 style fixture
            Map<String, Integer> expectedOld = new HashMap<String, Integer>() {{
                    put("apple", 5);
                    put("banana", 3);
            }};
            // After: Java 9+ style fixture
            Map<String, Integer> expectedNew = Map.of("apple", 5, "banana", 3);
            boolean sameFixture = expectedOld.equals(expectedNew);         // true
            show("sameFixture", sameFixture);
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

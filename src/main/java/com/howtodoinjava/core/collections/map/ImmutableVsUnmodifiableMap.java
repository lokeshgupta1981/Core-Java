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
 * Examples for the tutorial "Immutable vs Unmodifiable Map in Java: Differences and Examples".
 * https://howtodoinjava.com/java/collections/java-immutable-and-unmodifiable-maps/
 */
public class ImmutableVsUnmodifiableMap {
    static class Cart {
        private final LinkedHashMap<String, Integer> items = new LinkedHashMap<>();

        void add(String product, int qty) {
            items.merge(product, qty, Integer::sum);
        }

        SequencedMap<String, Integer> items() {
            return Collections.unmodifiableSequencedMap(items);
        }
    }
    static record AppConfig(String name, Map<String, String> settings) {
        AppConfig {
            settings = Map.copyOf(settings);
        }
    }
    public static void main(String[] args) throws Exception {
        {
            Map<String, Integer> source = new HashMap<>();
            source.put("apple", 5);
            Map<String, Integer> view = Collections.unmodifiableMap(source);
            Map<String, Integer> copy = Map.copyOf(source);
            source.put("banana", 3);
            int viewSize = view.size();                        // 2, the view sees the change
            show("viewSize", viewSize);
            int copySize = copy.size();                        // 1, the copy does not
            show("copySize", copySize);
            try { Integer blocked = view.put("cherry", 8); show("blocked", blocked); } catch (Throwable _t) { System.out.println("blocked -> " + _t); }
        }
        {
            Map<String, List<String>> roles = Map.of("anna", new ArrayList<>(List.of("viewer")));
            roles.get("anna").add("admin");
            List<String> annaRoles = roles.get("anna");        // [viewer, admin]
            show("annaRoles", annaRoles);
            Map<String, List<String>> safeRoles = Map.of("bob", List.of("viewer"));
            try { boolean added = safeRoles.get("bob").add("admin"); show("added", added); } catch (Throwable _t) { System.out.println("added -> " + _t); }
        }
        {
            Map<String, Integer> empty = Map.of();                                   // {}
            show("empty", empty);
            Map<String, Integer> fruit = Map.of("apple", 5, "banana", 3, "cherry", 8);
            Integer banana = fruit.get("banana");                                    // 3
            show("banana", banana);
            try { Map<String, Integer> withNull = Map.of("apple", null); show("withNull", withNull); } catch (Throwable _t) { System.out.println("withNull -> " + _t); }
            try { Map<String, Integer> duplicate = Map.of("apple", 5, "apple", 6); show("duplicate", duplicate); } catch (Throwable _t) { System.out.println("duplicate -> " + _t); }
            try { boolean nullLookup = fruit.containsKey(null); show("nullLookup", nullLookup); } catch (Throwable _t) { System.out.println("nullLookup -> " + _t); }
        }
        {
            Map<String, String> currencies = Map.ofEntries(
            Map.entry("US", "USD"),
            Map.entry("DE", "EUR"),
            Map.entry("IN", "INR"),
            Map.entry("JP", "JPY"));
            String india = currencies.get("IN");                         // "INR"
            show("india", india);
            int countries = currencies.size();                           // 4
            show("countries", countries);
        }
        {
            Map<String, Integer> stock = new HashMap<>();
            stock.put("apple", 5);
            Map<String, Integer> snapshot = Map.copyOf(stock);
            stock.put("banana", 3);
            int snapshotSize = snapshot.size();                          // 1
            show("snapshotSize", snapshotSize);
            boolean sameInstance = Map.copyOf(snapshot) == snapshot;     // true
            show("sameInstance", sameInstance);
            stock.put("cherry", null);
            try { Map<String, Integer> failed = Map.copyOf(stock); show("failed", failed); } catch (Throwable _t) { System.out.println("failed -> " + _t); }
        }
        {
            Map<String, String> raw = new HashMap<>();
            raw.put("theme", "dark");
            AppConfig config = new AppConfig("shop", raw);
            raw.put("theme", "light");
            String theme = config.settings().get("theme");               // "dark"
            show("theme", theme);
            try { String changed = config.settings().put("theme", "light"); show("changed", changed); } catch (Throwable _t) { System.out.println("changed -> " + _t); }
        }
        {
            List<String> words = List.of("apple", "fig", "pear", "plum");
            Map<String, Integer> lengths = words.stream()
                    .collect(Collectors.toUnmodifiableMap(w -> w, String::length));
            Integer pearLength = lengths.get("pear");                               // 4
            show("pearLength", pearLength);
            Map<Integer, String> byLength = words.stream()
                    .collect(Collectors.toUnmodifiableMap(String::length, w -> w, (a, b) -> a + "," + b));
            String fourLetters = byLength.get(4);                                   // "pear,plum"
            show("fourLetters", fourLetters);
            Map<String, Integer> ordered = words.stream()
                    .collect(Collectors.collectingAndThen(
            Collectors.toMap(w -> w, String::length, (a, b) -> a, LinkedHashMap::new),
            Collections::unmodifiableMap));
            String inOrder = ordered.toString();                                    // "{apple=5, fig=3, pear=4, plum=4}"
            show("inOrder", inOrder);
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

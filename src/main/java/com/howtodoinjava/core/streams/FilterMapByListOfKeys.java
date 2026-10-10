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
 * Examples for the tutorial "Filter a Map by a List of Keys in Java: Lookup vs Stream".
 * https://howtodoinjava.com/java/stream/filter-map-by-list-of-keys/
 */
public class FilterMapByListOfKeys {
    static record PricedCart(Map<String, BigDecimal> prices, List<String> unknown) {}
    static PricedCart priceCart(Map<String, BigDecimal> catalog, List<String> cartCodes) {
        Map<String, BigDecimal> prices = new LinkedHashMap<>();
        List<String> unknown = new ArrayList<>();
        for (String code : cartCodes) {
            BigDecimal price = catalog.get(code);
            if (price == null) {
                unknown.add(code);
            } else {
                prices.put(code, price);
            }
        }
        return new PricedCart(prices, List.copyOf(unknown));
    }
    public static void main(String[] args) throws Exception {
        {
            Map<Integer, String> users = Map.of(1, "Alex", 2, "Allen", 3, "Brian", 4, "Bob", 5, "Charles", 6, "David");
            List<Integer> ids = List.of(5, 1, 3, 7);

            Map<Integer, String> picked = new LinkedHashMap<>();
            for (Integer id : ids) {
                String name = users.get(id);
                if (name != null) {
                    picked.put(id, name);
                }
            }
            Map<Integer, String> byLookup = picked;                       // {5=Charles, 1=Alex, 3=Brian}
            show("byLookup", byLookup);

            Set<Integer> idSet = Set.copyOf(ids);
            Map<Integer, String> byStream = users.entrySet().stream()
                    .filter(e -> idSet.contains(e.getKey()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
            (a, b) -> a, TreeMap::new));                // {1=Alex, 3=Brian, 5=Charles}
        }
        {
            Map<Integer, String> users = Map.of(1, "Alex", 2, "Allen", 3, "Brian", 4, "Bob", 5, "Charles", 6, "David");
            List<Integer> ids = List.of(6, 2, 7, 2);

            Map<Integer, String> selected = ids.stream()
                    .filter(users::containsKey)
                    .collect(Collectors.toMap(id -> id, users::get, (first, dup) -> first, LinkedHashMap::new));   // {6=David, 2=Allen}
        }
        {
            Map<Integer, String> users = Map.of(1, "Alex", 2, "Allen");
            List<Integer> ids = List.of(2, 2);
            try { Map<Integer, String> dupes = ids.stream().filter(users::containsKey).collect(Collectors.toMap(id -> id, users::get)); show("dupes", dupes); } catch (Throwable _t) { System.out.println("dupes -> " + _t); }
        }
        {
            Map<Integer, String> users = Map.of(1, "Alex", 2, "Allen", 3, "Brian", 4, "Bob", 5, "Charles", 6, "David");
            List<Integer> ids = List.of(4, 6, 9);

            Set<Integer> wanted = new HashSet<>(ids);
            Map<Integer, String> found = users.entrySet().stream()
                    .filter(e -> wanted.contains(e.getKey()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            int size = found.size();                                     // 2
            show("size", size);
            String fourth = found.get(4);                                // "Bob"
            show("fourth", fourth);
        }
        {
            Map<Integer, String> users = new HashMap<>(Map.of(1, "Alex", 2, "Allen", 3, "Brian", 4, "Bob"));
            Set<Integer> keep = Set.of(1, 3, 7);

            boolean changed = users.keySet().retainAll(keep);             // true
            show("changed", changed);
            Map<Integer, String> left = new TreeMap<>(users);             // {1=Alex, 3=Brian}
            show("left", left);
        }
        {
            Map<Integer, String> fixed = Map.of(1, "Alex", 2, "Allen");
            try { boolean shrunk = fixed.keySet().retainAll(Set.of(1)); show("shrunk", shrunk); } catch (Throwable _t) { System.out.println("shrunk -> " + _t); }
        }
        {
            Map<Integer, String> users = new HashMap<>(Map.of(1, "Alex", 2, "Allen", 3, "Brian", 4, "Bob"));
            Set<Integer> blocked = Set.of(2, 4);

            Map<Integer, String> allowed = users.entrySet().stream()
                    .filter(e -> !blocked.contains(e.getKey()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> a, TreeMap::new));   // {1=Alex, 3=Brian}
            users.keySet().removeAll(blocked);
            Map<Integer, String> afterRemove = new TreeMap<>(users);      // {1=Alex, 3=Brian}
            show("afterRemove", afterRemove);
        }
        {
            Map<Integer, String> users = Map.of(1, "Alex", 2, "Allen", 3, "Brian", 4, "Bob", 5, "Charles", 6, "David");
            List<Integer> ids = List.of(3, 8, 1);

            List<String> names = ids.stream().map(users::get).filter(Objects::nonNull).toList();   // [Brian, Alex]
            show("names", names);
            List<String> withDefault = ids.stream().map(id -> users.getOrDefault(id, "unknown")).toList();   // [Brian, unknown, Alex]
            show("withDefault", withDefault);
        }
        {
            Map<Integer, String> users = Map.of(1, "Alex", 2, "Allen", 3, "Brian");
            List<Integer> ids = List.of(1, 9, 3, 12);

            Map<Boolean, List<Integer>> split = ids.stream().collect(Collectors.partitioningBy(users::containsKey));
            List<Integer> known = split.get(true);                        // [1, 3]
            show("known", known);
            List<Integer> missing = split.get(false);                     // [9, 12]
            show("missing", missing);
        }
        {
            Map<String, BigDecimal> catalog = Map.of("tea", new BigDecimal("4.50"), "mug", new BigDecimal("9.99"), "jam", new BigDecimal("3.20"));
            PricedCart cart = priceCart(catalog, List.of("mug", "tea", "cake"));
            Map<String, BigDecimal> cartPrices = cart.prices();          // {mug=9.99, tea=4.50}
            show("cartPrices", cartPrices);
            List<String> notSold = cart.unknown();                        // [cake]
            show("notSold", notSold);
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

package com.howtodoinjava.core.streams;

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
import java.util.Map.Entry;

/**
 * Examples for the tutorial "Java Stream to Map With Collectors.toMap() Examples".
 * https://howtodoinjava.com/java8/collect-stream-to-map/
 */
public class StreamToMapCollectorDecisionTree {
    static record Item(long id, String name) {}
    static record Product(long id, String name, BigDecimal price) {}
    public static void main(String[] args) throws Exception {
        {
            List<Item> items = List.of(new Item(1, "Item1"), new Item(2, "Item2"), new Item(3, "Item3"));
            List<Item> withDuplicates = List.of(new Item(1, "Item1"), new Item(3, "Item3-1"), new Item(3, "Item3-2"));
            Map<Long, String> names = items.stream().collect(Collectors.toMap(Item::id, Item::name));                                       // {1=Item1, 2=Item2, 3=Item3}
            show("names", names);
            Map<Long, Item> byId = items.stream().collect(Collectors.toMap(Item::id, Function.identity()));                                 // {1=Item[id=1, name=Item1], 2=Item[id=2, name=Item2], 3=Item[id=3, name=Item3]}
            show("byId", byId);
            Map<Long, String> lastWins = withDuplicates.stream().collect(Collectors.toMap(Item::id, Item::name, (oldValue, newValue) -> newValue));   // {1=Item1, 3=Item3-2}
            show("lastWins", lastWins);
            TreeMap<Long, String> sorted = items.reversed().stream().collect(Collectors.toMap(Item::id, Item::name, (o, n) -> n, TreeMap::new));     // {1=Item1, 2=Item2, 3=Item3}
            show("sorted", sorted);
        }
        {
            List<Item> items = List.of(new Item(1, "Item1"), new Item(2, "Item2"), new Item(3, "Item3"));
            Map<Long, String> names = items.stream().collect(Collectors.toMap(Item::id, Item::name));             // {1=Item1, 2=Item2, 3=Item3}
            show("names", names);
            Map<Long, Item> byId = items.stream().collect(Collectors.toMap(Item::id, Function.identity()));       // {1=Item[id=1, name=Item1], 2=Item[id=2, name=Item2], 3=Item[id=3, name=Item3]}
            show("byId", byId);
            Map<String, Long> idsByName = items.stream().collect(Collectors.toMap(Item::name, Item::id));         // {Item1=1, Item2=2, Item3=3}
            show("idsByName", idsByName);
            String second = byId.get(2L).name();                                                                  // "Item2"
            show("second", second);
        }
        {
            List<Item> itemsWithDuplicates = List.of(new Item(1, "Item1"), new Item(2, "Item2"), new Item(3, "Item3-1"), new Item(3, "Item3-2"), new Item(3, "Item3-3"));
            try { Map<Long, String> fails = itemsWithDuplicates.stream().collect(Collectors.toMap(Item::id, Item::name)); show("fails", fails); } catch (Throwable _t) { System.out.println("fails -> " + _t); }
        }
        {
            List<Item> itemsWithDuplicates = List.of(new Item(1, "Item1"), new Item(2, "Item2"), new Item(3, "Item3-1"), new Item(3, "Item3-2"), new Item(3, "Item3-3"));
            Map<Long, String> lastWins = itemsWithDuplicates.stream().collect(Collectors.toMap(Item::id, Item::name, (oldValue, newValue) -> newValue));   // {1=Item1, 2=Item2, 3=Item3-3}
            show("lastWins", lastWins);
            Map<Long, String> firstWins = itemsWithDuplicates.stream().collect(Collectors.toMap(Item::id, Item::name, (oldValue, newValue) -> oldValue));  // {1=Item1, 2=Item2, 3=Item3-1}
            show("firstWins", firstWins);
            Map<Long, String> joined = itemsWithDuplicates.stream().collect(Collectors.toMap(Item::id, Item::name, (a, b) -> a + ", " + b));               // {1=Item1, 2=Item2, 3=Item3-1, Item3-2, Item3-3}
            show("joined", joined);
            Map<Long, Integer> counts = itemsWithDuplicates.stream().collect(Collectors.toMap(Item::id, item -> 1, Integer::sum));                          // {1=1, 2=1, 3=3}
            show("counts", counts);
        }
        {
            List<Item> itemsWithNull = Arrays.asList(new Item(1, "Item1"), new Item(2, null), new Item(3, "Item3"));
            try { Map<Long, String> fails = itemsWithNull.stream().collect(Collectors.toMap(Item::id, Item::name)); show("fails", fails); } catch (Throwable _t) { System.out.println("fails -> " + _t); }
        }
        {
            List<Item> itemsWithNull = Arrays.asList(new Item(1, "Item1"), new Item(2, null), new Item(3, "Item3"));
            Map<Long, String> withDefault = itemsWithNull.stream().collect(Collectors.toMap(Item::id, item -> Objects.requireNonNullElse(item.name(), "")));   // {1=Item1, 2=, 3=Item3}
            show("withDefault", withDefault);
            Map<Long, String> withNulls = itemsWithNull.stream().collect(HashMap::new, (map, item) -> map.put(item.id(), item.name()), Map::putAll);          // {1=Item1, 2=null, 3=Item3}
            show("withNulls", withNulls);
        }
        {
            List<Item> unsortedItems = List.of(new Item(30, "Item30"), new Item(10, "Item10"), new Item(20, "Item20"));
            Map<Long, String> hashOrder = unsortedItems.stream().collect(Collectors.toMap(Item::id, Item::name));                                         // {20=Item20, 10=Item10, 30=Item30}
            show("hashOrder", hashOrder);
            LinkedHashMap<Long, String> insertionOrder = unsortedItems.stream().collect(Collectors.toMap(Item::id, Item::name, (o, n) -> n, LinkedHashMap::new));   // {30=Item30, 10=Item10, 20=Item20}
            show("insertionOrder", insertionOrder);
            TreeMap<Long, String> sortedKeys = unsortedItems.stream().collect(Collectors.toMap(Item::id, Item::name, (o, n) -> n, TreeMap::new));                   // {10=Item10, 20=Item20, 30=Item30}
            show("sortedKeys", sortedKeys);
        }
        {
            List<Item> items = List.of(new Item(1, "Item1"), new Item(2, "Item2"), new Item(3, "Item3"));
            Map<Long, String> unmodifiable = items.stream().collect(Collectors.toUnmodifiableMap(Item::id, Item::name));   // {1=Item1, 2=Item2, 3=Item3} in any order
            show("unmodifiable", unmodifiable);
            try { String added = unmodifiable.put(4L, "Item4"); show("added", added); } catch (Throwable _t) { System.out.println("added -> " + _t); }
        }
        {
            List<Item> itemsWithDuplicates = List.of(new Item(1, "Item1"), new Item(2, "Item2"), new Item(3, "Item3-1"), new Item(3, "Item3-2"), new Item(3, "Item3-3"));
            Map<Long, List<String>> grouped = itemsWithDuplicates.stream().collect(Collectors.groupingBy(Item::id, Collectors.mapping(Item::name, Collectors.toList())));   // {1=[Item1], 2=[Item2], 3=[Item3-1, Item3-2, Item3-3]}
            show("grouped", grouped);
        }
        {
            System.Logger log = System.getLogger("checkout");
            List<Product> fromDb = List.of(new Product(1, "pen", new BigDecimal("1.50")), new Product(2, "ink", new BigDecimal("4.00")), new Product(2, "ink", new BigDecimal("4.00")));
            Map<Long, Product> catalog = fromDb.stream().collect(Collectors.toMap(Product::id, Function.identity(), (first, duplicate) -> {
                log.log(System.Logger.Level.WARNING, "duplicate product id {0}", first.id());
                return first;
            }));
            Map<Long, Integer> cart = Map.of(1L, 3, 2L, 1);
            BigDecimal total = cart.entrySet().stream().map(line -> catalog.get(line.getKey()).price().multiply(BigDecimal.valueOf(line.getValue()))).reduce(BigDecimal.ZERO, BigDecimal::add);   // 8.50
            show("total", total);
            int catalogSize = catalog.size();   // 2
            show("catalogSize", catalogSize);
        }
        {
            Map<String, Integer> stock = Map.of("pen", 5, "ink", 5, "pad", 2);
            Map<Integer, String> byCount = stock.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey, (a, b) -> a.compareTo(b) < 0 ? a : b, TreeMap::new));   // {2=pad, 5=ink}
            show("byCount", byCount);
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

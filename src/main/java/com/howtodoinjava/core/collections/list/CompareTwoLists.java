package com.howtodoinjava.core.collections.list;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.time.*;
import java.nio.file.*;
import java.math.*;
import java.io.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;

/**
 * Examples for the tutorial "Compare Two Lists in Java: Equality, Differences, Common Items".
 * https://howtodoinjava.com/java/collections/arraylist/compare-two-arraylists/
 */
public class CompareTwoLists {
    static <T extends Comparable<T>> boolean equalIgnoringOrder(List<T> first, List<T> second) {
        if (first.size() != second.size()) {
            return false;
        }
        return first.stream().sorted().toList().equals(second.stream().sorted().toList());
    }
    static <T> boolean sameElements(List<T> first, List<T> second) {
        if (first.size() != second.size()) {
            return false;
        }
        Map<T, Long> counts = first.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        return counts.equals(second.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting())));
    }
    static record Product(String code, int qty) {}
    public static void main(String[] args) throws Exception {
        {
            List<String> warehouse = List.of("apple", "banana", "cherry");
            List<String> shop = List.of("cherry", "banana", "date");
            List<String> reordered = List.of("banana", "cherry", "apple");

            boolean equal = warehouse.equals(shop);                    // false
            show("equal", equal);
            boolean sameIgnoringOrder = warehouse.stream().sorted().toList()
                    .equals(reordered.stream().sorted().toList());     // true

            List<String> onlyInWarehouse = new ArrayList<>(warehouse);
            onlyInWarehouse.removeAll(shop);                           // [apple]
            List<String> missingInWarehouse = new ArrayList<>(shop);
            missingInWarehouse.removeAll(warehouse);                   // [date]
            List<String> common = new ArrayList<>(warehouse);
            common.retainAll(shop);                                    // [banana, cherry]
        }
        {
            List<String> warehouse = new ArrayList<>(List.of("apple", "banana"));
            boolean sameTypeFree = warehouse.equals(new LinkedList<>(List.of("apple", "banana")));   // true
            show("sameTypeFree", sameTypeFree);
            boolean reordered = warehouse.equals(List.of("banana", "apple"));   // false
            show("reordered", reordered);
            List<String> missing = null;
            boolean nullSafe = Objects.equals(missing, warehouse);     // false
            show("nullSafe", nullSafe);
        }
        {
            List<String> warehouse = List.of("apple", "banana", "cherry");
            boolean same = equalIgnoringOrder(warehouse, List.of("cherry", "apple", "banana"));   // true
            show("same", same);
            boolean diff = equalIgnoringOrder(List.of("a", "a", "b"), List.of("a", "b", "b"));    // false
            show("diff", diff);
        }
        {
            boolean same = sameElements(List.of("pear", "fig", "pear"), List.of("fig", "pear", "pear"));   // true
            show("same", same);
            boolean counted = sameElements(List.of("pear", "pear", "fig"), List.of("pear", "fig", "fig"));   // false
            show("counted", counted);
        }
        {
            List<String> first = List.of("a", "a", "b");
            List<String> second = List.of("a", "b", "b");
            boolean setsEqual = new HashSet<>(first).equals(new HashSet<>(second));   // true, wrong
            show("setsEqual", setsEqual);
            boolean containsBoth = first.containsAll(second) && second.containsAll(first);   // true, wrong
            show("containsBoth", containsBoth);
        }
        {
            List<String> actual = List.of("cherry", "apple", "banana");
            List<String> expected = List.of("apple", "banana", "cherry");
            assertThat(actual, containsInAnyOrder(expected.toArray()));
            org.assertj.core.api.Assertions.assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
        }
        {
            List<String> warehouse = List.of("apple", "banana", "cherry", "kiwi");
            List<String> shop = List.of("apple", "banana", "date", "fig");
            List<String> additional = new ArrayList<>(warehouse);
            additional.removeAll(shop);
            List<String> result = additional;                          // [cherry, kiwi]
            show("result", result);
        }
        {
            List<String> warehouse = List.of("apple", "banana", "cherry", "kiwi");
            Set<String> shop = new HashSet<>(List.of("apple", "banana", "date", "fig"));
            List<String> additional = warehouse.stream()
                    .filter(item -> !shop.contains(item))
                    .toList();                                         // [cherry, kiwi]
        }
        {
            List<String> warehouse = List.of("apple", "banana", "cherry", "kiwi");
            List<String> shop = List.of("apple", "banana", "date", "fig");
            List<String> missing = new ArrayList<>(shop);
            missing.removeAll(warehouse);
            List<String> result = missing;                             // [date, fig]
            show("result", result);
            Set<String> stock = new HashSet<>(warehouse);
            List<String> viaStream = shop.stream().filter(item -> !stock.contains(item)).toList();   // [date, fig]
            show("viaStream", viaStream);
        }
        {
            List<String> warehouse = List.of("apple", "banana", "cherry", "kiwi");
            List<String> shop = List.of("apple", "banana", "date", "fig");
            List<String> common = new ArrayList<>(warehouse);
            common.retainAll(shop);
            List<String> result = common;                              // [apple, banana]
            show("result", result);
            Set<String> listed = new HashSet<>(shop);
            List<String> viaStream = warehouse.stream().filter(listed::contains).toList();   // [apple, banana]
            show("viaStream", viaStream);
        }
        {
            List<Product> warehouse = List.of(new Product("apple", 5), new Product("kiwi", 2), new Product("fig", 7));
            List<Product> shop = List.of(new Product("apple", 5), new Product("kiwi", 4), new Product("date", 1));

            Map<String, Product> shopByCode = shop.stream()
                    .collect(Collectors.toMap(Product::code, Function.identity()));
            Set<String> warehouseCodes = warehouse.stream().map(Product::code).collect(Collectors.toSet());

            List<String> toPublish = warehouse.stream().map(Product::code).filter(c -> !shopByCode.containsKey(c)).toList();   // [fig]
            show("toPublish", toPublish);
            List<String> toDelete = shop.stream().map(Product::code).filter(c -> !warehouseCodes.contains(c)).toList();   // [date]
            show("toDelete", toDelete);
            List<String> toUpdate = warehouse.stream()
                    .filter(p -> shopByCode.containsKey(p.code()) && !shopByCode.get(p.code()).equals(p))
                    .map(Product::code)
                    .toList();                                         // [kiwi]
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

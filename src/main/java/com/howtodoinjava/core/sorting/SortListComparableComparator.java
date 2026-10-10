package com.howtodoinjava.core.sorting;

import java.util.concurrent.locks.*;
import java.lang.management.*;
import java.time.format.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.zip.*;
import java.util.regex.*;
import java.net.*;
import java.text.*;
import java.nio.file.attribute.*;
import java.security.*;
import java.nio.charset.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.time.*;
import java.nio.file.*;
import java.math.*;
import java.io.*;

import java.time.temporal.*;

/**
 * Examples for the tutorial "Sort a List in Java With Comparable and Comparator".
 * https://howtodoinjava.com/java/sort/comparable-comparator/
 */
public class SortListComparableComparator {
    static record Product(String name, int price, LocalDate added) implements Comparable<Product> {
        @Override
        public int compareTo(Product other) {
            return name.compareTo(other.name);
        }
    }
    static record Tag(String label) {}
    static enum SortBy { NAME, PRICE, NEWEST }
    static List<Product> sorted(List<Product> catalog, SortBy sortBy) {
        Comparator<Product> order = switch (sortBy) {
            case NAME -> Comparator.naturalOrder();
            case PRICE -> Comparator.comparingInt(Product::price).thenComparing(Comparator.naturalOrder());
            case NEWEST -> Comparator.comparing(Product::added).reversed();
        };
        return catalog.stream().sorted(order).toList();
    }
    public static void main(String[] args) throws Exception {
        {
            List<String> fruits = new ArrayList<>(List.of("pear", "fig", "apple", "kiwi"));
            fruits.sort(null);
            List<String> natural = List.copyOf(fruits);                                  // [apple, fig, kiwi, pear]
            show("natural", natural);
            fruits.sort(Comparator.reverseOrder());
            List<String> reversed = List.copyOf(fruits);                                 // [pear, kiwi, fig, apple]
            show("reversed", reversed);
            fruits.sort(Comparator.comparingInt(String::length));
            List<String> byLength = List.copyOf(fruits);                                 // [fig, pear, kiwi, apple]
            show("byLength", byLength);
            List<String> copy = fruits.stream().sorted().toList();                       // [apple, fig, kiwi, pear]
            show("copy", copy);
        }
        {
            List<Integer> prices = new ArrayList<>(List.of(30, 5, 120, 18));
            Collections.sort(prices);
            List<Integer> cheapFirst = prices;                                          // [5, 18, 30, 120]
            show("cheapFirst", cheapFirst);
            List<String> brands = new ArrayList<>(List.of("bose", "apple", "Zara"));
            brands.sort(null);
            List<String> byCharCode = List.copyOf(brands);                              // [Zara, apple, bose]
            show("byCharCode", byCharCode);
            List<String> mixed = new ArrayList<>(List.of("bose", "Zara", "apple"));
            mixed.sort(String.CASE_INSENSITIVE_ORDER);
            List<String> ignoreCase = mixed;                                            // [apple, bose, Zara]
            show("ignoreCase", ignoreCase);
            List<LocalDate> days = new ArrayList<>(List.of(LocalDate.of(2026, 3, 1), LocalDate.of(2025, 12, 24)));
            days.sort(null);
            LocalDate earliest = days.getFirst();                                       // 2025-12-24
            show("earliest", earliest);
            List<DayOfWeek> open = new ArrayList<>(List.of(DayOfWeek.FRIDAY, DayOfWeek.MONDAY));
            Collections.sort(open);
            List<DayOfWeek> inWeekOrder = open;                                         // [MONDAY, FRIDAY]
            show("inWeekOrder", inWeekOrder);
        }
        {
            List<Product> catalog = new ArrayList<>(List.of(
            new Product("mug", 1200, LocalDate.of(2026, 5, 2)),
            new Product("cap", 1900, LocalDate.of(2026, 1, 15)),
            new Product("tee", 1200, LocalDate.of(2026, 8, 30))));
            Collections.sort(catalog);
            List<String> byName = catalog.stream().map(Product::name).toList();       // [cap, mug, tee]
            show("byName", byName);
            catalog.sort(Comparator.reverseOrder());
            List<String> byNameDesc = catalog.stream().map(Product::name).toList();   // [tee, mug, cap]
            show("byNameDesc", byNameDesc);
        }
        {
            List<Product> catalog = new ArrayList<>(List.of(
            new Product("mug", 1200, LocalDate.of(2026, 5, 2)),
            new Product("cap", 1900, LocalDate.of(2026, 1, 15)),
            new Product("tee", 1200, LocalDate.of(2026, 8, 30))));
            catalog.sort(Comparator.comparingInt(Product::price));
            List<String> cheapest = catalog.stream().map(Product::name).toList();      // [mug, tee, cap]
            show("cheapest", cheapest);
            catalog.sort(Comparator.comparing(Product::added).reversed());
            List<String> newest = catalog.stream().map(Product::name).toList();        // [tee, mug, cap]
            show("newest", newest);
            catalog.sort(Comparator.comparingInt(Product::price).thenComparing(Comparator.naturalOrder()));
            List<String> priceThenName = catalog.stream().map(Product::name).toList(); // [mug, tee, cap]
            show("priceThenName", priceThenName);
        }
        {
            List<Integer> fixed = List.of(3, 1, 2);
            List<Integer> sortedCopy = fixed.stream().sorted().toList();       // [1, 2, 3]
            show("sortedCopy", sortedCopy);
            List<Integer> mutable = new ArrayList<>(fixed);
            mutable.sort(null);
            List<Integer> inPlace = mutable;                                    // [1, 2, 3]
            show("inPlace", inPlace);
            try { fixed.sort(null);  } catch (Throwable _t) { System.out.println("-> " + _t); }
        }
        {
            List<Tag> tags = new ArrayList<>(List.of(new Tag("new"), new Tag("sale")));
            try { tags.sort(null);  } catch (Throwable _t) { System.out.println("-> " + _t); }
            List<String> withGap = new ArrayList<>(Arrays.asList("mug", null, "cap"));
            try { withGap.sort(null);  } catch (Throwable _t) { System.out.println("-> " + _t); }
            withGap.sort(Comparator.nullsLast(Comparator.naturalOrder()));
            List<String> nullAtEnd = withGap;                                    // [cap, mug, null]
            show("nullAtEnd", nullAtEnd);
        }
        {
            List<Product> cached = List.of(
            new Product("mug", 1200, LocalDate.of(2026, 5, 2)),
            new Product("cap", 1900, LocalDate.of(2026, 1, 15)),
            new Product("tee", 1200, LocalDate.of(2026, 8, 30)));
            List<String> page = sorted(cached, SortBy.PRICE).stream().map(Product::name).toList();   // [mug, tee, cap]
            show("page", page);
            String stillFirst = cached.get(0).name();                                                 // "mug"
            show("stillFirst", stillFirst);
            SortBy fromParam = SortBy.valueOf("newest".toUpperCase(Locale.ROOT));                     // NEWEST
            show("fromParam", fromParam);
        }
        {
            Set<String> labels = new TreeSet<>(Set.of("sale", "eco", "new"));
            List<String> asList = List.copyOf(labels);                          // [eco, new, sale]
            show("asList", asList);
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

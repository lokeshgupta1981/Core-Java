package com.howtodoinjava.algorithms;

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
 * Examples for the tutorial "Linear Search in Java".
 * https://howtodoinjava.com/algorithm/linear-search/
 */
public class LinearSearch {
    static int linearSearch(int[] a, int target) {
        if (a == null) {
            return -1;
        }
        for (int i = 0; i < a.length; i++) {
            if (a[i] == target) {
                return i;                   // first match wins
            }
        }
        return -1;                          // not found
    }
    static <T> int linearSearch(T[] a, T target) {
        if (a == null) {
            return -1;
        }
        for (int i = 0; i < a.length; i++) {
            if (Objects.equals(a[i], target)) {
                return i;
            }
        }
        return -1;
    }
    static record Product(String name, int price) {}
    static <T> int findFirst(List<T> items, Predicate<? super T> condition) {
        for (int i = 0; i < items.size(); i++) {
            if (condition.test(items.get(i))) {
                return i;
            }
        }
        return -1;
    }
    public static void main(String[] args) throws Exception {
        {
            int[] shelf = {14, 7, 30, 21, 9};
            int position = linearSearch(shelf, 21);   // 3
            show("position", position);
            int absent = linearSearch(shelf, 50);     // -1
            show("absent", absent);
        }
        {
            int[] codes = {5, 8, 5, 2};
            int firstFive = linearSearch(codes, 5); // 0, the first of two matches
            show("firstFive", firstFive);
            int nothing = linearSearch(null, 5);    // -1
            show("nothing", nothing);
            int none = linearSearch(new int[0], 5); // -1
            show("none", none);
        }
        {
            String[] cities = {"Pune", null, "Delhi"};
            int delhi = linearSearch(cities, new String("Delhi"));   // 2
            show("delhi", delhi);
            int nullSlot = linearSearch(cities, null);                // 1
            show("nullSlot", nullSlot);
        }
        {
            List<Product> products = List.of(new Product("lamp", 40), new Product("mug", 12), new Product("rug", 9));
            int cheap = findFirst(products, p -> p.price() < 15);    // 1
            show("cheap", cheap);
            int free = findFirst(products, p -> p.price() == 0);     // -1
            show("free", free);
        }
        {
            List<String> fruits = List.of("apple", "banana", "kiwi");
            int kiwiIndex = fruits.indexOf("kiwi");                  // 2
            show("kiwiIndex", kiwiIndex);
            boolean hasPear = fruits.contains("pear");               // false
            show("hasPear", hasPear);
            int[] ids = {14, 7, 30, 21, 9};
            OptionalInt idx = IntStream.range(0, ids.length).filter(i -> ids[i] == 30).findFirst();   // OptionalInt[2]
            show("idx", idx);
            Optional<String> longName = fruits.stream().filter(f -> f.length() > 5).findFirst();      // Optional[banana]
            show("longName", longName);
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

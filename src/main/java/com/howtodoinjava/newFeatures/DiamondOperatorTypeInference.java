package com.howtodoinjava.newFeatures;

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
 * Examples for the tutorial "Diamond Operator in Java: Type Inference, Java 9 and var".
 * https://howtodoinjava.com/java/dimond-operator/
 */
public class DiamondOperatorTypeInference {
    static record Priced<T>(T item, int price) {}
    static int countDishes(List<String> dishes) {
        return dishes.size();
    }
    static List<String> emptyMenu() {
        return new ArrayList<>();
    }
    static abstract class Discount<T> {
        abstract T apply(T value);
    }
    public static void main(String[] args) throws Exception {
        {
            Map<String, Integer> prices = new HashMap<>();                 // inferred as HashMap<String, Integer>
            show("prices", prices);
            prices.put("soup", 5);
            List<String> dishes = new ArrayList<>(prices.keySet());          // [soup]
            show("dishes", dishes);
            int soup = prices.get("soup");                                  // 5
            show("soup", soup);
        }
        {
            List menu = new ArrayList();
            menu.add("soup");
            menu.add(5);
            Object first = menu.get(0);                                       // "soup"
            show("first", first);
            int size = menu.size();                                           // 2
            show("size", size);
        }
        {
            List raw = new ArrayList();
            raw.add(5);
            List<String> names = raw;                                         // unchecked warning only
            show("names", names);
            try { String name = names.get(0); show("name", name); } catch (Throwable _t) { System.out.println("name -> " + _t); }
        }
        {
            List<String> menu = new ArrayList<String>();
            menu.add("soup");
            String first = menu.get(0);                                      // "soup", no cast
            show("first", first);
        }
        {
            Map<String, List<Integer>> ratings = new HashMap<>();
            ratings.computeIfAbsent("soup", key -> new ArrayList<>()).add(4);
            String text = ratings.toString();                                 // "{soup=[4]}"
            show("text", text);
            int none = countDishes(new ArrayList<>());                        // 0
            show("none", none);
            Priced<String> offer = new Priced<>("soup", 5);                   // Priced[item=soup, price=5]
            show("offer", offer);
            List<String> menu = emptyMenu();                                  // []
            show("menu", menu);
        }
        {
            // compiles with a warning: new ArrayList() without <> is a raw type
            List<String> dishes = new ArrayList();
        }
        {
            Discount<Integer> halfPrice = new Discount<>() {
                @Override
                Integer apply(Integer value) {
                    return value / 2;
                }
            };
            int price = halfPrice.apply(10);                                  // 5
            show("price", price);
            Comparator<String> byLength = new Comparator<>() {
                @Override
                public int compare(String a, String b) {
                    return Integer.compare(a.length(), b.length());
                }
            };
            int order = byLength.compare("soup", "salad");                    // -1
            show("order", order);
        }
        {
            var mixed = new ArrayList<>();                                    // inferred as ArrayList<Object>
            show("mixed", mixed);
            mixed.add("soup");
            mixed.add(5);
            int count = mixed.size();                                         // 2
            show("count", count);
            var dishes = new ArrayList<String>();                             // inferred as ArrayList<String>
            show("dishes", dishes);
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

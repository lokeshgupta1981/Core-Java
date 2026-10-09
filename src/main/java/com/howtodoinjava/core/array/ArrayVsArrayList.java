package com.howtodoinjava.core.array;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.time.*;
import java.nio.file.*;
import java.math.*;
import java.io.*;

import java.util.regex.*;
import java.nio.charset.*;
import java.nio.*;
import java.security.*;
import java.lang.reflect.*;
import java.lang.invoke.*;

/**
 * Examples for the tutorial "Array vs ArrayList in Java: Differences and When to Use Each".
 * https://howtodoinjava.com/java/array/array-vs-arraylist/
 */
public class ArrayVsArrayList {

    public static void main(String[] args) throws Exception {
        {
            int[] steps = new int[7];                 // 7 days, all 0
            show("steps", steps);
            steps[0] = 8200;
            int days = steps.length;                  // 7
            show("days", days);
            List<String> cart = new ArrayList<>();
            cart.add("milk");
            cart.add("bread");
            int items = cart.size();                  // 2
            show("items", items);
        }
        {
            ArrayList<String> orders = new ArrayList<>(1_000);   // room for 1,000, size 0
            show("orders", orders);
            orders.add("A-1");
            int used = orders.size();                              // 1
            show("used", used);
            orders.ensureCapacity(5_000);                          // one resize up front
            orders.trimToSize();                                   // capacity = size
        }
        {
            List<Integer> scores = new ArrayList<>(List.of(1000, 1000, 7));
            boolean sameObject = scores.get(0) == scores.get(1);        // false
            show("sameObject", sameObject);
            boolean sameValue = scores.get(0).equals(scores.get(1));    // true
            show("sameValue", sameValue);
            Integer removed = scores.remove(1);                         // 1000, removes index 1
            show("removed", removed);
            boolean gone = scores.remove(Integer.valueOf(7));           // true, removes value 7
            show("gone", gone);
            List<Integer> withNull = new ArrayList<>();
            withNull.add(null);
            try { int value = withNull.get(0); show("value", value); } catch (Throwable _t) { System.out.println("value -> " + _t); }
        }
        {
            Object[] labels = new String[2];
            labels[0] = "sale";
            try { labels[1] = 42;  } catch (Throwable _t) { System.out.println("-> " + _t); }
        }
        {
            int[][] seats = new int[3][4];            // 3 rows, 4 seats each
            show("seats", seats);
            seats[1][2] = 1;                          // book row 1, seat 2
            int rows = seats.length;                  // 3
            show("rows", rows);
            List<List<String>> comments = new ArrayList<>();
            comments.add(new ArrayList<>(List.of("nice")));
            comments.add(new ArrayList<>());
            comments.get(1).add("thanks");
            String second = comments.get(1).get(0);   // "thanks"
            show("second", second);
        }
        {
            String[] fruits = {"apple", "kiwi"};
            List<String> view = Arrays.asList(fruits);                 // fixed-size view of the array
            show("view", view);
            ArrayList<String> list = new ArrayList<>(Arrays.asList(fruits));   // [apple, kiwi]
            show("list", list);
            boolean added = list.add("fig");                           // true
            show("added", added);
            String[] back = list.toArray(String[]::new);               // [apple, kiwi, fig]
            show("back", back);
            int[] nums = {3, 1};
            List<Integer> boxed = Arrays.stream(nums).boxed().toList();   // [3, 1]
            show("boxed", boxed);
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

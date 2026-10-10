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

/**
 * Examples for the tutorial "Check if ArrayList Is Empty in Java: isEmpty() vs size()".
 * https://howtodoinjava.com/java/collections/arraylist/check-arraylist-empty/
 */
public class CheckArrayListEmpty {
    static List<String> findCoupons(String customer) {
        if (customer == null || customer.isBlank()) {
            return List.of();
        }
        return customer.equals("lokesh") ? List.of("SAVE10") : List.of();
    }
    static String checkout(List<String> basket) {
        if (basket == null || basket.isEmpty()) {
            return "Your basket is empty";
        }
        String summary = basket.size() + " items, starting with " + basket.getFirst();
        basket.clear();
        return summary;
    }
    public static void main(String[] args) throws Exception {
        {
            List<String> basket = new ArrayList<>();
            boolean empty = basket.isEmpty();                        // true
            show("empty", empty);
            basket.add("milk");
            boolean stillEmpty = basket.isEmpty();                   // false
            show("stillEmpty", stillEmpty);
            boolean noItems = basket.size() == 0;                    // false
            show("noItems", noItems);
            boolean nullOrEmpty = basket == null || basket.isEmpty();   // false
            show("nullOrEmpty", nullOrEmpty);
            basket.clear();
            boolean emptyAgain = basket.isEmpty();                   // true
            show("emptyAgain", emptyAgain);
        }
        {
            List<String> basket = new ArrayList<>(List.of("milk", "eggs"));
            boolean hasItems = !basket.isEmpty();                    // true
            show("hasItems", hasItems);
            List<String> none = List.of();
            boolean nothing = none.isEmpty();                        // true
            show("nothing", nothing);
        }
        {
            List<String> basket = new ArrayList<>();
            try { String first = basket.getFirst(); show("first", first); } catch (Throwable _t) { System.out.println("first -> " + _t); }
            String safe = basket.isEmpty() ? "none" : basket.getFirst();   // "none"
            show("safe", safe);
            Optional<String> maybe = basket.stream().findFirst();    // Optional.empty
            show("maybe", maybe);
        }
        {
            List<String> basket = null;
            try { boolean bad = basket.isEmpty(); show("bad", bad); } catch (Throwable _t) { System.out.println("bad -> " + _t); }
            boolean isEmptyOrNull = basket == null || basket.isEmpty();   // true
            show("isEmptyOrNull", isEmptyOrNull);
            boolean viaDefault = Objects.requireNonNullElse(basket, List.<String>of()).isEmpty();   // true
            show("viaDefault", viaDefault);
        }
        {
            boolean noCoupons = findCoupons("alex").isEmpty();       // true
            show("noCoupons", noCoupons);
            int count = findCoupons("lokesh").size();                // 1
            show("count", count);
        }
        {
            List<String> basket = new ArrayList<>(List.of("milk", "eggs", "bread"));
            List<String> sameBasket = basket;
            basket.clear();
            int size = sameBasket.size();                            // 0
            show("size", size);
            boolean empty = basket.isEmpty();                        // true
            show("empty", empty);
        }
        {
            List<String> fixed = List.of("milk");
            try { fixed.clear();  } catch (Throwable _t) { System.out.println("-> " + _t); }
        }
        {
            List<String> basket = new ArrayList<>(List.of("milk", "eggs", "bread"));
            boolean changed = basket.removeAll(basket);              // true
            show("changed", changed);
            boolean empty = basket.isEmpty();                        // true
            show("empty", empty);
        }
        {
            List<String> batch = new ArrayList<>(List.of("milk", "eggs"));
            List<String> sent = batch;
            batch = new ArrayList<>();
            int sentSize = sent.size();                              // 2
            show("sentSize", sentSize);
            boolean fresh = batch.isEmpty();                         // true
            show("fresh", fresh);
        }
        {
            List<String> basket = new ArrayList<>(List.of("milk", "eggs"));
            String noBasket = checkout(null);                        // "Your basket is empty"
            show("noBasket", noBasket);
            String placed = checkout(basket);                        // "2 items, starting with milk"
            show("placed", placed);
            boolean resetDone = basket.isEmpty();                    // true
            show("resetDone", resetDone);
        }
        {
            List<String> notes = Arrays.asList("", " ", null);
            boolean blank = notes.stream().allMatch(s -> s == null || s.isBlank());   // true
            show("blank", blank);
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

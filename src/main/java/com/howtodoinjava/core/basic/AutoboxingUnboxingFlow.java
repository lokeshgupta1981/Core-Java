package com.howtodoinjava.core.basic;

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
 * Examples for the tutorial "Java Wrapper Classes: Autoboxing, Unboxing and Pitfalls".
 * https://howtodoinjava.com/java/basics/java-wrapper-classes/
 */
public class AutoboxingUnboxingFlow {
    static int firstScore(List<Integer> scores) {
        scores.add(10);
        int first = scores.get(0);
        return first;
    }
    public static void main(String[] args) throws Exception {
        {
            Integer boxed = Integer.valueOf(42);     // 42 (int to Integer)
            show("boxed", boxed);
            int plain = boxed.intValue();            // 42 (Integer to int)
            show("plain", plain);
            List<Integer> scores = new ArrayList<>();
            boolean added = scores.add(7);           // true, autoboxing of 7
            show("added", added);
            int first = scores.get(0);               // 7, unboxing
            show("first", first);
        }
        {
            Integer count = Integer.valueOf(10);         // 10, primitive to wrapper
            show("count", count);
            Integer parsed = Integer.valueOf("25");      // 25, String to wrapper
            show("parsed", parsed);
            int raw = Integer.parseInt("25");            // 25, String to primitive
            show("raw", raw);
            double price = Double.valueOf(9.5).doubleValue();   // 9.5, wrapper to primitive
            show("price", price);
            long asLong = count.longValue();             // 10, widening through Number
            show("asLong", asLong);
            String text = Integer.toString(255, 16);     // "ff"
            show("text", text);
            boolean flag = Boolean.parseBoolean("TRUE"); // true, case-insensitive
            show("flag", flag);
            try { Integer bad = Integer.valueOf("ten"); show("bad", bad); } catch (Throwable _t) { System.out.println("bad -> " + _t); }
        }
        {
            Integer level = 3;                       // 3, assignment
            show("level", level);
            List<Integer> stock = new ArrayList<>();
            boolean ok = stock.add(12);              // true, argument
            show("ok", ok);
            Map<String, Integer> prices = new HashMap<>();
            Integer old = prices.put("apple", 5);    // null, no previous value
            show("old", old);
            Object any = 99;                         // 99, boxed to Integer first
            show("any", any);
            String type = any.getClass().getSimpleName();   // "Integer"
            show("type", type);
        }
        {
            Integer a = 20;
            Integer b = 22;
            int sum = a + b;                         // 42
            show("sum", sum);
            boolean bigger = a < b;                  // true
            show("bigger", bigger);
            Boolean enabled = Boolean.TRUE;
            String mode = enabled ? "on" : "off";    // "on"
            show("mode", mode);
            Map<String, Integer> stock = Map.of("apple", 5);
            int apples = stock.get("apple");         // 5
            show("apples", apples);
        }
        {
            Integer small1 = 127;
            Integer small2 = 127;
            boolean sameSmall = small1 == small2;    // true, cached object
            show("sameSmall", sameSmall);
            Integer big1 = 128;
            Integer big2 = 128;
            boolean sameBig = big1 == big2;          // false, two objects
            show("sameBig", sameBig);
            boolean equalBig = big1.equals(big2);    // true
            show("equalBig", equalBig);
            boolean mixed = big1 == 128;             // true, big1 is unboxed
            show("mixed", mixed);
        }
        {
            Map<String, Integer> stock = new HashMap<>();
            try { int pears = stock.get("pear"); show("pears", pears); } catch (Throwable _t) { System.out.println("pears -> " + _t); }
            int safe = stock.getOrDefault("pear", 0);   // 0
            show("safe", safe);
            Integer discount = null;
            boolean member = false;
            try { int percent = member ? 10 : discount; show("percent", percent); } catch (Throwable _t) { System.out.println("percent -> " + _t); }
        }
        {
            Long id = 10L;
            boolean sameType = id.equals(10L);       // true
            show("sameType", sameType);
            boolean intArg = id.equals(10);          // false, 10 is boxed to Integer
            show("intArg", intArg);
            Map<Long, String> users = Map.of(10L, "Anna");
            String found = users.get(10);            // null, key is an Integer
            show("found", found);
            String fixed = users.get(10L);           // "Anna"
            show("fixed", fixed);
        }
        {
            List<Integer> codes = new ArrayList<>(List.of(10, 20, 30));
            Integer removed = codes.remove(1);       // 20, removed by index
            show("removed", removed);
            boolean found = codes.remove(Integer.valueOf(10));   // true, removed by value
            show("found", found);
            String left = codes.toString();          // "[30]"
            show("left", left);
        }
        {
            List<Integer> prices = List.of(120, 340, 99);
            long total = 0;
            for (int p : prices) {
                total += p;
            }
            long result = total;                     // 559
            show("result", result);
            int streamSum = prices.stream().mapToInt(Integer::intValue).sum();   // 559
            show("streamSum", streamSum);
        }
        {
            int hex = Integer.parseInt("ff", 16);      // 255
            show("hex", hex);
            int order = Integer.compare(3, 7);          // -1
            show("order", order);
            String bits = Integer.toBinaryString(10);   // "1010"
            show("bits", bits);
            boolean digit = Character.isDigit('7');     // true
            show("digit", digit);
            boolean nan = Double.isNaN(0.0 / 0.0);      // true
            show("nan", nan);
            boolean yes = Boolean.parseBoolean("yes");  // false
            show("yes", yes);
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

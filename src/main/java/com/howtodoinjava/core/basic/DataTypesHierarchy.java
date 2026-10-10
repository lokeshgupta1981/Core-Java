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
 * Examples for the tutorial "Data Types in Java: Primitive vs Reference with Examples".
 * https://howtodoinjava.com/java/basics/data-types-in-java/
 */
public class DataTypesHierarchy {
    static enum Unit { KG, PIECE }

    static record Product(String name, double price, Unit unit) {}
    public static void main(String[] args) throws Exception {
        {
            int servings = 4;                       // 32-bit whole number
            show("servings", servings);
            long views = 3_000_000_000L;            // 64-bit, needs the L suffix
            show("views", views);
            double weightKg = 1.25;                 // 64-bit floating point
            show("weightKg", weightKg);
            char size = 'M';                        // one 16-bit character
            show("size", size);
            boolean organic = true;                 // true or false
            show("organic", organic);
            String item = "apple";                  // reference to a String object
            show("item", item);
            int[] prices = {120, 80, 45};           // reference to an array object
            show("prices", prices);
        }
        {
            int a = 5;
            int b = a;
            b = 6;
            int first = a;                          // 5, a did not change
            show("first", first);
            int[] basket = {1, 2};
            int[] same = basket;
            same[0] = 9;
            int changed = basket[0];                // 9, both point to one array
            show("changed", changed);
        }
        {
            String typed = new String("apple");
            boolean sameObject = typed == "apple";      // false
            show("sameObject", sameObject);
            boolean sameText = typed.equals("apple");   // true
            show("sameText", sameText);
        }
        {
            int max = Integer.MAX_VALUE;            // 2147483647
            show("max", max);
            int wrapped = max + 1;                  // -2147483648
            show("wrapped", wrapped);
            long safe = max + 1L;                   // 2147483648
            show("safe", safe);
        }
        {
            long views = 3_000_000_000L;            // 3000000000
            show("views", views);
            float ratio = 0.75f;                    // 0.75
            show("ratio", ratio);
            int mask = 0xFF;                        // 255, hexadecimal
            show("mask", mask);
            int flags = 0b1010;                     // 10, binary
            show("flags", flags);
            int octal = 010;                        // 8, a leading 0 means octal
            show("octal", octal);
            char letter = 'A';                      // A
            show("letter", letter);
            int code = letter;                      // 65
            show("code", code);
        }
        {
            Product pear = new Product("pear", 0.5, Unit.KG);
            String name = pear.name();              // "pear"
            show("name", name);
            Unit unit = pear.unit();                // KG
            show("unit", unit);
            Product none = null;                    // allowed for reference types
            show("none", none);
        }
        {
            int big = 20_000_000;
            long wider = big;                       // 20000000, widening
            show("wider", wider);
            short cut = (short) big;                // 11520, high bits dropped
            show("cut", cut);
            double price = 9.99;
            int whole = (int) price;                // 9, fraction dropped
            show("whole", whole);
            int half = 7 / 2;                       // 3, int division
            show("half", half);
            double exact = 7 / 2.0;                 // 3.5
            show("exact", exact);
        }
        {
            List<Integer> stock = new ArrayList<>();
            stock.add(5);                           // autoboxing int to Integer
            int count = stock.get(0);               // 5, unboxing
            show("count", count);
            Integer a = 127;
            Integer b = 127;
            boolean cached = a == b;                // true, small values are cached
            show("cached", cached);
            Integer c = 128;
            Integer d = 128;
            boolean notCached = c == d;             // false, two objects
            show("notCached", notCached);
            boolean equal = c.equals(d);            // true
            show("equal", equal);
        }
        {
            Integer missing = null;
            try { int boom = missing; show("boom", boom); } catch (Throwable _t) { System.out.println("boom -> " + _t); }
            int fallback = missing != null ? missing : 0;   // 0
            show("fallback", fallback);
        }
        {
            double sum = 0.1 + 0.2;                                          // 0.30000000000000004
            show("sum", sum);
            BigDecimal total = new BigDecimal("0.10").add(new BigDecimal("0.20"));  // 0.30
            show("total", total);
            long cents = 10 + 20;                                            // 30
            show("cents", cents);
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

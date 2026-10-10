package com.howtodoinjava.core.examples;

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
 * Examples for the tutorial "Integer Cache in Java: Wrapper Class Caching (-128 to 127)".
 * https://howtodoinjava.com/java-examples/internal-cache-wrapper-classes/
 */
public class IntegerCacheValueof {

    public static void main(String[] args) throws Exception {
        {
            Integer bagels = 127;
            Integer moreBagels = 127;
            boolean sameSmall = bagels == moreBagels;        // true, both come from the cache
            show("sameSmall", sameSmall);
            Integer muffins = 128;
            Integer moreMuffins = 128;
            boolean sameLarge = muffins == moreMuffins;      // false, two different objects
            show("sameLarge", sameLarge);
            boolean equalLarge = muffins.equals(moreMuffins); // true, same value
            show("equalLarge", equalLarge);
        }
        {
            Integer fromString = Integer.valueOf("100");
            Integer boxed = 100;
            boolean shared = fromString == boxed;            // true, same cached object
            show("shared", shared);
            int primitive = Integer.parseInt("100");         // 100, no object
            show("primitive", primitive);
        }
        {
            Integer cached = Integer.valueOf(100);
            Integer fresh = new Integer(100);
            boolean same = cached == fresh;                  // false, new always allocates
            show("same", same);
        }
        {
            Long smallId = 127L;
            boolean sameLong = smallId == Long.valueOf(127);          // true
            show("sameLong", sameLong);
            Character letter = 'a';
            boolean sameChar = letter == Character.valueOf('a');      // true
            show("sameChar", sameChar);
            Character euro = '\u20ac';
            boolean sameEuro = euro == Character.valueOf('\u20ac');   // false, outside the cache
            show("sameEuro", sameEuro);
            Double price = 1.0;
            boolean sameDouble = price == Double.valueOf(1.0);        // false, Double has no cache
            show("sameDouble", sameDouble);
        }
        {
            Map<String, Integer> expected = Map.of("bagel", 120, "croissant", 300);
            Map<String, Integer> counted = new HashMap<>(Map.of("bagel", 120, "croissant", 300));
            boolean bagelOk = expected.get("bagel") == counted.get("bagel");                // true, cached
            show("bagelOk", bagelOk);
            boolean croissantOk = expected.get("croissant") == counted.get("croissant");    // false, 300 is not cached
            show("croissantOk", croissantOk);
            boolean croissantFixed = Objects.equals(expected.get("croissant"), counted.get("croissant")); // true
            show("croissantFixed", croissantFixed);
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

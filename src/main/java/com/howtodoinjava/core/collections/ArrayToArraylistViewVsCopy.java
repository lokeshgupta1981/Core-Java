package com.howtodoinjava.core.collections;

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
 * Examples for the tutorial "Convert Array to ArrayList in Java (Mutable and Unmodifiable)".
 * https://howtodoinjava.com/java/collections/arraylist/array-to-arraylist/
 */
public class ArrayToArraylistViewVsCopy {

    public static void main(String[] args) throws Exception {
        {
            String[] fruits = {"apple", "banana", "cherry"};
            List<String> mutable = new ArrayList<>(Arrays.asList(fruits));           // [apple, banana, cherry]
            show("mutable", mutable);
            List<String> readOnly = List.of(fruits);                                 // [apple, banana, cherry]
            show("readOnly", readOnly);
            int[] scores = {90, 75, 82};
            List<Integer> boxed = Arrays.stream(scores).boxed().collect(Collectors.toCollection(ArrayList::new));    // [90, 75, 82]
            show("boxed", boxed);
        }
        {
            String[] fruits = {"apple", "banana", "cherry"};
            List<String> basket = new ArrayList<>(Arrays.asList(fruits));
            basket.add("mango");                                                     // basket = [apple, banana, cherry, mango]
            fruits[0] = "kiwi";
            String firstInBasket = basket.get(0);                                    // "apple"
            show("firstInBasket", firstInBasket);
        }
        {
            String[] moreFruits = {"plum", "fig"};
            List<String> basket = new ArrayList<>(List.of("apple"));
            boolean changed = Collections.addAll(basket, moreFruits);                // true
            show("changed", changed);
            List<String> longNames = Arrays.stream(moreFruits)
                    .filter(name -> name.length() > 3)
                    .collect(Collectors.toCollection(ArrayList::new));                   // [plum]
        }
        {
            String[] fruits = {"apple", "banana", "cherry"};
            List<String> view = Arrays.asList(fruits);
            String previous = view.set(1, "blueberry");                              // "banana"
            show("previous", previous);
            String inArray = fruits[1];                                              // "blueberry"
            show("inArray", inArray);
            try { boolean added = view.add("mango"); show("added", added); } catch (Throwable _t) { System.out.println("added -> " + _t); }
        }
        {
            String[] fruits = {"apple", "banana", "cherry"};
            List<String> copied = List.of(fruits);                                   // [apple, banana, cherry]
            show("copied", copied);
            List<String> streamed = Arrays.stream(fruits).toList();                  // [apple, banana, cherry]
            show("streamed", streamed);
            List<String> wrapped = Collections.unmodifiableList(Arrays.asList(fruits));    // [apple, banana, cherry]
            show("wrapped", wrapped);
            fruits[0] = "kiwi";
            String firstCopied = copied.get(0);                                      // "apple"
            show("firstCopied", firstCopied);
            String firstWrapped = wrapped.get(0);                                    // "kiwi"
            show("firstWrapped", firstWrapped);
        }
        {
            int[] heartRates = {72, 95, 130};
            List<int[]> wrong = Arrays.asList(heartRates);
            int wrongSize = wrong.size();                                            // 1
            show("wrongSize", wrongSize);
        }
        {
            List<String> basket = new ArrayList<>(List.of("apple", "fig"));
            String[] asArray = basket.toArray(String[]::new);                        // [apple, fig]
            show("asArray", asArray);
            List<Integer> rates = List.of(72, 95, 130);
            int[] primitives = rates.stream().mapToInt(Integer::intValue).toArray();    // [72, 95, 130]
            show("primitives", primitives);
        }
        {
            String[] tags = {"red", "blue", "red"};
            Set<String> uniqueTags = new LinkedHashSet<>(Arrays.asList(tags));     // [red, blue]
            show("uniqueTags", uniqueTags);
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

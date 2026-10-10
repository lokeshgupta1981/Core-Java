package com.howtodoinjava.core.streams;

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

/**
 * Examples for the tutorial "Java Stream toArray(): Convert a Stream to an Array".
 * https://howtodoinjava.com/java8/convert-stream-to-array/
 */
public class StreamToArrayExamples {
    static record Product(String name, int price) {}
    public static void main(String[] args) throws Exception {
        {
            Object[] objects = Stream.of("apple", "banana").toArray();                    // [apple, banana]
            show("objects", objects);
            String[] fruits = Stream.of("apple", "banana").toArray(String[]::new);         // [apple, banana]
            show("fruits", fruits);
            int[] scores = Stream.of(7, 9, 4).mapToInt(Integer::intValue).toArray();      // [7, 9, 4]
            show("scores", scores);
        }
        {
            String[] byReference = Stream.of("kiwi", "fig").toArray(String[]::new);          // [kiwi, fig]
            show("byReference", byReference);
            String[] byLambda = Stream.of("kiwi", "fig").toArray(size -> new String[size]);   // [kiwi, fig]
            show("byLambda", byLambda);
        }
        {
            try { String[] wrong = (String[]) Stream.of("kiwi").toArray(); show("wrong", wrong); } catch (Throwable _t) { System.out.println("wrong -> " + _t); }
        }
        {
            List<String> basket = List.of("kiwi", "apple", "fig", "banana");

            String[] longNames = basket.stream()
                    .filter(name -> name.length() > 4)
                    .map(String::toUpperCase)
                    .toArray(String[]::new);                    // [APPLE, BANANA]
        }
        {
            List<Product> catalog = List.of(new Product("apple", 5), new Product("melon", 12), new Product("fig", 4));
            Product[] cheap = catalog.stream().filter(p -> p.price() < 10).toArray(Product[]::new);   // [Product[name=apple, price=5], Product[name=fig, price=4]]
            show("cheap", cheap);
        }
        {
            List<Integer> ratings = List.of(4, 5, 3);

            Integer[] boxed = ratings.stream().toArray(Integer[]::new);                  // [4, 5, 3]
            show("boxed", boxed);
            int[] primitive = ratings.stream().mapToInt(Integer::intValue).toArray();    // [4, 5, 3]
            show("primitive", primitive);
            double[] prices = Stream.of("1.5", "2.25").mapToDouble(Double::parseDouble).toArray();   // [1.5, 2.25]
            show("prices", prices);
        }
        {
            int[] firstTen = IntStream.iterate(1, i -> i + 1).limit(10).toArray();           // [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
            show("firstTen", firstTen);
            Integer[] evens = IntStream.rangeClosed(1, 6).filter(i -> i % 2 == 0).boxed().toArray(Integer[]::new);   // [2, 4, 6]
            show("evens", evens);
        }
        {
            Stream<Object> mixed = Stream.of("apple", 42);
            try { String[] notAllStrings = mixed.toArray(String[]::new); show("notAllStrings", notAllStrings); } catch (Throwable _t) { System.out.println("notAllStrings -> " + _t); }
        }
        {
            List<Object> values = List.of("apple", 42, "fig");
            String[] onlyStrings = values.stream().filter(String.class::isInstance).map(String.class::cast).toArray(String[]::new);   // [apple, fig]
            show("onlyStrings", onlyStrings);
        }
        {
            try { String[] tooSmall = Stream.of("a", "b", "c").toArray(n -> new String[2]); show("tooSmall", tooSmall); } catch (Throwable _t) { System.out.println("tooSmall -> " + _t); }
        }
        {
            List<String> tags = List.of("new", "sale");
            String[] tagArray = tags.toArray(String[]::new);                              // [new, sale]
            show("tagArray", tagArray);
        }
        {
            Integer[] doubled = Stream.of(5, 3, 8).parallel().map(i -> i * 2).toArray(Integer[]::new);   // [10, 6, 16]
            show("doubled", doubled);
        }
        {
            String upload = """
            apple,5,red
            banana,3,yellow

            kiwi,8,green
            """;

            String[][] rows = upload.lines()
                    .filter(line -> !line.isBlank())
                    .map(line -> line.split(","))
                    .toArray(String[][]::new);
            int rowCount = rows.length;                         // 3
            show("rowCount", rowCount);
            String kiwiColor = rows[2][2];                      // "green"
            show("kiwiColor", kiwiColor);
        }
        {
            int[] values = Stream.of(1, 2, 3).mapToInt(Integer::intValue).toArray();   // [1, 2, 3]
            show("values", values);
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

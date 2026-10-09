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

import org.apache.commons.lang3.ArrayUtils;
import java.util.regex.*;
import java.nio.charset.*;
import java.nio.*;
import java.security.*;
import java.lang.reflect.*;
import java.lang.invoke.*;

/**
 * Examples for the tutorial "Print an Array in Java: toString(), deepToString() and Loops".
 * https://howtodoinjava.com/java/array/print-array-elements/
 */
public class PrintArray {
    static record Item(String name, int qty) {}
    static record Student(String name, int[] marks) {}
    static record Report(String title, int[] totals) {
        @Override
        public String toString() {
            return "Report[title=" + title + ", totals=" + Arrays.toString(totals) + "]";
        }
    }
    public static void main(String[] args) throws Exception {
        {
            int[] scores = {90, 75, 82};
            String raw = String.valueOf(scores);             // "[I@" followed by a hash code
            show("raw", raw);
            String flat = Arrays.toString(scores);           // "[90, 75, 82]"
            show("flat", flat);
            int[][] board = {{1, 0}, {0, 1}};
            String nested = Arrays.deepToString(board);      // "[[1, 0], [0, 1]]"
            show("nested", nested);
        }
        {
            String intType = new int[0].getClass().getName();         // "[I"
            show("intType", intType);
            String textType = new String[0].getClass().getName();     // "[Ljava.lang.String;"
            show("textType", textType);
            String gridType = new int[0][0].getClass().getName();     // "[[I"
            show("gridType", gridType);
        }
        {
            double[] prices = {9.99, 4.5};
            String p = Arrays.toString(prices);                // "[9.99, 4.5]"
            show("p", p);
            boolean[] flags = {true, false};
            String f = Arrays.toString(flags);                 // "[true, false]"
            show("f", f);
            String[] fruits = {"apple", null, "kiwi"};
            String s = Arrays.toString(fruits);                // "[apple, null, kiwi]"
            show("s", s);
            int[] none = null;
            String n = Arrays.toString(none);                  // "null"
            show("n", n);
            String empty = Arrays.toString(new int[0]);        // "[]"
            show("empty", empty);
        }
        {
            Item[] cart = {new Item("milk", 2), new Item("bread", 1)};
            String items = Arrays.toString(cart);   // "[Item[name=milk, qty=2], Item[name=bread, qty=1]]"
            show("items", items);
        }
        {
            int[][] seats = {{1, 0, 1}, {0, 0, 1}};
            String shallow = Arrays.toString(seats);       // "[[I@..., [I@...]", row hash codes
            show("shallow", shallow);
            String deep = Arrays.deepToString(seats);      // "[[1, 0, 1], [0, 0, 1]]"
            show("deep", deep);
            String[][] menu = {{"tea", "coffee"}, {"cake"}};
            String jagged = Arrays.deepToString(menu);     // "[[tea, coffee], [cake]]"
            show("jagged", jagged);
        }
        {
            int[][] seats = {{1, 0, 1}, {0, 0, 1}};
            for (int[] row : seats) {
                System.out.println(Arrays.toString(row));
            }
        }
        {
            String[] tasks = {"build", "test", "deploy"};
            for (int i = 0; i < tasks.length; i++) {
                System.out.println((i + 1) + ". " + tasks[i]);
            }
        }
        {
            String[] tasks = {"build", "test", "deploy"};
            String pipeline = String.join(" -> ", tasks);       // "build -> test -> deploy"
            show("pipeline", pipeline);
            int[] ports = {8080, 8443, 9000};
            String csv = Arrays.stream(ports).mapToObj(String::valueOf).collect(Collectors.joining(","));   // "8080,8443,9000"
            show("csv", csv);
        }
        {
            String[] steps = {"build", "test", "deploy"};
            Arrays.stream(steps).forEach(System.out::println);
        }
        {
            Student s = new Student("Lokesh", new int[] {80, 92});
            String text = s.toString();          // "Student[name=Lokesh, marks=[I@...]"
            show("text", text);
        }
        {
            Report r = new Report("Q1", new int[] {120, 95});
            String line = r.toString();          // "Report[title=Q1, totals=[120, 95]]"
            show("line", line);
        }
        {
            char[] code = {'J', 'a', 'v', 'a'};
            System.out.println(code);                     // prints Java
            String joined = "Code: " + code;              // "Code: [C@" followed by a hash code
            show("joined", joined);
            String asText = "Code: " + new String(code);  // "Code: Java"
            show("asText", asText);
            String asList = Arrays.toString(code);        // "[J, a, v, a]"
            show("asList", asList);
        }
        {
            byte[] data = "Hi".getBytes(StandardCharsets.UTF_8);
            String numbers = Arrays.toString(data);                       // "[72, 105]"
            show("numbers", numbers);
            String text = new String(data, StandardCharsets.UTF_8);       // "Hi"
            show("text", text);
            String hex = HexFormat.of().formatHex(data);                  // "4869"
            show("hex", hex);
        }
        {
            int[] pair = {1, 2};
            String commons = ArrayUtils.toString(pair);          // "{1,2}"
            show("commons", commons);
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

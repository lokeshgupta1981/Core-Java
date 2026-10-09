package com.howtodoinjava.java25.keywords;

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
 * Examples for the tutorial "Java Keywords List".
 * https://howtodoinjava.com/java/basics/java-keywords/
 */
public class JavaKeywords {
    static record Point(int x, int y) {}
    public static void main(String[] args) throws Exception {
        {
            var point = new Point(3, -4);
            String side = switch (point) {
                case Point(var px, var _) when px > 0 -> {
                    yield "right";
                }
                default -> "left or center";
            };
            String result = side;                   // "right"
            show("result", result);
        }
        {
            String name = null;
            boolean known = name != null;           // false
            show("known", known);
        }
        {
            int record = 3;                         // legal, record is contextual
            show("record", record);
            int yield = 2;                          // legal variable name
            show("yield", yield);
            var var = "ok";                         // legal, but confusing
            show("var", var);
            int total = record + yield;             // 5
            show("total", total);
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

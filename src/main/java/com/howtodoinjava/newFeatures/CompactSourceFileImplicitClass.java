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
 * Examples for the tutorial "Compact Source Files and Instance Main Methods in Java 25".
 * https://howtodoinjava.com/java/java-unnamed-class-instance-method/
 */
public class CompactSourceFileImplicitClass {
    public static void main(String[] args) throws Exception {
        {
            BigDecimal price = new BigDecimal("4.50").multiply(BigDecimal.TWO);   // 9.00
            show("price", price);
            LocalDate due = LocalDate.of(2026, 10, 10).plusDays(30);              // 2026-11-09
            show("due", due);
            Path report = Path.of("reports", "october.txt");                      // reports/october.txt
            show("report", report);
            Map<String, Integer> stock = new TreeMap<>(Map.of("rice", 2));         // {rice=2}
            show("stock", stock);
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

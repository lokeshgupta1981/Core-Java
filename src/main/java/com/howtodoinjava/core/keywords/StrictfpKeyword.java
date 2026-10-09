package com.howtodoinjava.core.keywords;

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
 * Examples for the tutorial "Java strictfp Keyword".
 * https://howtodoinjava.com/java/keywords/strictfp-modifier/
 */
public class StrictfpKeyword {
    static strictfp class Invoice {
        double total(double net, double taxRate) {
            return net * (1 + taxRate);
        }
    }
    public static void main(String[] args) throws Exception {
        {
            double max = Double.MAX_VALUE;
            double result = max * 2 / 2;            // Infinity on Java 17+, with or without strictfp
            show("result", result);
        }
        {
            double total = new Invoice().total(100.0, 0.25);   // 125.0
            show("total", total);
        }
        {
            double sum = 0.1 + 0.2;                 // 0.30000000000000004
            show("sum", sum);
            boolean exact = sum == 0.3;             // false
            show("exact", exact);
            BigDecimal money = new BigDecimal("0.1").add(new BigDecimal("0.2"));   // 0.3
            show("money", money);
        }
        {
            double fast = Math.sin(1e6);           // may differ in the last bit on some JVMs
            show("fast", fast);
            double portable = StrictMath.sin(1e6);  // -0.34999350217129294
            show("portable", portable);
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

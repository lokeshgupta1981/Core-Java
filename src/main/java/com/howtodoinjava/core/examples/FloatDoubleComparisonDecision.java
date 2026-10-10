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
import org.junit.jupiter.api.Assertions;

/**
 * Examples for the tutorial "Compare Float and Double in Java: Epsilon, compare(), BigDecimal".
 * https://howtodoinjava.com/java-examples/correctly-compare-float-double/
 */
public class FloatDoubleComparisonDecision {
    static boolean nearlyEqual(double a, double b, double relTol, double absTol) {
        if (a == b) {
            return true;                                     // same value, also equal infinities
        }
        double diff = Math.abs(a - b);
        return diff <= absTol || diff <= relTol * Math.max(Math.abs(a), Math.abs(b));
    }
    public static void main(String[] args) throws Exception {
        {
            double distance = 0.0;
            for (int lap = 0; lap < 11; lap++) {
                distance += 0.1;
            }
            double target = 1.1;
            boolean equalOperator = distance == target;                // false, distance is 1.0999999999999999
            show("equalOperator", equalOperator);
            boolean withinTolerance = Math.abs(distance - target) < 1e-9; // true
            show("withinTolerance", withinTolerance);
            int order = Double.compare(distance, target);              // -1, distance is slightly smaller
            show("order", order);
        }
        {
            BigDecimal storedTenth = new BigDecimal(0.1);   // 0.1000000000000000055511151231257827021181583404541015625
            show("storedTenth", storedTenth);
            double sum = 0.1 + 0.2;                         // 0.30000000000000004
            show("sum", sum);
            boolean sumIsExact = sum == 0.3;                // false
            show("sumIsExact", sumIsExact);
            boolean quarterIsExact = 0.25 + 0.5 == 0.75;    // true, quarters are exact in binary
            show("quarterIsExact", quarterIsExact);
        }
        {
            float priceFloat = 0.1f;
            double priceDouble = 0.1;
            double widened = priceFloat;                              // 0.10000000149011612
            show("widened", widened);
            boolean mixed = priceFloat == priceDouble;                // false
            show("mixed", mixed);
            boolean asFloats = priceFloat == (float) priceDouble;     // true, both rounded to float
            show("asFloats", asFloats);
            boolean exactValue = 5.25f == 5.25;                       // true, 5.25 is exact in binary
            show("exactValue", exactValue);
        }
        {
            double total = 4_000_000.7 * 3;                            // 1.2000002100000001E7
            show("total", total);
            boolean bigAbsolute = Math.abs(total - 12_000_002.1) < 1e-9;  // false, the difference is 1.86E-9
            show("bigAbsolute", bigAbsolute);
            boolean tinyAbsolute = Math.abs(1e-10 - 3e-10) < 1e-9;        // true, although one value is three times the other
            show("tinyAbsolute", tinyAbsolute);
            double gapNearOne = Math.ulp(1.1);                            // 2.220446049250313E-16
            show("gapNearOne", gapNearOne);
            double gapNearBig = Math.ulp(1e15);                           // 0.125
            show("gapNearBig", gapNearBig);
        }
        {
            boolean small = nearlyEqual(0.1 + 0.2, 0.3, 1e-12, 1e-12);             // true
            show("small", small);
            boolean big = nearlyEqual(4_000_000.7 * 3, 12_000_002.1, 1e-12, 1e-12); // true
            show("big", big);
            boolean tiny = nearlyEqual(1e-10, 3e-10, 1e-12, 1e-12);                // false
            show("tiny", tiny);
            boolean nan = nearlyEqual(Double.NaN, Double.NaN, 1e-12, 1e-12);       // false
            show("nan", nan);
        }
        {
            Assertions.assertEquals(0.3, 0.1 + 0.2, 1e-9);          // passes, the difference is 5.6E-17
        }
        {
            double nan = 0.0 / 0.0;
            boolean nanEquals = nan == nan;                         // false, NaN is never == to anything
            show("nanEquals", nanEquals);
            int nanCompare = Double.compare(nan, nan);              // 0
            show("nanCompare", nanCompare);
            boolean nanCheck = Double.isNaN(nan);                   // true
            show("nanCheck", nanCheck);
            boolean zeros = 0.0 == -0.0;                            // true
            show("zeros", zeros);
            int zeroCompare = Double.compare(0.0, -0.0);            // 1, 0.0 sorts after -0.0
            show("zeroCompare", zeroCompare);
            boolean boxedZeros = Double.valueOf(0.0).equals(-0.0);  // false
            show("boxedZeros", boxedZeros);
        }
        {
            BigDecimal first = new BigDecimal("19.99");
            BigDecimal second = new BigDecimal("5.01");
            BigDecimal invoiceTotal = first.add(second);                  // 25.00
            show("invoiceTotal", invoiceTotal);
            BigDecimal paidTotal = new BigDecimal("25");
            boolean sameAmount = invoiceTotal.compareTo(paidTotal) == 0;   // true
            show("sameAmount", sameAmount);
            boolean sameObject = invoiceTotal.equals(paidTotal);           // false, scale 2 vs scale 0
            show("sameObject", sameObject);
        }
        {
            BigDecimal fromConstructor = new BigDecimal(0.1);     // 0.1000000000000000055511151231257827021181583404541015625
            show("fromConstructor", fromConstructor);
            BigDecimal fromValueOf = BigDecimal.valueOf(0.1);      // 0.1
            show("fromValueOf", fromValueOf);
            BigDecimal fromSum = BigDecimal.valueOf(0.1 + 0.2);   // 0.30000000000000004, the error is already in the double
            show("fromSum", fromSum);
            BigDecimal rounded = fromSum.setScale(2, RoundingMode.HALF_UP);   // 0.30
            show("rounded", rounded);
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

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
import java.text.DecimalFormat;
import org.apache.commons.math3.util.Precision;

/**
 * Examples for the tutorial "Round a Double to 2 Decimal Places in Java (or N Places)".
 * https://howtodoinjava.com/java/basics/round-off-n-decimal-places/
 */
public class RoundDouble2DecimalPlaces {
    static double round(double value, int places) {
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            return value;
        }
        return BigDecimal.valueOf(value)
                .setScale(places, RoundingMode.HALF_UP)
                .doubleValue();
    }
    static double roundWithMath(double value, int places) {
        double factor = Math.pow(10, places);
        return Math.round(value * factor) / factor;
    }
    public static void main(String[] args) throws Exception {
        {
            double price = 4.56789;

            double exact = BigDecimal.valueOf(price).setScale(2, RoundingMode.HALF_UP).doubleValue();   // 4.57
            show("exact", exact);
            double quick = Math.round(price * 100.0) / 100.0;                                          // 4.57
            show("quick", quick);
            String text = String.format("%.2f", price);                                                // "4.57"
            show("text", text);
            String shown = new DecimalFormat("0.00").format(price);                                    // "4.57"
            show("shown", shown);
        }
        {
            double amount = 2.675;

            BigDecimal fromCtor = new BigDecimal(amount).setScale(2, RoundingMode.HALF_UP);       // 2.67
            show("fromCtor", fromCtor);
            BigDecimal fromValueOf = BigDecimal.valueOf(amount).setScale(2, RoundingMode.HALF_UP); // 2.68
            show("fromValueOf", fromValueOf);
            BigDecimal fromText = new BigDecimal("2.675").setScale(2, RoundingMode.HALF_UP);       // 2.68
            show("fromText", fromText);
        }
        {
            double twoPlaces = round(4.56789, 2);       // 4.57
            show("twoPlaces", twoPlaces);
            double threePlaces = round(4.56789, 3);     // 4.568
            show("threePlaces", threePlaces);
            double zeroPlaces = round(4.56789, 0);      // 5.0
            show("zeroPlaces", zeroPlaces);
            double hundreds = round(1234.5, -2);        // 1200.0
            show("hundreds", hundreds);
        }
        {
            BigDecimal ok = new BigDecimal("2.50").setScale(2, RoundingMode.UNNECESSARY);    // 2.50
            show("ok", ok);
            try { BigDecimal lost = new BigDecimal("2.345").setScale(2, RoundingMode.UNNECESSARY); show("lost", lost); } catch (Throwable _t) { System.out.println("lost -> " + _t); }
        }
        {
            double rounded = roundWithMath(4.56789, 2);       // 4.57
            show("rounded", rounded);
            double wrongType = Math.round(4.56789 * 100) / 100;  // 4.0
            show("wrongType", wrongType);
            double surprise = roundWithMath(1.005, 2);          // 1.0
            show("surprise", surprise);
        }
        {
            double negative = Math.round(-0.125 * 100) / 100.0;   // -0.12
            show("negative", negative);
            double huge = Math.round(1e17 * 100) / 100.0;          // 9.223372036854776E16
            show("huge", huge);
        }
        {
            double temperature = 21.456;

            String label = String.format("%.2f", temperature);          // "21.46"
            show("label", label);
            String sameLabel = "%.2f".formatted(temperature);           // "21.46"
            show("sameLabel", sameLabel);
            String padded = String.format("%08.2f", temperature);       // "00021.46"
            show("padded", padded);
        }
        {
            String keepZeros = new DecimalFormat("0.00").format(1.5);    // "1.50"
            show("keepZeros", keepZeros);
            String dropZeros = new DecimalFormat("#.##").format(1.5);    // "1.5"
            show("dropZeros", dropZeros);
            String grouped = new DecimalFormat("#,##0.00").format(1234567.891);   // "1,234,567.89"
            show("grouped", grouped);
        }
        {
            String formatTie = String.format("%.2f", 0.125);                 // "0.13"
            show("formatTie", formatTie);
            String decimalTie = new DecimalFormat("0.00").format(0.125);     // "0.12"
            show("decimalTie", decimalTie);
            String formatBinary = String.format("%.2f", 2.675);              // "2.68"
            show("formatBinary", formatBinary);
            String decimalBinary = new DecimalFormat("0.00").format(2.675);  // "2.67"
            show("decimalBinary", decimalBinary);
        }
        {
            DecimalFormat df = new DecimalFormat("0.00");
            df.setRoundingMode(RoundingMode.HALF_UP);
            String halfUp = df.format(0.125);                                // "0.13"
            show("halfUp", halfUp);
        }
        {
            String german = String.format(Locale.GERMANY, "%.2f", 21.456);   // "21,46"
            show("german", german);
            String csvValue = String.format(Locale.ROOT, "%.2f", 21.456);    // "21.46"
            show("csvValue", csvValue);
        }
        {
            float weight = 1.005f;

            BigDecimal widened = BigDecimal.valueOf(weight).setScale(2, RoundingMode.HALF_UP);              // 1.00
            show("widened", widened);
            BigDecimal fromFloatText = new BigDecimal(Float.toString(weight)).setScale(2, RoundingMode.HALF_UP); // 1.01
            show("fromFloatText", fromFloatText);
            float back = fromFloatText.floatValue();                                                        // 1.01
            show("back", back);
        }
        {
            boolean sameFloat = 1_000_000.01f == 1_000_000.00f;    // true
            show("sameFloat", sameFloat);
        }
        {
            double fromLibrary = Precision.round(4.56789, 2);   // 4.57
            show("fromLibrary", fromLibrary);
            double tie = Precision.round(2.675, 2);             // 2.68
            show("tie", tie);
            double hardCase = Precision.round(1.005, 2);        // 1.01
            show("hardCase", hardCase);
        }
        {
            double driftSum = 0.1 + 0.2;                       // 0.30000000000000004
            show("driftSum", driftSum);
        }
        {
            BigDecimal unitPrice = new BigDecimal("19.99");
            BigDecimal taxRate = new BigDecimal("0.0825");

            BigDecimal rawTax = unitPrice.multiply(taxRate);                   // 1.649175
            show("rawTax", rawTax);
            BigDecimal tax = rawTax.setScale(2, RoundingMode.HALF_UP);         // 1.65
            show("tax", tax);
            BigDecimal total = unitPrice.add(tax);                             // 21.64
            show("total", total);
            String receiptLine = String.format(Locale.ROOT, "Total: %.2f", total);   // "Total: 21.64"
            show("receiptLine", receiptLine);
        }
        {
            BigDecimal cut = BigDecimal.valueOf(4.56789).setScale(2, RoundingMode.DOWN);   // 4.56
            show("cut", cut);
        }
        {
            BigDecimal withScale = BigDecimal.valueOf(1.5).setScale(2, RoundingMode.HALF_UP);   // 1.50
            show("withScale", withScale);
            double asDouble = withScale.doubleValue();                                          // 1.5
            show("asDouble", asDouble);
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

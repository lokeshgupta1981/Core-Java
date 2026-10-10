package com.howtodoinjava.core.string;

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
import java.text.DecimalFormatSymbols;

/**
 * Examples for the tutorial "Convert Float to String in Java with N Decimal Places".
 * https://howtodoinjava.com/java/string/float-to-string-format-decimal-points/
 */
public class FloatToStringDecimalPlaces {
    static String toPlainString(float value) {
        if (Float.isNaN(value) || Float.isInfinite(value)) {
            return Float.toString(value);
        }
        return new BigDecimal(Float.toString(value)).stripTrailingZeros().toPlainString();
    }
    public static void main(String[] args) throws Exception {
        {
            float distance = 19.5f;

            String text = Float.toString(distance);                               // "19.5"
            show("text", text);
            String same = String.valueOf(distance);                               // "19.5"
            show("same", same);
            String twoDecimals = String.format("%.2f", distance);                 // "19.50"
            show("twoDecimals", twoDecimals);
            String formatted = "%.2f".formatted(distance);                        // "19.50" (Java 15+)
            show("formatted", formatted);
            String pattern = new DecimalFormat("0.00").format(distance);          // "19.50"
            show("pattern", pattern);
            String noTrailingZeros = new DecimalFormat("0.##").format(distance);  // "19.5"
            show("noTrailingZeros", noTrailingZeros);
            String fixedLocale = String.format(Locale.ROOT, "%.2f", distance);    // "19.50" on every machine
            show("fixedLocale", fixedLocale);
            String german = String.format(Locale.GERMANY, "%.2f", distance);      // "19,50"
            show("german", german);
        }
        {
            Float discount = null;

            String viaValueOf = String.valueOf(discount);              // "null"
            show("viaValueOf", viaValueOf);
            String viaConcat = "Discount: " + discount;                // "Discount: null"
            show("viaConcat", viaConcat);
            String viaObjects = Objects.toString(discount, "none");    // "none"
            show("viaObjects", viaObjects);
            try { String crash = Float.toString(discount); show("crash", crash); } catch (Throwable _t) { System.out.println("crash -> " + _t); }
            String cut = String.format("%.2f", discount);              // "nu"
            show("cut", cut);
        }
        {
            Float discount = null;
            String safe = discount == null ? "none" : String.format("%.2f", discount);   // "none"
            show("safe", safe);
        }
        {
            float pi = 3.1415927f;

            String two = String.format("%.2f", pi);                     // "3.14"
            show("two", two);
            String none = String.format("%.0f", pi);                    // "3"
            show("none", none);
            String four = String.format("%.4f", pi);                    // "3.1416"
            show("four", four);
            String six = String.format("%f", pi);                       // "3.141593" (default precision is 6)
            show("six", six);
            String width = String.format("%8.2f", pi);                  // "    3.14" (8 characters, right-aligned)
            show("width", width);
            String left = String.format("%-8.2f", pi);                  // "3.14    "
            show("left", left);
            String zeros = String.format("%08.2f", pi);                 // "00003.14"
            show("zeros", zeros);
            String sign = String.format("%+.2f", pi);                   // "+3.14"
            show("sign", sign);
            String grouped = String.format(Locale.US, "%,.2f", 1234567.5f);   // "1,234,567.50"
            show("grouped", grouped);
            String scientific = String.format(Locale.US, "%.2e", 12345.678f); // "1.23e+04"
            show("scientific", scientific);
        }
        {
            DecimalFormat twoDigits = new DecimalFormat("0.00");

            String pi = twoDigits.format(3.1415927f);                         // "3.14"
            show("pi", pi);
            String five = twoDigits.format(5f);                               // "5.00"
            show("five", five);
            String trimmed = new DecimalFormat("0.##").format(5f);            // "5"
            show("trimmed", trimmed);
            String half = new DecimalFormat("0.##").format(0.5f);             // "0.5"
            show("half", half);
            String grouped = new DecimalFormat("#,##0.00").format(1234567.5f); // "1,234,567.50"
            show("grouped", grouped);
        }
        {
            DecimalFormat halfUp = new DecimalFormat("0.00");
            halfUp.setRoundingMode(RoundingMode.HALF_UP);
            String up = halfUp.format(0.125f);                                // "0.13"
            show("up", up);

            DecimalFormat down = new DecimalFormat("0.00");
            down.setRoundingMode(RoundingMode.DOWN);
            String cut = down.format(3.999f);                                 // "3.99"
            show("cut", cut);

            DecimalFormat german = new DecimalFormat("0.00", DecimalFormatSymbols.getInstance(Locale.GERMANY));
            String withComma = german.format(3.1415927f);                     // "3,14"
            show("withComma", withComma);
        }
        {
            float value = 2.675f;
            DecimalFormat halfUp = new DecimalFormat("0.00");
            halfUp.setRoundingMode(RoundingMode.HALF_UP);

            BigDecimal exact = new BigDecimal(value);              // 2.6749999523162841796875
            show("exact", exact);
            String viaFormat = String.format("%.2f", value);       // "2.67"
            show("viaFormat", viaFormat);
            String viaDecimalFormat = halfUp.format(value);        // "2.67" (RoundingMode.HALF_UP)
            show("viaDecimalFormat", viaDecimalFormat);
            String viaBigDecimal = new BigDecimal(Float.toString(value))
                    .setScale(2, RoundingMode.HALF_UP)
                    .toPlainString();                                  // "2.68"
        }
        {
            String big = toPlainString(1.0E10f);            // "10000000000"
            show("big", big);
            String small = toPlainString(1.0E-4f);          // "0.0001"
            show("small", small);
            String trimmed = toPlainString(19.50f);         // "19.5"
            show("trimmed", trimmed);
            String whole = toPlainString(100f);             // "100"
            show("whole", whole);
            String notANumber = toPlainString(Float.NaN);   // "NaN"
            show("notANumber", notANumber);
        }
        {
            BigDecimal rounded = new BigDecimal(Float.toString(19.456f)).setScale(2, RoundingMode.HALF_UP);   // 19.46
            show("rounded", rounded);
            float back = rounded.floatValue();                                                              // 19.46
            show("back", back);
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

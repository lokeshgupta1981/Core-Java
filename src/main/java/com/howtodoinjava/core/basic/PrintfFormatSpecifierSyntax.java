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

/**
 * Examples for the tutorial "Java printf() and String.format(): Format Specifier Guide".
 * https://howtodoinjava.com/java/basics/printf-style-output-formatting/
 */
public class PrintfFormatSpecifierSyntax {
    static record LineItem(String name, int qty, double unitPrice) {}
    public static void main(String[] args) throws Exception {
        {
            String name = "Lokesh";
            int age = 40;

            String sentence = String.format("%s is %d years old", name, age);    // "Lokesh is 40 years old"
            show("sentence", sentence);
            String price = String.format("%.2f", 7.5);                           // "7.50"
            show("price", price);
            String row = String.format("|%-8s|%5d|", "tea", 42);                 // "|tea     |   42|"
            show("row", row);
            String same = "%s is %d years old".formatted(name, age);             // "Lokesh is 40 years old"
            show("same", same);
            System.out.printf("%s is %d years old%n", name, age);               // prints Lokesh is 40 years old
        }
        {
            String full = String.format("%1$,10.2f", 1234.5);           // "  1,234.50"
            show("full", full);
        }
        {
            String upper = String.format("%S", "coffee");              // "COFFEE"
            show("upper", upper);
            String fixed = String.format("%f", 3.5);                    // "3.500000"
            show("fixed", fixed);
            String scientific = String.format("%e", 12345.678);         // "1.234568e+04"
            show("scientific", scientific);
            String hex = String.format("%x", 255);                      // "ff"
            show("hex", hex);
            String octal = String.format("%o", 8);                      // "10"
            show("octal", octal);
            String letter = String.format("%c", 65);                    // "A"
            show("letter", letter);
            String nullText = String.format("%s", (Object) null);       // "null"
            show("nullText", nullText);
            String nullFlag = String.format("%b", (Object) null);       // "false"
            show("nullFlag", nullFlag);
            String percent = String.format("%d%%", 15);                 // "15%"
            show("percent", percent);
        }
        {
            String right = String.format("[%10s]", "tea");             // "[       tea]"
            show("right", right);
            String left = String.format("[%-10s]", "tea");              // "[tea       ]"
            show("left", left);
            String cut = String.format("[%.3s]", "coffee");             // "[cof]"
            show("cut", cut);
            String cutAndPad = String.format("[%10.3s]", "coffee");     // "[       cof]"
            show("cutAndPad", cutAndPad);
        }
        {
            String zeros = String.format("%05d", 42);                   // "00042"
            show("zeros", zeros);
            String grouped = String.format("%,d", 1234567);             // "1,234,567"
            show("grouped", grouped);
            String signed = String.format("%+d", 42);                   // "+42"
            show("signed", signed);
            String accounting = String.format("%(d", -42);              // "(42)"
            show("accounting", accounting);
            String prefixed = String.format("%#x", 255);                // "0xff"
            show("prefixed", prefixed);
            String padded = String.format("%08.2f", -3.14159);          // "-0003.14"
            show("padded", padded);
            String negativeAmount = String.format("%(,.2f", -1234.5);   // "(1,234.50)"
            show("negativeAmount", negativeAmount);
        }
        {
            String twice = String.format("%1$d times %1$d is %2$d", 3, 9);   // "3 times 3 is 9"
            show("twice", twice);
            String reordered = String.format("%2$s, %1$s", "Lokesh", "Gupta");  // "Gupta, Lokesh"
            show("reordered", reordered);
            String previous = String.format("%s %<S", "tea");                  // "tea TEA"
            show("previous", previous);
        }
        {
            LocalDateTime meeting = LocalDateTime.of(2026, 10, 10, 14, 5, 9);

            String isoDate = String.format("%tF", meeting);                         // "2026-10-10"
            show("isoDate", isoDate);
            String time = String.format("%tT", meeting);                            // "14:05:09"
            show("time", time);
            String shortTime = String.format("%tR", meeting);                       // "14:05"
            show("shortTime", shortTime);
            String longDate = String.format("%1$tA, %1$tB %1$te, %1$tY", meeting);   // "Saturday, October 10, 2026"
            show("longDate", longDate);
            String clock = String.format("%tI:%<tM %<Tp", meeting);                 // "02:05 PM"
            show("clock", clock);
            try { String noZone = String.format("%tZ", meeting); show("noZone", noZone); } catch (Throwable _t) { System.out.println("noZone -> " + _t); }
        }
        {
            try { String wrongType = String.format("%d", 3.5); show("wrongType", wrongType); } catch (Throwable _t) { System.out.println("wrongType -> " + _t); }
            try { String missing = String.format("%s and %s", "tea"); show("missing", missing); } catch (Throwable _t) { System.out.println("missing -> " + _t); }
            try { String cStyle = String.format("%i", 3); show("cStyle", cStyle); } catch (Throwable _t) { System.out.println("cStyle -> " + _t); }
            try { String precision = String.format("%.2d", 5); show("precision", precision); } catch (Throwable _t) { System.out.println("precision -> " + _t); }
        }
        {
            String extra = String.format("%s", "tea", "coffee");    // "tea"
            show("extra", extra);
        }
        {
            StringBuilder report = new StringBuilder();
            try (Formatter formatter = new Formatter(report)) {
                formatter.format("%-6s%4d%n", "tea", 3);
                formatter.format("%-6s%4d%n", "milk", 12);
            }
            List<String> lines = report.toString().lines().toList();   // [tea      3, milk    12]
            show("lines", lines);
        }
        {
            String german = String.format(Locale.GERMANY, "%,.2f", 1234.5);    // "1.234,50"
            show("german", german);
            String us = String.format(Locale.US, "%,.2f", 1234.5);             // "1,234.50"
            show("us", us);
            String machine = String.format(Locale.ROOT, "%.2f", 1234.5);       // "1234.50"
            show("machine", machine);
        }
        {
            List<LineItem> items = List.of(
            new LineItem("Cappuccino", 2, 3.5),
            new LineItem("Croissant", 3, 2.25),
            new LineItem("Water", 1, 1.0));

            System.out.printf(Locale.ROOT, "%-12s%5s%10s%n", "Item", "Qty", "Total");
            double sum = 0;
            for (LineItem item : items) {
                double lineTotal = item.qty() * item.unitPrice();
                sum += lineTotal;
                System.out.printf(Locale.ROOT, "%-12s%5d%10.2f%n", item.name(), item.qty(), lineTotal);
            }
            System.out.printf(Locale.ROOT, "%-12s%5s%10.2f%n", "TOTAL", "", sum);
        }
        {
            String twoDecimals = String.format("%.2f", 7.456);    // "7.46"
            show("twoDecimals", twoDecimals);
            String noDecimals = String.format("%.0f", 2.5);       // "3"
            show("noDecimals", noDecimals);
        }
        {
            String rate = String.format("%.1f%%", 12.345);        // "12.3%"
            show("rate", rate);
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

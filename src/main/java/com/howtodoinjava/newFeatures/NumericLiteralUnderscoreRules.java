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
 * Examples for the tutorial "Underscores in Numeric Literals in Java: Rules and Examples".
 * https://howtodoinjava.com/java/underscores_formatted_literal/
 */
public class NumericLiteralUnderscoreRules {
    static long parseGroupedLong(String text) {
        String digits = text.strip().replace("_", "");
        try {
            return Long.parseLong(digits);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Not a number: " + text, e);
        }
    }
    public static void main(String[] args) throws Exception {
        {
            int population = 1_000_000;            // 1000000
            show("population", population);
            long uploadLimit = 10_485_760L;        // 10485760
            show("uploadLimit", uploadLimit);
            int timeoutMs = 30_000;                // 30000
            show("timeoutMs", timeoutMs);
            int flags = 0b1010_0101;               // 165
            show("flags", flags);
            int orange = 0xFF_88_00;               // 16746496
            show("orange", orange);
            double price = 1_299.99;               // 1299.99
            show("price", price);
            boolean same = 1_000_000 == 1000000;   // true
            show("same", same);
        }
        {
            byte mask = 0b0010_0101;               // 37
            show("mask", mask);
            short port = 8_080;                    // 8080
            show("port", port);
            long maxLong = 0x7FFF_FFFF_FFFF_FFFFL; // 9223372036854775807
            show("maxLong", maxLong);
            int octal = 0_52;                      // 42
            show("octal", octal);
            double avogadro = 6.022_140_76e2_3;    // 6.02214076E23
            show("avogadro", avogadro);
            float ratio = 0.333_333F;              // 0.333333
            show("ratio", ratio);
            double twoPowerTen = 0x1.0p1_0;        // 1024.0
            show("twoPowerTen", twoPowerTen);
            int lakh = 10_00_000;                  // 1000000
            show("lakh", lakh);
            int doubled = 1__000;                  // 1000
            show("doubled", doubled);
        }
        {
            int readWrite = 0b0000_0011;           // 3
            show("readWrite", readWrite);
            int adminRights = 0b1111_0000_0011;    // 3843
            show("adminRights", adminRights);
            int guestRole = adminRights & 0b1111_1111; // 3
            show("guestRole", guestRole);
            boolean canShare = (adminRights & 0b0100_0000_0000) != 0; // true
            show("canShare", canShare);
        }
        {
            String fromCode = String.valueOf(1_000_000);   // "1000000"
            show("fromCode", fromCode);
            try { int parsed = Integer.parseInt("1_000"); show("parsed", parsed); } catch (Throwable _t) { System.out.println("parsed -> " + _t); }
        }
        {
            long limit = parseGroupedLong(" 10_485_760 ");   // 10485760
            show("limit", limit);
            long plain = parseGroupedLong("5000");          // 5000
            show("plain", plain);
            try { long typo = parseGroupedLong("10_48O_760"); show("typo", typo); } catch (Throwable _t) { System.out.println("typo -> " + _t); }
        }
        {
            String report = String.format(Locale.US, "%,d", 10_485_760);         // "10,485,760"
            show("report", report);
            String german = String.format(Locale.GERMANY, "%,d", 10_485_760);     // "10.485.760"
            show("german", german);
        }
        {
            long wrong = 10 * 1024 * 1024 * 1024;           // -2147483648
            show("wrong", wrong);
            long right = 10L * 1024 * 1024 * 1024;          // 10737418240
            show("right", right);
            long sameValue = 10_737_418_240L;               // 10737418240
            show("sameValue", sameValue);
            Duration timeout = Duration.ofSeconds(30);      // PT30S
            show("timeout", timeout);
            long timeoutMillis = timeout.toMillis();        // 30000
            show("timeoutMillis", timeoutMillis);
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

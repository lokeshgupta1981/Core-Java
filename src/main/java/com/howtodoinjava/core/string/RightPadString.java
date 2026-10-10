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
import org.apache.commons.lang3.StringUtils;

/**
 * Examples for the tutorial "Right Pad a String in Java With Spaces, Zeros or Any Char".
 * https://howtodoinjava.com/java/string/right-pad-string-with-spaces-zeros/
 */
public class RightPadString {
    public static String rightPad(String input, int length, String padStr) {
        if (input == null || padStr == null || padStr.isEmpty()) {
            return input;
        }
        int padLength = length - input.length();
        if (padLength <= 0) {
            return input;
        }
        String pad = padStr.repeat(padLength).substring(0, padLength);
        return input + pad;
    }
    public static String fitWidth(String text, int width) {
        if (text.length() > width) {
            return text.substring(0, width);
        }
        return text + " ".repeat(width - text.length());
    }
    public static void main(String[] args) throws Exception {
        {
            String spaces = "[" + String.format("%-10s", "apple") + "]";      // [apple     ]   (5 spaces added)
            show("spaces", spaces);
            String leftZeros = String.format("%010d", 42);                    // 0000000042     (zeros go on the LEFT)
            show("leftZeros", leftZeros);
            String commons = "[" + StringUtils.rightPad("apple", 10) + "]";   // [apple     ]
            show("commons", commons);
            String rightZeros = StringUtils.rightPad("42", 6, '0');           // 420000         (zeros on the right)
            show("rightZeros", rightZeros);
            String dots = "apple" + ".".repeat(10 - "apple".length());        // apple.....     (any character)
            show("dots", dots);
        }
        {
            String banana = "[" + String.format("%-8s", "banana") + "]";           // [banana  ]
            show("banana", banana);
            String number = "[" + String.format("%-8s", 42) + "]";                 // [42      ] (numbers work too)
            show("number", number);
            String plum = "[" + "%-8s".formatted("plum") + "]";                    // [plum    ] (Java 15+)
            show("plum", plum);

            int width = 12;
            String wide = "[" + String.format("%-" + width + "s", "plum") + "]";   // [plum        ]
            show("wide", wide);

            String notCut = "[" + String.format("%-3s", "banana") + "]";           // [banana] (longer text is not cut)
            show("notCut", notCut);
        }
        {
            try { String bad = String.format("%-010d", 42); show("bad", bad); } catch (Throwable _t) { System.out.println("bad -> " + _t); }

            String zeros = String.format("%-6s", "42").replace(' ', '0');           // 420000
            show("zeros", zeros);
            String wrong = String.format("%-12s", "green tea").replace(' ', '0');   // green0tea000 (wrong)
            show("wrong", wrong);
        }
        {
            String fruit = "apple";

            String dotted = fruit + ".".repeat(10 - fruit.length());                  // apple.....
            show("dotted", dotted);
            String zeroed = fruit + "0".repeat(10 - fruit.length());                  // apple00000
            show("zeroed", zeroed);

            try { String tooLong = "watermelon!" + ".".repeat(10 - "watermelon!".length()); show("tooLong", tooLong); } catch (Throwable _t) { System.out.println("tooLong -> " + _t); }

            String safe = fruit + ".".repeat(Math.max(0, 3 - fruit.length()));       // apple (already long enough)
            show("safe", safe);
        }
        {
            String digits = rightPad("123456789", 10, "0");      // 1234567890
            show("digits", digits);
            String spaced = rightPad("apple", 10, " ");           // "apple     "
            show("spaced", spaced);
            String mixed = rightPad("apple", 10, "-=");           // apple-=-=-  (pad cut to fit)
            show("mixed", mixed);
            String longer = rightPad("watermelon", 5, ".");       // watermelon  (no change)
            show("longer", longer);
            String noText = rightPad(null, 10, ".");              // null
            show("noText", noText);
        }
        {
            String one = StringUtils.rightPad("java", 5);               // "java "
            show("one", one);
            String ten = StringUtils.rightPad("java", 10, " ");         // "java      "
            show("ten", ten);
            String kept = StringUtils.rightPad("javascript", 4);        // javascript (no change, no cut)
            show("kept", kept);
            String noText = StringUtils.rightPad(null, 10);             // null
            show("noText", noText);
            String empty = StringUtils.rightPad("java", 8, "");         // "java    " (empty padStr means spaces)
            show("empty", empty);
        }
        {
            String five = StringUtils.rightPad("123", 5, "0");          // 12300
            show("five", five);
            String tenZeros = StringUtils.rightPad("123", 10, '0');     // 1230000000
            show("tenZeros", tenZeros);
            String mixed = StringUtils.rightPad("java", 10, "-=");      // java-=-=-=
            show("mixed", mixed);
        }
        {
            String right = StringUtils.rightPad("apple", 10, '*');      // apple*****
            show("right", right);
            String left = StringUtils.leftPad("apple", 10, '*');        // *****apple
            show("left", left);
            String center = StringUtils.center("apple", 10, '*');       // **apple***
            show("center", center);

            String orderNo = StringUtils.leftPad("42", 6, '0');         // 000042
            show("orderNo", orderNo);
            String even = StringUtils.center("java", 10, '*');          // ***java***
            show("even", even);
            String odd = StringUtils.center("java", 9, '*');            // **java***
            show("odd", odd);
            String spaced = StringUtils.center("java", 10);             // "   java   "
            show("spaced", spaced);
        }
        {
            String cut = String.format("%-6.6s", "watermelon");                                // "waterm"
            show("cut", cut);
            String padded = String.format("%-6.6s", "fig");                                    // "fig   "
            show("padded", padded);

            String commons = StringUtils.rightPad(StringUtils.truncate("watermelon", 6), 6);   // "waterm"
            show("commons", commons);
            String dots = StringUtils.abbreviate("watermelon", 6);                             // "wat..."
            show("dots", dots);
        }
        {
            String longName = fitWidth("watermelon", 6);     // "waterm"
            show("longName", longName);
            String shortName = fitWidth("fig", 6);           // "fig   "
            show("shortName", shortName);
        }
        {
            Map<String, Integer> stock = new LinkedHashMap<>();
            stock.put("apple", 5);
            stock.put("banana", 12);
            stock.put("kiwi", 3);

            System.out.printf("%-10s|%5s%n", "Fruit", "Qty");
            System.out.println("-".repeat(16));
            stock.forEach((name, qty) -> System.out.printf("%-10s|%5d%n", name, qty));
        }
        {
            String commons = StringUtils.rightPad(String.valueOf(42), 6, '0');   // 420000
            show("commons", commons);
            String plain = String.valueOf(42) + "0".repeat(4);                  // 420000
            show("plain", plain);
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

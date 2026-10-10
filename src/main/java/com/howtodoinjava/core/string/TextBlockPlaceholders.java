package com.howtodoinjava.core.string;

import java.util.concurrent.locks.*;
import java.lang.management.*;
import java.time.format.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.zip.*;
import java.util.regex.*;
import java.net.*;
import java.text.*;
import java.nio.file.attribute.*;
import java.security.*;
import java.nio.charset.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.time.*;
import java.nio.file.*;
import java.math.*;
import java.io.*;

import java.time.temporal.*;

/**
 * Examples for the tutorial "Java Text Block Formatting with Variables and Placeholders".
 * https://howtodoinjava.com/java/string/text-block-formatting/
 */
public class TextBlockPlaceholders {
    static String fill(String template, Map<String, ?> values) {
        String result = template;
        for (Map.Entry<String, ?> entry : values.entrySet()) {
            result = result.replace("{" + entry.getKey() + "}", String.valueOf(entry.getValue()));
        }
        return result;
    }
    public static void main(String[] args) throws Exception {
        {
            String name = "Lokesh";
            int orderId = 1042;
            String email = """
            Hi %s,
            your order %d is on its way.
            """.formatted(name, orderId);
            boolean filled = email.equals("Hi Lokesh,\nyour order 1042 is on its way.\n");   // true
            show("filled", filled);
        }
        {
            String json = """
            {
                "orderId": %d,
                "customer": "%s",
                "total": %.2f
            }
            """.formatted(1042, "Lokesh", 18.5);
            boolean hasTotal = json.contains("\"total\": 18.50");        // true (English default locale)
            show("hasTotal", hasTotal);
        }
        {
            String template = """
            "total": %.2f
            """;
            String safe = String.format(Locale.ROOT, template, 18.5);
            boolean dot = safe.contains("18.50");                        // true
            show("dot", dot);
        }
        {
            Object[] values = { 1042, "Lokesh" };
            String line = "Order %d for %s".formatted(values);          // "Order 1042 for Lokesh"
            show("line", line);
        }
        {
            String reminder = """
            Hi %1$s, your order %2$d is ready.
            Thank you for shopping with us, %1$s!""".formatted("Lokesh", 1042);
            boolean twice = reminder.lines().allMatch(l -> l.contains("Lokesh"));   // true
            show("twice", twice);
        }
        {
            String sale = "Save %d%% on %s".formatted(20, "Tea");       // "Save 20% on Tea"
            show("sale", sale);
            try { String missing = "%s and %s".formatted("Tea"); show("missing", missing); } catch (Throwable _t) { System.out.println("missing -> " + _t); }
            try { String wrongType = "%d cups".formatted("two"); show("wrongType", wrongType); } catch (Throwable _t) { System.out.println("wrongType -> " + _t); }
            String extra = "%s".formatted("Tea", "Coffee");              // "Tea"
            show("extra", extra);
        }
        {
            String sameText = String.format("Order %d for %s", 1042, "Lokesh");   // "Order 1042 for Lokesh"
            show("sameText", sameText);
        }
        {
            String json = """
            '{'
            "orderId": {0,number,#},
            "customer": "{1}"
            '}'
            """;
            String body = MessageFormat.format(json, 1042, "Lokesh");
            boolean valid = body.startsWith("{\n  \"orderId\": 1042,");    // true
            show("valid", valid);
        }
        {
            String broken = MessageFormat.format("It's {0}", "ready");     // "Its {0}"
            show("broken", broken);
            String fixed = MessageFormat.format("It''s {0}", "ready");     // "It's ready"
            show("fixed", fixed);
            String grouped = MessageFormat.format("{0} items", 1042);      // "1,042 items" (English default locale)
            show("grouped", grouped);
        }
        {
            String shipped = """
            Hi {customer},
            order {orderId} was shipped today.""";
            String text = fill(shipped, Map.of("customer", "Lokesh", "orderId", 1042));
            boolean ready = text.equals("Hi Lokesh,\norder 1042 was shipped today.");   // true
            show("ready", ready);
        }
        {
            String name = "Lokesh";
            String greeting = """
            Hello, """ + name + """
            Welcome to Java text blocks!
            """;
            boolean oneLine = greeting.equals("Hello,LokeshWelcome to Java text blocks!\n");   // true
            show("oneLine", oneLine);
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

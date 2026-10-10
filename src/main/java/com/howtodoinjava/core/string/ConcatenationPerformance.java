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
 * Examples for the tutorial "Java String Concatenation Performance: + vs StringBuilder".
 * https://howtodoinjava.com/java/string/string-concatenation-performance/
 */
public class ConcatenationPerformance {
    static String message(String name, int count) {
        return "Hi " + name + ", you have " + count + " new messages";
    }
    static String report(String[] rows) {
        String csv = "";
        for (String row : rows) {
            csv += row + "\n";
        }
        return csv;
    }
    static String reportWithBuilder(String[] rows) {
        StringBuilder csv = new StringBuilder(rows.length * 16);
        for (String row : rows) {
            csv.append(row).append('\n');
        }
        return csv.toString();
    }
    public static void main(String[] args) throws Exception {
        {
            String name = "Lokesh";
            int count = 3;
            String message = "Hi " + name + ", you have " + count + " new messages";  // "Hi Lokesh, you have 3 new messages"
            show("message", message);

            StringBuilder export = new StringBuilder();
            for (String row : List.of("tea,2", "milk,1")) {
                export.append(row).append(';');
            }
            String csv = export.toString();                                         // "tea,2;milk,1;"
            show("csv", csv);
        }
        {
            final String greeting = "Hi";
            String folded = greeting + " there";                       // compile-time constant
            show("folded", folded);
            boolean pooled = folded == "Hi there";                     // true
            show("pooled", pooled);
            String name = "Lokesh";
            boolean runtime = ("Hi " + name) == "Hi Lokesh";           // false
            show("runtime", runtime);
        }
        {
            String[] rows = {"order-1,42.50", "order-2,9.90"};
            boolean same = report(rows).equals(reportWithBuilder(rows));    // true
            show("same", same);
        }
        {
            StringBuilder small = new StringBuilder();
            int defaultCapacity = small.capacity();                    // 16
            show("defaultCapacity", defaultCapacity);
            StringBuilder sized = new StringBuilder(4096);
            int presized = sized.capacity();                           // 4096
            show("presized", presized);
            String line = new StringBuilder().repeat("-", 10).toString();       // "----------"
            show("line", line);
            StringBuilder reused = new StringBuilder("old text");
            reused.setLength(0);
            int afterReset = reused.length();                          // 0
            show("afterReset", afterReset);
        }
        {
            String base = "Hello";
            String joined = base.concat(" World");                      // "Hello World"
            show("joined", joined);
            boolean sameObject = base.concat("") == base;              // true
            show("sameObject", sameObject);
            String plusNull = base + null;                             // "Hellonull"
            show("plusNull", plusNull);
            try { String concatNull = base.concat(null); show("concatNull", concatNull); } catch (Throwable _t) { System.out.println("concatNull -> " + _t); }
        }
        {
            List<String> items = List.of("tea", "milk", "bread");
            String joined = String.join(", ", items);                  // "tea, milk, bread"
            show("joined", joined);
            String streamed = items.stream().collect(Collectors.joining(", ", "[", "]"));   // "[tea, milk, bread]"
            show("streamed", streamed);
            String formatted = String.format("%s has %d items", "cart", 3);                // "cart has 3 items"
            show("formatted", formatted);
            String modern = "%s has %d items".formatted("cart", 3);                        // "cart has 3 items"
            show("modern", modern);
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

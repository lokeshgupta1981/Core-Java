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
 * Examples for the tutorial "Java Remove Leading and Trailing Whitespace: trim() vs strip()".
 * https://howtodoinjava.com/java/string/remove-leading-trailing-whitespaces/
 */
public class TrimVsStrip {

    public static void main(String[] args) throws Exception {
        {
            String name = "   Lokesh Gupta  ";
            String both = name.strip();                  // "Lokesh Gupta"
            show("both", both);
            String leading = name.stripLeading();        // "Lokesh Gupta  "
            show("leading", leading);
            String trailing = name.stripTrailing();      // "   Lokesh Gupta"
            show("trailing", trailing);
            String classic = name.trim();                // "Lokesh Gupta"
            show("classic", classic);
        }
        {
            String emSpace = "\u2003hello\u2003";
            int trimmedLength = emSpace.trim().length();      // 7
            show("trimmedLength", trimmedLength);
            int strippedLength = emSpace.strip().length();    // 5
            show("strippedLength", strippedLength);
            String bell = "\u0007hello\u0007";
            int bellTrim = bell.trim().length();              // 5
            show("bellTrim", bellTrim);
            int bellStrip = bell.strip().length();            // 7
            show("bellStrip", bellStrip);
        }
        {
            String code = "    return total;";
            String stripped = code.stripLeading();                 // "return total;"
            show("stripped", stripped);
            String viaRegex = code.replaceFirst("^\\s+", "");      // "return total;"
            show("viaRegex", viaRegex);
        }
        {
            String line = "id,name,price   ";
            String trimmedEnd = line.stripTrailing();              // "id,name,price"
            show("trimmedEnd", trimmedEnd);
            String regexEnd = line.replaceAll("\\s+$", "");        // "id,name,price"
            show("regexEnd", regexEnd);
        }
        {
            String blogName = "    how to do in java    ";
            String strippedString = blogName.strip();               // "how to do in java"
            show("strippedString", strippedString);
            String trimmedString = blogName.trim();                 // "how to do in java"
            show("trimmedString", trimmedString);
            String regexBoth = blogName.replaceAll("^\\s+|\\s+$", "");  // "how to do in java"
            show("regexBoth", regexBoth);
        }
        {
            String cell = "\u00A012.50 ";
            String afterStrip = cell.strip();                                   // contains U+00A0
            show("afterStrip", afterStrip);
            try { BigDecimal broken = new BigDecimal(afterStrip); show("broken", broken); } catch (Throwable _t) { System.out.println("broken -> " + _t); }
            String clean = cell.replaceAll("(?U)^\\s+|\\s+$", "");              // "12.50"
            show("clean", clean);
            BigDecimal price = new BigDecimal(clean);                           // 12.50
            show("price", price);
        }
        {
            String missing = null;
            try { String failed = missing.strip(); show("failed", failed); } catch (Throwable _t) { System.out.println("failed -> " + _t); }
            String safe = Objects.requireNonNullElse(missing, "").strip();      // ""
            show("safe", safe);
            boolean blank = "  \t ".isBlank();                                  // true
            show("blank", blank);
            boolean empty = "  \t ".strip().isEmpty();                          // true
            show("empty", empty);
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

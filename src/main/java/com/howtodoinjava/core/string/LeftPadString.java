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
import org.apache.commons.lang3.StringUtils;

/**
 * Examples for the tutorial "Left Pad a String with Spaces or Zeros in Java (5 Ways)".
 * https://howtodoinjava.com/java/string/left-pad-string-with-spaces-zeros/
 */
public class LeftPadString {
    static String leftPad(String input, int length, char padChar) {
        Objects.requireNonNull(input, "input");
        int missing = length - input.length();
        return missing <= 0 ? input : String.valueOf(padChar).repeat(missing) + input;
    }
    static String leftPad(String input, int length, String padStr) {
        int missing = length - input.length();
        if (missing <= 0 || padStr.isEmpty()) {
            return input;
        }
        return padStr.repeat(missing / padStr.length() + 1).substring(0, missing) + input;
    }
    public static void main(String[] args) throws Exception {
        {
            String spaces = String.format("%8s", "42");              // "      42"
            show("spaces", spaces);
            String zeros = String.format("%08d", 42);                // "00000042"
            show("zeros", zeros);
            String code = "0".repeat(8 - "A7".length()) + "A7";      // "000000A7"
            show("code", code);
            String dots = leftPad("Java", 8, '.');                   // "....Java"
            show("dots", dots);
        }
        {
            String invoice = "INV-" + String.format("%05d", 42);        // "INV-00042"
            show("invoice", invoice);
            String negative = String.format("%06d", -42);              // "-00042"
            show("negative", negative);
            String longValue = String.format("%010d", 9876543210L);    // "9876543210"
            show("longValue", longValue);
            String tooLong = String.format("%03d", 123456);            // "123456"
            show("tooLong", tooLong);
            String price = String.format(java.util.Locale.ROOT, "%08.2f", 3.5);   // "00003.50"
            show("price", price);
        }
        {
            int length = 10;
            String padded = String.format("%" + length + "s", "Java");   // "      Java"
            show("padded", padded);
            String same = "%10s".formatted("Java");                        // "      Java"
            show("same", same);
            try { String zeroText = String.format("%05s", "Java"); show("zeroText", zeroText); } catch (Throwable _t) { System.out.println("zeroText -> " + _t); }
        }
        {
            String broken = String.format("%6s", "A 7").replace(' ', '0');   // "000A07"
            show("broken", broken);
        }
        {
            String account = leftPad("12345", 10, '0');      // "0000012345"
            show("account", account);
            String label = leftPad("Java", 8, ' ');          // "    Java"
            show("label", label);
            String unchanged = leftPad("Javascript", 4, '.'); // "Javascript"
            show("unchanged", unchanged);
            try { String missing = leftPad(null, 5, '0'); show("missing", missing); } catch (Throwable _t) { System.out.println("missing -> " + _t); }
        }
        {
            String pattern = leftPad("7", 5, "ab");          // "abab7"
            show("pattern", pattern);
        }
        {
            String withSpaces = StringUtils.leftPad("Java", 8);           // "    Java"
            show("withSpaces", withSpaces);
            String withZeros = StringUtils.leftPad("789", 10, '0');        // "0000000789"
            show("withZeros", withZeros);
            String withString = StringUtils.leftPad("7", 5, "ab");         // "abab7"
            show("withString", withString);
            String nullInput = StringUtils.leftPad(null, 5, '0');          // null
            show("nullInput", nullInput);
            String emptyPad = StringUtils.leftPad("Java", 8, "");          // "    Java"
            show("emptyPad", emptyPad);
        }
        {
            String account = leftPad("12345", 10, '0');                   // "0000012345"
            show("account", account);
            String cents = String.format("%012d", 4_999L);                // "000000004999"
            show("cents", cents);
            String name = String.format("%-20.20s", "Lokesh Gupta");      // "Lokesh Gupta        "
            show("name", name);
            String record = account + cents + name;
            int recordLength = record.length();                           // 42
            show("recordLength", recordLength);
        }
        {
            String sku = "0".repeat(Math.max(0, 6 - "B12".length())) + "B12";   // "000B12"
            show("sku", sku);
        }
        {
            String trimmed = "007".replaceFirst("^0+(?!$)", "");     // "7"
            show("trimmed", trimmed);
            String zero = "000".replaceFirst("^0+(?!$)", "");        // "0"
            show("zero", zero);
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

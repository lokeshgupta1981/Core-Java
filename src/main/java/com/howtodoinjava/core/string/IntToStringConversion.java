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
 * Examples for the tutorial "Convert int to String in Java (Integer.toString vs valueOf)".
 * https://howtodoinjava.com/java/string/convert-int-to-string/
 */
public class IntToStringConversion {

    public static void main(String[] args) throws Exception {
        {
            int quantity = 42;
            String byToString = Integer.toString(quantity);        // "42"
            show("byToString", byToString);
            String byValueOf = String.valueOf(quantity);           // "42"
            show("byValueOf", byValueOf);
            String byConcat = "" + quantity;                       // "42"
            show("byConcat", byConcat);
            String padded = String.format(Locale.ROOT, "%05d", quantity);   // "00042"
            show("padded", padded);
        }
        {
            String zero = Integer.toString(0);                  // "0"
            show("zero", zero);
            String negative = Integer.toString(-40);            // "-40"
            show("negative", negative);
            String max = String.valueOf(Integer.MAX_VALUE);     // "2147483647"
            show("max", max);
            String min = String.valueOf(Integer.MIN_VALUE);     // "-2147483648"
            show("min", min);
        }
        {
            int wrapped = Integer.MAX_VALUE + 1;
            String overflowText = Integer.toString(wrapped);           // "-2147483648"
            show("overflowText", overflowText);
            try { int checked = Math.addExact(Integer.MAX_VALUE, 1); show("checked", checked); } catch (Throwable _t) { System.out.println("checked -> " + _t); }
        }
        {
            Integer maybeQty = null;
            String asText = String.valueOf(maybeQty);           // "null"
            show("asText", asText);
            String withDefault = Objects.toString(maybeQty, "");   // ""
            show("withDefault", withDefault);
            try { String unboxed = Integer.toString(maybeQty); show("unboxed", unboxed); } catch (Throwable _t) { System.out.println("unboxed -> " + _t); }
        }
        {
            int room = 204;
            String label = "Room " + room;                            // "Room 204"
            show("label", label);
            String key = "room-" + room;                              // "room-204"
            show("key", key);
            String built = new StringBuilder("Floor ").append(room / 100).toString();   // "Floor 2"
            show("built", built);
        }
        {
            String orderNo = String.format(Locale.ROOT, "order-%06d", 381);      // "order-000381"
            show("orderNo", orderNo);
            String grouped = String.format(Locale.ROOT, "%,d", 1234567);       // "1,234,567"
            show("grouped", grouped);
            String width = String.format(Locale.ROOT, "[%5d]", 42);              // "[   42]"
            show("width", width);
        }
        {
            String arabic = String.format(Locale.forLanguageTag("ar-EG"), "%d", 123);
            boolean asciiDigits = arabic.equals("123");                     // false
            show("asciiDigits", asciiDigits);
            int firstChar = arabic.charAt(0);                               // 1633
            show("firstChar", firstChar);
            String fixed = String.format(Locale.ROOT, "%d", 123);          // "123"
            show("fixed", fixed);
        }
        {
            String german = NumberFormat.getIntegerInstance(Locale.GERMANY).format(1234567);   // "1.234.567"
            show("german", german);
        }
        {
            String binary = Integer.toBinaryString(10);              // "1010"
            show("binary", binary);
            String octal = Integer.toOctalString(8);                 // "10"
            show("octal", octal);
            String hex = Integer.toHexString(255);                   // "ff"
            show("hex", hex);
            String base36 = Integer.toString(35, 36);                // "z"
            show("base36", base36);
            String signed = Integer.toString(-255, 2);               // "-11111111"
            show("signed", signed);
            String unsignedHex = Integer.toHexString(-1);            // "ffffffff"
            show("unsignedHex", unsignedHex);
            String unsignedDec = Integer.toUnsignedString(-1);       // "4294967295"
            show("unsignedDec", unsignedDec);
        }
        {
            String hex8 = HexFormat.of().toHexDigits(255);           // "000000ff"
            show("hex8", hex8);
        }
        {
            long fileBytes = 5_000_000_000L;
            String size = Long.toString(fileBytes);                   // "5000000000"
            show("size", size);
            String sizeToo = String.valueOf(fileBytes);               // "5000000000"
            show("sizeToo", sizeToo);
            String millis = "ts-" + 1760054400000L;                   // "ts-1760054400000"
            show("millis", millis);
            String longHex = Long.toHexString(255L);                  // "ff"
            show("longHex", longHex);
            String unsignedLong = Long.toUnsignedString(-1L);         // "18446744073709551615"
            show("unsignedLong", unsignedLong);
        }
        {
            Long missingId = null;
            String idText = String.valueOf(missingId);                // "null"
            show("idText", idText);
            try { String idFails = Long.toString(missingId); show("idFails", idFails); } catch (Throwable _t) { System.out.println("idFails -> " + _t); }
        }
        {
            String code = String.format(Locale.ROOT, "%04d", 7);   // "0007"
            show("code", code);
        }
        {
            int[] scores = {7, 9, 10};
            String bracketed = Arrays.toString(scores);                                                     // "[7, 9, 10]"
            show("bracketed", bracketed);
            String csv = Arrays.stream(scores).mapToObj(String::valueOf).collect(Collectors.joining(","));   // "7,9,10"
            show("csv", csv);
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

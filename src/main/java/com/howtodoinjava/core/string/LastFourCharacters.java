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
 * Examples for the tutorial "Get Last 4 Characters of a String in Java (Null-Safe)".
 * https://howtodoinjava.com/java/string/get-last-4-characters/
 */
public class LastFourCharacters {
    static String lastChars(String text, int n) {
        if (n < 0) {
            throw new IllegalArgumentException("n must not be negative: " + n);
        }
        if (text == null) {
            return null;
        }
        return text.substring(Math.max(0, text.length() - n));
    }
    static String lastCodePoints(String text, int n) {
        if (n < 0) {
            throw new IllegalArgumentException("n must not be negative: " + n);
        }
        if (text == null) {
            return null;
        }
        int count = Math.min(n, text.codePointCount(0, text.length()));
        return text.substring(text.offsetByCodePoints(text.length(), -count));
    }
    public static void main(String[] args) throws Exception {
        {
            String card = "4111222233334444";
            String last4 = card.substring(card.length() - 4);                 // "4444"
            show("last4", last4);
            String pin = "123";
            String safe = pin.substring(Math.max(0, pin.length() - 4));       // "123"
            show("safe", safe);
        }
        {
            String code = "123";
            try { String tail = code.substring(code.length() - 4); show("tail", tail); } catch (Throwable _t) { System.out.println("tail -> " + _t); }
        }
        {
            String value = "98";
            String byCheck = value.length() > 4 ? value.substring(value.length() - 4) : value;   // "98"
            show("byCheck", byCheck);
            String byMax = value.substring(Math.max(0, value.length() - 4));                   // "98"
            show("byMax", byMax);
        }
        {
            String digits = lastChars("4111222233334444", 4);   // "4444"
            show("digits", digits);
            String shortOne = lastChars("123", 4);               // "123"
            show("shortOne", shortOne);
            String none = lastChars(null, 4);                    // null
            show("none", none);
            String zero = lastChars("apple", 0);                 // ""
            show("zero", zero);
            try { String bad = lastChars("apple", -1); show("bad", bad); } catch (Throwable _t) { System.out.println("bad -> " + _t); }
        }
        {
            String nick = "sam" + Character.toString(0x1F44D) + "999";
            int units = nick.length();                                           // 8
            show("units", units);
            String byChars = nick.substring(nick.length() - 4);
            boolean startsBroken = Character.isLowSurrogate(byChars.charAt(0));   // true
            show("startsBroken", startsBroken);
        }
        {
            String nickname = "sam" + Character.toString(0x1F44D) + "999";
            String hint = lastCodePoints(nickname, 4);
            int hintPoints = hint.codePointCount(0, hint.length());       // 4
            show("hintPoints", hintPoints);
            boolean whole = hint.startsWith(Character.toString(0x1F44D));   // true
            show("whole", whole);
        }
        {
            String phone = "555-010-7788";
            String phoneDigits = phone.replaceAll("\\D", "");          // "5550107788"
            show("phoneDigits", phoneDigits);
            String lastFour = lastChars(phoneDigits, 4);               // "7788"
            show("lastFour", lastFour);
            String spaced = lastChars("4111 2222 3333 44".replaceAll("\\D", ""), 4);   // "3344"
            show("spaced", spaced);
        }
        {
            String right4 = StringUtils.right("4111222233334444", 4);   // "4444"
            show("right4", right4);
            String rightShort = StringUtils.right("123", 4);           // "123"
            show("rightShort", rightShort);
            String rightNull = StringUtils.right(null, 4);             // null
            show("rightNull", rightNull);
            String rightNeg = StringUtils.right("apple", -1);          // ""
            show("rightNeg", rightNeg);
        }
        {
            long account = 9876500042L;
            long tail = Math.abs(account % 10_000);                // 42
            show("tail", tail);
            String tailText = String.format("%04d", tail);          // "0042"
            show("tailText", tailText);
        }
        {
            String word = "apple";
            char lastChar = word.charAt(word.length() - 1);   // 'e'
            show("lastChar", lastChar);
            String lastWordChar = word.substring(word.length() - 1);   // "e"
            show("lastWordChar", lastWordChar);
        }
        {
            boolean endsWithDigits = "order-2026".matches(".*\\d{4}");   // true
            show("endsWithDigits", endsWithDigits);
            boolean noDigits = "order-26".matches(".*\\d{4}");          // false
            show("noDigits", noDigits);
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

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
 * Examples for the tutorial "Get First 4 Characters of a String in Java (Safe Ways)".
 * https://howtodoinjava.com/java/string/get-first-4-characters/
 */
public class FirstFourCharacters {
    static String firstChars(String text, int n) {
        if (n < 0) {
            throw new IllegalArgumentException("n must not be negative: " + n);
        }
        if (text == null) {
            return null;
        }
        return text.substring(0, Math.min(n, text.length()));
    }
    static String firstCodePoints(String text, int n) {
        if (n < 0) {
            throw new IllegalArgumentException("n must not be negative: " + n);
        }
        if (text == null) {
            return null;
        }
        int count = Math.min(n, text.codePointCount(0, text.length()));
        return text.substring(0, text.offsetByCodePoints(0, count));
    }
    static String preview(String message, int max) {
        if (message == null) {
            return "";
        }
        String text = message.strip();
        if (text.codePointCount(0, text.length()) <= max) {
            return text;
        }
        return firstCodePoints(text, max).stripTrailing() + "...";
    }
    public static void main(String[] args) throws Exception {
        {
            String date = "2026-10-10";
            String year = date.substring(0, Math.min(4, date.length()));     // "2026"
            show("year", year);
            String user = "al";
            String prefix = user.substring(0, Math.min(4, user.length()));   // "al"
            show("prefix", prefix);
        }
        {
            String name = "al";
            try { String first4 = name.substring(0, 4); show("first4", first4); } catch (Throwable _t) { System.out.println("first4 -> " + _t); }
        }
        {
            String yearPart = firstChars("2026-10-10", 4);   // "2026"
            show("yearPart", yearPart);
            String shortName = firstChars("al", 4);          // "al"
            show("shortName", shortName);
            String noName = firstChars(null, 4);             // null
            show("noName", noName);
            String nothing = firstChars("lokesh", 0);        // ""
            show("nothing", nothing);
            try { String negative = firstChars("lokesh", -2); show("negative", negative); } catch (Throwable _t) { System.out.println("negative -> " + _t); }
        }
        {
            String byFormat = String.format("%.4s", "lokesh");                  // "loke"
            show("byFormat", byFormat);
            String bySequence = "lokesh".subSequence(0, 4).toString();           // "loke"
            show("bySequence", bySequence);
            StringBuilder points = "lokesh".codePoints().limit(4)
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append);
            String byPoints = points.toString();                                 // "loke"
            show("byPoints", byPoints);
        }
        {
            String left4 = StringUtils.left("lokesh", 4);   // "loke"
            show("left4", left4);
            String leftShort = StringUtils.left("al", 4);   // "al"
            show("leftShort", leftShort);
            String leftNull = StringUtils.left(null, 4);    // null
            show("leftNull", leftNull);
        }
        {
            String party = Character.toString(0x1F389) + "Party";
            String firstOne = party.substring(0, 1);
            boolean halfEmoji = Character.isHighSurrogate(firstOne.charAt(0));      // true
            show("halfEmoji", halfEmoji);
        }
        {
            String banner = Character.toString(0x1F389) + "Party";
            String head = firstCodePoints(banner, 1);
            boolean wholeEmoji = head.equals(Character.toString(0x1F389));   // true
            show("wholeEmoji", wholeEmoji);
            String head4 = firstCodePoints(banner, 4);
            int head4Units = head4.length();                                 // 5
            show("head4Units", head4Units);
        }
        {
            String longMsg = preview("See you at the station at seven tonight", 20);   // "See you at the stati..."
            show("longMsg", longMsg);
            String shortMsg = preview("  On my way  ", 20);                             // "On my way"
            show("shortMsg", shortMsg);
            String emptyMsg = preview(null, 20);                                         // ""
            show("emptyMsg", emptyMsg);
        }
        {
            String city = "paris";
            char initial = city.charAt(0);                                   // 'p'
            show("initial", initial);
            String capital = city.substring(0, 1).toUpperCase(Locale.ROOT);   // "P"
            show("capital", capital);
        }
        {
            String sentence = "Pack light for the weekend trip";
            String[] words = sentence.split("\\s+", 4);
            String firstThree = String.join(" ", Arrays.copyOf(words, Math.min(3, words.length)));   // "Pack light for"
            show("firstThree", firstThree);
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

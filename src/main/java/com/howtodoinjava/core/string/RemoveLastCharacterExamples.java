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
 * Examples for the tutorial "Remove Last Character from a String in Java (6 Ways)".
 * https://howtodoinjava.com/java/string/java-string-remove-last-char/
 */
public class RemoveLastCharacterExamples {
    static String removeLastChar(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        return text.substring(0, text.length() - 1);
    }
    static String removeLastCodePoint(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        int last = text.codePointBefore(text.length());
        return text.substring(0, text.length() - Character.charCount(last));
    }
    static String removeTrailing(String text, char ch) {
        if (text != null && !text.isEmpty() && text.charAt(text.length() - 1) == ch) {
            return text.substring(0, text.length() - 1);
        }
        return text;
    }
    public static void main(String[] args) throws Exception {
        {
            String word = "apples";
            String singular = word.substring(0, word.length() - 1);   // "apple"
            show("singular", singular);
            String empty = "";
            String safe = empty.isEmpty() ? empty : empty.substring(0, empty.length() - 1);   // ""
            show("safe", safe);
        }
        {
            String none = "";
            try { String broken = none.substring(0, none.length() - 1); show("broken", broken); } catch (Throwable _t) { System.out.println("broken -> " + _t); }
        }
        {
            String plural = removeLastChar("apples");   // "apple"
            show("plural", plural);
            String one = removeLastChar("a");           // ""
            show("one", one);
            String blank = removeLastChar("");          // ""
            show("blank", blank);
            String missing = removeLastChar(null);      // null
            show("missing", missing);
        }
        {
            String status = "Hi" + Character.toString(0x1F600);
            int units = status.length();                                    // 4
            show("units", units);
            int codePoints = status.codePointCount(0, status.length());     // 3
            show("codePoints", codePoints);
            String cut = status.substring(0, status.length() - 1);
            boolean brokenHalf = Character.isHighSurrogate(cut.charAt(cut.length() - 1));   // true
            show("brokenHalf", brokenHalf);
        }
        {
            String greeting = removeLastCodePoint("Hi" + Character.toString(0x1F600));   // "Hi"
            show("greeting", greeting);
            String plain = removeLastCodePoint("apples");                                // "apple"
            show("plain", plain);
        }
        {
            List<String> tags = List.of("vegan", "quick", "dinner");
            StringBuilder line = new StringBuilder();
            for (String tag : tags) {
                line.append(tag).append(',');
            }
            if (!line.isEmpty()) {
                line.setLength(line.length() - 1);
            }
            String tagLine = line.toString();   // "vegan,quick,dinner"
            show("tagLine", tagLine);
        }
        {
            StringBuilder word = new StringBuilder("apples");
            String trimmed = word.deleteCharAt(word.length() - 1).toString();   // "apple"
            show("trimmed", trimmed);
        }
        {
            List<String> tags = List.of("vegan", "quick", "dinner");
            String joined = String.join(",", tags);                            // "vegan,quick,dinner"
            show("joined", joined);
            String streamed = tags.stream().collect(Collectors.joining(", "));    // "vegan, quick, dinner"
            show("streamed", streamed);
        }
        {
            String base = removeTrailing("https://api.example.com/", '/');   // "https://api.example.com"
            show("base", base);
            String clean = removeTrailing("https://api.example.com", '/');    // "https://api.example.com"
            show("clean", clean);
            String url = base + "/recipes";                                    // "https://api.example.com/recipes"
            show("url", url);
        }
        {
            String slashes = "/recipes///".replaceAll("/+$", "");   // "/recipes"
            show("slashes", slashes);
            String spaces = "apple \t ".stripTrailing();           // "apple"
            show("spaces", spaces);
        }
        {
            String regexCut = "apples".replaceAll(".$", "");                            // "apple"
            show("regexCut", regexCut);
            String emojiCut = ("Hi" + Character.toString(0x1F600)).replaceAll(".$", "");   // "Hi"
            show("emojiCut", emojiCut);
        }
        {
            String keepsNewline = "abc\n".replaceAll(".$", "");     // "ab\n"
            show("keepsNewline", keepsNewline);
            String lastOnly = "abc\n".replaceAll("(?s).\\z", "");   // "abc"
            show("lastOnly", lastOnly);
        }
        {
            String chopped = StringUtils.chop("apples");      // "apple"
            show("chopped", chopped);
            String chopNull = StringUtils.chop(null);         // null
            show("chopNull", chopNull);
            String windows = StringUtils.chop("line\r\n");    // "line"
            show("windows", windows);
        }
        {
            String file = "report.csv";
            int n = 4;
            String name = file.substring(0, Math.max(0, file.length() - n));   // "report"
            show("name", name);
        }
        {
            String quoted = "\"apple\"";
            String unquoted = quoted.length() >= 2 ? quoted.substring(1, quoted.length() - 1) : quoted;   // "apple"
            show("unquoted", unquoted);
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

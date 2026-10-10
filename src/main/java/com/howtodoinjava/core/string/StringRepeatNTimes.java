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
 * Examples for the tutorial "Repeat a String N Times in Java: repeat() and Alternatives".
 * https://howtodoinjava.com/java11/repeat-string-n-times/
 */
public class StringRepeatNTimes {
    static String repeat(String text, int count) {
        if (count < 0) {
            throw new IllegalArgumentException("count is negative: " + count);
        }
        StringBuilder sb = new StringBuilder(text.length() * count);
        for (int i = 0; i < count; i++) {
            sb.append(text);
        }
        return sb.toString();
    }
    static String indent(int depth, String label) {
        return "  ".repeat(depth) + "- " + label;
    }
    public static void main(String[] args) throws Exception {
        {
            String line = "-".repeat(5);                                  // "-----"
            show("line", line);
            String word = "ab".repeat(3);                                  // "ababab"
            show("word", word);
            String none = "ab".repeat(0);                                  // ""
            show("none", none);
            String built = new StringBuilder("|").repeat("ab", 2).toString();   // "|abab"
            show("built", built);
            String joined = String.join(",", Collections.nCopies(3, "?"));      // "?,?,?"
            show("joined", joined);
            String oldWay = new String(new char[3]).replace("\0", "ab");        // "ababab"
            show("oldWay", oldWay);
        }
        {
            String stars = "*".repeat(8);              // "********"
            show("stars", stars);
            String pair = "ab".repeat(1);               // "ab"
            show("pair", pair);
            String empty = "".repeat(100);              // ""
            show("empty", empty);
            int length = "abc".repeat(4).length();      // 12
            show("length", length);
        }
        {
            try { String negative = "ab".repeat(-1); show("negative", negative); } catch (Throwable _t) { System.out.println("negative -> " + _t); }
            try { String tooLong = "abcde".repeat(Integer.MAX_VALUE); show("tooLong", tooLong); } catch (Throwable _t) { System.out.println("tooLong -> " + _t); }
        }
        {
            String value = "Inception";
            int padding = 6 - value.length();                        // -3
            show("padding", padding);
            String safePad = " ".repeat(Math.max(0, padding));       // ""
            show("safePad", safePad);
        }
        {
            char dot = '.';
            String dots = String.valueOf(dot).repeat(4);                    // "...."
            show("dots", dots);
            int smileLength = Character.toString(0x1F600).repeat(2).length();   // 4
            show("smileLength", smileLength);
            String bar = new StringBuilder().repeat('=', 6).toString();     // "======"
            show("bar", bar);
        }
        {
            StringBuilder row = new StringBuilder();
            row.append('+').repeat("-", 8).append('+').repeat("-", 5).append('+');
            String border = row.toString();                    // "+--------+-----+"
            show("border", border);
        }
        {
            List<Long> bookIds = List.of(7L, 12L, 31L);
            String marks = String.join(", ", Collections.nCopies(bookIds.size(), "?"));    // "?, ?, ?"
            show("marks", marks);
            String sql = "SELECT * FROM book WHERE id IN (" + marks + ")";            // "SELECT * FROM book WHERE id IN (?, ?, ?)"
            show("sql", sql);
        }
        {
            String inClause = Stream.generate(() -> "?").limit(3).collect(Collectors.joining(", ", "(", ")"));   // "(?, ?, ?)"
            show("inClause", inClause);
        }
        {
            String viaLoop = repeat("ab", 3);                                   // "ababab"
            show("viaLoop", viaLoop);
            String viaArray = new String(new char[3]).replace("\0", "ab");      // "ababab"
            show("viaArray", viaArray);
            String viaCopies = String.join("", Collections.nCopies(3, "ab"));    // "ababab"
            show("viaCopies", viaCopies);
        }
        {
            String title = "Movies by genre";
            String underline = "=".repeat(title.length());          // "==============="
            show("underline", underline);
            String genre = indent(0, "Drama");                       // "- Drama"
            show("genre", genre);
            String movie = indent(1, "Inception");                   // "  - Inception"
            show("movie", movie);
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

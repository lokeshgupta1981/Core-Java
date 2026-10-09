package com.howtodoinjava.core.array;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.time.*;
import java.nio.file.*;
import java.math.*;
import java.io.*;

import java.util.regex.*;
import java.nio.charset.*;
import java.nio.*;
import java.security.*;
import java.lang.reflect.*;
import java.lang.invoke.*;

/**
 * Examples for the tutorial "Convert String to String Array in Java with split()".
 * https://howtodoinjava.com/java/array/string-to-string-array/
 */
public class StringToStringArray {

    public static void main(String[] args) throws Exception {
        {
            String tags = "java,spring,docker";
            String[] parts = tags.split(",");             // [java, spring, docker]
            show("parts", parts);
            String[] single = new String[] {tags};        // [java,spring,docker], one element
            show("single", single);
            String[] letters = "abc".split("");           // [a, b, c]
            show("letters", letters);
        }
        {
            String row = "a,b,,c,,";
            String[] dropped = row.split(",");        // [a, b, , c]
            show("dropped", dropped);
            String[] kept = row.split(",", -1);       // [a, b, , c, , ]
            show("kept", kept);
            String[] two = row.split(",", 2);         // [a, b,,c,,]
            show("two", two);
        }
        {
            String query = "  red   green blue ";
            String[] raw = query.split("\\s+");              // [, red, green, blue]
            show("raw", raw);
            String[] words = query.strip().split("\\s+");    // [red, green, blue]
            show("words", words);
        }
        {
            String version = "25.0.4";
            String[] wrong = version.split(".");                  // []
            show("wrong", wrong);
            String[] escaped = version.split("\\.");            // [25, 0, 4]
            show("escaped", escaped);
            String[] quoted = version.split(Pattern.quote("."));   // [25, 0, 4]
            show("quoted", quoted);
        }
        {
            Pattern pipe = Pattern.compile(Pattern.quote("|"));
            String[] fields = pipe.split("ana|31|pune");                     // [ana, 31, pune]
            show("fields", fields);
            List<String> asList = pipe.splitAsStream("ana|31|pune").toList();   // [ana, 31, pune]
            show("asList", asList);
        }
        {
            String word = "java";
            String[] letters = word.split("");                // [j, a, v, a]
            show("letters", letters);
            char[] chars = word.toCharArray();                // [j, a, v, a]
            show("chars", chars);
            String[] viaChars = word.chars().mapToObj(Character::toString).toArray(String[]::new);   // [j, a, v, a]
            show("viaChars", viaChars);
        }
        {
            String chat = "ok\uD83D\uDE00";
            int charCount = chat.length();                                  // 4
            show("charCount", charCount);
            String[] broken = chat.split("");                                // 4 elements, the emoji is split in two
            show("broken", broken);
            String[] whole = chat.codePoints().mapToObj(Character::toString).toArray(String[]::new);
            int pieces = whole.length;                                      // 3
            show("pieces", pieces);
        }
        {
            String file = "report.pdf";
            String[] one = {file};                                // [report.pdf]
            show("one", one);
            Path path = Path.of("docs", new String[] {file});     // docs/report.pdf
            show("path", path);
        }
        {
            String noTags = "";
            String[] parts = noTags.split(",");
            int count = parts.length;                    // 1, the only element is ""
            show("count", count);
            String[] safe = noTags.isBlank() ? new String[0] : noTags.split(",");
            int safeCount = safe.length;                 // 0
            show("safeCount", safeCount);
        }
        {
            String formula = "12+7-3";
            String[] tokens = formula.splitWithDelimiters("[+-]", 0);   // [12, +, 7, -, 3]
            show("tokens", tokens);
        }
        {
            String input = " java, Spring ,,docker ";
            String[] tags = Arrays.stream(input.split(","))
                    .map(String::strip)
                    .filter(t -> !t.isEmpty())
                    .map(String::toLowerCase)
                    .toArray(String[]::new);
            String shown = Arrays.toString(tags);   // "[java, spring, docker]"
            show("shown", shown);
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

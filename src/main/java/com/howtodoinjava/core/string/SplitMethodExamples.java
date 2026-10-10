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
 * Examples for the tutorial "Java String split(): Limit, Regex Escaping and Empty Strings".
 * https://howtodoinjava.com/java/string/java-string-split-example/
 */
public class SplitMethodExamples {
    static record Contact(String name, String email, String phone, String city, String note) {}
    static Contact parseContact(String row) {
        String[] f = row.split(";", -1);
        if (f.length != 5) {
            throw new IllegalArgumentException("expected 5 columns but got " + f.length + " in: " + row);
        }
        return new Contact(f[0].strip(), f[1].strip(), f[2].strip(), f[3].strip(), f[4].strip());
    }
    public static void main(String[] args) throws Exception {
        {
            String row = "red,green,,blue,,";
            String[] parts = row.split(",");                    // [red, green, , blue]
            show("parts", parts);
            String[] keepAll = row.split(",", -1);              // [red, green, , blue, , ]
            show("keepAll", keepAll);
            String[] firstTwo = row.split(",", 2);              // [red, green,,blue,,]
            show("firstTwo", firstTwo);
            String[] byDot = "app.config.yml".split("\\.");     // [app, config, yml]
            show("byDot", byDot);
            String[] words = "  to  be or ".strip().split("\\s+");   // [to, be, or]
            show("words", words);
        }
        {
            String[] noMatch = "red".split(",");          // [red]
            show("noMatch", noMatch);
            String[] emptyInput = "".split(",");           // []
            show("emptyInput", emptyInput);
            int emptyLength = "".split(",").length;         // 1
            show("emptyLength", emptyLength);
            String[] leading = ",red,green".split(",");    // [, red, green]
            show("leading", leading);
            String[] chars = "red".split("");              // [r, e, d]
            show("chars", chars);
        }
        {
            String setting = "url=https://shop.test/?a=1";
            String[] naive = setting.split("=");             // [url, https://shop.test/?a, 1]
            show("naive", naive);
            String[] keyValue = setting.split("=", 2);       // [url, https://shop.test/?a=1]
            show("keyValue", keyValue);
            int pieces = "a b c d".split(" ", 3).length;     // 3
            show("pieces", pieces);
        }
        {
            String[] dotWrong = "app.config.yml".split(".");          // []
            show("dotWrong", dotWrong);
            String[] pipeWrong = "a|b".split("|");                      // [a, |, b]
            show("pipeWrong", pipeWrong);
            try { String[] plusWrong = "1+2".split("+"); show("plusWrong", plusWrong); } catch (Throwable _t) { System.out.println("plusWrong -> " + _t); }
        }
        {
            String[] dot = "app.config.yml".split("\\.");                // [app, config, yml]
            show("dot", dot);
            String[] pipe = "a|b".split("\\|");                           // [a, b]
            show("pipe", pipe);
            String[] plus = "1+2".split("[+]");                           // [1, 2]
            show("plus", plus);
            String[] quoted = "a$$b$$c".split(Pattern.quote("$$"));       // [a, b, c]
            show("quoted", quoted);
        }
        {
            String full = "Asha;Pune;note";
            String partial = "Asha;Pune;";
            int fullCount = full.split(";").length;                     // 3
            show("fullCount", fullCount);
            int partialCount = partial.split(";").length;               // 2
            show("partialCount", partialCount);
            try { String lastColumn = partial.split(";")[2]; show("lastColumn", lastColumn); } catch (Throwable _t) { System.out.println("lastColumn -> " + _t); }
            String safeLast = partial.split(";", -1)[2];                // ""
            show("safeLast", safeLast);
        }
        {
            String[] withLeading = "  to be".split("\\s+");            // [, to, be]
            show("withLeading", withLeading);
            String[] stripped = "  to be".strip().split("\\s+");       // [to, be]
            show("stripped", stripped);
            String[] blank = "   ".strip().split("\\s+");              // []
            show("blank", blank);
            int blankLength = "   ".strip().split("\\s+").length;       // 1
            show("blankLength", blankLength);
        }
        {
            String tags = "java, spring;docker  kafka";
            String[] anyOf = tags.split("[,;\\s]+");                       // [java, spring, docker, kafka]
            show("anyOf", anyOf);
            String[] trimmed = "a , b ;c".split("\\s*[,;]\\s*");            // [a, b, c]
            show("trimmed", trimmed);
            String[] words = "one AND two OR three".split("\\s+(AND|OR)\\s+");   // [one, two, three]
            show("words", words);
        }
        {
            String expression = "3+4-2";
            String[] numbersOnly = expression.split("[+-]");                           // [3, 4, 2]
            show("numbersOnly", numbersOnly);
            String[] withOps = expression.splitWithDelimiters("[+-]", 0);              // [3, +, 4, -, 2]
            show("withOps", withOps);
            String[] firstOp = expression.splitWithDelimiters("[+-]", 2);              // [3, +, 4-2]
            show("firstOp", firstOp);
        }
        {
            String[] lookaround = "3+4-2".split("(?<=[+-])|(?=[+-])");    // [3, +, 4, -, 2]
            show("lookaround", lookaround);
        }
        {
            Pattern separator = Pattern.compile("\\s*,\\s*");
            String[] fromPattern = separator.split("a , b,c");                          // [a, b, c]
            show("fromPattern", fromPattern);
            List<String> asList = separator.splitAsStream("a , b,c").toList();          // [a, b, c]
            show("asList", asList);
        }
        {
            String text = "a[b";
            try { String[] bracket = text.split("["); show("bracket", bracket); } catch (Throwable _t) { System.out.println("bracket -> " + _t); }
            String[] quotedBracket = text.split(Pattern.quote("["));     // [a, b]
            show("quotedBracket", quotedBracket);
            String noRegex = null;
            try { String[] nullRegex = text.split(noRegex); show("nullRegex", nullRegex); } catch (Throwable _t) { System.out.println("nullRegex -> " + _t); }
        }
        {
            int defaultCount = "Asha;asha@mail.test;;Pune;".split(";").length;     // 4
            show("defaultCount", defaultCount);
            Contact asha = parseContact("Asha;asha@mail.test;;Pune;");               // Contact[name=Asha, email=asha@mail.test, phone=, city=Pune, note=]
            show("asha", asha);
            Contact ben = parseContact(" Ben ; ben@mail.test;555-0101;Delhi;VIP");   // Contact[name=Ben, email=ben@mail.test, phone=555-0101, city=Delhi, note=VIP]
            show("ben", ben);
            try { Contact bad = parseContact("Chen;chen@mail.test"); show("bad", bad); } catch (Throwable _t) { System.out.println("bad -> " + _t); }
        }
        {
            List<String> colors = List.of("red,green,blue".split(","));           // [red, green, blue]
            show("colors", colors);
            List<String> streamed = Pattern.compile(",").splitAsStream("red,green").toList();   // [red, green]
            show("streamed", streamed);
        }
        {
            String header = "Content-Type: text/html; charset=UTF-8";
            String[] nameValue = header.split(":\\s*", 2);                          // [Content-Type, text/html; charset=UTF-8]
            show("nameValue", nameValue);
            String name = header.substring(0, header.indexOf(':'));                       // "Content-Type"
            show("name", name);
        }
        {
            String[] letters = "java".split("");                                          // [j, a, v, a]
            show("letters", letters);
            int[] codePoints = "java".codePoints().toArray();                              // [106, 97, 118, 97]
            show("codePoints", codePoints);
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

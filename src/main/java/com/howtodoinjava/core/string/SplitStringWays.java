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
 * Examples for the tutorial "Split a String in Java: split(), Pattern, Scanner and More".
 * https://howtodoinjava.com/java/string/split-tokenize-strings/
 */
public class SplitStringWays {
    static Map<String, String> parseQuery(String query) {
        Map<String, String> params = new LinkedHashMap<>();
        if (query == null || query.isBlank()) {
            return params;
        }
        for (String pair : query.split("&")) {
            String[] kv = pair.split("=", 2);
            String key = URLDecoder.decode(kv[0], StandardCharsets.UTF_8);
            String value = kv.length > 1 ? URLDecoder.decode(kv[1], StandardCharsets.UTF_8) : "";
            params.put(key, value);
        }
        return params;
    }
    public static void main(String[] args) throws Exception {
        {
            String list = "milk,eggs,,bread,";
            String[] parts = list.split(",");                                        // [milk, eggs, , bread]
            show("parts", parts);
            String[] all = list.split(",", -1);                                      // [milk, eggs, , bread, ]
            show("all", all);
            String[] safe = "1.5.2".split(Pattern.quote("."));                       // [1, 5, 2]
            show("safe", safe);
            List<String> clean = Pattern.compile(",").splitAsStream(list).filter(s -> !s.isBlank()).toList(); // [milk, eggs, bread]
            show("clean", clean);
            String[] withOps = "2+3-4".splitWithDelimiters("[+-]", 0);               // [2, +, 3, -, 4]
            show("withOps", withOps);
            List<String> lines = "milk\neggs\r\nbread".lines().toList();            // [milk, eggs, bread]
            show("lines", lines);
        }
        {
            String version = "1.5.2";
            String[] wrong = version.split(".");                       // []
            show("wrong", wrong);
            String[] escaped = version.split("\\.");                   // [1, 5, 2]
            show("escaped", escaped);
            String[] quoted = version.split(Pattern.quote("."));       // [1, 5, 2]
            show("quoted", quoted);
            String[] piped = "red|green".split("\\|");                 // [red, green]
            show("piped", piped);
        }
        {
            Pattern comma = Pattern.compile("\\s*,\\s*");
            String[] tags = comma.split("java , spring,  docker");     // [java, spring, docker]
            show("tags", tags);
        }
        {
            List<Integer> ports = Pattern.compile(",")
                    .splitAsStream("8080, 8443,,9090")
                    .map(String::strip)
                    .filter(s -> !s.isEmpty())
                    .map(Integer::valueOf)
                    .toList();                                         // [8080, 8443, 9090]
        }
        {
            String[] tokens = "10+20-5".splitWithDelimiters("[+-]", 0);     // [10, +, 20, -, 5]
            show("tokens", tokens);
            String[] pathParts = "/home/docs".splitWithDelimiters("/", 0);     // [, /, home, /, docs]
            show("pathParts", pathParts);
        }
        {
            String[] lookaround = "10+20-5".split("(?=[+-])|(?<=[+-])");    // [10, +, 20, -, 5]
            show("lookaround", lookaround);
        }
        {
            String note = "milk\r\neggs\nbread";
            List<String> lines = note.lines().toList();                // [milk, eggs, bread]
            show("lines", lines);
            String[] byRegex = note.split("\\R");                      // [milk, eggs, bread]
            show("byRegex", byRegex);
            int unixOnly = note.split("\n")[0].length();               // 5 (milk plus \r)
            show("unixOnly", unixOnly);
        }
        {
            String word = "hi\uD83D\uDE00";                             // "hi" plus a smiley emoji
            show("word", word);
            int byChars = word.split("").length;                       // 4
            show("byChars", byChars);
            int byCodePoints = (int) word.codePoints().count();        // 3
            show("byCodePoints", byCodePoints);
        }
        {
            List<Integer> scores = new ArrayList<>();
            try (Scanner scanner = new Scanner("12; 7; 30").useDelimiter("\\s*;\\s*")) {
                while (scanner.hasNextInt()) {
                    scores.add(scanner.nextInt());
                }
            }
            List<Integer> result = scores;                             // [12, 7, 30]
            show("result", result);
        }
        {
            StringTokenizer tokenizer = new StringTokenizer("milk, eggs;;bread", ",; ");
            List<String> items = new ArrayList<>();
            while (tokenizer.hasMoreTokens()) {
                items.add(tokenizer.nextToken());
            }
            List<String> found = items;                                // [milk, eggs, bread]
            show("found", found);
        }
        {
            Map<String, String> params = parseQuery("q=red+shoes&size=42&filter=a=b");  // {q=red shoes, size=42, filter=a=b}
            show("params", params);
            String filter = parseQuery("filter=a=b").get("filter");     // "a=b"
            show("filter", filter);
            String flag = parseQuery("debug").get("debug");             // ""
            show("flag", flag);
            int none = parseQuery(null).size();                         // 0
            show("none", none);
        }
        {
            String[] notFound = "milk".split(",");                    // [milk]
            show("notFound", notFound);
            int emptyInput = "".split(",").length;                     // 1
            show("emptyInput", emptyInput);
        }
        {
            String[] mixed = "milk, eggs;bread  jam".split("[,;\\s]+");   // [milk, eggs, bread, jam]
            show("mixed", mixed);
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

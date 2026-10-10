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
 * Examples for the tutorial "Comma-Separated String to List in Java (split and Streams)".
 * https://howtodoinjava.com/java/string/java-split-csv-string-to-list/
 */
public class CommaSeparatedToList {
    static List<String> splitToList(String csv) {
        if (csv == null || csv.isBlank()) {
            return List.of();
        }
        return Arrays.stream(csv.split(","))
                .map(String::strip)
                .filter(value -> !value.isEmpty())
                .toList();
    }
    static List<Integer> parseIds(String csv) {
        return Arrays.stream(csv.split(","))
                .map(String::strip)
                .<Integer>mapMulti((value, out) -> {
            try {
                out.accept(Integer.valueOf(value));
            } catch (NumberFormatException e) {
                // skip or log the invalid value
            }
        })
                .toList();
    }
    public static void main(String[] args) throws Exception {
        {
            String tags = "java, spring, docker";
            String[] array = tags.split("\\s*,\\s*");                       // [java, spring, docker]
            show("array", array);
            List<String> fixedSize = Arrays.asList(array);                     // [java, spring, docker]
            show("fixedSize", fixedSize);
            List<String> readOnly = List.of(array);                            // [java, spring, docker]
            show("readOnly", readOnly);
            List<String> mutable = new ArrayList<>(Arrays.asList(array));      // [java, spring, docker]
            show("mutable", mutable);
            List<String> cleaned = Arrays.stream(tags.split(",")).map(String::strip).toList(); // [java, spring, docker]
            show("cleaned", cleaned);
        }
        {
            String raw = " java, spring ,docker ";
            String[] plain = raw.split(",");                           // [ java,  spring , docker ]
            show("plain", plain);
            String[] regex = raw.split("\\s*,\\s*");                  // [ java, spring, docker ]
            show("regex", regex);
            String[] stripped = raw.strip().split("\\s*,\\s*");       // [java, spring, docker]
            show("stripped", stripped);
        }
        {
            String[] array = "java,spring".split(",");
            List<String> fixedSize = Arrays.asList(array);
            String old = fixedSize.set(0, "kotlin");                   // "java"
            show("old", old);
            String inArray = array[0];                                 // "kotlin"
            show("inArray", inArray);
            try { boolean added = fixedSize.add("docker"); show("added", added); } catch (Throwable _t) { System.out.println("added -> " + _t); }
        }
        {
            List<String> mutable = new ArrayList<>(Arrays.asList("java,spring".split(",")));
            boolean added = mutable.add("docker");                     // true
            show("added", added);
            List<String> result = mutable;                             // [java, spring, docker]
            show("result", result);
        }
        {
            String[] middle = "java,,spring".split(",");              // [java, , spring]
            show("middle", middle);
            String[] trailing = "java,spring,,".split(",");            // [java, spring]
            show("trailing", trailing);
            String[] kept = "java,spring,,".split(",", -1);            // [java, spring, , ]
            show("kept", kept);
            int emptyInput = "".split(",").length;                     // 1
            show("emptyInput", emptyInput);
        }
        {
            List<String> tags = splitToList(" java,, spring , ");      // [java, spring]
            show("tags", tags);
            List<String> none = splitToList("");                       // []
            show("none", none);
            List<String> missing = splitToList(null);                  // []
            show("missing", missing);
        }
        {
            List<Integer> ids = Arrays.stream("12, 7, 30".split(","))
                    .map(String::strip)
                    .map(Integer::valueOf)
                    .toList();
            List<Integer> result = ids;                                // [12, 7, 30]
            show("result", result);
            try { int bad = Integer.parseInt("abc"); show("bad", bad); } catch (Throwable _t) { System.out.println("bad -> " + _t); }
        }
        {
            List<Integer> valid = parseIds("12, abc, 30");             // [12, 30]
            show("valid", valid);
        }
        {
            String row = "Lokesh,\"Delhi, India\",37";
            int naive = row.split(",").length;                                       // 4
            show("naive", naive);
            int quoted = row.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)").length;       // 3
            show("quoted", quoted);
        }
        {
            List<String> tags = List.of("java", "spring", "docker");
            String csv = String.join(",", tags);                                     // "java,spring,docker"
            show("csv", csv);
            String upper = tags.stream().map(t -> t.toUpperCase(Locale.ROOT)).collect(Collectors.joining(", ")); // "JAVA, SPRING, DOCKER"
            show("upper", upper);
            List<String> withNull = Arrays.asList("java", null);
            String nullText = String.join(",", withNull);                            // "java,null"
            show("nullText", nullText);
        }
        {
            String allowedOrigins = "https://shop.example.com, https://admin.example.com,,https://shop.example.com, ";
            Set<String> origins = new LinkedHashSet<>(splitToList(allowedOrigins));
            int count = origins.size();                                // 2
            show("count", count);
            boolean adminAllowed = origins.contains("https://admin.example.com");    // true
            show("adminAllowed", adminAllowed);
        }
        {
            ArrayList<String> list = new ArrayList<>(Arrays.asList("java,spring".split(",")));    // [java, spring]
            show("list", list);
        }
        {
            Set<String> unique = Arrays.stream("b,a,b".split(",")).collect(Collectors.toCollection(TreeSet::new));  // [a, b]
            show("unique", unique);
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

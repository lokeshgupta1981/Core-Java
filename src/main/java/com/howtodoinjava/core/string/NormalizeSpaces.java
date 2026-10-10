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
 * Examples for the tutorial "Java Remove Extra Spaces Between Words in a String".
 * https://howtodoinjava.com/java/string/remove-extra-whitespaces-between-words/
 */
public class NormalizeSpaces {
    static final class SearchText {
        private static final Pattern WHITESPACE = Pattern.compile("\\s+");

        static String normalize(String query) {
            if (query == null) {
                return "";
            }
            return WHITESPACE.matcher(query.strip()).replaceAll(" ").toLowerCase(Locale.ROOT);
        }
    }
    static String collapseSpaces(String text) {
        if (text == null) {
            return "";
        }
        StringBuilder out = new StringBuilder(text.length());
        boolean pendingSpace = false;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (Character.isWhitespace(c)) {
                pendingSpace = out.length() > 0;
            } else {
                if (pendingSpace) {
                    out.append(' ');
                    pendingSpace = false;
                }
                out.append(c);
            }
        }
        return out.toString();
    }
    public static void main(String[] args) throws Exception {
        {
            String blogName = "  how to   do    in  java   .         com ";
            String normalized = blogName.strip().replaceAll("\\s+", " ");   // "how to do in java . com"
            show("normalized", normalized);
            String commons = StringUtils.normalizeSpace(blogName);          // "how to do in java . com"
            show("commons", commons);
        }
        {
            String blogName = "  how to   do    in  java   .         com ";
            String noStrip = blogName.replaceAll("\\s+", " ");               // " how to do in java . com "
            show("noStrip", noStrip);
            String tabs = "name:\t\tLokesh\n\nrole:  author".replaceAll("\\s+", " ");  // "name: Lokesh role: author"
            show("tabs", tabs);
        }
        {
            String query = SearchText.normalize("  Spring   Boot\tTesting ");  // "spring boot testing"
            show("query", query);
            String missing = SearchText.normalize(null);                        // ""
            show("missing", missing);
        }
        {
            String blogName = "how to   do    in  java   .         com";
            String nameWithProperSpacing = StringUtils.normalizeSpace(blogName);  // "how to do in java . com"
            show("nameWithProperSpacing", nameWithProperSpacing);
            String nothing = StringUtils.normalizeSpace(null);                    // null
            show("nothing", nothing);
        }
        {
            String blogName = "  how to   do    in  java   .         com ";
            String joined = String.join(" ", blogName.strip().split("\\s+"));  // "how to do in java . com"
            show("joined", joined);
            String emptyText = String.join(" ", "   ".strip().split("\\s+"));   // ""
            show("emptyText", emptyText);
        }
        {
            String looped = collapseSpaces("  how to   do \t in  java  ");  // "how to do in java"
            show("looped", looped);
            String blank = collapseSpaces(" \t\n ");                          // ""
            show("blank", blank);
        }
        {
            String copied = "java\u00A0\u00A0streams \u2003 guide";
            boolean nbspLeft = copied.replaceAll("\\s+", " ").contains("\u00A0");      // true
            show("nbspLeft", nbspLeft);
            String unicodeAware = copied.replaceAll("(?U)\\s+", " ").strip();          // "java streams guide"
            show("unicodeAware", unicodeAware);
            String commonsNbsp = StringUtils.normalizeSpace("java\u00A0\u00A0streams"); // "java  streams"
            show("commonsNbsp", commonsNbsp);
        }
        {
            String address = "  42   Main  Street\n   Springfield,\t IL ";
            List<String> lines = address.lines()
                    .map(line -> line.replaceAll("\\h+", " ").strip())
                    .toList();                                     // [42 Main Street, Springfield, IL]
            String cleaned = String.join("\n", lines);
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

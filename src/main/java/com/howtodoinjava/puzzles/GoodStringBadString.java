package com.howtodoinjava.puzzles;

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
 * Examples for the tutorial "Remove Consecutive Duplicate Characters in Java (Good String)".
 * https://howtodoinjava.com/java/puzzles/java-puzzle-good-string-bad-string/
 */
public class GoodStringBadString {
    static String toGoodString(String s) {
        if (s == null || s.length() < 2) {
            return s;                               // nothing to compare
        }
        StringBuilder good = new StringBuilder(s.length());
        good.append(s.charAt(0));
        for (int i = 1; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c != s.charAt(i - 1)) {
                good.append(c);                     // keep the first char of each run
            }
        }
        return good.toString();
    }
    static String toGoodStringIgnoreCase(String s) {
        StringBuilder good = new StringBuilder(s.length());
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (i == 0 || Character.toLowerCase(c) != Character.toLowerCase(s.charAt(i - 1))) {
                good.append(c);
            }
        }
        return good.toString();
    }
    static String removeAdjacentPairs(String s) {
        StringBuilder stack = new StringBuilder();
        for (char c : s.toCharArray()) {
            int top = stack.length() - 1;
            if (top >= 0 && stack.charAt(top) == c) {
                stack.deleteCharAt(top);            // the pair cancels out
            } else {
                stack.append(c);
            }
        }
        return stack.toString();
    }
    static String toGoodStringCodePoints(String s) {
        StringBuilder good = new StringBuilder(s.length());
        int previous = -1;
        for (int cp : s.codePoints().toArray()) {
            if (cp != previous) {
                good.appendCodePoint(cp);
            }
            previous = cp;
        }
        return good.toString();
    }
    public static void main(String[] args) throws Exception {
        {
            String letters = toGoodString("aabbbcdd");      // "abcd"
            show("letters", letters);
            String word = toGoodString("bookkeeper");        // "bokeper"
            show("word", word);
            String clean = toGoodString("abc");              // "abc"
            show("clean", clean);
        }
        {
            String empty = toGoodString("");                // ""
            show("empty", empty);
            String single = toGoodString("a");              // "a"
            show("single", single);
            String run = toGoodString("aaaa");              // "a"
            show("run", run);
            String keepsNull = toGoodString(null);          // null
            show("keepsNull", keepsNull);
        }
        {
            String regexWord = "bookkeeper".replaceAll("(.)\\1+", "$1");    // "bokeper"
            show("regexWord", regexWord);
            String regexRun = "aaab".replaceAll("(.)\\1+", "$1");          // "ab"
            show("regexRun", regexRun);
        }
        {
            String pairsOnly = "aaab".replaceAll("(\\p{L})\\1", "$1");      // "aab", the run of three keeps two
            show("pairsOnly", pairsOnly);
            String digitsKept = "1122".replaceAll("(\\p{L})\\1+", "$1");    // "1122", digits are not letters
            show("digitsKept", digitsKept);
        }
        {
            Pattern runs = Pattern.compile("(.)\\1+");
            String reused = runs.matcher("bookkeeper").replaceAll("$1");   // "bokeper"
            show("reused", reused);
        }
        {
            String mixed = toGoodStringIgnoreCase("Good Oops, Bad Oops");     // "God Ops, Bad Ops"
            show("mixed", mixed);
            String regexMixed = "AaBb".replaceAll("(?i)(.)\\1+", "$1");       // "AB"
            show("regexMixed", regexMixed);
        }
        {
            String pairs = removeAdjacentPairs("abbaca");       // "ca"
            show("pairs", pairs);
            String allGone = removeAdjacentPairs("abba");       // ""
            show("allGone", allGone);
        }
        {
            String twoSmiles = "\uD83D\uDE00\uD83D\uDE00";
            int charLoopLength = toGoodString(twoSmiles).length();             // 4, both emoji kept
            show("charLoopLength", charLoopLength);
            int codePointLength = toGoodStringCodePoints(twoSmiles).length();  // 2, one emoji left
            show("codePointLength", codePointLength);
        }
        {
            String path = "/api//books///42";
            String cleanPath = path.replaceAll("/{2,}", "/");                  // "/api/books/42"
            show("cleanPath", cleanPath);
            String query = "java    regex  tips";
            String cleanQuery = query.replaceAll(" {2,}", " ");                // "java regex tips"
            show("cleanQuery", cleanQuery);
        }
        {
            String stretched = "soooo cool book".replaceAll("(.)\\1{2,}", "$1");   // "so cool book"
            show("stretched", stretched);
        }
        {
            String unique = "bookkeeper".chars().distinct().collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();   // "bokepr"
            show("unique", unique);
        }
        {
            String words = "this is is a test test test".replaceAll("\\b(\\w+)(\\s+\\1\\b)+", "$1");   // "this is a test"
            show("words", words);
        }
        {
            int deletions = "bookkeeper".length() - toGoodString("bookkeeper").length();   // 3
            show("deletions", deletions);
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

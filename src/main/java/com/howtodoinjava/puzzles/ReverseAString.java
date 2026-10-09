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
 * Examples for the tutorial "Reverse a String in Java: StringBuilder, Loops and Unicode".
 * https://howtodoinjava.com/java/puzzles/how-to-reverse-string-in-java/
 */
public class ReverseAString {
    static String reverse(String s) {
        if (s == null) {
            return null;
        }
        return new StringBuilder(s).reverse().toString();
    }
    static String reverseCharArray(String s) {
        char[] chars = s.toCharArray();
        for (int left = 0, right = chars.length - 1; left < right; left++, right--) {
            char tmp = chars[left];
            chars[left] = chars[right];
            chars[right] = tmp;
        }
        return new String(chars);
    }
    static String reverseLoop(String s) {
        StringBuilder sb = new StringBuilder(s.length());
        for (int i = s.length() - 1; i >= 0; i--) {
            sb.append(s.charAt(i));
        }
        return sb.toString();
    }
    static String reverseRecursive(String s) {
        if (s.isEmpty()) {
            return s;
        }
        return reverseRecursive(s.substring(1)) + s.charAt(0);
    }
    static String reverseCodePoints(String s) {
        int[] cps = s.codePoints().toArray();
        int[] rev = IntStream.range(0, cps.length).map(i -> cps[cps.length - 1 - i]).toArray();
        return new String(rev, 0, rev.length);
    }
    static String reverseGraphemes(String s) {
        List<String> clusters = Pattern.compile("\\X").matcher(s).results().map(MatchResult::group).toList();
        return String.join("", clusters.reversed());
    }
    static String reverseWords(String sentence) {
        if (sentence == null || sentence.isBlank()) {
            return "";
        }
        List<String> words = Arrays.asList(sentence.strip().split("\\s+"));
        return String.join(" ", words.reversed());
    }
    public static void main(String[] args) throws Exception {
        {
            String word = "Java";
            String reversed = new StringBuilder(word).reverse().toString();   // "avaJ"
            show("reversed", reversed);
            String sentence = "Java is fun";
            String backwards = reverseWords(sentence);                        // "fun is Java"
            show("backwards", backwards);
        }
        {
            String hello = reverse("hello");     // "olleh"
            show("hello", hello);
            String empty = reverse("");           // ""
            show("empty", empty);
            String none = reverse(null);          // null
            show("none", none);
            try { String crash = new StringBuilder(null).reverse().toString(); show("crash", crash); } catch (Throwable _t) { System.out.println("crash -> " + _t); }
        }
        {
            String swapped = reverseCharArray("stressed");   // "desserts"
            show("swapped", swapped);
            String odd = reverseCharArray("abc");             // "cba"
            show("odd", odd);
        }
        {
            String looped = reverseLoop("drawer");   // "reward"
            show("looped", looped);
        }
        {
            String rec = reverseRecursive("live");   // "evil"
            show("rec", rec);
        }
        {
            String streamed = reverseCodePoints("stream");   // "maerts"
            show("streamed", streamed);
        }
        {
            String emoji = "a\uD83D\uDE00b";                                 // a, grinning face, b
            show("emoji", emoji);
            String safe = new StringBuilder(emoji).reverse().toString();
            boolean safeOk = safe.equals("b\uD83D\uDE00a");                   // true
            show("safeOk", safeOk);
            String broken = reverseCharArray(emoji);
            boolean brokenOk = broken.equals("b\uDE00\uD83Da");               // true, the pair is reversed
            show("brokenOk", brokenOk);
            long emojiCount = broken.codePoints().filter(Character::isSupplementaryCodePoint).count();   // 0
            show("emojiCount", emojiCount);
        }
        {
            String cafe = "cafe\u0301";                                    // accent on the last e
            show("cafe", cafe);
            boolean wrongAccent = reverse(cafe).equals("\u0301efac");          // true, the accent comes first
            show("wrongAccent", wrongAccent);
            boolean rightAccent = reverseGraphemes(cafe).equals("e\u0301fac"); // true
            show("rightAccent", rightAccent);
        }
        {
            String spaced = reverseWords("  learn   Java  today ");   // "today Java learn"
            show("spaced", spaced);
            String blank = reverseWords("   ");                         // ""
            show("blank", blank);
            String eachWord = Arrays.stream("Java is fun".split(" ")).map(w -> reverse(w)).collect(Collectors.joining(" "));   // "avaJ si nuf"
            show("eachWord", eachWord);
        }
        {
            String host = "api.shop.example.com";
            List<String> labels = Arrays.asList(host.split("\\."));
            String sortKey = String.join(".", labels.reversed());   // "com.example.shop.api"
            show("sortKey", sortKey);
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

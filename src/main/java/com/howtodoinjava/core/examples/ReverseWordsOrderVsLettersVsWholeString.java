package com.howtodoinjava.core.examples;

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
 * Examples for the tutorial "Reverse Words in a String in Java (6 Ways with Code)".
 * https://howtodoinjava.com/java-examples/how-to-reverse-words-in-string/
 */
public class ReverseWordsOrderVsLettersVsWholeString {
    static String sentence = "Java is fun to learn";
    static String reverseInPlace(String text) {
        char[] chars = text.toCharArray();
        int n = chars.length;

        // 1. Reverse the whole array: "Java is fun" -> "nuf si avaJ"
        reverse(chars, 0, n - 1);

        // 2. Reverse each word back: "nuf si avaJ" -> "fun is Java"
        int start = 0;
        while (start < n) {
            while (start < n && chars[start] == ' ') start++;
            int end = start;
            while (end < n && chars[end] != ' ') end++;
            reverse(chars, start, end - 1);
            start = end;
        }

        // 3. Copy words left, one space between them
        int write = 0, read = 0;
        while (read < n) {
            while (read < n && chars[read] == ' ') read++;
            if (read == n) break;
            if (write > 0) chars[write++] = ' ';
            while (read < n && chars[read] != ' ') chars[write++] = chars[read++];
        }
        return new String(chars, 0, write);
    }

    static void reverse(char[] chars, int left, int right) {
        while (left < right) {
            char tmp = chars[left];
            chars[left++] = chars[right];
            chars[right--] = tmp;
        }
    }
    static String reverseWordOrder(String text) {
        if (text == null || text.isBlank()) {
            return "";
        }
        return String.join(" ", List.of(text.strip().split("\\s+")).reversed());
    }
    static String reverseKeepEndPunctuation(String text) {
        String body = text.strip();
        String end = "";
        if (!body.isEmpty() && ".!?".indexOf(body.charAt(body.length() - 1)) >= 0) {
            end = body.substring(body.length() - 1);            // "!"
            body = body.substring(0, body.length() - 1);        // "Hello, world of Java"
        }
        List<String> words = new ArrayList<>();
        for (String word : body.split("\\s+")) {
            words.add(word.replaceAll("[,;:]+$", ""));          // "Hello," -> "Hello"
        }
        return String.join(" ", words.reversed()) + end;      // "Java of world Hello!"
    }
    public static void main(String[] args) throws Exception {
        {
            String sentence = "Java is fun to learn";

            // 1. Reverse the order of words (Java 8+)
            List<String> words = Arrays.asList(sentence.strip().split("\\s+"));
            Collections.reverse(words);
            String reversed = String.join(" ", words);                                    // "learn to fun is Java"
            show("reversed", reversed);

            // 2. Reverse the order of words (Java 21+)
            String reversed21 = String.join(" ", List.of(sentence.strip().split("\\s+")).reversed());   // "learn to fun is Java"
            show("reversed21", reversed21);

            // 3. Reverse the letters of each word, keep the word order
            String eachWord = Arrays.stream(sentence.split(" "))
                    .map(word -> new StringBuilder(word).reverse())
                    .collect(Collectors.joining(" "));                                          // "avaJ si nuf ot nrael"

            // 4. Reverse the whole string
            String whole = new StringBuilder(sentence).reverse().toString();              // "nrael ot nuf si avaJ"
            show("whole", whole);

            // 5. Apache Commons Lang
            String commons = StringUtils.reverseDelimited(sentence, ' ');                 // "learn to fun is Java"
            show("commons", commons);
        }
        {
            List<String> words = Arrays.asList(sentence.strip().split("\\s+"));   // [Java, is, fun, to, learn]
            show("words", words);
            Collections.reverse(words);                                            // [learn, to, fun, is, Java]
            String reversed = String.join(" ", words);                             // "learn to fun is Java"
            show("reversed", reversed);
        }
        {
            String reversed21 = String.join(" ", List.of(sentence.strip().split("\\s+")).reversed());   // "learn to fun is Java"
            show("reversed21", reversed21);
        }
        {
            String[] words = sentence.strip().split("\\s+");
            StringBuilder result = new StringBuilder();

            for (int i = words.length - 1; i >= 0; i--) {
                result.append(words[i]);
                if (i > 0) {
                    result.append(' ');          // no space after the last word
                }
            }
            String reversed = result.toString();   // "learn to fun is Java"
            show("reversed", reversed);
        }
        {
            String[] words = sentence.strip().split("\\s+");

            String reversed = IntStream.range(0, words.length)
                    .mapToObj(i -> words[words.length - 1 - i])     // learn, to, fun, is, Java
                    .collect(Collectors.joining(" "));              // "learn to fun is Java"
        }
        {
            String reversed = reverseInPlace("Java is fun to learn");   // "learn to fun is Java"
            show("reversed", reversed);
            String cleaned = reverseInPlace("  Java   is fun  ");       // "fun is Java"
            show("cleaned", cleaned);
        }
        {
            String[] words = sentence.strip().split("\\s+");
            StringBuilder result = new StringBuilder();

            for (int i = 0; i < words.length; i++) {
                String word = words[i];
                for (int j = word.length() - 1; j >= 0; j--) {
                    result.append(word.charAt(j));
                }
                if (i < words.length - 1) {
                    result.append(' ');
                }
            }
            String eachWord = result.toString();   // "avaJ si nuf ot nrael"
            show("eachWord", eachWord);
        }
        {
            String messy = "  Java   is fun  ";

            String withSpace = String.join(" ", List.of(messy.split("\\s+")).reversed());       // "fun is Java " (trailing space)
            show("withSpace", withSpace);
            String clean = String.join(" ", List.of(messy.strip().split("\\s+")).reversed());   // "fun is Java"
            show("clean", clean);
        }
        {
            String spaced = reverseWordOrder("  Java   is fun  ");   // "fun is Java"
            show("spaced", spaced);
            String tabs = reverseWordOrder("Java\tis\nfun");         // "fun is Java"
            show("tabs", tabs);
            String single = reverseWordOrder("Java");                // "Java"
            show("single", single);
            String blank = reverseWordOrder("   ");                  // ""
            show("blank", blank);
            String missing = reverseWordOrder(null);                 // ""
            show("missing", missing);
        }
        {
            String moved = reverseWordOrder("Hello, world of Java!");          // "Java! of world Hello,"
            show("moved", moved);
            String kept = reverseKeepEndPunctuation("Hello, world of Java!");  // "Java of world Hello!"
            show("kept", kept);
        }
        {
            String words = StringUtils.reverseDelimited("Java is fun to learn", ' ');       // "learn to fun is Java"
            show("words", words);
            String spaces = StringUtils.reverseDelimited("  Java   is fun  ", ' ');         // "fun is Java"
            show("spaces", spaces);
            String parts = StringUtils.reverseDelimited("com.howtodoinjava.core", '.');     // "core.howtodoinjava.com"
            show("parts", parts);
            String nothing = StringUtils.reverseDelimited(null, ' ');                       // null
            show("nothing", nothing);

            // Letters of each word: reverse everything, then reverse the word order back
            String letters = StringUtils.reverseDelimited(StringUtils.reverse("Java is fun to learn"), ' ');   // "avaJ si nuf ot nrael"
            show("letters", letters);
        }
        {
            try { Collections.reverse(List.of("a", "b"));  } catch (Throwable _t) { System.out.println("-> " + _t); }
            List<String> reversed = List.of("a", "b").reversed();   // [b, a]
            show("reversed", reversed);
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

package com.howtodoinjava.core.sorting;

import java.util.regex.*;
import java.net.*;
import java.text.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.time.*;
import java.nio.file.*;
import java.math.*;
import java.io.*;

/**
 * Examples for the tutorial "Sort a String Alphabetically in Java (Arrays.sort, Streams)".
 * https://howtodoinjava.com/java/sort/sort-string-chars-alphabetically/
 */
public class SortStringAlphabetically {
    static String sortIgnoringCase(String text) {
        return text.codePoints()
                .boxed()
                .sorted(Comparator.comparingInt(Character::toLowerCase))
                .map(Character::toString)
                .collect(Collectors.joining());
    }
    static String insertionSort(String text) {
        char[] chars = text.toCharArray();
        for (int i = 1; i < chars.length; i++) {
            char current = chars[i];
            int j = i - 1;
            while (j >= 0 && chars[j] > current) {
                chars[j + 1] = chars[j];
                j--;
            }
            chars[j + 1] = current;
        }
        return new String(chars);
    }
    static String anagramKey(String word) {
        char[] letters = word.toLowerCase(Locale.ROOT).replaceAll("[^a-z]", "").toCharArray();
        Arrays.sort(letters);
        return new String(letters);
    }
    static String sortLettersOnly(String text) {
        char[] chars = text.toCharArray();
        char[] letters = text.chars().filter(Character::isLetter).sorted().mapToObj(c -> String.valueOf((char) c)).collect(Collectors.joining()).toCharArray();
        int next = 0;
        for (int i = 0; i < chars.length; i++) {
            if (Character.isLetter(chars[i])) {
                chars[i] = letters[next++];
            }
        }
        return new String(chars);
    }
    public static void main(String[] args) throws Exception {
        {
            String word = "stack";
            char[] chars = word.toCharArray();
            Arrays.sort(chars);
            String sorted = new String(chars);                                                     // "ackst"
            show("sorted", sorted);
            String withStream = word.chars().sorted().mapToObj(Character::toString).collect(Collectors.joining());   // "ackst"
            show("withStream", withStream);
            String descending = new StringBuilder(sorted).reverse().toString();                   // "tskca"
            show("descending", descending);

            char[] mixed = "Banana".toCharArray();
            Arrays.sort(mixed);
            String caseSensitive = new String(mixed);                                             // "Baaann"
            show("caseSensitive", caseSensitive);
        }
        {
            String code = "dcab";
            char[] letters = code.toCharArray();
            Arrays.sort(letters);
            String sortedCode = String.valueOf(letters);   // "abcd"
            show("sortedCode", sortedCode);
            String original = code;                        // "dcab"
            show("original", original);
        }
        {
            char[] name = "Java Dev".toCharArray();
            Arrays.sort(name);
            String withSpace = new String(name);                                                            // " DJaaevv"
            show("withSpace", withSpace);
            String lettersOnly = "Java Dev".chars().filter(Character::isLetter).sorted().mapToObj(Character::toString).collect(Collectors.joining());   // "DJaaevv"
            show("lettersOnly", lettersOnly);
        }
        {
            String ignoreCase = sortIgnoringCase("Banana");   // "aaaBnn"
            show("ignoreCase", ignoreCase);
            String names = sortIgnoringCase("bAcB");          // "AbBc"
            show("names", names);
        }
        {
            String word = "stack";
            String byChars = word.chars().sorted().mapToObj(Character::toString).collect(Collectors.joining());        // "ackst"
            show("byChars", byChars);
            String byCodePoints = word.codePoints().sorted().mapToObj(Character::toString).collect(Collectors.joining());   // "ackst"
            show("byCodePoints", byCodePoints);
            String unique = "banana".chars().distinct().sorted().mapToObj(Character::toString).collect(Collectors.joining());   // "abn"
            show("unique", unique);
        }
        {
            String emoji = "\uD83D\uDE00\uD83C\uDF55";
            char[] halves = emoji.toCharArray();
            Arrays.sort(halves);
            String broken = new String(halves);
            boolean pairsIntact = broken.codePoints().allMatch(cp -> Character.isSupplementaryCodePoint(cp));   // false
            show("pairsIntact", pairsIntact);
            String safe = emoji.codePoints().sorted().mapToObj(Character::toString).collect(Collectors.joining());
            boolean safeIntact = safe.codePoints().allMatch(cp -> Character.isSupplementaryCodePoint(cp));       // true
            show("safeIntact", safeIntact);
            int firstCodePoint = safe.codePointAt(0);                                                            // 127829
            show("firstCodePoint", firstCodePoint);
        }
        {
            char[] chars = "stack".toCharArray();
            Arrays.sort(chars);
            String reversed = new StringBuilder(new String(chars)).reverse().toString();   // "tskca"
            show("reversed", reversed);
            String inPipeline = "stack".codePoints().boxed().sorted(Comparator.reverseOrder()).map(Character::toString).collect(Collectors.joining());   // "tskca"
            show("inPipeline", inPipeline);
        }
        {
            String letters = "fe\u00e9a";
            char[] chars = letters.toCharArray();
            Arrays.sort(chars);
            String byCharValue = new String(chars);                                                                   // "aef\u00e9"
            show("byCharValue", byCharValue);
            Collator french = Collator.getInstance(Locale.FRENCH);
            String byCollator = letters.codePoints().mapToObj(Character::toString).sorted(french).collect(Collectors.joining());   // "ae\u00e9f"
            show("byCollator", byCollator);
        }
        {
            String manual = insertionSort("stack");   // "ackst"
            show("manual", manual);
            String empty = insertionSort("");          // ""
            show("empty", empty);
        }
        {
            boolean anagram = anagramKey("Listen").equals(anagramKey("Silent"));      // true
            show("anagram", anagram);
            boolean notAnagram = anagramKey("stack").equals(anagramKey("stick"));      // false
            show("notAnagram", notAnagram);
            Map<String, List<String>> groups = Stream.of("listen", "stack", "silent", "tacks", "enlist").collect(Collectors.groupingBy(w -> anagramKey(w), TreeMap::new, Collectors.toList()));   // {ackst=[stack, tacks], eilnst=[listen, silent, enlist]}
            show("groups", groups);
        }
        {
            String unique = "mississippi".chars().distinct().sorted().mapToObj(Character::toString).collect(Collectors.joining());   // "imps"
            show("unique", unique);
        }
        {
            String mixedCode = sortLettersOnly("d3c1b");   // "b3c1d"
            show("mixedCode", mixedCode);
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

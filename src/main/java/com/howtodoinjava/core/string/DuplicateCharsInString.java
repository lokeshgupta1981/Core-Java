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
 * Examples for the tutorial "Find Duplicate Characters in a String in Java (With Counts)".
 * https://howtodoinjava.com/java/string/find-duplicate-characters/
 */
public class DuplicateCharsInString {
    static Map<Character, Integer> countChars(String input) {
        Map<Character, Integer> counts = new LinkedHashMap<>();
        if (input == null || input.isEmpty()) {
            return counts;
        }
        for (char c : input.toCharArray()) {
            counts.merge(c, 1, Integer::sum);
        }
        return counts;
    }
    static Optional<Character> firstUnique(String input) {
        return countChars(input).entrySet().stream()
                .filter(e -> e.getValue() == 1)
                .map(Map.Entry::getKey)
                .findFirst();
    }
    static boolean tooManyRepeats(String password, int limit) {
        Map<Character, Integer> counts = countChars(password);
        return !counts.isEmpty() && Collections.max(counts.values()) > limit;
    }
    public static void main(String[] args) throws Exception {
        {
            String input = "howtodoinjava";
            Map<Character, Long> counts = input.chars()
                    .mapToObj(c -> (char) c)
                    .collect(Collectors.groupingBy(c -> c, LinkedHashMap::new, Collectors.counting()));
            // {h=1, o=3, w=1, t=1, d=1, i=1, n=1, j=1, a=2, v=1}
            List<Character> duplicates = counts.entrySet().stream()
                    .filter(e -> e.getValue() > 1)
                    .map(Map.Entry::getKey)
                    .toList();                      // [o, a]
        }
        {
            Map<Character, Integer> bag = countChars("howtodoinjava");
            List<Character> duplicateChars = bag.entrySet().stream()
                    .filter(e -> e.getValue() > 1).map(Map.Entry::getKey).toList();    // [o, a]
            Map<Character, Integer> duplicatesWithCount = new LinkedHashMap<>(bag);
            duplicatesWithCount.values().removeIf(count -> count == 1);
            String withCount = duplicatesWithCount.toString();                         // "{o=3, a=2}"
            show("withCount", withCount);
            List<Character> uniqueChars = bag.entrySet().stream()
                    .filter(e -> e.getValue() == 1).map(Map.Entry::getKey).toList();   // [h, w, t, d, i, n, j, v]
        }
        {
            String input = "howtodoinjava";
            long oCount = input.chars().filter(c -> c == 'o').count();             // 3
            show("oCount", oCount);
            String repeated = input.chars()
                    .mapToObj(c -> (char) c)
                    .collect(Collectors.groupingBy(c -> c, LinkedHashMap::new, Collectors.counting()))
                    .entrySet().stream()
                    .filter(e -> e.getValue() > 1)
                    .map(e -> e.getKey() + "=" + e.getValue())
                    .collect(Collectors.joining(", "));                            // "o=3, a=2"
        }
        {
            String input = "howtodoinjava";
            int[] counts = new int[128];
            for (char c : input.toCharArray()) {
                if (c < 128) {
                    counts[c]++;
                }
            }
            StringBuilder dups = new StringBuilder();
            for (char c = 0; c < 128; c++) {
                if (counts[c] > 1) {
                    dups.append(c);
                }
            }
            String asciiDups = dups.toString();     // "ao"
            show("asciiDups", asciiDups);
        }
        {
            String input = "howtodoinjava";
            char[] chars = input.toCharArray();
            Arrays.sort(chars);                     // aadhijnoootvw
            StringBuilder found = new StringBuilder();
            for (int i = 1; i < chars.length; i++) {
                boolean repeat = chars[i] == chars[i - 1];
                boolean alreadyAdded = !found.isEmpty() && found.charAt(found.length() - 1) == chars[i];
                if (repeat && !alreadyAdded) {
                    found.append(chars[i]);
                }
            }
            String sortedDups = found.toString();   // "ao"
            show("sortedDups", sortedDups);
        }
        {
            String title = "Java Jump Start";
            Map<Character, Long> letters = title.chars()
                    .filter(Character::isLetter)
                    .mapToObj(c -> Character.toLowerCase((char) c))
                    .collect(Collectors.groupingBy(c -> c, LinkedHashMap::new, Collectors.counting()));
            List<Character> repeatedLetters = letters.entrySet().stream()
                    .filter(e -> e.getValue() > 1).map(Map.Entry::getKey).toList();   // [j, a, t]
        }
        {
            String reactions = "\uD83D\uDE00\uD83D\uDE00\uD83D\uDC4D";       // grinning, grinning, thumbs up
            show("reactions", reactions);
            Map<String, Long> byCodePoint = reactions.codePoints()
                    .mapToObj(Character::toString)
                    .collect(Collectors.groupingBy(s -> s, LinkedHashMap::new, Collectors.counting()));
            int distinctEmoji = byCodePoint.size();                        // 2
            show("distinctEmoji", distinctEmoji);
            long grinningCount = byCodePoint.get("\uD83D\uDE00");          // 2
            show("grinningCount", grinningCount);
            long charKeys = reactions.chars().distinct().count();          // 3
            show("charKeys", charKeys);
        }
        {
            Optional<Character> first = firstUnique("howtodoinjava");      // Optional[h]
            show("first", first);
            Optional<Character> second = firstUnique("aabbc");              // Optional[c]
            show("second", second);
            Optional<Character> none = firstUnique("aabb");                 // Optional.empty
            show("none", none);
        }
        {
            boolean weak = tooManyRepeats("aaaa1234", 3);                  // true
            show("weak", weak);
            boolean ok = tooManyRepeats("abca1234", 3);                    // false
            show("ok", ok);
            boolean blank = tooManyRepeats("", 3);                         // false
            show("blank", blank);
        }
        {
            String once = "howtodoinjava".codePoints().distinct()
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString();                    // "howtdinjav"
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

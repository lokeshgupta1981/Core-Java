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
import java.util.regex.*;
import java.text.Normalizer;

/**
 * Examples for the tutorial "Count Vowels and Consonants in a String in Java".
 * https://howtodoinjava.com/java-examples/count-vowels-consonants-in-string/
 */
public class CountVowelsConsonantsFlow {
    static record LetterCount(int vowels, int consonants) {}
    static LetterCount countLetters(String text) {
        int vowels = 0;
        int consonants = 0;
        for (char ch : text.toLowerCase(Locale.ROOT).toCharArray()) {
            switch (ch) {
                case 'a', 'e', 'i', 'o', 'u' -> vowels++;
                default -> {
                    if (ch >= 'a' && ch <= 'z') {
                        consonants++;
                    }
                }
            }
        }
        return new LetterCount(vowels, consonants);
    }
    static final class Syllables {
        private static final Pattern VOWEL_GROUP = Pattern.compile("[aeiouy]+");

        static long estimate(String word) {
            long groups = VOWEL_GROUP.matcher(word.toLowerCase(Locale.ROOT)).results().count();
            return Math.max(1, groups);
        }
    }
    static int countVowels(String s) {
        if (s.isEmpty()) {
            return 0;
        }
        int first = "aeiouAEIOU".indexOf(s.charAt(0)) >= 0 ? 1 : 0;
        return first + countVowels(s.substring(1));
    }
    public static void main(String[] args) throws Exception {
        {
            String title = "Rhythm and Blues 1979";
            int vowels = 0;
            int consonants = 0;
            for (char ch : title.toLowerCase(Locale.ROOT).toCharArray()) {
                if ("aeiou".indexOf(ch) >= 0) {
                    vowels++;
                } else if (ch >= 'a' && ch <= 'z') {
                    consonants++;
                }
            }
            String result = vowels + " vowels, " + consonants + " consonants";   // "3 vowels, 11 consonants"
            show("result", result);
        }
        {
            LetterCount song = countLetters("Rhythm and Blues 1979");   // LetterCount[vowels=3, consonants=11]
            show("song", song);
            LetterCount empty = countLetters("");                       // LetterCount[vowels=0, consonants=0]
            show("empty", empty);
            LetterCount digits = countLetters("2025-10-10");            // LetterCount[vowels=0, consonants=0]
            show("digits", digits);
        }
        {
            String title = "Rhythm and Blues 1979";
            Map<Boolean, Long> byType = title.toLowerCase(Locale.ROOT).chars()
                    .filter(ch -> ch >= 'a' && ch <= 'z')
                    .boxed()
                    .collect(Collectors.partitioningBy(ch -> "aeiou".indexOf(ch) >= 0, Collectors.counting()));
            long vowels = byType.get(true);                  // 3
            show("vowels", vowels);
            long consonants = byType.get(false);             // 11
            show("consonants", consonants);
            String asMap = byType.toString();                // "{false=11, true=3}"
            show("asMap", asMap);
        }
        {
            long vowelCount = "Rhythm and Blues 1979".toLowerCase(Locale.ROOT).chars()
                    .filter(ch -> "aeiou".indexOf(ch) >= 0)
                    .count();
            long count = vowelCount;                         // 3
            show("count", count);
        }
        {
            String lower = "Rhythm and Blues 1979".toLowerCase(Locale.ROOT);
            int vowels = lower.replaceAll("[^aeiou]", "").length();                  // 3
            show("vowels", vowels);
            int consonants = lower.replaceAll("[^b-df-hj-np-tv-z]", "").length();    // 11
            show("consonants", consonants);
            int letters = lower.replaceAll("[^a-z]", "").length();                   // 14
            show("letters", letters);
        }
        {
            Map<Character, Long> perVowel = "Jazz in New Orleans".toLowerCase(Locale.ROOT).chars()
                    .filter(ch -> "aeiou".indexOf(ch) >= 0)
                    .mapToObj(ch -> (char) ch)
                    .collect(Collectors.groupingBy(ch -> ch, TreeMap::new, Collectors.counting()));
            String counts = perVowel.toString();             // "{a=2, e=2, i=1, o=1}"
            show("counts", counts);
            long missingU = perVowel.getOrDefault('u', 0L);  // 0
            show("missingU", missingU);
        }
        {
            String city = "ISTANBUL";
            long rootVowels = city.toLowerCase(Locale.ROOT).chars()
                    .filter(ch -> "aeiou".indexOf(ch) >= 0).count();
            long turkishVowels = city.toLowerCase(Locale.forLanguageTag("tr")).chars()
                    .filter(ch -> "aeiou".indexOf(ch) >= 0).count();
            long withRoot = rootVowels;                      // 3
            show("withRoot", withRoot);
            long withTurkish = turkishVowels;                // 2
            show("withTurkish", withTurkish);
        }
        {
            String drink = "Caf\u00e9 cr\u00e8me";
            String plain = Normalizer.normalize(drink, Normalizer.Form.NFD).replaceAll("\\p{M}", "");   // "Cafe creme"
            show("plain", plain);
            LetterCount before = countLetters(drink);        // LetterCount[vowels=2, consonants=5]
            show("before", before);
            LetterCount after = countLetters(plain);         // LetterCount[vowels=4, consonants=5]
            show("after", after);
        }
        {
            long read = Syllables.estimate("reading");              // 2
            show("read", read);
            long city = Syllables.estimate("city");                 // 2
            show("city", city);
            long readability = Syllables.estimate("Readability");   // 5
            show("readability", readability);
            long rhythm = Syllables.estimate("rhythm");             // 1
            show("rhythm", rhythm);
        }
        {
            Scanner scanner = new Scanner("Rhythm and Blues 1979");
            String line = scanner.hasNextLine() ? scanner.nextLine() : "";
            LetterCount typed = countLetters(line);          // LetterCount[vowels=3, consonants=11]
            show("typed", typed);
        }
        {
            String text = "Order #42 ships today!";
            int vowels = 0, consonants = 0, digits = 0, spaces = 0, others = 0;
            for (char ch : text.toLowerCase(Locale.ROOT).toCharArray()) {
                if ("aeiou".indexOf(ch) >= 0) vowels++;
                else if (ch >= 'a' && ch <= 'z') consonants++;
                else if (Character.isDigit(ch)) digits++;
                else if (Character.isWhitespace(ch)) spaces++;
                else others++;
            }
            String summary = vowels + " " + consonants + " " + digits + " " + spaces + " " + others;   // "5 10 2 3 2"
            show("summary", summary);
        }
        {
            int recursive = countVowels("Rhythm and Blues 1979");   // 3
            show("recursive", recursive);
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

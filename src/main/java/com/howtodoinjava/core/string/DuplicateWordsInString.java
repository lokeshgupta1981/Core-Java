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
 * Examples for the tutorial "Find Duplicate Words in a String in Java (Count and Remove)".
 * https://howtodoinjava.com/java/string/how-to-find-duplicate-words-in-a-string-in-java/
 */
public class DuplicateWordsInString {
    static Optional<String> firstRepeated(String text) {
        Set<String> seen = new HashSet<>();
        for (String word : text.split("\\s+")) {
            if (!seen.add(word)) {
                return Optional.of(word);
            }
        }
        return Optional.empty();
    }
    public static void main(String[] args) throws Exception {
        {
            String sentence = "alex brian charles alex charles david eric david";
            Map<String, Long> counts = Arrays.stream(sentence.split("\\s+"))
                    .collect(Collectors.groupingBy(w -> w, LinkedHashMap::new, Collectors.counting()));
            // {alex=2, brian=1, charles=2, david=2, eric=1}
            List<String> duplicates = counts.entrySet().stream()
                    .filter(e -> e.getValue() > 1)
                    .map(Map.Entry::getKey)
                    .toList();                             // [alex, charles, david]
        }
        {
            String sentence = "alex brian charles alex charles david eric david";
        }
        {
            String sentence = "alex brian charles alex charles david eric david";
            Set<String> seen = new HashSet<>();
            List<String> duplicateWords = Arrays.stream(sentence.split("\\s+"))
                    .filter(w -> !seen.add(w))
                    .toList();                             // [alex, charles, david]
        }
        {
            String sentence = "alex brian charles alex charles david eric david";
            Map<String, Long> wordCounts = Arrays.stream(sentence.split("\\s+"))
                    .collect(Collectors.groupingBy(w -> w, LinkedHashMap::new, Collectors.counting()));
            // {alex=2, brian=1, charles=2, david=2, eric=1}
            Map<String, Long> dupWordsWithCount = wordCounts.entrySet().stream()
                    .filter(e -> e.getValue() > 1)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> a, LinkedHashMap::new));
            // {alex=2, charles=2, david=2}
        }
        {
            String sentence = "alex brian charles alex charles david eric david";
            Map<String, Integer> wordsMapWithCount = Arrays.stream(sentence.split("\\s+"))
                    .collect(Collectors.toMap(w -> w, w -> 1, Math::addExact, LinkedHashMap::new));
            // {alex=2, brian=1, charles=2, david=2, eric=1}
        }
        {
            String sentence = "alex brian charles alex charles david eric david";
            String[] words = sentence.split("\\s+");
            Set<String> unique = new HashSet<>();
            Set<String> repeated = new LinkedHashSet<>();
            for (String word : words) {
                if (!unique.add(word)) {
                    repeated.add(word);
                }
            }
            String found = repeated.toString();            // "[alex, charles, david]"
            show("found", found);
        }
        {
            String[] words = "alex brian charles alex charles david eric david".split("\\s+");
            Map<String, Integer> frequency = new LinkedHashMap<>();
            for (String word : words) {
                frequency.merge(word, 1, Integer::sum);
            }
            String freq = frequency.toString();            // "{alex=2, brian=1, charles=2, david=2, eric=1}"
            show("freq", freq);
        }
        {
            List<String> wordList = List.of("alex", "brian", "david", "eric", "david");
            int davidCount = Collections.frequency(wordList, "david");  // 2
            show("davidCount", davidCount);
        }
        {
            String review = "Great food, great service. Food was hot!";
            Map<String, Long> reviewCounts = Arrays.stream(review.toLowerCase(Locale.ROOT).split("[^\\p{L}\\p{N}']+"))
                    .filter(w -> !w.isEmpty())
                    .collect(Collectors.groupingBy(w -> w, LinkedHashMap::new, Collectors.counting()));
            // {great=2, food=2, service=1, was=1, hot=1}
        }
        {
            Pattern doubled = Pattern.compile("\\b(\\w+)(\\s+\\1\\b)+", Pattern.CASE_INSENSITIVE);
            String draft = "We ship the the order today. Today today it ships";
            List<String> typos = doubled.matcher(draft).results().map(m -> m.group()).toList();
            // [the the, Today today]
            String fixed = doubled.matcher(draft).replaceAll("$1");  // "We ship the order today. Today it ships"
            show("fixed", fixed);
        }
        {
            String sentence = "alex brian charles alex charles david eric david";
            String deduped = String.join(" ", new LinkedHashSet<>(Arrays.asList(sentence.split("\\s+"))));
            // "alex brian charles david eric"
            String distinctWords = Arrays.stream(sentence.split("\\s+")).distinct().collect(Collectors.joining(" "));
            // "alex brian charles david eric"
        }
        {
            Optional<String> first = firstRepeated("alex brian charles alex charles");  // Optional[alex]
            show("first", first);
            Optional<String> noRepeat = firstRepeated("alex brian");                      // Optional.empty
            show("noRepeat", noRepeat);
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

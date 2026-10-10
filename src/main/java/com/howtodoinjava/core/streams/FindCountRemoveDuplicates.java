package com.howtodoinjava.core.streams;

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

/**
 * Examples for the tutorial "How to Find Duplicates in a Java Stream (Count and Remove)".
 * https://howtodoinjava.com/java8/stream-find-remove-duplicates/
 */
public class FindCountRemoveDuplicates {
    static record Invoice(String number, String customer, double amount) {}
    public static void main(String[] args) throws Exception {
        {
            List<String> cities = List.of("Pune", "Delhi", "Pune", "Goa", "Delhi", "Pune");
            Map<String, Long> counts = cities.stream().collect(Collectors.groupingBy(Function.identity(), LinkedHashMap::new, Collectors.counting()));   // {Pune=3, Delhi=2, Goa=1}
            show("counts", counts);
            List<String> duplicates = counts.entrySet().stream().filter(e -> e.getValue() > 1).map(Map.Entry::getKey).toList();   // [Pune, Delhi]
            show("duplicates", duplicates);
            List<String> withoutDuplicates = cities.stream().distinct().toList();   // [Pune, Delhi, Goa]
            show("withoutDuplicates", withoutDuplicates);
        }
        {
            List<Integer> numbers = List.of(1, 1, 2, 3, 3, 3, 4, 5, 6, 6, 6, 7, 8);
            Map<Integer, Long> inOrder = numbers.stream().collect(Collectors.groupingBy(Function.identity(), LinkedHashMap::new, Collectors.counting()));   // {1=2, 2=1, 3=3, 4=1, 5=1, 6=3, 7=1, 8=1}
            show("inOrder", inOrder);
            Map<Integer, Long> sortedKeys = numbers.stream().collect(Collectors.groupingBy(Function.identity(), TreeMap::new, Collectors.counting()));      // {1=2, 2=1, 3=3, 4=1, 5=1, 6=3, 7=1, 8=1}
            show("sortedKeys", sortedKeys);
            Map<Integer, Long> anyOrder = numbers.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
            long timesSix = anyOrder.get(6);   // 3
            show("timesSix", timesSix);
        }
        {
            List<String> votes = List.of("red", "blue", "red");
            Map<String, Long> tally = votes.stream().collect(Collectors.toMap(Function.identity(), v -> 1L, Long::sum, LinkedHashMap::new));   // {red=2, blue=1}
            show("tally", tally);
        }
        {
            List<String> logins = List.of("ana", "raj", "ana", "lee", "raj", "ana");
            Map<String, Long> perUser = logins.stream().collect(Collectors.groupingBy(Function.identity(), LinkedHashMap::new, Collectors.counting()));
            List<String> repeated = perUser.entrySet().stream().filter(e -> e.getValue() > 1).map(Map.Entry::getKey).toList();   // [ana, raj]
            show("repeated", repeated);
            List<String> once = perUser.entrySet().stream().filter(e -> e.getValue() == 1).map(Map.Entry::getKey).toList();      // [lee]
            show("once", once);
            Map<String, Long> repeatCounts = perUser.entrySet().stream().filter(e -> e.getValue() > 1).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, Long::sum, LinkedHashMap::new));   // {ana=3, raj=2}
            show("repeatCounts", repeatCounts);
        }
        {
            List<String> logins = List.of("ana", "raj", "ana", "lee", "raj", "ana");
            Set<String> seen = new HashSet<>();
            List<String> extraOccurrences = logins.stream().filter(name -> !seen.add(name)).toList();   // [ana, raj, ana]
            show("extraOccurrences", extraOccurrences);
            Set<String> seen2 = new HashSet<>();
            List<String> duplicateNames = logins.stream().filter(name -> !seen2.add(name)).distinct().toList();   // [ana, raj]
            show("duplicateNames", duplicateNames);
        }
        {
            List<Integer> withRepeats = List.of(4, 1, 4, 2, 1);
            List<Integer> distinctList = withRepeats.stream().distinct().toList();                                   // [4, 1, 2]
            show("distinctList", distinctList);
            Set<Integer> orderedSet = withRepeats.stream().collect(Collectors.toCollection(LinkedHashSet::new));      // [4, 1, 2]
            show("orderedSet", orderedSet);
            Set<Integer> plainSet = withRepeats.stream().collect(Collectors.toSet());
            int setSize = plainSet.size();                                                                           // 3
            show("setSize", setSize);
        }
        {
            String word = "programming";
            List<Character> repeatedChars = word.chars().mapToObj(c -> (char) c).collect(Collectors.groupingBy(Function.identity(), LinkedHashMap::new, Collectors.counting())).entrySet().stream().filter(e -> e.getValue() > 1).map(Map.Entry::getKey).toList();   // [r, g, m]
            show("repeatedChars", repeatedChars);
        }
        {
            List<String> monday = List.of("ana", "raj", "lee");
            List<String> tuesday = List.of("lee", "kim", "ana", "lee");
            Set<String> mondaySet = new HashSet<>(monday);
            List<String> bothDays = tuesday.stream().filter(mondaySet::contains).distinct().toList();   // [lee, ana]
            show("bothDays", bothDays);
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

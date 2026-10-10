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
 * Examples for the tutorial "Sort a Map by Values in Java (Ascending, Descending, Top N)".
 * https://howtodoinjava.com/java/sort/java-sort-map-by-values/
 */
public class SortMapByValues {
    static record Product(String name, int priceCents) {

        @Override
        public String toString() {
            return name + "@" + priceCents;
        }
    }
    public static void main(String[] args) throws Exception {
        {
            Map<String, Integer> wordCounts = Map.of("java", 5, "spring", 3, "maven", 8, "gradle", 1);

            Map<String, Integer> ascending = wordCounts.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue())
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> a, LinkedHashMap::new));   // {gradle=1, spring=3, java=5, maven=8}

            Map<String, Integer> descending = wordCounts.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> a, LinkedHashMap::new));   // {maven=8, java=5, spring=3, gradle=1}

            List<String> topTwo = wordCounts.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .limit(2)
                    .map(Map.Entry::getKey)
                    .toList();                                                                                            // [maven, java]
        }
        {
            Comparator<Map.Entry<String, Integer>> byValue = Map.Entry.comparingByValue();
            Comparator<Map.Entry<String, Integer>> byValueDesc = Map.Entry.comparingByValue(Comparator.reverseOrder());
            Comparator<Map.Entry<String, Integer>> byValueDescToo = Map.Entry.<String, Integer>comparingByValue().reversed();
            int firstIsSmaller = byValue.compare(Map.entry("gradle", 1), Map.entry("java", 5));   // -1
            show("firstIsSmaller", firstIsSmaller);
        }
        {
            Map<String, Integer> wordCounts = Map.of("java", 5, "spring", 3, "maven", 8, "gradle", 1);

            LinkedHashMap<String, Integer> sortedMap = wordCounts.entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByValue())
                    .collect(Collectors.toMap(
            Map.Entry::getKey,
            Map.Entry::getValue,
            (oldValue, newValue) -> oldValue, LinkedHashMap::new));   // {gradle=1, spring=3, java=5, maven=8}
        }
        {
            Map<String, Integer> wordCounts = Map.of("java", 5, "spring", 3, "maven", 8, "gradle", 1);

            LinkedHashMap<String, Integer> sortedMap = wordCounts.entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .collect(Collectors.toMap(
            Map.Entry::getKey,
            Map.Entry::getValue,
            (oldValue, newValue) -> oldValue, LinkedHashMap::new));   // {maven=8, java=5, spring=3, gradle=1}
        }
        {
            Map<String, Integer> wordCounts = Map.of("java", 5, "spring", 3, "maven", 8, "gradle", 1);
            LinkedHashMap<String, Integer> sortedMap = wordCounts.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> a, LinkedHashMap::new));
            Map.Entry<String, Integer> most = sortedMap.firstEntry();          // maven=8
            show("most", most);
            Map.Entry<String, Integer> least = sortedMap.lastEntry();          // gradle=1
            show("least", least);
            SequencedMap<String, Integer> ascending = sortedMap.reversed();    // {gradle=1, spring=3, java=5, maven=8}
            show("ascending", ascending);
        }
        {
            Map<String, Integer> wordCounts = Map.of("java", 5, "kotlin", 5, "maven", 8, "ant", 1);
            Comparator<Map.Entry<String, Integer>> byCountThenWord = Map.Entry.<String, Integer>comparingByValue(Comparator.reverseOrder())
                    .thenComparing(Map.Entry.comparingByKey());

            LinkedHashMap<String, Integer> sortedMap = wordCounts.entrySet().stream()
                    .sorted(byCountThenWord)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> a, LinkedHashMap::new));   // {maven=8, java=5, kotlin=5, ant=1}
        }
        {
            List<String> searchLog = List.of("shoes", "bag", "shoes", "hat", "bag", "shoes", "belt", "hat", "bag", "scarf");
            Map<String, Long> counts = searchLog.stream().collect(Collectors.groupingBy(term -> term, Collectors.counting()));

            List<Map.Entry<String, Long>> topThree = counts.entrySet().stream()
                    .sorted(Map.Entry.<String, Long>comparingByValue().reversed().thenComparing(Map.Entry.comparingByKey()))
                    .limit(3)
                    .toList();                                                   // [bag=3, shoes=3, hat=2]
        }
        {
            Map<String, Product> catalog = Map.of(
            "p1", new Product("mug", 1200),
            "p2", new Product("pen", 250),
            "p3", new Product("lamp", 3900));

            List<String> cheapestFirst = catalog.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.comparingInt(Product::priceCents)))
                    .map(Map.Entry::getKey)
                    .toList();                                                   // [p2, p1, p3]

            LinkedHashMap<String, Product> byName = catalog.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.comparing(Product::name)))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> a, LinkedHashMap::new));   // {p3=lamp@3900, p1=mug@1200, p2=pen@250}
        }
        {
            Map<String, Integer> prices = new HashMap<>();
            prices.put("mug", 12);
            prices.put("lamp", null);
            prices.put("pen", 3);

            LinkedHashMap<String, Integer> broken = prices.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.nullsLast(Comparator.naturalOrder())))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> a, LinkedHashMap::new));   // NullPointerException
        }
        {
            Map<String, Integer> prices = new HashMap<>();
            prices.put("mug", 12);
            prices.put("lamp", null);
            prices.put("pen", 3);

            LinkedHashMap<String, Integer> sortedPrices = new LinkedHashMap<>();
            prices.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.nullsLast(Comparator.naturalOrder())))
                    .forEachOrdered(e -> sortedPrices.put(e.getKey(), e.getValue()));
            LinkedHashMap<String, Integer> result = sortedPrices;              // {pen=3, mug=12, lamp=null}
            show("result", result);
        }
        {
            Map<String, Integer> wordCounts = Map.of("java", 5, "kotlin", 5, "maven", 8);
            TreeMap<String, Integer> byValue = new TreeMap<>(Comparator.comparing(wordCounts::get));
            byValue.putAll(wordCounts);
            int size = byValue.size();                                         // 2
            show("size", size);
        }
        {
            Map<String, Integer> wordCounts = Map.of("java", 5, "spring", 3, "maven", 8, "gradle", 1);
            String mostFrequent = wordCounts.entrySet().stream().max(Map.Entry.comparingByValue()).map(Map.Entry::getKey).orElseThrow();   // "maven"
            show("mostFrequent", mostFrequent);
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

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
 * Examples for the tutorial "Sort a Map by Keys in Java: TreeMap and Streams".
 * https://howtodoinjava.com/java/sort/java-sort-map-by-key/
 */
public class SortMapByKeys {

    public static void main(String[] args) throws Exception {
        {
            Map<String, Integer> settings = Map.of("timeout", 30, "port", 8080, "retries", 3, "cache", 64);

            Map<String, Integer> treeAsc = new TreeMap<>(settings);                                     // {cache=64, port=8080, retries=3, timeout=30}
            show("treeAsc", treeAsc);
            Map<String, Integer> treeDesc = new TreeMap<>(Comparator.reverseOrder());
            treeDesc.putAll(settings);
            Map<String, Integer> descending = treeDesc;                                                 // {timeout=30, retries=3, port=8080, cache=64}
            show("descending", descending);

            Map<String, Integer> streamAsc = settings.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> a, LinkedHashMap::new));   // {cache=64, port=8080, retries=3, timeout=30}
        }
        {
            Map<String, Integer> settings = Map.of("timeout", 30, "port", 8080, "retries", 3, "cache", 64);
            Map<String, Integer> sortedTreeMap = new TreeMap<>(settings);   // {cache=64, port=8080, retries=3, timeout=30}
            show("sortedTreeMap", sortedTreeMap);
        }
        {
            Map<String, Integer> settings = Map.of("timeout", 30, "port", 8080, "retries", 3, "cache", 64);
            Map<String, Integer> sortedTreeMap = new TreeMap<>(Comparator.reverseOrder());
            sortedTreeMap.putAll(settings);
            Map<String, Integer> reverse = sortedTreeMap;                   // {timeout=30, retries=3, port=8080, cache=64}
            show("reverse", reverse);

            TreeMap<String, Integer> ascending = new TreeMap<>(settings);
            NavigableMap<String, Integer> view = ascending.descendingMap();   // {timeout=30, retries=3, port=8080, cache=64}
            show("view", view);
            SequencedMap<String, Integer> reversedView = ascending.reversed();   // {timeout=30, retries=3, port=8080, cache=64}
            show("reversedView", reversedView);
        }
        {
            Comparator<Map.Entry<String, Integer>> byKey = Map.Entry.comparingByKey();
            int cacheFirst = byKey.compare(Map.entry("cache", 64), Map.entry("port", 8080));   // -13
            show("cacheFirst", cacheFirst);
        }
        {
            Map<String, Integer> settings = Map.of("timeout", 30, "port", 8080, "retries", 3, "cache", 64);

            LinkedHashMap<String, Integer> sortedMap = settings.entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByKey())
                    .collect(Collectors.toMap(
            Map.Entry::getKey,
            Map.Entry::getValue,
            (oldValue, newValue) -> oldValue, LinkedHashMap::new));   // {cache=64, port=8080, retries=3, timeout=30}
        }
        {
            Map<String, Integer> settings = Map.of("timeout", 30, "port", 8080, "retries", 3, "cache", 64);

            LinkedHashMap<String, Integer> sortedMap = settings.entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByKey(Comparator.reverseOrder()))
                    .collect(Collectors.toMap(
            Map.Entry::getKey,
            Map.Entry::getValue,
            (oldValue, newValue) -> oldValue, LinkedHashMap::new));   // {timeout=30, retries=3, port=8080, cache=64}
        }
        {
            Map<String, Integer> settings = Map.of("timeout", 30, "port", 8080, "retries", 3, "cache", 64);
            TreeMap<String, Integer> tree = new TreeMap<>(settings);
            LinkedHashMap<String, Integer> snapshot = settings.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> a, LinkedHashMap::new));
            tree.put("host", 1);
            snapshot.put("host", 1);
            Map<String, Integer> treeAfter = tree;                          // {cache=64, host=1, port=8080, retries=3, timeout=30}
            show("treeAfter", treeAfter);
            Map<String, Integer> snapshotAfter = snapshot;                  // {cache=64, port=8080, retries=3, timeout=30, host=1}
            show("snapshotAfter", snapshotAfter);
        }
        {
            Map<String, String> headers = Map.of("Host", "shop.dev", "accept", "json", "Content-Type", "text");
            Map<String, String> natural = new TreeMap<>(headers);                                  // {Content-Type=text, Host=shop.dev, accept=json}
            show("natural", natural);
            Map<String, String> ignoreCase = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
            ignoreCase.putAll(headers);
            Map<String, String> sortedHeaders = ignoreCase;                                        // {accept=json, Content-Type=text, Host=shop.dev}
            show("sortedHeaders", sortedHeaders);
        }
        {
            Map<String, String> pages = Map.of("10", "index", "9", "faq", "2", "about", "1", "home");
            Map<String, String> asText = new TreeMap<>(pages);                                     // {1=home, 10=index, 2=about, 9=faq}
            show("asText", asText);
            Map<String, String> asNumbers = new TreeMap<>(Comparator.comparingInt(Integer::parseInt));
            asNumbers.putAll(pages);
            Map<String, String> sortedPages = asNumbers;                                           // {1=home, 2=about, 9=faq, 10=index}
            show("sortedPages", sortedPages);
        }
        {
            Map<String, Integer> withNullKey = new HashMap<>();
            withNullKey.put("port", 8080);
            withNullKey.put(null, 0);
            withNullKey.put("cache", 64);

            try { Map<String, Integer> broken = new TreeMap<>(withNullKey); show("broken", broken); } catch (Throwable _t) { System.out.println("broken -> " + _t); }
            Map<String, Integer> nullsFirst = new TreeMap<>(Comparator.nullsFirst(Comparator.naturalOrder()));
            nullsFirst.putAll(withNullKey);
            Map<String, Integer> sortedWithNull = nullsFirst;                                      // {null=0, cache=64, port=8080}
            show("sortedWithNull", sortedWithNull);
        }
        {
            Map<String, Integer> settings = Map.of("timeout", 30, "port", 8080, "retries", 3, "cache", 64);
            List<String> keys = settings.keySet().stream().sorted().toList();                      // [cache, port, retries, timeout]
            show("keys", keys);
            List<String> keysDesc = settings.keySet().stream().sorted(Comparator.reverseOrder()).toList();   // [timeout, retries, port, cache]
            show("keysDesc", keysDesc);
            TreeSet<String> keySet = new TreeSet<>(settings.keySet());                             // [cache, port, retries, timeout]
            show("keySet", keySet);
            String firstKey = keySet.first();                                                      // "cache"
            show("firstKey", firstKey);
        }
        {
            Map<String, Integer> settings = new HashMap<>(Map.of("timeout", 30, "port", 8080, "retries", 3, "cache", 64));
            String logLine = new TreeMap<>(settings).entrySet().stream()
                    .map(e -> e.getKey() + "=" + e.getValue())
                    .collect(Collectors.joining(", ", "config: ", ""));   // "config: cache=64, port=8080, retries=3, timeout=30"
        }
        {
            TreeMap<String, Integer> sorted = new TreeMap<>(Map.of("timeout", 30, "port", 8080, "cache", 64));
            String first = sorted.firstKey();                                  // "cache"
            show("first", first);
            Map.Entry<String, Integer> last = sorted.lastEntry();              // timeout=30
            show("last", last);
        }
        {
            Map<String, Integer> settings = Map.of("timeout", 30, "port", 8080, "retries", 3, "cache", 64);
            Map<String, Integer> byLength = new TreeMap<>(Comparator.comparingInt(String::length).thenComparing(Comparator.naturalOrder()));
            byLength.putAll(settings);
            Map<String, Integer> sortedByLength = byLength;                    // {port=8080, cache=64, retries=3, timeout=30}
            show("sortedByLength", sortedByLength);
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

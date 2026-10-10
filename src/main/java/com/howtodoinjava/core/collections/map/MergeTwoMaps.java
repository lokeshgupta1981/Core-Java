package com.howtodoinjava.core.collections.map;

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
 * Examples for the tutorial "Merge Two Maps in Java: putAll(), merge() and Streams".
 * https://howtodoinjava.com/java/collections/hashmap/merge-two-hashmaps/
 */
public class MergeTwoMaps {

    public static void main(String[] args) throws Exception {
        {
            Map<String, Integer> berlin = Map.of("pen", 5, "ink", 2);
            Map<String, Integer> munich = Map.of("ink", 3, "pad", 4);

            Map<String, Integer> overwrite = new TreeMap<>(berlin);
            overwrite.putAll(munich);
            Map<String, Integer> lastWins = overwrite;                    // {ink=3, pad=4, pen=5}
            show("lastWins", lastWins);

            Map<String, Integer> keepFirst = new TreeMap<>(berlin);
            munich.forEach(keepFirst::putIfAbsent);
            Map<String, Integer> firstWins = keepFirst;                   // {ink=2, pad=4, pen=5}
            show("firstWins", firstWins);

            Map<String, Integer> totals = new TreeMap<>(berlin);
            munich.forEach((product, sold) -> totals.merge(product, sold, Integer::sum));
            Map<String, Integer> summed = totals;                         // {ink=5, pad=4, pen=5}
            show("summed", summed);

            Map<String, Integer> viaStream = Stream.concat(berlin.entrySet().stream(), munich.entrySet().stream()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, Integer::sum, TreeMap::new));   // {ink=5, pad=4, pen=5}
            show("viaStream", viaStream);
        }
        {
            Map<String, String> defaults = Map.of("theme", "light", "lang", "en");
            Map<String, String> userPrefs = Map.of("theme", "dark");
            Map<String, String> settings = new TreeMap<>(defaults);
            settings.putAll(userPrefs);
            Map<String, String> effective = settings;                     // {lang=en, theme=dark}
            show("effective", effective);
        }
        {
            Map<String, String> defaults = Map.of("theme", "light", "lang", "en");
            Map<String, String> userPrefs = Map.of("theme", "dark");
            Map<String, String> settings = new TreeMap<>(userPrefs);
            defaults.forEach(settings::putIfAbsent);
            Map<String, String> effective = settings;                     // {lang=en, theme=dark}
            show("effective", effective);
        }
        {
            Map<String, Integer> berlin = Map.of("pen", 5, "ink", 2);
            Map<String, Integer> munich = Map.of("ink", 3, "pad", 4);
            Map<String, Integer> totals = new TreeMap<>(berlin);
            munich.forEach((product, sold) -> totals.merge(product, sold, Integer::sum));
            Map<String, Integer> result = totals;                         // {ink=5, pad=4, pen=5}
            show("result", result);
        }
        {
            Map<String, String> notes = new TreeMap<>(Map.of("ink", "blue"));
            notes.merge("ink", "black", (oldValue, newValue) -> oldValue + ", " + newValue);
            notes.merge("pen", "red", (oldValue, newValue) -> oldValue + ", " + newValue);
            Map<String, String> joined = notes;                           // {ink=blue, black, pen=red}
            show("joined", joined);

            Map<String, Integer> best = new TreeMap<>(Map.of("ink", 2));
            best.merge("ink", 3, Integer::max);
            Integer highest = best.get("ink");                            // 3
            show("highest", highest);
        }
        {
            Map<String, Integer> stock = new TreeMap<>(Map.of("ink", 3, "pen", 5));
            stock.merge("ink", -3, (have, change) -> have + change == 0 ? null : have + change);
            Map<String, Integer> afterSale = stock;                       // {pen=5}
            show("afterSale", afterSale);
            try { Integer failed = stock.merge("pad", null, Integer::sum); show("failed", failed); } catch (Throwable _t) { System.out.println("failed -> " + _t); }
        }
        {
            Map<String, Integer> berlin = Map.of("pen", 5, "ink", 2);
            Map<String, Integer> munich = Map.of("ink", 3, "pad", 4);
            Map<String, Integer> summed = Stream.concat(berlin.entrySet().stream(), munich.entrySet().stream()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, Integer::sum, TreeMap::new));   // {ink=5, pad=4, pen=5}
            show("summed", summed);
            Map<String, Integer> lastWins = Stream.concat(berlin.entrySet().stream(), munich.entrySet().stream()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (first, second) -> second, TreeMap::new));   // {ink=3, pad=4, pen=5}
            show("lastWins", lastWins);
        }
        {
            Map<String, Integer> berlin = Map.of("pen", 5, "ink", 2);
            Map<String, Integer> munich = Map.of("ink", 3, "pad", 4);
            try { Map<String, Integer> strict = Stream.concat(berlin.entrySet().stream(), munich.entrySet().stream()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)); show("strict", strict); } catch (Throwable _t) { System.out.println("strict -> " + _t); }
        }
        {
            Map<String, Integer> berlin = Map.of("pen", 5, "ink", 2);
            Map<String, Integer> munich = Map.of("ink", 3, "pad", 4);
            Map<String, Integer> hamburg = Map.of("pen", 1);
            Map<String, Integer> all = Stream.of(berlin, munich, hamburg).flatMap(m -> m.entrySet().stream()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, Integer::sum, TreeMap::new));   // {ink=5, pad=4, pen=6}
            show("all", all);
        }
        {
            List<Map<String, Integer>> stores = List.of(Map.of("pen", 5, "ink", 2), Map.of("ink", 3, "pad", 4), Map.of("pen", 1));
            Map<String, Integer> totals = new TreeMap<>();
            for (Map<String, Integer> store : stores) {
                store.forEach((product, sold) -> totals.merge(product, sold, Integer::sum));
            }
            Map<String, Integer> result = totals;                         // {ink=5, pad=4, pen=6}
            show("result", result);
        }
        {
            Map<String, List<String>> editor = Map.of("posts", List.of("read", "write"));
            Map<String, List<String>> reviewer = Map.of("posts", List.of("read", "approve"), "users", List.of("read"));
            Map<String, List<String>> permissions = new TreeMap<>(editor);
            reviewer.forEach((resource, actions) -> permissions.merge(resource, actions, (a, b) -> Stream.concat(a.stream(), b.stream()).distinct().toList()));
            Map<String, List<String>> merged = permissions;               // {posts=[read, write, approve], users=[read]}
            show("merged", merged);
        }
        {
            Map<String, Integer> berlin = Map.of("pen", 5, "ink", 2);
            Map<String, Integer> munich = Map.of("ink", 3, "pad", 4);
            Map<String, List<Integer>> perStore = Stream.of(berlin, munich).flatMap(m -> m.entrySet().stream()).collect(Collectors.groupingBy(Map.Entry::getKey, TreeMap::new, Collectors.mapping(Map.Entry::getValue, Collectors.toList())));   // {ink=[2, 3], pad=[4], pen=[5]}
            show("perStore", perStore);
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

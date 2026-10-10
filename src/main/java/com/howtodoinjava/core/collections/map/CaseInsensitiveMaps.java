package com.howtodoinjava.core.collections.map;

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
 * Examples for the tutorial "Java Case-Insensitive Map: TreeMap, Commons and Spring".
 * https://howtodoinjava.com/java/collections/case-insensitive-maps/
 */
public class CaseInsensitiveMaps {
    private static String key(String name) {
        return name.toLowerCase(Locale.ROOT);
    }
    public static void main(String[] args) throws Exception {
        {
            Map<String, Integer> ages = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
            ages.put("Lokesh", 37);
            ages.put("LOKESH", 38);                           // replaces 37, same key

            Integer age = ages.get("lokesh");                 // 38
            show("age", age);
            boolean found = ages.containsKey("LoKeSh");       // true
            show("found", found);
            int size = ages.size();                           // 1
            show("size", size);
            String keys = ages.toString();                    // "{Lokesh=38}"
            show("keys", keys);
        }
        {
            Map<String, Integer> hashMap = new HashMap<>();
            hashMap.put("Lokesh", 37);
            hashMap.put("lokesh", 38);

            int entries = hashMap.size();                     // 2
            show("entries", entries);
            Integer missing = hashMap.get("LOKESH");          // null
            show("missing", missing);
        }
        {
            TreeMap<String, Integer> treeMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
            treeMap.put("Lokesh", 37);
            treeMap.put("alex", 30);
            treeMap.put("Bob", 25);
            treeMap.put("LOKESH", 38);

            String sorted = treeMap.toString();               // "{alex=30, Bob=25, Lokesh=38}"
            show("sorted", sorted);
            Integer removed = treeMap.remove("ALEX");         // 30
            show("removed", removed);
            String first = treeMap.firstKey();                // "Bob"
            show("first", first);
            try { Integer nullKey = treeMap.put(null, 0); show("nullKey", nullKey); } catch (Throwable _t) { System.out.println("nullKey -> " + _t); }
        }
        {
            Map<String, Integer> normalized = new HashMap<>();
            normalized.put(key("Lokesh"), 37);
            normalized.put(key("LOKESH"), 38);                // replaces 37
            Integer age = normalized.get(key("lokesh"));      // 38
            show("age", age);
            String content = normalized.toString();           // "{lokesh=38}"
            show("content", content);
        }
        {
            Map<String, Integer> source = new HashMap<>();
            source.put("Lokesh", 37);
            source.put("LOKESH", 38);

            Map<String, Integer> converted = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
            converted.putAll(source);

            int before = source.size();                       // 2
            show("before", before);
            int after = converted.size();                     // 1, one value is lost
            show("after", after);
        }
        {
            Map<String, Integer> ciMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
            ciMap.put("Lokesh", 37);
            Map<String, Integer> plain = Map.of("LOKESH", 37);

            boolean ciEqualsPlain = ciMap.equals(plain);      // false
            show("ciEqualsPlain", ciEqualsPlain);
            boolean plainEqualsCi = plain.equals(ciMap);      // true
            show("plainEqualsCi", plainEqualsCi);
        }
        {
            Set<String> names = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
            names.add("Lokesh");
            boolean added = names.add("LOKESH");              // false, already present
            show("added", added);
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

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

import java.util.Objects;

/**
 * Examples for the tutorial "How HashMap Works Internally in Java: Buckets, Trees, Resize".
 * https://howtodoinjava.com/java/collections/hashmap/how-hashmap-works-in-java/
 */
public class HashMapInternals {
    static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }
    static record Cell(int x, int y) {
        @Override
        public int hashCode() {
            return x + y;                        // poor: (1, 2) and (2, 1) collide
        }
    }
    public static void main(String[] args) throws Exception {
        {
            Map<String, Integer> scores = new HashMap<>();
            Integer first = scores.put("alex", 40);      // null, the table of 16 buckets is created here
            show("first", first);
            Integer old = scores.put("alex", 55);        // 40, same key, so the value is replaced
            show("old", old);
            Integer score = scores.get("alex");          // 55
            show("score", score);
            int hash = "alex".hashCode();                // 2996766
            show("hash", hash);
            int spread = hash ^ (hash >>> 16);           // 2996787
            show("spread", spread);
            int bucket = spread & (16 - 1);              // 3, the bucket for "alex"
            show("bucket", bucket);
        }
        {
            Map<String, Integer> scores = new HashMap<>();
            Integer added = scores.put("maria", 70);     // null, no previous value
            show("added", added);
            Integer found = scores.get("maria");         // 70
            show("found", found);
            Integer missing = scores.get("sam");         // null, no such key
            show("missing", missing);
            int size = scores.size();                    // 1
            show("size", size);
        }
        {
            int h = 65536;                       // 65536 is 0x10000, only bit 16 is set
            show("h", h);
            int rawIndex = h & (16 - 1);         // 0, the high bit is ignored
            show("rawIndex", rawIndex);
            int spread = h ^ (h >>> 16);         // 65537
            show("spread", spread);
            int index = spread & (16 - 1);       // 1, the high bit now changes the bucket
            show("index", index);
        }
        {
            Map<String, Integer> codes = new HashMap<>();
            int h1 = "Aa".hashCode();                    // 2112
            show("h1", h1);
            int h2 = "BB".hashCode();                    // 2112
            show("h2", h2);
            Integer a = codes.put("Aa", 1);              // null
            show("a", a);
            Integer b = codes.put("BB", 2);              // null, a second node in the same bucket
            show("b", b);
            Integer readA = codes.get("Aa");             // 1
            show("readA", readA);
            Integer readB = codes.get("BB");             // 2
            show("readB", readB);
            int count = codes.size();                    // 2
            show("count", count);
        }
        {
            Map<String, Integer> scores = new HashMap<>(Map.of("alex", 55, "maria", 70));
            Integer alex = scores.get("alex");                     // 55
            show("alex", alex);
            Integer nobody = scores.get("sam");                    // null
            show("nobody", nobody);
            Integer orZero = scores.getOrDefault("sam", 0);        // 0
            show("orZero", orZero);
            boolean hasSam = scores.containsKey("sam");            // false
            show("hasSam", hasSam);
        }
        {
            List<Cell> cells = IntStream.range(0, 10_000).mapToObj(i -> new Cell(i / 100, i % 100)).toList();
            long badCodes = cells.stream().map(Cell::hashCode).distinct().count();                         // 199
            show("badCodes", badCodes);
            long betterCodes = cells.stream().map(c -> Objects.hash(c.x(), c.y())).distinct().count();      // 3169
            show("betterCodes", betterCodes);
        }
        {
            Map<String, Integer> sized = new HashMap<>(10);   // table of 16 buckets on the first put
            show("sized", sized);
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

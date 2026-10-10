package com.howtodoinjava.core.collections.compare;

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
 * Examples for the tutorial "Java Comparable Interface: compareTo() and Natural Ordering".
 * https://howtodoinjava.com/java/collections/java-comparable-interface/
 */
public class ComparableExamples {
    static record Version(int major, int minor, int patch) implements Comparable<Version> {

        @Override
        public int compareTo(Version other) {
            int result = Integer.compare(major, other.major);
            if (result == 0) {
                result = Integer.compare(minor, other.minor);
            }
            if (result == 0) {
                result = Integer.compare(patch, other.patch);
            }
            return result;
        }

        @Override
        public String toString() {
            return major + "." + minor + "." + patch;
        }
    }
    static record Release(int major, int minor, int patch) implements Comparable<Release> {

        private static final Comparator<Release> ORDER = Comparator
                .comparingInt(Release::major)
                .thenComparingInt(Release::minor)
                .thenComparingInt(Release::patch);

        @Override
        public int compareTo(Release other) {
            return ORDER.compare(this, other);
        }
    }
    static record Tag(String name) {}
    public static void main(String[] args) throws Exception {
        {
            int byName = "apple".compareTo("banana");          // -1
            show("byName", byName);
            int byValue = Integer.valueOf(7).compareTo(3);      // 1
            show("byValue", byValue);
            List<String> fruits = new ArrayList<>(List.of("cherry", "apple", "banana"));
            Collections.sort(fruits);
            List<String> sorted = fruits;                       // [apple, banana, cherry]
            show("sorted", sorted);
            TreeSet<Integer> scores = new TreeSet<>(List.of(42, 7, 19));
            Integer lowest = scores.first();                    // 7
            show("lowest", lowest);
        }
        {
            List<String> tags = new ArrayList<>(List.of("2.0.0", "1.9.2", "1.10.0"));
            Collections.sort(tags);
            List<String> asText = tags;                         // [1.10.0, 1.9.2, 2.0.0]
            show("asText", asText);
        }
        {
            List<Version> releases = new ArrayList<>(List.of(new Version(2, 0, 0), new Version(1, 9, 2), new Version(1, 10, 0)));
            Collections.sort(releases);
            List<Version> ordered = releases;                   // [1.9.2, 1.10.0, 2.0.0]
            show("ordered", ordered);
            int newer = new Version(1, 10, 0).compareTo(new Version(1, 9, 2));   // 1
            show("newer", newer);
        }
        {
            int cmp = new Release(3, 1, 0).compareTo(new Release(3, 0, 9));   // 1
            show("cmp", cmp);
            int same = new Release(3, 1, 0).compareTo(new Release(3, 1, 0));  // 0
            show("same", same);
        }
        {
            int min = Integer.MIN_VALUE;
            int bySubtraction = min - 1;                        // 2147483647
            show("bySubtraction", bySubtraction);
            int byCompare = Integer.compare(min, 1);            // -1
            show("byCompare", byCompare);
        }
        {
            BigDecimal price = new BigDecimal("1.0");
            BigDecimal samePrice = new BigDecimal("1.00");
            boolean equal = price.equals(samePrice);            // false
            show("equal", equal);
            int order = price.compareTo(samePrice);             // 0
            show("order", order);
            int hashSize = new HashSet<>(List.of(price, samePrice)).size();   // 2
            show("hashSize", hashSize);
            int treeSize = new TreeSet<>(List.of(price, samePrice)).size();   // 1
            show("treeSize", treeSize);
        }
        {
            try { int withNull = "apple".compareTo(null); show("withNull", withNull); } catch (Throwable _t) { System.out.println("withNull -> " + _t); }
            TreeSet<Tag> labels = new TreeSet<>();
            try { boolean added = labels.add(new Tag("java")); show("added", added); } catch (Throwable _t) { System.out.println("added -> " + _t); }
        }
        {
            List<Version> releases = List.of(new Version(2, 0, 0), new Version(1, 9, 2), new Version(1, 10, 0));
            List<Version> ascending = releases.stream().sorted().toList();        // [1.9.2, 1.10.0, 2.0.0]
            show("ascending", ascending);
            List<Version> descending = releases.stream().sorted(Comparator.reverseOrder()).toList();   // [2.0.0, 1.10.0, 1.9.2]
            show("descending", descending);
            Version latest = Collections.max(releases);                           // 2.0.0
            show("latest", latest);
            TreeMap<Version, String> notes = new TreeMap<>(Map.of(new Version(1, 10, 0), "dark mode", new Version(1, 9, 2), "bug fixes"));
            Version firstKey = notes.firstKey();                                  // 1.9.2
            show("firstKey", firstKey);
        }
        {
            List<Version> releases = new ArrayList<>(List.of(new Version(2, 0, 3), new Version(1, 9, 2), new Version(1, 10, 0)));
            releases.sort(Comparator.comparingInt(Version::patch));
            List<Version> byPatch = releases;                   // [1.10.0, 1.9.2, 2.0.3]
            show("byPatch", byPatch);
        }
        {
            DayOfWeek monday = DayOfWeek.MONDAY;
            int dayOrder = monday.compareTo(DayOfWeek.FRIDAY);  // -4
            show("dayOrder", dayOrder);
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

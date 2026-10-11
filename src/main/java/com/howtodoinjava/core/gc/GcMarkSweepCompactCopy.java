package com.howtodoinjava.core.gc;

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
import java.lang.management.*;

/**
 * Examples for the tutorial "Java Garbage Collection Algorithms and Collectors (Java 25)".
 * https://howtodoinjava.com/java/garbage-collection/revisiting-memory-management-and-garbage-collection-mechanisms-in-java/
 */
public class GcMarkSweepCompactCopy {
    static public class Node {
        Node next;
    }
    static public class RecentSearches<K, V> extends LinkedHashMap<K, V> {

        private final int maxEntries;

        public RecentSearches(int maxEntries) {
            super(16, 0.75f, true);
            this.maxEntries = maxEntries;
        }

        @Override
        protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
            return size() > maxEntries;
        }
    }
    public static void main(String[] args) throws Exception {
        {
            List<String> collectors = ManagementFactory.getGarbageCollectorMXBeans().stream().map(GarbageCollectorMXBean::getName).toList();   // [G1 Young Generation, G1 Concurrent GC, G1 Old Generation]
            show("collectors", collectors);
            long youngCollections = ManagementFactory.getGarbageCollectorMXBeans().get(0).getCollectionCount();                                 // collections so far, 0 right after startup
            show("youngCollections", youngCollections);
        }
        {
            Node first = new Node();
            Node second = new Node();
            first.next = second;
            second.next = first;
            first = null;
            second = null;
        }
        {
            RecentSearches<String, Integer> searches = new RecentSearches<>(2);
            searches.put("apple", 5);
            searches.put("banana", 3);
            searches.put("cherry", 7);
            int size = searches.size();                               // 2
            show("size", size);
            boolean hasApple = searches.containsKey("apple");         // false
            show("hasApple", hasApple);
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

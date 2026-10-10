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

import java.lang.ref.*;

/**
 * Examples for the tutorial "Java WeakHashMap: Weak Keys, GC Behavior and Use Cases".
 * https://howtodoinjava.com/java/collections/java-weakhashmap/
 */
public class WeakHashMapExamples {
    static record Profile(Object owner, String name) {}
    static final class Document {
        private final String text;
        Document(String text) { this.text = text; }
        String text() { return text; }
    }
    static int wordCount(Document doc) {
        return WORD_COUNTS.computeIfAbsent(doc, d -> d.text().split("\\s+").length);
    }

    static final Map<Document, Integer> WORD_COUNTS =
    Collections.synchronizedMap(new WeakHashMap<>());
    public static void main(String[] args) throws Exception {
        {
            Map<Object, String> labels = new WeakHashMap<>();
            Object session = new Object();
            labels.put(session, "lokesh");

            String label = labels.get(session);        // "lokesh"
            show("label", label);
            int before = labels.size();                // 1
            show("before", before);

            session = null;                            // the key is no longer strongly reachable
            System.gc();                               // only a request to the JVM
            int after = labels.size();                 // 0 once the GC has cleared the key, else still 1
            show("after", after);
        }
        {
            Map<String, Integer> empty = new WeakHashMap<>();                       // 16 buckets, load factor 0.75
            show("empty", empty);
            Map<String, Integer> sized = new WeakHashMap<>(64);                     // 64 buckets
            show("sized", sized);
            Map<String, Integer> forHundred = WeakHashMap.newWeakHashMap(100);      // room for 100 mappings without resizing
            show("forHundred", forHundred);
            Map<String, Integer> copy = new WeakHashMap<>(Map.of("apple", 5));      // copies the entries
            show("copy", copy);
        }
        {
            Map<String, Integer> stock = new WeakHashMap<>();
            stock.put("apple", 5);
            stock.put("banana", 3);
            stock.merge("apple", 2, Integer::sum);

            Integer apples = stock.get("apple");                    // 7
            show("apples", apples);
            Integer pears = stock.getOrDefault("pear", 0);          // 0
            show("pears", pears);
            boolean hasBanana = stock.containsKey("banana");        // true
            show("hasBanana", hasBanana);
            boolean hasNine = stock.containsValue(9);               // false
            show("hasNine", hasNine);
            Integer removed = stock.remove("banana");               // 3
            show("removed", removed);
            int size = stock.size();                                // 1
            show("size", size);
        }
        {
            Map<Object, String> owners = new WeakHashMap<>();
            Object kept = new Object();
            Object dropped = new Object();
            owners.put(kept, "alex");
            owners.put(dropped, "bob");

            dropped = null;                                         // only the map refers to the second key
            System.gc();
            boolean keptPresent = owners.containsKey(kept);         // true, kept is still strongly reachable
            show("keptPresent", keptPresent);
            int remaining = owners.size();                          // 1 after the GC clears the dropped key, 2 before
            show("remaining", remaining);
        }
        {
            Map<Object, Profile> leaking = new WeakHashMap<>();
            Object user = new Object();
            leaking.put(user, new Profile(user, "lokesh"));         // the value points back to the key
            user = null;
            System.gc();
            int leaked = leaking.size();                            // stays 1, the value keeps the key alive
            show("leaked", leaked);
        }
        {
            Map<Object, WeakReference<Profile>> safe = new WeakHashMap<>();
            Object owner = new Object();
            Profile profile = new Profile(owner, "lokesh");
            safe.put(owner, new WeakReference<>(profile));

            WeakReference<Profile> ref = safe.get(owner);
            Profile found = ref == null ? null : ref.get();
            String name = found != null ? found.name() : "unknown";   // "lokesh"
            show("name", name);
        }
        {
            Document tab = new Document("weak keys are cleared by the collector");
            int words = wordCount(tab);                             // 7
            show("words", words);
            int cached = WORD_COUNTS.size();                        // 1, until the editor drops the tab
            show("cached", cached);
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

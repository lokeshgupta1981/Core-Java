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
 * Examples for the tutorial "Java IdentityHashMap: Reference Equality vs HashMap".
 * https://howtodoinjava.com/java/collections/java-identityhashmap/
 */
public class IdentityHashMapExamples {
    static record Task(String name, List<Task> dependsOn) {}
    static int countReachable(Task start) {
        Set<Task> visited = Collections.newSetFromMap(new IdentityHashMap<>());
        Deque<Task> todo = new ArrayDeque<>(List.of(start));
        while (!todo.isEmpty()) {
            Task task = todo.pop();
            if (visited.add(task)) {
                todo.addAll(task.dependsOn());
            }
        }
        return visited.size();
    }
    public static void main(String[] args) throws Exception {
        {
            String first = new String("lokesh");
            String second = new String("lokesh");

            Map<String, Integer> byIdentity = new IdentityHashMap<>();
            byIdentity.put(first, 1);
            byIdentity.put(second, 2);

            int size = byIdentity.size();                     // 2, two different objects
            show("size", size);
            Integer one = byIdentity.get(first);              // 1
            show("one", one);
            Integer literal = byIdentity.get("lokesh");       // null, the literal is a third object
            show("literal", literal);
            int merged = new HashMap<>(byIdentity).size();    // 1, equals() sees one key
            show("merged", merged);
        }
        {
            Map<String, Integer> empty = new IdentityHashMap<>();                    // expected maximum size 21
            show("empty", empty);
            Map<String, Integer> sized = new IdentityHashMap<>(100);                 // expected maximum size 100
            show("sized", sized);
            Map<String, Integer> copy = new IdentityHashMap<>(Map.of("apple", 5));   // copies the entries
            show("copy", copy);
        }
        {
            Map<String, Integer> stock = new IdentityHashMap<>();
            stock.put("apple", 5);
            stock.put("banana", 3);
            stock.merge("apple", 2, Integer::sum);
            stock.put(null, 0);                                      // null key is allowed

            Integer apples = stock.get("apple");                     // 7
            show("apples", apples);
            boolean hasBanana = stock.containsKey("banana");         // true
            show("hasBanana", hasBanana);
            boolean hasSeven = stock.containsValue(7);               // true, 7 is a cached Integer
            show("hasSeven", hasSeven);
            Integer removed = stock.remove("banana");                // 3
            show("removed", removed);
            int size = stock.size();                                 // 2
            show("size", size);
        }
        {
            Integer a = Integer.valueOf(1000);
            Integer b = Integer.valueOf(1000);                       // a new object, outside the cache
            show("b", b);

            Map<Integer, String> hashMap = new HashMap<>();
            hashMap.put(a, "alex");
            hashMap.put(b, "bob");
            int hashSize = hashMap.size();                           // 1, b replaced the value of a
            show("hashSize", hashSize);

            Map<Integer, String> identityMap = new IdentityHashMap<>();
            identityMap.put(a, "alex");
            identityMap.put(b, "bob");
            int identitySize = identityMap.size();                   // 2
            show("identitySize", identitySize);
        }
        {
            List<String> cart = new ArrayList<>(List.of("apple"));
            Map<List<String>, String> hashed = new HashMap<>();
            Map<List<String>, String> identity = new IdentityHashMap<>();
            hashed.put(cart, "lokesh");
            identity.put(cart, "lokesh");

            cart.add("banana");                                      // changes cart.hashCode()
            String fromHashMap = hashed.get(cart);                   // null, wrong bucket
            show("fromHashMap", fromHashMap);
            String fromIdentityMap = identity.get(cart);             // "lokesh"
            show("fromIdentityMap", fromIdentityMap);
        }
        {
            Map<String, String> settings = new IdentityHashMap<>();
            String key = "theme";
            settings.put(key, "dark");

            boolean removed = settings.remove(key, new String("dark"));    // false on Java 20 and later
            show("removed", removed);
            boolean replaced = settings.replace(key, "dark", "light");     // true, same literal object
            show("replaced", replaced);
        }
        {
            Map<Integer, String> ids = new IdentityHashMap<>();
            ids.put(100, "small");
            ids.put(1000, "large");

            String small = ids.get(100);                             // "small", 100 comes from the Integer cache
            show("small", small);
            String large = ids.get(1000);                            // null, 1000 is boxed into a new Integer
            show("large", large);
        }
        {
            Task build = new Task("build", new ArrayList<>());
            Task test = new Task("test", new ArrayList<>());
            build.dependsOn().add(test);
            test.dependsOn().add(build);                             // a cycle

            Set<Task> hashed = new HashSet<>();
            try { boolean added = hashed.add(build); show("added", added); } catch (Throwable _t) { System.out.println("added -> " + _t); }
            int reachable = countReachable(build);                   // 2
            show("reachable", reachable);
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

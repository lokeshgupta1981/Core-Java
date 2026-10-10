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
 * Examples for the tutorial "Java HashMap: Complete Guide With Examples".
 * https://howtodoinjava.com/java/collections/hashmap/java-hashmap/
 */
public class HashMapGuide {
    static record AccountKey(String bank, int number) {}
    static boolean recordFailure(Map<String, Integer> failures, String user) {
        int count = failures.merge(user, 1, Integer::sum);
        return count >= 5;                                         // true means lock the account
    }
    public static void main(String[] args) throws Exception {
        {
            Map<String, Integer> scores = new HashMap<>();
            Integer added = scores.put("alex", 40);                    // null, new key
            show("added", added);
            Integer replaced = scores.put("alex", 45);                 // 40, old value returned
            show("replaced", replaced);
            Integer kept = scores.putIfAbsent("alex", 99);             // 45, existing value stays
            show("kept", kept);
            Integer alex = scores.get("alex");                         // 45
            show("alex", alex);
            Integer none = scores.get("sam");                          // null, no such key
            show("none", none);
            Integer orZero = scores.getOrDefault("sam", 0);            // 0
            show("orZero", orZero);
            Integer counted = scores.merge("maria", 10, Integer::sum); // 10, inserted
            show("counted", counted);
            Integer summed = scores.merge("maria", 5, Integer::sum);   // 15, added to the old value
            show("summed", summed);
            boolean hasAlex = scores.containsKey("alex");              // true
            show("hasAlex", hasAlex);
            Integer removed = scores.remove("alex");                   // 45
            show("removed", removed);
            int size = scores.size();                                  // 1
            show("size", size);
        }
        {
            Map<String, Integer> empty = new HashMap<>();                          // 16 buckets on the first put
            show("empty", empty);
            Map<String, Integer> forThousand = HashMap.newHashMap(1_000);          // Java 19+, no resize up to 1,000 entries
            show("forThousand", forThousand);
            Map<String, Integer> filled = new HashMap<>(Map.of("alex", 40, "maria", 70));
            Integer maria = filled.get("maria");                                   // 70
            show("maria", maria);
            Map<String, Integer> copy = new HashMap<>(filled);
            Integer old = copy.put("alex", 50);                                    // 40, the original map still has 40
            show("old", old);
            Integer original = filled.get("alex");                                 // 40
            show("original", original);
        }
        {
            Map<String, String> cities = new HashMap<>();
            String first = cities.put("lokesh", "Delhi");             // null
            show("first", first);
            String moved = cities.replace("lokesh", "Pune");           // "Delhi"
            show("moved", moved);
            String nobody = cities.replace("sam", "Rome");             // null, sam was never added
            show("nobody", nobody);
            boolean wrong = cities.remove("lokesh", "Delhi");          // false, the value is "Pune"
            show("wrong", wrong);
            boolean right = cities.remove("lokesh", "Pune");           // true
            show("right", right);
            String nullKey = cities.put(null, "unknown");              // null, one null key is allowed
            show("nullKey", nullKey);
            String readNull = cities.get(null);                        // "unknown"
            show("readNull", readNull);
        }
        {
            Map<String, Integer> views = new HashMap<>();
            Integer v1 = views.merge("home", 1, Integer::sum);                         // 1
            show("v1", v1);
            Integer v2 = views.merge("home", 1, Integer::sum);                         // 2
            show("v2", v2);
            Map<String, List<String>> tags = new HashMap<>();
            boolean tagged = tags.computeIfAbsent("java", k -> new ArrayList<>()).add("streams");   // true
            show("tagged", tagged);
            Integer doubled = views.computeIfPresent("home", (page, n) -> n * 2);     // 4
            show("doubled", doubled);
            Integer gone = views.compute("home", (page, n) -> null);                   // null, the entry is removed
            show("gone", gone);
            boolean hasHome = views.containsKey("home");                               // false
            show("hasHome", hasHome);
        }
        {
            Map<String, Integer> scores = new HashMap<>(Map.of("alex", 40, "maria", 70));
            int total = 0;
            for (Map.Entry<String, Integer> entry : scores.entrySet()) {
                total += entry.getValue();
            }
            int sum = total;                                                          // 110
            show("sum", sum);
            List<String> sortedNames = scores.keySet().stream().sorted().toList();    // [alex, maria]
            show("sortedNames", sortedNames);
            boolean removed = scores.values().removeIf(score -> score < 50);          // true
            show("removed", removed);
        }
        {
            Map<AccountKey, Long> balances = new HashMap<>();
            Long first = balances.put(new AccountKey("hdfc", 1001), 500L);         // null
            show("first", first);
            Long read = balances.get(new AccountKey("hdfc", 1001));                // 500, an equal record finds the entry
            show("read", read);
        }
        {
            ConcurrentHashMap<String, Integer> hits = new ConcurrentHashMap<>();
            Integer h1 = hits.merge("home", 1, Integer::sum);                     // 1, safe from many threads
            show("h1", h1);
            Map<String, Integer> fixed = Map.copyOf(Map.of("timeout", 30));
            Integer t = fixed.get("timeout");                                     // 30, unmodifiable, safe to share
            show("t", t);
        }
        {
            Map<String, Integer> failures = new HashMap<>();
            boolean locked = false;
            for (int i = 0; i < 5; i++) {
                locked = recordFailure(failures, "alex");
            }
            boolean alexLocked = locked;                                   // true
            show("alexLocked", alexLocked);
            Integer reset = failures.remove("alex");                       // 5, cleared after a successful login
            show("reset", reset);
        }
        {
            Map<String, Integer> ordered = new LinkedHashMap<>();
            Integer a = ordered.put("maria", 70);                // null
            show("a", a);
            Integer b = ordered.put("alex", 40);                 // null
            show("b", b);
            String firstKey = ordered.keySet().iterator().next();  // "maria"
            show("firstKey", firstKey);
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

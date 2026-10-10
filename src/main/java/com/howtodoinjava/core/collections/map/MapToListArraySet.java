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
 * Examples for the tutorial "Convert a Map to a List, Array or Set in Java".
 * https://howtodoinjava.com/java/collections/hashmap/convert-map-to-array-list-set/
 */
public class MapToListArraySet {
    static record Member(String name, int age) {}
    public static void main(String[] args) throws Exception {
        {
            Map<String, Integer> ages = new TreeMap<>(Map.of("Lokesh", 37, "Alex", 31, "Maria", 31));

            List<String> names = new ArrayList<>(ages.keySet());                       // [Alex, Lokesh, Maria]
            show("names", names);
            List<Integer> ageList = new ArrayList<>(ages.values());                    // [31, 37, 31]
            show("ageList", ageList);
            List<Map.Entry<String, Integer>> pairs = new ArrayList<>(ages.entrySet()); // [Alex=31, Lokesh=37, Maria=31]
            show("pairs", pairs);

            String[] nameArray = ages.keySet().toArray(String[]::new);                 // [Alex, Lokesh, Maria]
            show("nameArray", nameArray);
            int[] ageArray = ages.values().stream().mapToInt(Integer::intValue).toArray();   // [31, 37, 31]
            show("ageArray", ageArray);

            Set<String> nameSet = new HashSet<>(ages.keySet());                        // 3 names
            show("nameSet", nameSet);
            Set<Integer> distinctAges = new TreeSet<>(ages.values());                  // [31, 37]
            show("distinctAges", distinctAges);
        }
        {
            Map<String, Integer> ages = new TreeMap<>(Map.of("Lokesh", 37, "Alex", 31, "Maria", 31));
            Set<String> keyView = ages.keySet();
            boolean removed = keyView.remove("Alex");                                  // true
            show("removed", removed);
            int sizeAfter = ages.size();                                               // 2 (removed from the map too)
            show("sizeAfter", sizeAfter);
            try { boolean added = keyView.add("Zoe"); show("added", added); } catch (Throwable _t) { System.out.println("added -> " + _t); }
        }
        {
            Map<String, Integer> ages = new TreeMap<>(Map.of("Lokesh", 37, "Alex", 31, "Maria", 31));
            List<Integer> ageList = new ArrayList<>(ages.values());                    // [31, 37, 31]
            show("ageList", ageList);
            List<Integer> readOnly = List.copyOf(ages.values());                       // [31, 37, 31] (unmodifiable)
            show("readOnly", readOnly);
            List<String> labels = ages.values().stream().map(age -> age + " years").toList();   // [31 years, 37 years, 31 years]
            show("labels", labels);
        }
        {
            Map<String, Integer> ages = new TreeMap<>(Map.of("Lokesh", 37, "Alex", 31, "Maria", 31));
            List<String> names = new ArrayList<>(ages.keySet());                       // [Alex, Lokesh, Maria]
            show("names", names);
            List<String> upper = ages.keySet().stream().map(String::toUpperCase).toList();   // [ALEX, LOKESH, MARIA]
            show("upper", upper);
        }
        {
            Map<String, Integer> ages = new TreeMap<>(Map.of("Lokesh", 37, "Alex", 31, "Maria", 31));
            List<Map.Entry<String, Integer>> pairs = new ArrayList<>(ages.entrySet());
            Integer old = pairs.get(0).setValue(32);                                   // 31
            show("old", old);
            Integer inMap = ages.get("Alex");                                          // 32 (the map changed)
            show("inMap", inMap);

            List<Map.Entry<String, Integer>> snapshot = ages.entrySet().stream().map(Map.Entry::copyOf).toList();
            try { Integer blocked = snapshot.get(0).setValue(40); show("blocked", blocked); } catch (Throwable _t) { System.out.println("blocked -> " + _t); }
        }
        {
            Map<String, Integer> ages = new TreeMap<>(Map.of("Lokesh", 37, "Alex", 31, "Maria", 31));
            List<String> youngestFirst = ages.entrySet().stream().sorted(Map.Entry.comparingByValue()).map(Map.Entry::getKey).toList();   // [Alex, Maria, Lokesh]
            show("youngestFirst", youngestFirst);
            List<String> over35 = ages.entrySet().stream().filter(e -> e.getValue() > 35).map(Map.Entry::getKey).toList();               // [Lokesh]
            show("over35", over35);
        }
        {
            Map<String, Integer> ages = new TreeMap<>(Map.of("Lokesh", 37, "Alex", 31, "Maria", 31));
            String[] nameArray = ages.keySet().toArray(String[]::new);                 // [Alex, Lokesh, Maria]
            show("nameArray", nameArray);
            Integer[] boxedAges = ages.values().toArray(Integer[]::new);               // [31, 37, 31]
            show("boxedAges", boxedAges);
            Object[] untyped = ages.keySet().toArray();                                // [Alex, Lokesh, Maria]
            show("untyped", untyped);
        }
        {
            Map<String, Integer> ages = new TreeMap<>(Map.of("Lokesh", 37, "Alex", 31, "Maria", 31));
            int[] ageArray = ages.values().stream().mapToInt(Integer::intValue).toArray();   // [31, 37, 31]
            show("ageArray", ageArray);
            int total = Arrays.stream(ageArray).sum();                                       // 99
            show("total", total);
        }
        {
            Map<String, Integer> ages = new TreeMap<>(Map.of("Lokesh", 37, "Alex", 31, "Maria", 31));
            Set<String> nameSet = new HashSet<>(ages.keySet());                        // 3 names, no order
            show("nameSet", nameSet);
            Set<Integer> distinctAges = new TreeSet<>(ages.values());                  // [31, 37]
            show("distinctAges", distinctAges);
            Set<Integer> fixedAges = Set.copyOf(ages.values());                        // 2 ages, unmodifiable
            show("fixedAges", fixedAges);
            Set<Map.Entry<String, Integer>> entrySetCopy = new HashSet<>(ages.entrySet());   // 3 entries
            show("entrySetCopy", entrySetCopy);
        }
        {
            SequencedMap<String, Integer> visits = new LinkedHashMap<>();
            visits.put("home", 3);
            visits.put("blog", 7);
            visits.put("about", 1);
            List<String> oldestFirst = new ArrayList<>(visits.sequencedKeySet());            // [home, blog, about]
            show("oldestFirst", oldestFirst);
            List<String> newestFirst = new ArrayList<>(visits.sequencedKeySet().reversed()); // [about, blog, home]
            show("newestFirst", newestFirst);
            List<Integer> lastValues = visits.sequencedValues().reversed().stream().limit(2).toList();   // [1, 7]
            show("lastValues", lastValues);
        }
        {
            Map<String, Integer> ages = new TreeMap<>(Map.of("Lokesh", 37, "Alex", 31, "Maria", 31));
            List<Member> members = ages.entrySet().stream().map(e -> new Member(e.getKey(), e.getValue())).toList();   // [Member[name=Alex, age=31], Member[name=Lokesh, age=37], Member[name=Maria, age=31]]
            show("members", members);
        }
        {
            Map<String, List<String>> teams = new TreeMap<>(Map.of("backend", List.of("Alex", "Lokesh"), "design", List.of("Maria")));
            List<String> everyone = teams.values().stream().flatMap(List::stream).toList();   // [Alex, Lokesh, Maria]
            show("everyone", everyone);
        }
        {
            Map<String, Integer> ages = new HashMap<>(Map.of("Lokesh", 37));
            try { List<Integer> cast = (List<Integer>) ages.values(); show("cast", cast); } catch (Throwable _t) { System.out.println("cast -> " + _t); }
            List<Integer> copy = new ArrayList<>(ages.values());                       // [37]
            show("copy", copy);
        }
        {
            Map<String, Integer> scores = new HashMap<>();
            scores.put("ben", null);
            List<Integer> viaStream = scores.values().stream().toList();               // [null]
            show("viaStream", viaStream);
            try { List<Integer> viaCopy = List.copyOf(scores.values()); show("viaCopy", viaCopy); } catch (Throwable _t) { System.out.println("viaCopy -> " + _t); }
        }
        {
            Map<String, Integer> ages = new TreeMap<>(Map.of("Lokesh", 37, "Alex", 31));
            Object[][] rows = ages.entrySet().stream().map(e -> new Object[] {e.getKey(), e.getValue()}).toArray(Object[][]::new);   // [[Alex, 31], [Lokesh, 37]]
            show("rows", rows);
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

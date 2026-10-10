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
 * Examples for the tutorial "Java TreeMap: Sorted Keys, floorKey(), subMap() and headMap()".
 * https://howtodoinjava.com/java/collections/treemap-class/
 */
public class TreeMapExamples {

    public static void main(String[] args) throws Exception {
        {
            TreeMap<Integer, String> tiers = new TreeMap<>();
            tiers.put(100, "silver");
            tiers.put(0, "basic");
            tiers.put(500, "gold");
            String sorted = tiers.toString();                          // "{0=basic, 100=silver, 500=gold}"
            show("sorted", sorted);
            Integer lowest = tiers.firstKey();                         // 0
            show("lowest", lowest);
            Integer floor = tiers.floorKey(250);                       // 100
            show("floor", floor);
            Integer ceiling = tiers.ceilingKey(250);                   // 500
            show("ceiling", ceiling);
            SortedMap<Integer, String> below500 = tiers.headMap(500);  // {0=basic, 100=silver}
            show("below500", below500);
            NavigableMap<Integer, String> desc = tiers.descendingMap(); // {500=gold, 100=silver, 0=basic}
            show("desc", desc);
        }
        {
            TreeMap<String, Integer> natural = new TreeMap<>();                                   // natural order of String
            show("natural", natural);
            TreeMap<String, Integer> reverse = new TreeMap<>(Comparator.reverseOrder());          // descending
            show("reverse", reverse);
            TreeMap<String, Integer> fromMap = new TreeMap<>(Map.of("pear", 2, "fig", 7));        // {fig=7, pear=2}
            show("fromMap", fromMap);
            TreeMap<String, Integer> sameOrder = new TreeMap<>(reverse);                          // copies the comparator too
            show("sameOrder", sameOrder);
        }
        {
            TreeMap<String, String> headers = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
            headers.put("Content-Type", "text/html");
            String type = headers.get("content-type");                 // "text/html"
            show("type", type);
            headers.put("CONTENT-TYPE", "application/json");
            int headerCount = headers.size();                          // 1
            show("headerCount", headerCount);
            String stored = headers.toString();                        // "{Content-Type=application/json}"
            show("stored", stored);
        }
        {
            TreeMap<String, Integer> byLength = new TreeMap<>(Comparator.comparingInt(String::length));
            byLength.put("kiwi", 1);
            byLength.put("pear", 2);
            String lost = byLength.toString();                         // "{kiwi=2}"
            show("lost", lost);
            TreeMap<String, Integer> fixed = new TreeMap<>(Comparator.comparingInt(String::length).thenComparing(Comparator.naturalOrder()));
            fixed.put("kiwi", 1);
            fixed.put("pear", 2);
            String kept = fixed.toString();                            // "{kiwi=1, pear=2}"
            show("kept", kept);
        }
        {
            TreeMap<String, Integer> pantry = new TreeMap<>();
            pantry.put("rice", 2);
            pantry.put("flour", 1);
            pantry.merge("rice", 3, Integer::sum);
            Integer rice = pantry.get("rice");                          // 5
            show("rice", rice);
            Integer sugar = pantry.getOrDefault("sugar", 0);            // 0
            show("sugar", sugar);
            Integer removed = pantry.remove("flour");                   // 1
            show("removed", removed);
            boolean hasFlour = pantry.containsKey("flour");             // false
            show("hasFlour", hasFlour);
            StringBuilder report = new StringBuilder();
            pantry.forEach((item, qty) -> report.append(item).append('=').append(qty).append(' '));
            String line = report.toString().strip();                    // "rice=5"
            show("line", line);
        }
        {
            TreeMap<Double, Double> rates = new TreeMap<>(Map.of(0.0, 4.0, 1.0, 6.5, 5.0, 9.0, 20.0, 15.0));
            Map.Entry<Double, Double> band = rates.floorEntry(2.4);     // 1.0=6.5
            show("band", band);
            Double price = band.getValue();                             // 6.5
            show("price", price);
            Map.Entry<Double, Double> none = rates.floorEntry(-1.0);    // null
            show("none", none);
            Map.Entry<Double, Double> heaviest = rates.lastEntry();     // 20.0=15.0
            show("heaviest", heaviest);
        }
        {
            TreeMap<LocalTime, String> slots = new TreeMap<>();
            slots.put(LocalTime.of(9, 0), "anna");
            slots.put(LocalTime.of(10, 30), "bob");
            slots.put(LocalTime.of(12, 0), "carl");
            slots.put(LocalTime.of(15, 0), "dina");
            int booked = slots.size();                                  // 4
            show("booked", booked);
        }
        {
            TreeMap<LocalTime, String> slots = new TreeMap<>(Map.of(LocalTime.of(9, 0), "anna", LocalTime.of(10, 30), "bob", LocalTime.of(12, 0), "carl", LocalTime.of(15, 0), "dina"));
            SortedMap<LocalTime, String> morning = slots.subMap(LocalTime.of(9, 0), LocalTime.of(12, 0));                      // {09:00=anna, 10:30=bob}
            show("morning", morning);
            NavigableMap<LocalTime, String> withNoon = slots.subMap(LocalTime.of(9, 0), true, LocalTime.of(12, 0), true);      // {09:00=anna, 10:30=bob, 12:00=carl}
            show("withNoon", withNoon);
        }
        {
            TreeMap<LocalTime, String> slots = new TreeMap<>(Map.of(LocalTime.of(9, 0), "anna", LocalTime.of(10, 30), "bob", LocalTime.of(12, 0), "carl", LocalTime.of(15, 0), "dina"));
            SortedMap<LocalTime, String> beforeNoon = slots.headMap(LocalTime.of(12, 0));               // {09:00=anna, 10:30=bob}
            show("beforeNoon", beforeNoon);
            NavigableMap<LocalTime, String> untilNoon = slots.headMap(LocalTime.of(12, 0), true);       // {09:00=anna, 10:30=bob, 12:00=carl}
            show("untilNoon", untilNoon);
        }
        {
            TreeMap<LocalTime, String> slots = new TreeMap<>(Map.of(LocalTime.of(9, 0), "anna", LocalTime.of(10, 30), "bob", LocalTime.of(12, 0), "carl", LocalTime.of(15, 0), "dina"));
            SortedMap<LocalTime, String> fromNoon = slots.tailMap(LocalTime.of(12, 0));                 // {12:00=carl, 15:00=dina}
            show("fromNoon", fromNoon);
            NavigableMap<LocalTime, String> afterNoon = slots.tailMap(LocalTime.of(12, 0), false);      // {15:00=dina}
            show("afterNoon", afterNoon);
        }
        {
            TreeMap<LocalTime, String> slots = new TreeMap<>(Map.of(LocalTime.of(9, 0), "anna", LocalTime.of(15, 0), "dina"));
            SortedMap<LocalTime, String> morning = slots.subMap(LocalTime.of(8, 0), LocalTime.of(12, 0));
            slots.put(LocalTime.of(11, 0), "eva");
            int inView = morning.size();                                               // 2
            show("inView", inView);
            morning.remove(LocalTime.of(9, 0));
            boolean annaGone = !slots.containsKey(LocalTime.of(9, 0));                // true
            show("annaGone", annaGone);
            try { String outside = morning.put(LocalTime.of(16, 0), "finn"); show("outside", outside); } catch (Throwable _t) { System.out.println("outside -> " + _t); }
            TreeMap<LocalTime, String> copy = new TreeMap<>(morning);                  // independent copy
            show("copy", copy);
            int copied = copy.size();                                                  // 1
            show("copied", copied);
        }
        {
            Map<String, Integer> stock = new HashMap<>(Map.of("apple", 5, "banana", 3, "cherry", 8));
            Set<String> wanted = Set.of("apple", "cherry");
            Map<String, Integer> filtered = stock.entrySet().stream()
                    .filter(e -> wanted.contains(e.getKey()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            int filteredSize = filtered.size();                         // 2
            show("filteredSize", filteredSize);
            Map<String, Integer> retained = new HashMap<>(stock);
            retained.keySet().retainAll(wanted);
            boolean sameEntries = retained.equals(filtered);             // true
            show("sameEntries", sameEntries);
            int original = stock.size();                                 // 3
            show("original", original);
        }
        {
            TreeMap<String, Integer> scores = new TreeMap<>(Map.of("anna", 7, "bob", 9));
            SequencedMap<String, Integer> sequenced = scores;
            String firstName = sequenced.firstEntry().getKey();          // "anna"
            show("firstName", firstName);
            SequencedMap<String, Integer> reversed = scores.reversed();  // {bob=9, anna=7}
            show("reversed", reversed);
            try { Integer forced = sequenced.putFirst("zoe", 1); show("forced", forced); } catch (Throwable _t) { System.out.println("forced -> " + _t); }
        }
        {
            TreeMap<String, Integer> plain = new TreeMap<>();
            try { Integer rejected = plain.put(null, 1); show("rejected", rejected); } catch (Throwable _t) { System.out.println("rejected -> " + _t); }
            TreeMap<String, Integer> nullable = new TreeMap<>(Comparator.nullsFirst(Comparator.naturalOrder()));
            nullable.put("b", 2);
            nullable.put(null, 0);
            String withNull = nullable.toString();                       // "{null=0, b=2}"
            show("withNull", withNull);
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

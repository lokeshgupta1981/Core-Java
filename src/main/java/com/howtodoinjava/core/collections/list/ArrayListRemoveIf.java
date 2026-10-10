package com.howtodoinjava.core.collections.list;

import java.util.concurrent.locks.*;
import java.lang.management.*;
import java.time.format.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.zip.*;
import java.util.regex.*;
import java.net.*;
import java.text.*;
import java.nio.file.attribute.*;
import java.security.*;
import java.nio.charset.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.time.*;
import java.nio.file.*;
import java.math.*;
import java.io.*;

import java.time.temporal.*;

/**
 * Examples for the tutorial "ArrayList removeIf() in Java: Remove Elements by Condition".
 * https://howtodoinjava.com/java/collections/arraylist/arraylist-removeif/
 */
public class ArrayListRemoveIf {
    static record Item(String name, LocalDate expires, int qty) {}
    static int cleanUp(List<Item> pantry, LocalDate today) {
        int before = pantry.size();
        pantry.removeIf(i -> i.expires() == null || i.expires().isBefore(today) || i.qty() <= 0);
        return before - pantry.size();
    }
    public static void main(String[] args) throws Exception {
        {
            List<Integer> scores = new ArrayList<>(List.of(12, 7, 30, 7, 45));
            boolean removed = scores.removeIf(s -> s < 10);    // true
            show("removed", removed);
            List<Integer> left = scores;                       // [12, 30, 45]
            show("left", left);
            boolean again = scores.removeIf(s -> s < 10);      // false, nothing matched
            show("again", again);
        }
        {
            List<Integer> array = new ArrayList<>(List.of(5, 2, 0, 4));
            try { boolean a1 = array.removeIf(n -> 10 / n == 5); show("a1", a1); } catch (Throwable _t) { System.out.println("a1 -> " + _t); }
            List<Integer> arrayAfter = array;                    // [5, 2, 0, 4]
            show("arrayAfter", arrayAfter);
            List<Integer> linked = new LinkedList<>(List.of(5, 2, 0, 4));
            try { boolean l1 = linked.removeIf(n -> 10 / n == 5); show("l1", l1); } catch (Throwable _t) { System.out.println("l1 -> " + _t); }
            List<Integer> linkedAfter = linked;                  // [5, 0, 4]
            show("linkedAfter", linkedAfter);
        }
        {
            List<String> tags = new ArrayList<>(List.of("java", "", "list"));
            try { boolean nullFilter = tags.removeIf(null); show("nullFilter", nullFilter); } catch (Throwable _t) { System.out.println("nullFilter -> " + _t); }
            try { boolean selfEdit = tags.removeIf(t -> tags.add("x")); show("selfEdit", selfEdit); } catch (Throwable _t) { System.out.println("selfEdit -> " + _t); }
        }
        {
            List<String> lines = new ArrayList<>(Arrays.asList("rice", null, "  ", "salt", ""));
            boolean nulls = lines.removeIf(Objects::isNull);          // true
            show("nulls", nulls);
            boolean blanks = lines.removeIf(String::isBlank);         // true
            show("blanks", blanks);
            List<String> clean = lines;                               // [rice, salt]
            show("clean", clean);
        }
        {
            List<String> words = new ArrayList<>(List.of("tea", "Tea", "coffee", "tonic"));
            boolean keepOnlyT = words.removeIf(Predicate.not(w -> w.startsWith("t")));   // true
            show("keepOnlyT", keepOnlyT);
            List<String> tWords = words;                                                 // [tea, tonic]
            show("tWords", tWords);
            List<String> drinks = new ArrayList<>(List.of("tea", "milk", "tea"));
            boolean noTea = drinks.removeIf(Predicate.isEqual("tea"));                   // true
            show("noTea", noTea);
            List<String> rest = drinks;                                                  // [milk]
            show("rest", rest);
        }
        {
            Set<String> banned = Set.of("spam", "ads");
            List<String> topics = new ArrayList<>(List.of("java", "spam", "SPAM", "ads", "sql"));
            boolean filtered = topics.removeIf(t -> banned.contains(t.toLowerCase(Locale.ROOT)));   // true
            show("filtered", filtered);
            List<String> allowed = topics;                                                          // [java, sql]
            show("allowed", allowed);
        }
        {
            LocalDate today = LocalDate.of(2026, 10, 10);
            List<Item> pantry = new ArrayList<>(List.of(
            new Item("milk", LocalDate.of(2026, 10, 8), 1),
            new Item("rice", LocalDate.of(2027, 5, 1), 2),
            new Item("eggs", LocalDate.of(2026, 10, 12), 0),
            new Item("jam", LocalDate.of(2026, 12, 1), 1)));
            boolean expired = pantry.removeIf(i -> i.expires().isBefore(today));   // true, removes milk
            show("expired", expired);
            boolean usedUp = pantry.removeIf(i -> i.qty() == 0);                   // true, removes eggs
            show("usedUp", usedUp);
            List<String> names = pantry.stream().map(Item::name).toList();         // [rice, jam]
            show("names", names);
        }
        {
            List<Item> stock = new ArrayList<>(List.of(new Item("milk", LocalDate.of(2026, 10, 8), 1), new Item("eggs", LocalDate.of(2026, 10, 12), 0), new Item("jam", LocalDate.of(2026, 12, 1), 1)));
            Predicate<Item> isExpired = i -> i.expires().isBefore(LocalDate.of(2026, 10, 10));
            Predicate<Item> isEmpty = i -> i.qty() == 0;
            boolean both = stock.removeIf(isExpired.or(isEmpty));                  // true
            show("both", both);
            int kept = stock.size();                                               // 1
            show("kept", kept);
        }
        {
            List<Integer> fixedSize = Arrays.asList(1, 2, 3);
            boolean noMatch = fixedSize.removeIf(n -> n > 5);           // false
            show("noMatch", noMatch);
            try { boolean match = fixedSize.removeIf(n -> n > 2); show("match", match); } catch (Throwable _t) { System.out.println("match -> " + _t); }
            List<Integer> immutable = List.of(1, 2, 3);
            try { boolean neverWorks = immutable.removeIf(n -> n > 5); show("neverWorks", neverWorks); } catch (Throwable _t) { System.out.println("neverWorks -> " + _t); }
            List<Integer> copy = new ArrayList<>(List.of(1, 2, 3));
            boolean works = copy.removeIf(n -> n > 2);                  // true
            show("works", works);
        }
        {
            Map<String, Integer> stockLevels = new TreeMap<>(Map.of("milk", 0, "rice", 4, "salt", 0, "tea", 9));
            boolean byValue = stockLevels.values().removeIf(qty -> qty == 0);              // true
            show("byValue", byValue);
            String afterValues = stockLevels.toString();                                   // "{rice=4, tea=9}"
            show("afterValues", afterValues);
            boolean byKey = stockLevels.keySet().removeIf(k -> k.startsWith("t"));         // true
            show("byKey", byKey);
            boolean byEntry = stockLevels.entrySet().removeIf(e -> e.getValue() > 3 && e.getKey().length() == 4);   // true
            show("byEntry", byEntry);
            int entries = stockLevels.size();                                              // 0
            show("entries", entries);
        }
        {
            List<Integer> readings = new ArrayList<>(List.of(3, -1, 8, -4, 6));
            Iterator<Integer> it = readings.iterator();
            while (it.hasNext()) {
                if (it.next() < 0) {
                    it.remove();
                }
            }
            List<Integer> viaIterator = readings;                       // [3, 8, 6]
            show("viaIterator", viaIterator);
        }
        {
            List<Integer> source = List.of(3, -1, 8, -4, 6);
            List<Integer> positives = source.stream().filter(n -> n >= 0).toList();   // [3, 8, 6]
            show("positives", positives);
            int sourceSize = source.size();                                           // 5
            show("sourceSize", sourceSize);
        }
        {
            List<String> queue = new ArrayList<>(List.of("ok:1", "bad:2", "ok:3", "bad:4"));
            Map<Boolean, List<String>> parts = queue.stream().collect(Collectors.partitioningBy(s -> s.startsWith("bad")));
            List<String> dropped = parts.get(true);                    // [bad:2, bad:4]
            show("dropped", dropped);
            queue.removeIf(s -> s.startsWith("bad"));
            List<String> kept = queue;                                 // [ok:1, ok:3]
            show("kept", kept);
        }
        {
            List<Item> shelf = new ArrayList<>(List.of(
            new Item("milk", LocalDate.of(2026, 10, 8), 1),
            new Item("flour", null, 3),
            new Item("eggs", LocalDate.of(2026, 10, 12), 0),
            new Item("jam", LocalDate.of(2026, 12, 1), 1)));
            int removedCount = cleanUp(shelf, LocalDate.of(2026, 10, 10));   // 3
            show("removedCount", removedCount);
            String onShelf = shelf.get(0).name();                            // "jam"
            show("onShelf", onShelf);
        }
        {
            List<Integer> values = new ArrayList<>(List.of(4, 9, 2, 11));
            OptionalInt firstBig = IntStream.range(0, values.size()).filter(i -> values.get(i) > 5).findFirst();
            firstBig.ifPresent(i -> values.remove(i));
            List<Integer> result = values;                             // [4, 2, 11]
            show("result", result);
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

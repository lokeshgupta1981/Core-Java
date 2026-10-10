package com.howtodoinjava.core.collections;

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

/**
 * Examples for the tutorial "Immutable Collections Factory Methods: List.of, Set.of, Map.of".
 * https://howtodoinjava.com/java9/create-immutable-collections-factory-methods/
 */
public class ImmutableCollectionsFactoryMethods {
    static record Playlist(String name, List<String> songs) {
        Playlist {
            songs = List.copyOf(songs);
        }
    }
    public static void main(String[] args) throws Exception {
        {
            List<String> weekend = List.of("Sat", "Sun");                        // [Sat, Sun]
            show("weekend", weekend);
            Set<String> roles = Set.of("admin", "editor", "viewer");              // 3 elements, order not fixed
            show("roles", roles);
            int roleCount = roles.size();                                        // 3
            show("roleCount", roleCount);
            Map<Integer, String> statusText = Map.of(200, "OK", 404, "Not Found");
            String notFound = statusText.get(404);                               // "Not Found"
            show("notFound", notFound);
            List<String> snapshot = List.copyOf(new ArrayList<>(weekend));      // [Sat, Sun]
            show("snapshot", snapshot);
            try { boolean added = weekend.add("Mon"); show("added", added); } catch (Throwable _t) { System.out.println("added -> " + _t); }
            try { List<String> withNull = List.of("Sat", null); show("withNull", withNull); } catch (Throwable _t) { System.out.println("withNull -> " + _t); }
        }
        {
            List<String> steps = List.of("wash", "chop", "boil", "chop");       // [wash, chop, boil, chop]
            show("steps", steps);
            String second = steps.get(1);                                         // "chop"
            show("second", second);
            int firstChop = steps.indexOf("chop");                                // 1
            show("firstChop", firstChop);
            String last = steps.getLast();                                        // "chop"
            show("last", last);
            List<String> backwards = steps.reversed();                           // [chop, boil, chop, wash]
            show("backwards", backwards);
            try { String replaced = steps.set(0, "rinse"); show("replaced", replaced); } catch (Throwable _t) { System.out.println("replaced -> " + _t); }
        }
        {
            List<String> menu = List.of("tea", "coffee");
            try { boolean hasNull = menu.contains(null); show("hasNull", hasNull); } catch (Throwable _t) { System.out.println("hasNull -> " + _t); }
            boolean safeCheck = new ArrayList<>(menu).contains(null);     // false
            show("safeCheck", safeCheck);
        }
        {
            Set<String> vowels = Set.of("a", "e", "i", "o", "u");
            boolean hasE = vowels.contains("e");                         // true
            show("hasE", hasE);
            try { Set<String> typo = Set.of("a", "e", "a"); show("typo", typo); } catch (Throwable _t) { System.out.println("typo -> " + _t); }
            Set<String> deduped = Set.copyOf(List.of("a", "e", "a"));    // 2 elements
            show("deduped", deduped);
            int dedupedSize = deduped.size();                            // 2
            show("dedupedSize", dedupedSize);
        }
        {
            Map<String, Integer> stock = Map.of("apple", 5, "banana", 3);
            int apples = stock.get("apple");                                       // 5
            show("apples", apples);
            Map<String, Integer> prices = Map.ofEntries(Map.entry("tea", 2), Map.entry("coffee", 3));
            int coffee = prices.get("coffee");                                     // 3
            show("coffee", coffee);
            try { Map<String, Integer> sameKey = Map.of("apple", 5, "apple", 7); show("sameKey", sameKey); } catch (Throwable _t) { System.out.println("sameKey -> " + _t); }
            try { Map<String, Integer> nullValue = Map.of("apple", null); show("nullValue", nullValue); } catch (Throwable _t) { System.out.println("nullValue -> " + _t); }
        }
        {
            Map<String, String> defaults = Map.of("theme", "light", "lang", "en");
            Map<String, String> userSettings = new HashMap<>(defaults);
            String oldTheme = userSettings.put("theme", "dark");                   // "light"
            show("oldTheme", oldTheme);
            String effective = userSettings.get("theme");                          // "dark"
            show("effective", effective);
            String unchanged = defaults.get("theme");                              // "light"
            show("unchanged", unchanged);
        }
        {
            List<String> songs = new ArrayList<>(List.of("Intro", "Outro"));
            List<String> copy = List.copyOf(songs);
            List<String> view = Collections.unmodifiableList(songs);
            songs.add("Bonus");
            List<String> copyNow = copy;                             // [Intro, Outro]
            show("copyNow", copyNow);
            List<String> viewNow = view;                             // [Intro, Outro, Bonus]
            show("viewNow", viewNow);
            boolean sameInstance = List.copyOf(copy) == copy;        // true
            show("sameInstance", sameInstance);
        }
        {
            List<String> picks = new ArrayList<>(List.of("Intro"));
            Playlist playlist = new Playlist("Morning", picks);
            picks.add("Outro");
            List<String> stored = playlist.songs();                     // [Intro]
            show("stored", stored);
            try { boolean changed = playlist.songs().add("Bonus"); show("changed", changed); } catch (Throwable _t) { System.out.println("changed -> " + _t); }
        }
        {
            StringBuilder draft = new StringBuilder("v1");
            List<StringBuilder> drafts = List.of(draft);
            draft.append("-edited");
            String inList = drafts.get(0).toString();                 // "v1-edited"
            show("inList", inList);
        }
        {
            List<String> upper = Stream.of("tea", "coffee").map(String::toUpperCase).toList();   // [TEA, COFFEE]
            show("upper", upper);
            List<String> withNulls = Stream.of("tea", null).toList();                           // [tea, null]
            show("withNulls", withNulls);
            List<String> collected = Stream.of("tea", "coffee").collect(Collectors.toUnmodifiableList());   // [tea, coffee]
            show("collected", collected);
            try { List<String> rejected = Stream.of("tea", null).collect(Collectors.toUnmodifiableList()); show("rejected", rejected); } catch (Throwable _t) { System.out.println("rejected -> " + _t); }
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

package com.howtodoinjava.core.collections.cursors;

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
 * Examples for the tutorial "Iterate a Collection in Java: Loops for List, Set and Map".
 * https://howtodoinjava.com/java/collections/different-ways-to-iterate-over-collections-in-java/
 */
public class IterateCollections {

    public static void main(String[] args) throws Exception {
        {
            List<String> songs = List.of("Intro", "Verse", "Chorus");

            // 1. Enhanced for loop
            for (String song : songs) {
                System.out.println(song);                        // Intro, Verse, Chorus
            }
            // 2. forEach() with a method reference
            songs.forEach(System.out::println);                  // Intro, Verse, Chorus
            // 3. Iterator
            Iterator<String> it = songs.iterator();
            while (it.hasNext()) {
                System.out.println(it.next());                   // Intro, Verse, Chorus
            }
            // 4. Index-based loop, lists only
            for (int i = 0; i < songs.size(); i++) {
                System.out.println(i + " " + songs.get(i));      // 0 Intro, 1 Verse, 2 Chorus
            }
            // 5. Stream
            List<String> loud = songs.stream().map(String::toUpperCase).toList();  // [INTRO, VERSE, CHORUS]
            show("loud", loud);
        }
        {
            List<String> queue = List.of("Intro", "Verse", "Chorus", "Bridge", "Outro");
            int current = 1;
            List<String> upNext = new ArrayList<>();
            for (int i = current + 1; i < queue.size() && upNext.size() < 3; i++) {
                upNext.add(queue.get(i));
            }
            List<String> shown = upNext;                         // [Chorus, Bridge, Outro]
            show("shown", shown);
        }
        {
            List<String> queue = List.of("Intro", "Verse", "Chorus", "Bridge", "Outro");
            List<String> numbered = IntStream.range(0, 3).mapToObj(i -> (i + 1) + ". " + queue.get(i)).toList();  // [1. Intro, 2. Verse, 3. Chorus]
            show("numbered", numbered);
        }
        {
            List<String> queue = List.of("Intro", "Verse", "Chorus", "Bridge", "Outro");
            List<String> backwards = queue.reversed();            // [Outro, Bridge, Chorus, Verse, Intro]
            show("backwards", backwards);
            String lastSong = queue.getLast();                    // "Outro"
            show("lastSong", lastSong);
            Deque<String> history = new ArrayDeque<>(List.of("a", "b", "c"));
            Iterator<String> newestFirst = history.descendingIterator();
            String newest = newestFirst.next();                   // "c"
            show("newest", newest);
        }
        {
            Set<String> genres = new TreeSet<>(Set.of("rock", "jazz", "pop"));
            String sorted = String.join(" ", genres);            // "jazz pop rock"
            show("sorted", sorted);
            Set<String> tags = new LinkedHashSet<>(List.of("live", "remix", "live"));
            StringJoiner joined = new StringJoiner(",");
            for (String tag : tags) {
                joined.add(tag);
            }
            String inOrder = joined.toString();                  // "live,remix"
            show("inOrder", inOrder);
        }
        {
            Map<String, Integer> plays = new LinkedHashMap<>();
            plays.put("Intro", 12);
            plays.put("Verse", 40);
            plays.put("Chorus", 95);

            StringJoiner report = new StringJoiner(", ");
            for (Map.Entry<String, Integer> entry : plays.entrySet()) {
                report.add(entry.getKey() + "=" + entry.getValue());
            }
            String text = report.toString();                     // "Intro=12, Verse=40, Chorus=95"
            show("text", text);
        }
        {
            Map<String, Integer> plays = new LinkedHashMap<>();
            plays.put("Intro", 12);
            plays.put("Verse", 40);
            plays.put("Chorus", 95);
            List<String> hits = new ArrayList<>();
            plays.forEach((song, count) -> {
                if (count > 30) hits.add(song);
            });
            int hitCount = hits.size();                          // 2
            show("hitCount", hitCount);
            int totalPlays = plays.values().stream().mapToInt(Integer::intValue).sum();  // 147
            show("totalPlays", totalPlays);
            String firstSong = plays.keySet().iterator().next(); // "Intro"
            show("firstSong", firstSong);
        }
        {
            Map<String, Integer> plays = new LinkedHashMap<>();
            plays.put("Intro", 12);
            plays.put("Verse", 40);
            plays.put("Chorus", 95);
            Iterator<Map.Entry<String, Integer>> entries = plays.entrySet().iterator();
            Map.Entry<String, Integer> first = entries.next();   // Intro=12
            show("first", first);
            String key = first.getKey();                          // "Intro"
            show("key", key);
            Integer value = first.getValue();                     // 12
            show("value", value);
        }
        {
            List<String> playQueue = new ArrayList<>(List.of("Intro", "Ad 1", "Verse", "Ad 2", "Chorus"));
            try { for (String s : playQueue) if (s.startsWith("Ad")) playQueue.remove(s);  } catch (Throwable _t) { System.out.println("-> " + _t); }
        }
        {
            List<String> premium = new ArrayList<>(List.of("Intro", "Ad 1", "Verse", "Ad 2", "Chorus"));
            boolean changed = premium.removeIf(s -> s.startsWith("Ad"));   // true
            show("changed", changed);
            String after = String.join(", ", premium);            // "Intro, Verse, Chorus"
            show("after", after);

            List<String> free = new ArrayList<>(List.of("Intro", "Ad 1", "Verse"));
            Iterator<String> cursor = free.iterator();
            while (cursor.hasNext()) {
                if (cursor.next().startsWith("Ad")) {
                    cursor.remove();
                }
            }
            String cleaned = String.join(", ", free);             // "Intro, Verse"
            show("cleaned", cleaned);
        }
        {
            Map<String, Integer> counts = new LinkedHashMap<>(Map.of("Intro", 12));
            counts.put("Verse", 40);
            boolean dropped = counts.values().removeIf(c -> c < 20);       // true
            show("dropped", dropped);
            Set<String> remaining = counts.keySet();              // [Verse]
            show("remaining", remaining);
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

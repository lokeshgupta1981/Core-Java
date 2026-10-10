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

import org.apache.commons.lang3.SerializationUtils;

/**
 * Examples for the tutorial "Deep Copy a HashMap in Java and When a Shallow Copy Is Enough".
 * https://howtodoinjava.com/java/collections/hashmap/shallow-deep-copy-hashmap/
 */
public class DeepCopyHashMap {
    static class Track {
        private final String title;
        private int plays;

        Track(String title, int plays) {
            this.title = title;
            this.plays = plays;
        }

        Track(Track other) {
            this(other.title, other.plays);
        }

        void play() {
            plays++;
        }

        int plays() {
            return plays;
        }
    }
    public static void main(String[] args) throws Exception {
        {
            Map<String, List<String>> playlists = new HashMap<>();
            playlists.put("gym", new ArrayList<>(List.of("warm-up")));

            Map<String, List<String>> shallow = new HashMap<>(playlists);
            Map<String, List<String>> deep = new HashMap<>();
            playlists.forEach((name, songs) -> deep.put(name, new ArrayList<>(songs)));

            playlists.get("gym").add("sprint");
            List<String> inShallow = shallow.get("gym");          // [warm-up, sprint] (shared list)
            show("inShallow", inShallow);
            List<String> inDeep = deep.get("gym");                // [warm-up] (own list)
            show("inDeep", inDeep);
        }
        {
            Map<String, Integer> plays = new HashMap<>(Map.of("intro", 3, "outro", 1));
            Map<String, Integer> copy = new HashMap<>(plays);
            copy.put("intro", 10);
            Integer original = plays.get("intro");                // 3
            show("original", original);
            Integer changed = copy.get("intro");                  // 10
            show("changed", changed);
        }
        {
            HashMap<String, Integer> plays = new HashMap<>(Map.of("intro", 3));
            @SuppressWarnings("unchecked")
            HashMap<String, Integer> cloned = (HashMap<String, Integer>) plays.clone();
            boolean equal = cloned.equals(plays);                 // true
            show("equal", equal);
            boolean sameObject = cloned == plays;                 // false
            show("sameObject", sameObject);
        }
        {
            Map<String, Integer> plays = Map.of("intro", 3, "outro", 1);
            Map<String, Integer> target = new HashMap<>(Map.of("bonus", 7));
            target.putAll(plays);
            int size = target.size();                             // 3
            show("size", size);
        }
        {
            Map<String, Integer> plays = new HashMap<>(Map.of("intro", 3));
            Map<String, Integer> frozen = Map.copyOf(plays);
            plays.put("outro", 1);
            int frozenSize = frozen.size();                       // 1 (not affected)
            show("frozenSize", frozenSize);
            try { Integer blocked = frozen.put("bonus", 7); show("blocked", blocked); } catch (Throwable _t) { System.out.println("blocked -> " + _t); }
        }
        {
            Map<String, Integer> plays = Map.of("intro", 3, "outro", 1, "skit", 0);
            Map<String, Integer> played = plays.entrySet().stream().filter(e -> e.getValue() > 0).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            int playedSize = played.size();                       // 2
            show("playedSize", playedSize);
        }
        {
            Map<String, List<String>> moods = new HashMap<>();
            moods.put("calm", new ArrayList<>(List.of("jazz", "ambient")));

            Map<String, List<String>> viaStream = moods.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> new ArrayList<>(e.getValue())));
            Map<String, List<String>> viaLoop = new HashMap<>();
            moods.forEach((mood, genres) -> viaLoop.put(mood, new ArrayList<>(genres)));

            viaStream.get("calm").add("lofi");
            List<String> original = moods.get("calm");            // [jazz, ambient]
            show("original", original);
            List<String> edited = viaStream.get("calm");          // [jazz, ambient, lofi]
            show("edited", edited);
        }
        {
            Map<String, Track> library = new HashMap<>();
            library.put("t1", new Track("Intro", 3));

            Map<String, Track> shallow = new HashMap<>(library);
            Map<String, Track> deep = library.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> new Track(e.getValue())));

            library.get("t1").play();
            int shallowPlays = shallow.get("t1").plays();         // 4 (same Track object)
            show("shallowPlays", shallowPlays);
            int deepPlays = deep.get("t1").plays();               // 3 (own Track object)
            show("deepPlays", deepPlays);
        }
        {
            HashMap<String, ArrayList<String>> moods = new HashMap<>();
            moods.put("calm", new ArrayList<>(List.of("jazz")));
            HashMap<String, ArrayList<String>> copy = SerializationUtils.clone(moods);
            copy.get("calm").add("lofi");
            List<String> original = moods.get("calm");            // [jazz]
            show("original", original);
        }
        {
            Map<String, List<String>> moods = new HashMap<>();
            moods.put("calm", new ArrayList<>(List.of("jazz")));
            Map<String, List<String>> frozen = Map.copyOf(moods);
            frozen.get("calm").add("lofi");
            List<String> original = moods.get("calm");            // [jazz, lofi]
            show("original", original);
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

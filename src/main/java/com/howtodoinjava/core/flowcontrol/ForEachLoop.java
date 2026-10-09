package com.howtodoinjava.core.flowcontrol;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.time.*;
import java.nio.file.*;
import java.math.*;
import java.io.*;

import java.util.regex.*;
import java.nio.charset.*;
import java.nio.*;
import java.security.*;
import java.lang.reflect.*;
import java.lang.invoke.*;

/**
 * Examples for the tutorial "Java for-each Loop (Enhanced for Loop) with Examples".
 * https://howtodoinjava.com/java/flow-control/enhanced-for-each-loop-in-java/
 */
public class ForEachLoop {
    static List<String> removeSkipped(List<String> songs, String skipped) {
        for (String song : songs) {
            if (song.equals(skipped)) {
                songs.remove(song);            // modifies the list behind the iterator
            }
        }
        return songs;
    }
    static record Stay(LocalDate checkIn, LocalDate checkOut) implements Iterable<LocalDate> {
        public Iterator<LocalDate> iterator() {
            return checkIn.datesUntil(checkOut).iterator();
        }
    }
    public static void main(String[] args) throws Exception {
        {
            int[] durations = {210, 185, 245};      // seconds
            show("durations", durations);
            int total = 0;
            for (int seconds : durations) {
                total += seconds;
            }
            String length = total / 60 + " min " + total % 60 + " s";   // "10 min 40 s"
            show("length", length);
        }
        {
            List<String> playlist = List.of("Intro", "Echoes", "Outro");
            StringBuilder shortSongs = new StringBuilder();
            for (String song : playlist) {
                if (song.length() == 5) {
                    shortSongs.append(song).append(' ');
                }
            }
            String fiveLetters = shortSongs.toString().strip();   // "Intro Outro"
            show("fiveLetters", fiveLetters);
        }
        {
            Map<String, Integer> plays = new TreeMap<>(Map.of("Echoes", 12, "Intro", 40, "Outro", 7));
            StringBuilder report = new StringBuilder();
            for (Map.Entry<String, Integer> entry : plays.entrySet()) {
                report.append(entry.getKey()).append('=').append(entry.getValue()).append(' ');
            }
            String played = report.toString().strip();           // "Echoes=12 Intro=40 Outro=7"
            show("played", played);
            int totalPlays = 0;
            for (int count : plays.values()) {
                totalPlays += count;
            }
            int allPlays = totalPlays;                             // 59
            show("allPlays", allPlays);
        }
        {
            int[] volumes = {3, 5, 7};
            for (int v : volumes) {
                v = v * 2;                        // changes only the local copy
            }
            String unchanged = Arrays.toString(volumes);        // "[3, 5, 7]"
            show("unchanged", unchanged);
            for (int i = 0; i < volumes.length; i++) {
                volumes[i] = volumes[i] * 2;
            }
            String doubled = Arrays.toString(volumes);          // "[6, 10, 14]"
            show("doubled", doubled);
        }
        {
            List<String> titles = new ArrayList<>(List.of("intro", "outro"));
            titles.replaceAll(String::toUpperCase);
            List<String> upper = titles;                        // [INTRO, OUTRO]
            show("upper", upper);
        }
        {
            List<String> queue = new ArrayList<>(List.of("Intro", "Echoes", "Outro", "Bonus"));
            try { List<String> broken = removeSkipped(queue, "Intro"); show("broken", broken); } catch (Throwable _t) { System.out.println("broken -> " + _t); }
        }
        {
            List<String> tracks = new ArrayList<>(List.of("Intro", "Echoes", "Outro", "Bonus"));
            boolean removed = tracks.removeIf(t -> t.equals("Intro"));   // true
            show("removed", removed);
            List<String> rest = tracks;                                  // [Echoes, Outro, Bonus]
            show("rest", rest);
            Iterator<String> it = tracks.iterator();
            while (it.hasNext()) {
                if (it.next().startsWith("B")) {
                    it.remove();
                }
            }
            List<String> noBonus = tracks;                               // [Echoes, Outro]
            show("noBonus", noBonus);
        }
        {
            Stay stay = new Stay(LocalDate.of(2026, 12, 30), LocalDate.of(2027, 1, 2));
            int price = 0;
            for (LocalDate night : stay) {
                price += night.getMonthValue() == 12 ? 150 : 100;   // December rate
            }
            int bill = price;                                        // 400, two December nights and one January night
            show("bill", bill);
        }
        {
            List<String> favorites = null;                       // user has no favorites yet
            show("favorites", favorites);
            int count = 0;
            for (String song : Objects.requireNonNullElse(favorites, List.<String>of())) {
                count++;
            }
            int favoriteCount = count;                           // 0, no exception
            show("favoriteCount", favoriteCount);
        }
        {
            List<String> album = List.of("Intro", "Echoes", "Outro");
            StringBuilder backwards = new StringBuilder();
            for (String song : album.reversed()) {
                backwards.append(song.charAt(0));
            }
            String initials = backwards.toString();              // "OEI"
            show("initials", initials);
        }
        {
            List<String> album = List.of("Intro", "Echoes", "Outro");
            List<String> lines = new ArrayList<>();
            album.forEach(song -> lines.add("Now playing " + song));
            String first = lines.get(0);                          // "Now playing Intro"
            show("first", first);
            int[] bpm = {90, 120};
            int sumBpm = Arrays.stream(bpm).sum();                // 210, arrays have no forEach() method
            show("sumBpm", sumBpm);
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

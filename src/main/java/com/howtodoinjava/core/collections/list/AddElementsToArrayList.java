package com.howtodoinjava.core.collections.list;

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
 * Examples for the tutorial "Add Multiple Elements to ArrayList in Java: addAll() and More".
 * https://howtodoinjava.com/java/collections/arraylist/add-multiple-elements-arraylist/
 */
public class AddElementsToArrayList {
    static void insertAt(List<String> list, int index, String song) {
        int safeIndex = Math.clamp(index, 0, list.size());
        list.add(safeIndex, song);
    }
    static record Song(String title, int seconds) {}
    static int importSongs(List<String> queue, List<String> shared) {
        Set<String> queued = new HashSet<>(queue);
        List<String> fresh = shared.stream()
                .filter(queued::add)
                .toList();
        queue.addAll(fresh);
        return fresh.size();
    }
    public static void main(String[] args) throws Exception {
        {
            List<String> songs = new ArrayList<>(List.of("Help", "Girl"));         // [Help, Girl]
            show("songs", songs);
            boolean merged = songs.addAll(List.of("Rain", "Something"));           // true, songs = [Help, Girl, Rain, Something]
            show("merged", merged);
            boolean listed = Collections.addAll(songs, "Michelle", "Yesterday");   // true, songs = [Help, Girl, Rain, Something, Michelle, Yesterday]
            show("listed", listed);
            boolean front = songs.addAll(0, List.of("Taxman"));                    // true, songs = [Taxman, Help, Girl, Rain, Something, Michelle, Yesterday]
            show("front", front);
            boolean one = songs.add("Julia");                                      // true, songs ends with Julia
            show("one", one);
            songs.add(1, "Intro");                                                 // [Taxman, Intro, Help, Girl, Rain, Something, Michelle, Yesterday, Julia]
        }
        {
            List<String> queue = new ArrayList<>();
            boolean first = queue.add("Help");            // true, queue = [Help]
            show("first", first);
            boolean again = queue.add("Help");            // true, queue = [Help, Help]
            show("again", again);
            boolean empty = queue.add(null);              // true, queue = [Help, Help, null]
            show("empty", empty);
        }
        {
            List<String> upNext = new ArrayList<>(List.of("Girl", "Rain"));
            upNext.addFirst("Help");                      // [Help, Girl, Rain]
            upNext.addLast("Julia");                      // [Help, Girl, Rain, Julia]
            String playing = upNext.getFirst();           // "Help"
            show("playing", playing);
        }
        {
            List<String> songs = new ArrayList<>(List.of("Help", "Girl", "Rain"));
            songs.add(1, "Taxman");                       // [Help, Taxman, Girl, Rain]
            songs.add(0, "Intro");                        // [Intro, Help, Taxman, Girl, Rain]
            songs.add(songs.size(), "Outro");             // [Intro, Help, Taxman, Girl, Rain, Outro]
        }
        {
            List<String> songs = new ArrayList<>(List.of("Help", "Girl", "Rain"));
            try { songs.add(9, "Taxman");  } catch (Throwable _t) { System.out.println("-> " + _t); }
            try { songs.add(-1, "Taxman");  } catch (Throwable _t) { System.out.println("-> " + _t); }
        }
        {
            List<String> songs = new ArrayList<>(List.of("Help", "Girl"));
            insertAt(songs, 1, "Rain");                   // [Help, Rain, Girl]
            insertAt(songs, 50, "Julia");                 // [Help, Rain, Girl, Julia]
        }
        {
            List<String> playlist = new ArrayList<>(List.of("Help", "Girl"));
            List<String> shared = List.of("Rain", "Julia");
            boolean changed = playlist.addAll(shared);                 // true, playlist = [Help, Girl, Rain, Julia]
            show("changed", changed);
            boolean nothing = playlist.addAll(List.of());              // false
            show("nothing", nothing);
            boolean fromSet = playlist.addAll(new TreeSet<>(Set.of("Taxman", "Michelle")));   // true, playlist = [Help, Girl, Rain, Julia, Michelle, Taxman]
            show("fromSet", fromSet);
        }
        {
            List<String> playlist = new ArrayList<>(List.of("Help", "Girl", "Rain"));
            boolean middle = playlist.addAll(1, List.of("Taxman", "Julia"));   // true, playlist = [Help, Taxman, Julia, Girl, Rain]
            show("middle", middle);
            boolean start = playlist.addAll(0, List.of("Intro"));              // true, playlist = [Intro, Help, Taxman, Julia, Girl, Rain]
            show("start", start);
        }
        {
            List<String> playlist = new ArrayList<>(List.of("Help"));
            List<String> missing = null;
            try { boolean failed = playlist.addAll(missing); show("failed", failed); } catch (Throwable _t) { System.out.println("failed -> " + _t); }
            boolean safe = playlist.addAll(Objects.requireNonNullElse(missing, List.of()));   // false, playlist unchanged
            show("safe", safe);
        }
        {
            List<String> songs = new ArrayList<>(List.of("Help"));
            boolean added = Collections.addAll(songs, "Girl", "Rain", "Julia");   // true, songs = [Help, Girl, Rain, Julia]
            show("added", added);

            String[] fromFile = {"Taxman", "Michelle"};
            boolean copied = Collections.addAll(songs, fromFile);                 // true, songs = [Help, Girl, Rain, Julia, Taxman, Michelle]
            show("copied", copied);
        }
        {
            List<String> songs = new ArrayList<>(List.of("Help", "Girl", "Rain"));   // modifiable
            show("songs", songs);
            boolean more = songs.add("Julia");                                      // true
            show("more", more);

            List<String> fixed = List.of("Help", "Girl");
            try { boolean rejected = fixed.add("Rain"); show("rejected", rejected); } catch (Throwable _t) { System.out.println("rejected -> " + _t); }
        }
        {
            List<String> fromArray = Arrays.asList("Help", "Girl");
            String old = fromArray.set(1, "Rain");                // "Girl", fromArray = [Help, Rain]
            show("old", old);
            try { boolean grow = fromArray.add("Julia"); show("grow", grow); } catch (Throwable _t) { System.out.println("grow -> " + _t); }
            try { List<String> withNull = List.of("Help", null); show("withNull", withNull); } catch (Throwable _t) { System.out.println("withNull -> " + _t); }
        }
        {
            List<String> shortSongs = new ArrayList<>(List.of("Help"));
            List<Song> album = List.of(new Song("Girl", 153), new Song("Something", 182), new Song("Hey Jude", 431));
            List<String> picks = album.stream().filter(s -> s.seconds() < 240).map(Song::title).toList();   // [Girl, Something]
            show("picks", picks);
            boolean picked = shortSongs.addAll(picks);                                                       // true, shortSongs = [Help, Girl, Something]
            show("picked", picked);
        }
        {
            List<String> sideA = List.of("Help", "Girl");
            List<String> sideB = List.of("Rain", "Julia");
            List<String> both = Stream.concat(sideA.stream(), sideB.stream()).toList();   // [Help, Girl, Rain, Julia]
            show("both", both);
        }
        {
            List<String> queue = new ArrayList<>(List.of("Help", "Girl"));
            int imported = importSongs(queue, List.of("Girl", "Rain", "Julia", "Rain"));   // 2, queue = [Help, Girl, Rain, Julia]
            show("imported", imported);
        }
        {
            List<Object> mixed = new ArrayList<>();
            boolean nested = mixed.add(List.of("Help", "Girl"));   // true, mixed = [[Help, Girl]]
            show("nested", nested);
            int size = mixed.size();                               // 1
            show("size", size);
        }
        {
            List<String> loop = new ArrayList<>(List.of("Help", "Girl"));
            boolean doubled = loop.addAll(new ArrayList<>(loop));   // true, loop = [Help, Girl, Help, Girl]
            show("doubled", doubled);
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

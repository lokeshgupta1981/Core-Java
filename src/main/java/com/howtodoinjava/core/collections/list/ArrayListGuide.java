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
 * Examples for the tutorial "Java ArrayList Guide: Methods, Capacity, subList and Examples".
 * https://howtodoinjava.com/java/collections/arraylist/java-arraylist/
 */
public class ArrayListGuide {
    static record Song(String title, int seconds) {}
    static Optional<String> trackAt(List<String> list, int index) {
        if (index < 0 || index >= list.size()) {
            return Optional.empty();
        }
        return Optional.ofNullable(list.get(index));
    }
    static void removeWhileLooping(List<String> list, String target) {
        for (String song : list) {
            if (song.equals(target)) {
                list.remove(song);
            }
        }
    }
    public static void main(String[] args) throws Exception {
        {
            List<String> songs = new ArrayList<>(List.of("Intro", "Rain", "Echo"));
            boolean added = songs.add("Glow");              // true, [Intro, Rain, Echo, Glow]
            show("added", added);
            songs.add(1, "Drift");                          // [Intro, Drift, Rain, Echo, Glow]
            String second = songs.get(1);                   // "Drift"
            show("second", second);
            String old = songs.set(2, "Storm");             // "Rain", list is [Intro, Drift, Storm, Echo, Glow]
            show("old", old);
            boolean removed = songs.remove("Echo");         // true
            show("removed", removed);
            int count = songs.size();                       // 4
            show("count", count);
            int position = songs.indexOf("Glow");           // 3
            show("position", position);
            String first = songs.getFirst();                // "Intro" (Java 21)
            show("first", first);
            List<String> middle = songs.subList(1, 3);      // [Drift, Storm]
            show("middle", middle);
            String shown = middle.toString();               // "[Drift, Storm]"
            show("shown", shown);
        }
        {
            List<String> empty = new ArrayList<>();                         // [], 10 slots allocated on the first add
            show("empty", empty);
            List<String> sized = new ArrayList<>(500);                      // [], room for 500 elements before any resize
            show("sized", sized);
            List<String> fromSet = new ArrayList<>(Set.of("Rain"));         // [Rain]
            show("fromSet", fromSet);
            List<String> filled = new ArrayList<>(List.of("Intro", "Rain")); // [Intro, Rain]
            show("filled", filled);
            var inferred = new ArrayList<String>();                         // [], type is ArrayList<String>
            show("inferred", inferred);
        }
        {
            ArrayList<String> upper = Stream.of("rain", "echo").map(String::toUpperCase).collect(Collectors.toCollection(ArrayList::new));   // [RAIN, ECHO]
            show("upper", upper);
        }
        {
            List<Song> playlist = new ArrayList<>(List.of(new Song("Rain", 215), new Song("Echo", 187)));
            boolean found = playlist.contains(new Song("Rain", 215));     // true, records compare fields
            show("found", found);
            int where = playlist.indexOf(new Song("Echo", 187));          // 1
            show("where", where);
        }
        {
            List<String> queue = new ArrayList<>(List.of("Intro", "Rain"));
            boolean appended = queue.add("Echo");                            // true, [Intro, Rain, Echo]
            show("appended", appended);
            boolean appendedAll = queue.addAll(List.of("Glow", "Drift"));    // true, [Intro, Rain, Echo, Glow, Drift]
            show("appendedAll", appendedAll);
            queue.add(0, "Opener");                                          // [Opener, Intro, Rain, Echo, Glow, Drift]
            String replaced = queue.set(1, "Prelude");                       // "Intro"
            show("replaced", replaced);
            String removedAt = queue.remove(0);                              // "Opener"
            show("removedAt", removedAt);
            boolean removedValue = queue.remove("Glow");                     // true
            show("removedValue", removedValue);
            boolean removedShort = queue.removeIf(s -> s.length() < 5);      // true, removes Rain and Echo
            show("removedShort", removedShort);
            String state = queue.toString();                                 // "[Prelude, Drift]"
            show("state", state);
        }
        {
            List<Integer> ratings = new ArrayList<>(List.of(5, 3, 1));
            Integer atIndex = ratings.remove(1);                             // 3, removed index 1
            show("atIndex", atIndex);
            boolean byValue = ratings.remove(Integer.valueOf(1));            // true, removed the value 1
            show("byValue", byValue);
            String left = ratings.toString();                                // "[5]"
            show("left", left);
        }
        {
            List<String> tracks = new ArrayList<>(List.of("Intro", "Rain", "Echo", "Glow"));
            String opener = tracks.get(0);                                   // "Intro"
            show("opener", opener);
            String closer = tracks.get(tracks.size() - 1);                   // "Glow"
            show("closer", closer);
            try { String missing = tracks.get(4); show("missing", missing); } catch (Throwable _t) { System.out.println("missing -> " + _t); }
        }
        {
            List<String> tracks = List.of("Intro", "Rain", "Echo", "Glow");
            Optional<String> third = trackAt(tracks, 2);                    // Optional[Echo]
            show("third", third);
            Optional<String> tenth = trackAt(tracks, 9);                     // Optional.empty
            show("tenth", tenth);
        }
        {
            List<String> tracks = new ArrayList<>(List.of("Intro", "Rain", "Echo", "Glow"));
            String head = tracks.getFirst();                                 // "Intro"
            show("head", head);
            String tail = tracks.getLast();                                  // "Glow"
            show("tail", tail);
            List<String> backwards = tracks.reversed();                      // [Glow, Echo, Rain, Intro], a view
            show("backwards", backwards);
            try { String none = new ArrayList<String>().getFirst(); show("none", none); } catch (Throwable _t) { System.out.println("none -> " + _t); }
        }
        {
            List<Integer> plays = new ArrayList<>(List.of(1, 2, 2, 3, 4, 4, 4, 5));
            boolean hasTwo = plays.contains(2);                              // true
            show("hasTwo", hasTwo);
            int firstFour = plays.indexOf(4);                                // 4
            show("firstFour", firstFour);
            int lastFour = plays.lastIndexOf(4);                             // 6
            show("lastFour", lastFour);
            int noEight = plays.indexOf(8);                                  // -1
            show("noEight", noEight);
            int sortedHit = Collections.binarySearch(plays, 5);              // 7, list must be sorted
            show("sortedHit", sortedHit);
        }
        {
            List<Integer> counts = new ArrayList<>();
            int start = counts.size();                                       // 0
            show("start", start);
            counts.add(10);
            counts.add(20);
            int afterAdds = counts.size();                                   // 2
            show("afterAdds", afterAdds);
            counts.set(0, 15);
            int afterSet = counts.size();                                    // 2, set() does not change the size
            show("afterSet", afterSet);
            counts.remove(Integer.valueOf(20));
            int afterRemove = counts.size();                                 // 1
            show("afterRemove", afterRemove);
            counts.clear();
            boolean nothing = counts.isEmpty();                              // true
            show("nothing", nothing);
        }
        {
            ArrayList<String> rows = new ArrayList<>();
            rows.ensureCapacity(50_000);                                     // one allocation for 50,000 slots
            int stillEmpty = rows.size();                                    // 0, capacity is not size
            show("stillEmpty", stillEmpty);
        }
        {
            ArrayList<String> genres = new ArrayList<>(1_000);
            genres.addAll(List.of("rock", "jazz", "pop"));
            genres.trimToSize();                                             // capacity 1000 reduced to 3
            int genreCount = genres.size();                                  // 3
            show("genreCount", genreCount);
        }
        {
            List<String> setlist = new ArrayList<>(List.of("Intro", "Rain", "Echo", "Glow", "Drift", "Storm"));
            List<String> middle = setlist.subList(2, 4);                     // [Echo, Glow]
            show("middle", middle);
            List<String> fromTwo = setlist.subList(2, setlist.size());       // [Echo, Glow, Drift, Storm]
            show("fromTwo", fromTwo);
            String swapped = middle.set(0, "Wave");                          // "Echo", setlist is [Intro, Rain, Wave, Glow, Drift, Storm]
            show("swapped", swapped);
            setlist.subList(0, 2).clear();                                   // removes Intro and Rain
            String trimmed = setlist.toString();                             // "[Wave, Glow, Drift, Storm]"
            show("trimmed", trimmed);
        }
        {
            List<String> setlist = new ArrayList<>(List.of("Wave", "Glow", "Drift", "Storm"));
            List<String> firstTwo = setlist.subList(0, 2);
            setlist.add("Sunrise");
            try { int broken = firstTwo.size(); show("broken", broken); } catch (Throwable _t) { System.out.println("broken -> " + _t); }
        }
        {
            List<String> setlist = new ArrayList<>(List.of("Wave", "Glow", "Drift", "Storm"));
            List<String> page = List.copyOf(setlist.subList(0, 2));          // [Wave, Glow], independent copy
            show("page", page);
            setlist.add("Sunrise");
            String kept = page.toString();                                   // "[Wave, Glow]"
            show("kept", kept);
        }
        {
            List<String> lineup = List.of("Intro", "Rain", "Echo");
            StringBuilder line = new StringBuilder();
            for (String song : lineup) {
                line.append(song).append(' ');
            }
            String printed = line.toString().strip();                        // "Intro Rain Echo"
            show("printed", printed);
        }
        {
            List<String> mixes = new ArrayList<>(List.of("Rain", "Echo", "Glow"));
            try { removeWhileLooping(mixes, "Rain");  } catch (Throwable _t) { System.out.println("-> " + _t); }
            boolean gone = new ArrayList<>(List.of("Rain", "Echo")).removeIf(s -> s.equals("Rain"));   // true
            show("gone", gone);
        }
        {
            List<String> titles = new ArrayList<>(List.of("rain", "echo"));
            ListIterator<String> it = titles.listIterator();
            while (it.hasNext()) {
                it.set(it.next().toUpperCase());
            }
            String edited = titles.toString();                               // "[RAIN, ECHO]"
            show("edited", edited);
        }
        {
            List<Song> album = new ArrayList<>(List.of(new Song("Rain", 215), new Song("Echo", 187), new Song("Glow", 240)));
            album.sort(Comparator.comparing(Song::seconds));
            String shortest = album.getFirst().title();                      // "Echo"
            show("shortest", shortest);
            album.sort(Comparator.comparing(Song::title).reversed());
            String lastByName = album.getFirst().title();                    // "Rain"
            show("lastByName", lastByName);
        }
        {
            List<Song> album = List.of(new Song("Rain", 215), new Song("Echo", 187), new Song("Glow", 240));
            List<String> longOnes = album.stream().filter(s -> s.seconds() > 200).map(Song::title).toList();   // [Rain, Glow]
            show("longOnes", longOnes);
            ArrayList<String> editable = album.stream().map(Song::title).collect(Collectors.toCollection(ArrayList::new));   // [Rain, Echo, Glow]
            show("editable", editable);
            try { boolean changed = longOnes.add("Wave"); show("changed", changed); } catch (Throwable _t) { System.out.println("changed -> " + _t); }
        }
        {
            List<String> picks = new ArrayList<>(List.of("Rain", "Echo"));
            String[] asArray = picks.toArray(String[]::new);                 // [Rain, Echo]
            show("asArray", asArray);
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

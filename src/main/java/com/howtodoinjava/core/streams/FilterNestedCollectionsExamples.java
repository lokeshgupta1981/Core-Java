package com.howtodoinjava.core.streams;

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
 * Examples for the tutorial "Filter Nested Collections with Java Streams (flatMap, anyMatch)".
 * https://howtodoinjava.com/java/stream/filter-nested-collections/
 */
public class FilterNestedCollectionsExamples {
    static record Song(String title, int minutes, boolean explicit) {}
    static record Playlist(String name, List<Song> songs) {}
    static List<Playlist> library() {
        return List.of(
        new Playlist("Road Trip", List.of(new Song("Drive", 4, false), new Song("Highway", 7, true), new Song("Exit", 3, false))),
        new Playlist("Focus", List.of(new Song("Rain", 2, false), new Song("Calm", 3, false))),
        new Playlist("Party", List.of(new Song("Jump", 9, true), new Song("Dance", 5, false))));
    }
    static record Hit(String playlist, String song) {}
    static record Album(String name, List<Song> songs) {
        Album {
            songs = songs == null ? List.of() : List.copyOf(songs);
        }
    }
    static record CleanView(List<Playlist> playlists, int hiddenSongs) {}
    static CleanView cleanMode(List<Playlist> library) {
        List<Playlist> visible = library.stream()
                .map(p -> new Playlist(p.name(), p.songs().stream().filter(s -> !s.explicit()).toList()))
                .filter(p -> !p.songs().isEmpty())
                .toList();
        int hidden = (int) library.stream()
                .flatMap(p -> p.songs().stream())
                .filter(Song::explicit)
                .count();
        return new CleanView(visible, hidden);
    }
    public static void main(String[] args) throws Exception {
        {
            List<Playlist> library = library();

            List<String> longSongs = library.stream()
                    .flatMap(p -> p.songs().stream())
                    .filter(s -> s.minutes() > 6)
                    .map(Song::title)
                    .toList();                                          // [Highway, Jump]

            List<String> withLongSong = library.stream()
                    .filter(p -> p.songs().stream().anyMatch(s -> s.minutes() > 6))
                    .map(Playlist::name)
                    .toList();                                          // [Road Trip, Party]

            List<Playlist> trimmed = library.stream()
                    .map(p -> new Playlist(p.name(), p.songs().stream().filter(s -> s.minutes() > 6).toList()))
                    .filter(p -> !p.songs().isEmpty())
                    .toList();
            int trimmedSongs = trimmed.get(0).songs().size();           // 1
            show("trimmedSongs", trimmedSongs);
        }
        {
            List<Playlist> library = library();

            List<Song> explicitSongs = library.stream()
                    .flatMap(p -> p.songs().stream())
                    .filter(Song::explicit)
                    .toList();
            List<String> titles = explicitSongs.stream().map(Song::title).toList();   // [Highway, Jump]
            show("titles", titles);
            int totalMinutes = library.stream().flatMap(p -> p.songs().stream()).mapToInt(Song::minutes).sum();   // 33
            show("totalMinutes", totalMinutes);
        }
        {
            List<Playlist> library = library();

            List<String> longTitles = library.stream()
                    .<Song>mapMulti((playlist, sink) -> {
                for (Song song : playlist.songs()) {
                    if (song.minutes() > 6) {
                        sink.accept(song);
                    }
                }
            })
                    .map(Song::title)
                    .toList();
            List<String> result = longTitles;                           // [Highway, Jump]
            show("result", result);
        }
        {
            List<Playlist> library = library();

            List<Hit> hits = library.stream()
                    .flatMap(p -> p.songs().stream()
                    .filter(s -> s.minutes() > 6)
                    .map(s -> new Hit(p.name(), s.title())))
                    .toList();                                          // [Hit[playlist=Road Trip, song=Highway], Hit[playlist=Party, song=Jump]]
        }
        {
            List<Playlist> library = library();

            List<String> withExplicit = library.stream()
                    .filter(p -> p.songs().stream().anyMatch(Song::explicit))
                    .map(Playlist::name)
                    .toList();                                          // [Road Trip, Party]
        }
        {
            List<Playlist> library = library();

            List<String> familySafe = library.stream()
                    .filter(p -> p.songs().stream().noneMatch(Song::explicit))
                    .map(Playlist::name)
                    .toList();                                          // [Focus]
            List<String> allShort = library.stream()
                    .filter(p -> p.songs().stream().allMatch(s -> s.minutes() <= 4))
                    .map(Playlist::name)
                    .toList();                                          // [Focus]
        }
        {
            List<Playlist> library = library();

            List<Playlist> clean = library.stream()
                    .map(p -> new Playlist(p.name(), p.songs().stream().filter(s -> !s.explicit()).toList()))
                    .toList();
            List<Integer> sizes = clean.stream().map(p -> p.songs().size()).toList();   // [2, 2, 1]
            show("sizes", sizes);
            int originalSize = library.get(0).songs().size();                           // 3
            show("originalSize", originalSize);
        }
        {
            Map<String, List<Playlist>> byUser = Map.of("lokesh", library(), "alex", List.of());

            List<String> longSongs = byUser.values().stream()
                    .flatMap(List::stream)
                    .flatMap(p -> p.songs().stream())
                    .filter(s -> s.minutes() > 6)
                    .map(Song::title)
                    .toList();                                          // [Highway, Jump]
        }
        {
            List<Playlist> raw = new ArrayList<>(library());
            raw.add(new Playlist("Draft", null));

            try { List<String> broken = raw.stream().flatMap(p -> p.songs().stream()).map(Song::title).toList(); show("broken", broken); } catch (Throwable _t) { System.out.println("broken -> " + _t); }
        }
        {
            List<Album> albums = List.of(new Album("Demo", null), new Album("Live", List.of(new Song("Encore", 8, false))));

            List<String> titles = albums.stream()
                    .flatMap(a -> a.songs().stream())
                    .map(Song::title)
                    .toList();                                          // [Encore]
        }
        {
            List<Playlist> library = new ArrayList<>(library());
            library.add(new Playlist("Late Night", List.of(new Song("Neon", 4, true))));

            CleanView view = cleanMode(library);
            List<String> shown = view.playlists().stream().map(Playlist::name).toList();   // [Road Trip, Focus, Party]
            show("shown", shown);
            int hidden = view.hiddenSongs();                                                // 3
            show("hidden", hidden);
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

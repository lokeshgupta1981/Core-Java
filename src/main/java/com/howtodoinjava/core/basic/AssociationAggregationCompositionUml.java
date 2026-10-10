package com.howtodoinjava.core.basic;

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

/**
 * Examples for the tutorial "Association, Aggregation and Composition in Java (with UML)".
 * https://howtodoinjava.com/java/oops/association-aggregation-composition/
 */
public class AssociationAggregationCompositionUml {
    static class Playlist {
        private final String name;
        private final Listener owner;                           // association: knows its listener
        private final List<Entry> entries = new ArrayList<>();  // composition: owns its entries

        Playlist(String name, Listener owner) {
            this.name = name;
            this.owner = owner;
        }

        void add(Song song) {                                   // aggregation: songs come from the catalog
            entries.add(new Entry(song, entries.size() + 1));
        }

        List<Song> songs() {
            return entries.stream().map(Entry::song).toList();
        }

        private record Entry(Song song, int position) { }       // created and kept only by Playlist
    }
    static record Artist(String name) { }
    static record Song(String title, Artist artist) { }
    static class Listener {
        private final String name;
        private final Set<Artist> following = new LinkedHashSet<>();

        Listener(String name) { this.name = name; }

        void follow(Artist artist) { following.add(artist); }

        void unfollow(Artist artist) { following.remove(artist); }

        int followingCount() { return following.size(); }
    }
    public static void main(String[] args) throws Exception {
        {
            Artist nora = new Artist("Nora");
            Listener mia = new Listener("Mia");
            mia.follow(nora);
            mia.unfollow(nora);
            int followed = mia.followingCount();        // 0
            show("followed", followed);
            String stillThere = nora.name();            // "Nora", unfollowing removes only the link
            show("stillThere", stillThere);
        }
        {
            Listener mia = new Listener("Mia");
            Song blue = new Song("Blue", new Artist("Nora"));
            Playlist roadTrip = new Playlist("Road trip", mia);
            Playlist gym = new Playlist("Gym", mia);
            roadTrip.add(blue);
            gym.add(blue);
            boolean sameSong = roadTrip.songs().get(0) == gym.songs().get(0);   // true, one shared Song object
            show("sameSong", sameSong);
        }
        {
            Playlist chill = new Playlist("Chill", new Listener("Ana"));
            chill.add(new Song("Rain", new Artist("Leo")));
            List<Song> tracks = chill.songs();
            int trackCount = tracks.size();             // 1
            show("trackCount", trackCount);
            try { boolean added = tracks.add(new Song("Sun", new Artist("Leo"))); show("added", added); } catch (Throwable _t) { System.out.println("added -> " + _t); }
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

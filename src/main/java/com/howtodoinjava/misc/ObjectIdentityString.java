package com.howtodoinjava.misc;

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
 * Examples for the tutorial "Object Identity String in Java with Objects.toIdentityString()".
 * https://howtodoinjava.com/java/object-identity-string/
 */
public class ObjectIdentityString {
    static record Song(long id, String title) {}
    static class Track {
        private final long id;
        private final String title;

        Track(long id, String title) {
            this.id = id;
            this.title = title;
        }
    }
    static class Album {
        private final String name;

        Album(String name) {
            this.name = name;
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof Album other && name.equals(other.name);
        }

        @Override
        public int hashCode() {
            return name.hashCode();
        }
    }
    static String identityOf(Object object) {
        return object == null ? "null" : Objects.toIdentityString(object);
    }
    static String getIdentityString(Object object) {
        return object.getClass().getName() + "@" + Integer.toHexString(System.identityHashCode(object));
    }
    public static void main(String[] args) throws Exception {
        {
            Song song = new Song(1L, "Intro");
            String text = song.toString();                              // "Song[id=1, title=Intro]"
            show("text", text);
            String identity = Objects.toIdentityString(song);           // e.g. com.howtodoinjava.Song@1b6d3586
            show("identity", identity);
            boolean sameBuild = identity.equals(song.getClass().getName() + "@" + Integer.toHexString(System.identityHashCode(song)));  // true
            show("sameBuild", sameBuild);
        }
        {
            Track track = new Track(1L, "Intro");
            String printed = track.toString();                          // e.g. com.howtodoinjava.Track@6e5e91e4
            show("printed", printed);
            boolean isIdentity = printed.equals(Objects.toIdentityString(track));  // true
            show("isIdentity", isIdentity);
        }
        {
            Album one = new Album("Blue");
            Album two = new Album("Blue");
            boolean sameText = one.toString().equals(two.toString());   // true
            show("sameText", sameText);
            boolean sameIdentity = Objects.toIdentityString(one).equals(Objects.toIdentityString(two));  // false
            show("sameIdentity", sameIdentity);
        }
        {
            Song song = new Song(1L, "Intro");
            String details = song.toString();                           // "Song[id=1, title=Intro]"
            show("details", details);
            String identity = Objects.toIdentityString(song);           // e.g. com.howtodoinjava.Song@7591083d
            show("identity", identity);
        }
        {
            try { String missing = Objects.toIdentityString(null); show("missing", missing); } catch (Throwable _t) { System.out.println("missing -> " + _t); }
            String safe = identityOf(null);                             // "null"
            show("safe", safe);
        }
        {
            Song song = new Song(1L, "Intro");
            String legacy = getIdentityString(song);                    // e.g. com.howtodoinjava.Song@7591083d
            show("legacy", legacy);
            boolean same = legacy.equals(Objects.toIdentityString(song));  // true
            show("same", same);
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

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
import java.math.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Examples for the tutorial "Java hashCode() and equals(): Contract, Examples, Records".
 * https://howtodoinjava.com/java/basics/java-hashcode-equals-methods/
 */
public class HashcodeEqualsHashsetLookup {
    static class PlainSong {
        private final String title;
        private final String artist;

        PlainSong(String title, String artist) {
            this.title = title;
            this.artist = artist;
        }
    }
    static class HalfSong {
        private final String title;
        private final String artist;

        HalfSong(String title, String artist) {
            this.title = title;
            this.artist = artist;
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof HalfSong other
                    && title.equals(other.title)
                    && artist.equals(other.artist);
        }
    }
    static final class Song {
        private final String title;
        private final String artist;

        Song(String title, String artist) {
            this.title = Objects.requireNonNull(title);
            this.artist = Objects.requireNonNull(artist);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            return o instanceof Song other
                    && title.equals(other.title)
                    && artist.equals(other.artist);
        }

        @Override
        public int hashCode() {
            return Objects.hash(title, artist);
        }

        @Override
        public String toString() {
            return title + " by " + artist;
        }
    }
    static record Track(String title, String artist) {}
    static record Album(String name, String[] tracks) {}
    static record AlbumList(String name, List<String> tracks) {}
    static class EditableSong {
        private String title;

        EditableSong(String title) {
            this.title = title;
        }

        void setTitle(String title) {
            this.title = title;
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof EditableSong other && title.equals(other.title);
        }

        @Override
        public int hashCode() {
            return title.hashCode();
        }
    }
    static final class CatalogSong {
        private final String title;
        private final String artist;

        CatalogSong(String title, String artist) {
            this.title = title;
            this.artist = artist;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof CatalogSong other)) {
                return false;
            }
            return new EqualsBuilder()
                    .append(title, other.title)
                    .append(artist, other.artist)
                    .isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(17, 37).append(title).append(artist).toHashCode();
        }
    }
    public static void main(String[] args) throws Exception {
        {
            Song first = new Song("Hello", "Adele");
            Song second = new Song("Hello", "Adele");
            boolean sameObject = first == second;                        // false
            show("sameObject", sameObject);
            boolean equal = first.equals(second);                        // true
            show("equal", equal);
            boolean sameHash = first.hashCode() == second.hashCode();    // true
            show("sameHash", sameHash);
            Set<Song> playlist = new HashSet<>(List.of(first, second));
            int size = playlist.size();                                  // 1
            show("size", size);
        }
        {
            PlainSong one = new PlainSong("Hello", "Adele");
            PlainSong two = new PlainSong("Hello", "Adele");
            boolean sameValue = one.equals(two);       // false
            show("sameValue", sameValue);
            boolean itself = one.equals(one);          // true
            show("itself", itself);
            boolean text = new String("Hello").equals("Hello");   // true
            show("text", text);
        }
        {
            int aa = "Aa".hashCode();                  // 2112
            show("aa", aa);
            int bb = "BB".hashCode();                  // 2112
            show("bb", bb);
            boolean sameText = "Aa".equals("BB");      // false
            show("sameText", sameText);
        }
        {
            Set<HalfSong> merged = new HashSet<>();
            merged.add(new HalfSong("Hello", "Adele"));
            merged.add(new HalfSong("Hello", "Adele"));
            int songCount = merged.size();                                        // 2
            show("songCount", songCount);
            boolean found = merged.contains(new HalfSong("Hello", "Adele"));      // false
            show("found", found);
            boolean equalSongs = new HalfSong("Hello", "Adele").equals(new HalfSong("Hello", "Adele"));   // true
            show("equalSongs", equalSongs);
        }
        {
            Song hello = new Song("Hello", "Adele");
            Song copy = new Song("Hello", "Adele");
            Song other = new Song("Hello", "Lionel Richie");
            boolean same = hello.equals(copy);                     // true
            show("same", same);
            boolean differ = hello.equals(other);                  // false
            show("differ", differ);
            boolean nullSafe = hello.equals(null);                 // false
            show("nullSafe", nullSafe);
            boolean hashMatch = hello.hashCode() == copy.hashCode();   // true
            show("hashMatch", hashMatch);
            Map<Song, Integer> plays = new HashMap<>();
            plays.put(hello, 12);
            Integer count = plays.get(copy);                       // 12
            show("count", count);
        }
        {
            Integer smallA = 100;
            Integer smallB = 100;
            boolean smallSame = smallA == smallB;                  // true, cached
            show("smallSame", smallSame);
            Integer bigA = 1000;
            Integer bigB = 1000;
            boolean bigSame = bigA == bigB;                        // false
            show("bigSame", bigSame);
            boolean bigEqual = Objects.equals(bigA, bigB);         // true
            show("bigEqual", bigEqual);
        }
        {
            Track t1 = new Track("Hello", "Adele");
            Track t2 = new Track("Hello", "Adele");
            boolean recordEqual = t1.equals(t2);                   // true
            show("recordEqual", recordEqual);
            boolean recordHash = t1.hashCode() == t2.hashCode();   // true
            show("recordHash", recordHash);
            String shown = t1.toString();                          // "Track[title=Hello, artist=Adele]"
            show("shown", shown);
        }
        {
            Album album1 = new Album("25", new String[] {"Hello"});
            Album album2 = new Album("25", new String[] {"Hello"});
            boolean albumEqual = album1.equals(album2);            // false
            show("albumEqual", albumEqual);
            boolean listEqual = new AlbumList("25", List.of("Hello")).equals(new AlbumList("25", List.of("Hello")));   // true
            show("listEqual", listEqual);
        }
        {
            EditableSong song = new EditableSong("Helo");
            Set<EditableSong> library = new HashSet<>();
            library.add(song);
            song.setTitle("Hello");
            boolean stillThere = library.contains(song);           // false
            show("stillThere", stillThere);
            boolean removed = library.remove(song);                // false
            show("removed", removed);
            int librarySize = library.size();                      // 1
            show("librarySize", librarySize);
        }
        {
            boolean builderEqual = new CatalogSong("Hello", "Adele").equals(new CatalogSong("Hello", "Adele"));   // true
            show("builderEqual", builderEqual);
            boolean builderHash = new CatalogSong("Hello", "Adele").hashCode() == new CatalogSong("Hello", "Adele").hashCode();   // true
            show("builderHash", builderHash);
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

package com.howtodoinjava.core.serialization;

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
 * Examples for the tutorial "Externalizable vs Serializable in Java: Differences & Example".
 * https://howtodoinjava.com/java/serialization/externalizable-vs-serializable/
 */
public class SerializableVsExternalizable {
    static class Track implements Serializable {
        String title;
        int seconds;

        Track(String title, int seconds) {
            this.title = title;
            this.seconds = seconds;
        }
    }

    static class ExtTrack implements Externalizable {
        String title;
        int seconds;

        public ExtTrack() { }                      // required by Externalizable

        ExtTrack(String title, int seconds) {
            this.title = title;
            this.seconds = seconds;
        }

        @Override
        public void writeExternal(ObjectOutput out) throws IOException {
            out.writeUTF(title);
            out.writeInt(seconds);
        }

        @Override
        public void readExternal(ObjectInput in) throws IOException {
            title = in.readUTF();
            seconds = in.readInt();
        }
    }
    static byte[] toBytes(Object value) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try (ObjectOutputStream out = new ObjectOutputStream(buffer)) {
            out.writeObject(value);
        }
        return buffer.toByteArray();
    }
    @SuppressWarnings("unchecked")
    static <T> T fromBytes(byte[] bytes) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bytes))) {
            return (T) in.readObject();
        }
    }
    static class Media {
        String genre = "unknown";
        Media() { }
    }

    static class Song extends Media implements Serializable {
        String title;
        transient String lyricsCache;

        Song(String title, String genre) {
            this.title = title;
            this.genre = genre;
            this.lyricsCache = "la la la";
        }
    }
    static class PlayerSettings implements Externalizable {
        @Serial
        private static final long serialVersionUID = 1L;

        private int volume;
        private String lastTrack;                 // may be null
        private boolean shuffle;
        private byte[] coverCache;                // never stored

        public PlayerSettings() { }

        PlayerSettings(int volume, String lastTrack, boolean shuffle) {
            this.volume = volume;
            this.lastTrack = lastTrack;
            this.shuffle = shuffle;
            this.coverCache = new byte[1024];
        }

        @Override
        public void writeExternal(ObjectOutput out) throws IOException {
            out.writeInt(volume);
            out.writeBoolean(lastTrack != null);
            if (lastTrack != null) {
                out.writeUTF(lastTrack);
            }
            out.writeBoolean(shuffle);
        }

        @Override
        public void readExternal(ObjectInput in) throws IOException {
            volume = in.readInt();
            lastTrack = in.readBoolean() ? in.readUTF() : null;
            shuffle = in.readBoolean();
        }

        int volume() { return volume; }
        String lastTrack() { return lastTrack; }
        boolean shuffle() { return shuffle; }
        byte[] coverCache() { return coverCache; }
    }
    static class SwappedTrack implements Externalizable {
        int seconds;
        int plays;

        public SwappedTrack() { }

        SwappedTrack(int seconds, int plays) {
            this.seconds = seconds;
            this.plays = plays;
        }

        @Override
        public void writeExternal(ObjectOutput out) throws IOException {
            out.writeInt(seconds);
            out.writeInt(plays);
        }

        @Override
        public void readExternal(ObjectInput in) throws IOException {
            plays = in.readInt();                 // wrong order
            seconds = in.readInt();
        }
    }
    static class NoDefaultCtor implements Externalizable {
        int volume;

        NoDefaultCtor(int volume) {
            this.volume = volume;
        }

        @Override
        public void writeExternal(ObjectOutput out) throws IOException {
            out.writeInt(volume);
        }

        @Override
        public void readExternal(ObjectInput in) throws IOException {
            volume = in.readInt();
        }
    }
    static class VersionedSettings implements Externalizable {
        private static final int FORMAT = 2;

        int volume;
        String preset = "flat";

        public VersionedSettings() { }

        @Override
        public void writeExternal(ObjectOutput out) throws IOException {
            out.writeByte(FORMAT);
            out.writeInt(volume);
            out.writeUTF(preset);                 // added in format 2
        }

        @Override
        public void readExternal(ObjectInput in) throws IOException {
            int format = in.readByte();
            volume = in.readInt();
            if (format >= 2) {
                preset = in.readUTF();
            }
        }
    }
    public static void main(String[] args) throws Exception {
        {
            byte[] plain = toBytes(new Track("Intro", 95));
            byte[] custom = toBytes(new ExtTrack("Intro", 95));
            int plainSize = plain.length;                     // 83, includes field names and types
            show("plainSize", plainSize);
            int customSize = custom.length;                   // 49, only our two values after the class name
            show("customSize", customSize);

            ExtTrack copy = fromBytes(custom);
            String title = copy.title;                        // "Intro"
            show("title", title);
            int seconds = copy.seconds;                       // 95
            show("seconds", seconds);
        }
        {
            Song restored = fromBytes(toBytes(new Song("Intro", "rock")));
            String songTitle = restored.title;              // "Intro"
            show("songTitle", songTitle);
            String genre = restored.genre;                  // "unknown", Media is not Serializable
            show("genre", genre);
            String lyrics = restored.lyricsCache;           // null, transient
            show("lyrics", lyrics);
        }
        {
            PlayerSettings saved = new PlayerSettings(70, "Intro", true);
            PlayerSettings loaded = fromBytes(toBytes(saved));
            int volume = loaded.volume();                  // 70
            show("volume", volume);
            String lastTrack = loaded.lastTrack();         // "Intro"
            show("lastTrack", lastTrack);
            boolean shuffle = loaded.shuffle();            // true
            show("shuffle", shuffle);
            byte[] cover = loaded.coverCache();            // null, never written
            show("cover", cover);

            PlayerSettings empty = fromBytes(toBytes(new PlayerSettings(30, null, false)));
            String noTrack = empty.lastTrack();            // null
            show("noTrack", noTrack);
        }
        {
            SwappedTrack swapped = fromBytes(toBytes(new SwappedTrack(95, 3)));
            int swappedSeconds = swapped.seconds;          // 3, should be 95
            show("swappedSeconds", swappedSeconds);
            int swappedPlays = swapped.plays;              // 95, should be 3
            show("swappedPlays", swappedPlays);
        }
        {
            byte[] written = toBytes(new NoDefaultCtor(50));
            try { NoDefaultCtor broken = fromBytes(written); show("broken", broken); } catch (Throwable _t) { System.out.println("broken -> " + _t); }
        }
        {
            ExtTrack noTitle = new ExtTrack(null, 60);
            try { byte[] failed = toBytes(noTitle); show("failed", failed); } catch (Throwable _t) { System.out.println("failed -> " + _t); }
        }
        {
            List<Track> plainList = new ArrayList<>();
            List<ExtTrack> customList = new ArrayList<>();
            for (int i = 0; i < 1000; i++) {
                plainList.add(new Track("Track " + i, i));
                customList.add(new ExtTrack("Track " + i, i));
            }
            int plainListSize = toBytes(plainList).length;      // 22009
            show("plainListSize", plainListSize);
            int customListSize = toBytes(customList).length;    // 23973
            show("customListSize", customListSize);
        }
        {
            ObjectInputFilter onlySettings = ObjectInputFilter.allowFilter(
            type -> type == PlayerSettings.class, ObjectInputFilter.Status.REJECTED);
            byte[] stored = toBytes(new PlayerSettings(70, "Intro", true));
            try (ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(stored))) {
                in.setObjectInputFilter(onlySettings);
                PlayerSettings accepted = (PlayerSettings) in.readObject();
                int acceptedVolume = accepted.volume();     // 70
            }
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

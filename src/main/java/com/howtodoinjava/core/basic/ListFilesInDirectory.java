package com.howtodoinjava.core.basic;

import java.util.regex.*;
import java.net.*;
import java.text.*;
import java.nio.file.attribute.*;
import java.security.*;
import java.nio.charset.*;
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
 * Examples for the tutorial "List All Files in a Directory in Java (list, walk, find)".
 * https://howtodoinjava.com/java8/java-8-list-all-files-example/
 */
public class ListFilesInDirectory {
    static Path createMusicFolder() throws IOException {
        Path music = Files.createTempDirectory("music");
        Files.createDirectories(music.resolve("rock/live"));
        Files.createDirectories(music.resolve("jazz"));
        for (String file : List.of("playlist.m3u", ".thumbs.db", "rock/intro.mp3", "rock/anthem.mp3",
        "rock/live/encore.mp3", "jazz/blue.mp3", "jazz/notes.txt")) {
            Files.writeString(music.resolve(file), file);
        }
        return music;
    }
    static void collectFiles(Path dir, List<Path> result) throws IOException {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
            for (Path path : stream) {
                if (Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS)) {
                    collectFiles(path, result);
                } else if (Files.isRegularFile(path)) {
                    result.add(path);
                }
            }
        }
    }
    static List<Path> listReadableFiles(Path start) throws IOException {
        List<Path> files = new ArrayList<>();
        Files.walkFileTree(start, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                if (attrs.isRegularFile()) {
                    files.add(file);
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException e) {
                System.err.println("Skipped " + file + ": " + e);
                return FileVisitResult.CONTINUE;
            }
        });
        return files;
    }
    public static void main(String[] args) throws Exception {
        {
            Path music = createMusicFolder();

            try (Stream<Path> entries = Files.list(music)) {
                List<String> topFiles = entries.filter(Files::isRegularFile).map(p -> p.getFileName().toString()).sorted().toList();   // [.thumbs.db, playlist.m3u]
            }

            try (Stream<Path> tree = Files.walk(music)) {
                List<String> allFiles = tree.filter(Files::isRegularFile).map(p -> music.relativize(p).toString()).sorted().toList();   // [.thumbs.db, jazz/blue.mp3, jazz/notes.txt, playlist.m3u, rock/anthem.mp3, rock/intro.mp3, rock/live/encore.mp3]
            }

            try (Stream<Path> found = Files.find(music, 2, (p, attrs) -> attrs.isRegularFile() && p.toString().endsWith(".mp3"))) {
                List<String> songs = found.map(p -> p.getFileName().toString()).sorted().toList();   // [anthem.mp3, blue.mp3, intro.mp3]
            }
        }
        {
            Path music = createMusicFolder();

            try (Stream<Path> entries = Files.list(music)) {
                List<Path> files = entries.filter(Files::isRegularFile).sorted().toList();
                int count = files.size();                                    // 2
                String first = files.getFirst().getFileName().toString();   // ".thumbs.db"
            }

            try (Stream<Path> entries = Files.list(music)) {
                List<String> folders = entries.filter(Files::isDirectory).map(p -> p.getFileName().toString()).sorted().toList();   // [jazz, rock]
            }
        }
        {
            Path music = createMusicFolder();
            List<String> names = new ArrayList<>();

            try (DirectoryStream<Path> stream = Files.newDirectoryStream(music)) {
                for (Path path : stream) {
                    if (Files.isRegularFile(path)) {
                        names.add(path.getFileName().toString());
                    }
                }
            }
            Collections.sort(names);
            List<String> topFiles = names;   // [.thumbs.db, playlist.m3u]
            show("topFiles", topFiles);
        }
        {
            Path music = createMusicFolder();
            List<String> playlists = new ArrayList<>();

            try (DirectoryStream<Path> stream = Files.newDirectoryStream(music, "*.{mp3,m3u}")) {
                stream.forEach(p -> playlists.add(p.getFileName().toString()));
            }
            List<String> matched = playlists;   // [playlist.m3u]
            show("matched", matched);
        }
        {
            Path music = createMusicFolder();

            try (Stream<Path> tree = Files.walk(music)) {
                List<Path> all = tree.toList();
                int entries = all.size();                                    // 11
                boolean startIncluded = all.getFirst().equals(music);        // true
            }

            try (Stream<Path> tree = Files.walk(music)) {
                List<String> folders = tree.filter(Files::isDirectory).map(p -> music.relativize(p).toString()).sorted().toList();   // [, jazz, rock, rock/live]
            }
        }
        {
            Path music = createMusicFolder();

            try (Stream<Path> tree = Files.walk(music, 2)) {
                List<String> twoLevels = tree.filter(Files::isRegularFile).map(p -> music.relativize(p).toString()).sorted().toList();   // [.thumbs.db, jazz/blue.mp3, jazz/notes.txt, playlist.m3u, rock/anthem.mp3, rock/intro.mp3]
            }
        }
        {
            Path music = createMusicFolder();

            try (Stream<Path> found = Files.find(music, Integer.MAX_VALUE, (p, attrs) -> attrs.isRegularFile() && attrs.size() > 14)) {
                List<String> large = found.map(p -> music.relativize(p).toString()).sorted().toList();   // [rock/anthem.mp3, rock/live/encore.mp3]
            }
        }
        {
            Path music = createMusicFolder();
            List<Path> collected = new ArrayList<>();
            collectFiles(music, collected);
            int fileCount = collected.size();   // 7
            show("fileCount", fileCount);
        }
        {
            File notADirectory = Files.createTempFile("song", ".mp3").toFile();
            File[] children = notADirectory.listFiles();   // null
            show("children", children);
        }
        {
            Path music = createMusicFolder();

            try (Stream<Path> tree = Files.walk(music)) {
                List<String> mp3Files = tree.filter(Files::isRegularFile)
                        .filter(p -> p.getFileName().toString().endsWith(".mp3"))
                        .map(p -> p.getFileName().toString())
                        .sorted()
                        .toList();   // [anthem.mp3, blue.mp3, encore.mp3, intro.mp3]
            }

            Path blue = music.resolve("jazz/blue.mp3");
            boolean pathEndsWith = blue.endsWith(".mp3");                         // false
            show("pathEndsWith", pathEndsWith);
            boolean nameEndsWith = blue.getFileName().toString().endsWith(".mp3");   // true
            show("nameEndsWith", nameEndsWith);
        }
        {
            Path music = createMusicFolder();
            PathMatcher audio = FileSystems.getDefault().getPathMatcher("glob:**.{mp3,m3u}");

            try (Stream<Path> tree = Files.walk(music)) {
                List<String> audioFiles = tree.filter(Files::isRegularFile)
                        .filter(p -> audio.matches(music.relativize(p)))
                        .map(p -> music.relativize(p).toString())
                        .sorted()
                        .toList();   // [jazz/blue.mp3, playlist.m3u, rock/anthem.mp3, rock/intro.mp3, rock/live/encore.mp3]
            }
        }
        {
            Path music = createMusicFolder();

            try (Stream<Path> entries = Files.list(music)) {
                List<String> hidden = entries.filter(p -> p.toFile().isHidden()).map(p -> p.getFileName().toString()).toList();   // [.thumbs.db]
            }

            try (Stream<Path> tree = Files.walk(music)) {
                long visibleFiles = tree.filter(Files::isRegularFile).filter(p -> !p.toFile().isHidden()).count();   // 6
            }
        }
        {
            Path missing = Path.of("no-such-folder");
            try { Stream<Path> broken = Files.list(missing); show("broken", broken); } catch (Throwable _t) { System.out.println("broken -> " + _t); }
        }
        {
            Path music = createMusicFolder();
            List<Path> readable = listReadableFiles(music);
            int readableCount = readable.size();   // 7
            show("readableCount", readableCount);
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

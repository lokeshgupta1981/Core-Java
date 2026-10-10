package com.howtodoinjava.io;

import java.nio.*;
import java.nio.channels.*;
import java.util.zip.*;
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
 * Examples for the tutorial "Copy a Directory Recursively in Java (Files.walk Examples)".
 * https://howtodoinjava.com/java/io/how-to-copy-directories-in-java/
 */
public class CopyDirectoryRecursively {
    static void copyDirectory(Path source, Path target, CopyOption... options) throws IOException {
        try (Stream<Path> paths = Files.walk(source)) {
            for (Path entry : (Iterable<Path>) paths::iterator) {
                Path destination = target.resolve(source.relativize(entry).toString());
                Files.copy(entry, destination, options);
            }
        }
    }
    static List<String> listTree(Path root) throws IOException {
        try (Stream<Path> paths = Files.walk(root)) {
            return paths.filter(p -> !p.equals(root))
                    .map(p -> root.relativize(p).toString().replace('\\', '/'))
                    .sorted()
                    .toList();
        }
    }
    static void mergeDirectory(Path source, Path target) throws IOException {
        try (Stream<Path> paths = Files.walk(source)) {
            for (Path entry : (Iterable<Path>) paths::iterator) {
                Path destination = target.resolve(source.relativize(entry).toString());
                if (Files.isDirectory(entry)) {
                    Files.createDirectories(destination);
                } else {
                    Files.copy(entry, destination, StandardCopyOption.REPLACE_EXISTING);
                }
            }
        }
    }
    static void copyWithTimes(Path source, Path target) throws IOException {
        Files.walkFileTree(source, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                Files.createDirectories(target.resolve(source.relativize(dir).toString()));
                return FileVisitResult.CONTINUE;
            }
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.copy(file, target.resolve(source.relativize(file).toString()),
                StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING);
                return FileVisitResult.CONTINUE;
            }
            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException failure) throws IOException {
                if (failure != null) {
                    throw failure;
                }
                Path copy = target.resolve(source.relativize(dir).toString());
                Files.setLastModifiedTime(copy, Files.getLastModifiedTime(dir));
                return FileVisitResult.CONTINUE;
            }
        });
    }
    public static void main(String[] args) throws Exception {
        {
            Path recipes = Files.createTempDirectory("recipes");
            Path desserts = Files.createDirectories(recipes.resolve("desserts"));
            Path cakeFile = Files.writeString(desserts.resolve("cake.txt"), "flour, eggs");
            Path backup = recipes.resolveSibling(recipes.getFileName() + "-backup");
            try (Stream<Path> tree = Files.walk(recipes)) {
                for (Path source : (Iterable<Path>) tree::iterator) {
                    Files.copy(source, backup.resolve(recipes.relativize(source)));
                }
            }
            String cake = Files.readString(backup.resolve("desserts/cake.txt"));     // "flour, eggs"
            show("cake", cake);
        }
        {
            Path music = Files.createTempDirectory("music");
            Files.createDirectories(music.resolve("jazz/live"));
            Files.writeString(music.resolve("jazz/live/set1.txt"), "track 1");
            Files.writeString(music.resolve("playlist.txt"), "jazz");
            Path musicCopy = music.resolveSibling(music.getFileName() + "-copy");
            copyDirectory(music, musicCopy);
            List<String> copied = listTree(musicCopy);       // [jazz, jazz/live, jazz/live/set1.txt, playlist.txt]
            show("copied", copied);
        }
        {
            Path photos = Files.createTempDirectory("photos");
            Files.writeString(photos.resolve("beach.jpg"), "jpg");
            Path album = Files.createTempDirectory("album");
            Files.writeString(album.resolve("old.jpg"), "jpg");
            try { copyDirectory(photos, album);  } catch (Throwable _t) { System.out.println("-> " + _t); }
            try { copyDirectory(photos, album, StandardCopyOption.REPLACE_EXISTING);  } catch (Throwable _t) { System.out.println("-> " + _t); }
        }
        {
            Path assets = Files.createTempDirectory("assets");
            Files.createDirectories(assets.resolve("css"));
            Files.writeString(assets.resolve("css/site.css"), "body{}");
            Path publicDir = Files.createTempDirectory("public");
            Files.writeString(publicDir.resolve("robots.txt"), "allow");
            mergeDirectory(assets, publicDir);
            List<String> merged = listTree(publicDir);       // [css, css/site.css, robots.txt]
            show("merged", merged);
        }
        {
            Path notes = Files.createTempDirectory("notes");
            Path week = Files.createDirectories(notes.resolve("week1"));
            Files.writeString(week.resolve("monday.txt"), "plan");
            FileTime oldTime = FileTime.from(Instant.parse("2026-03-01T08:00:00Z"));
            Files.setLastModifiedTime(week, oldTime);
            Path notesCopy = notes.resolveSibling(notes.getFileName() + "-copy");
            copyWithTimes(notes, notesCopy);
            FileTime copiedTime = Files.getLastModifiedTime(notesCopy.resolve("week1"));   // 2026-03-01T08:00:00Z
            show("copiedTime", copiedTime);
        }
        {
            Path template = Files.createTempDirectory("template");
            Files.createDirectories(template.resolve(".git"));
            Files.writeString(template.resolve(".git/HEAD"), "main");
            Files.createDirectories(template.resolve("src"));
            Files.writeString(template.resolve("src/App.java"), "class App {}");
            Files.writeString(template.resolve("README.md"), "demo");
            Path project = template.resolveSibling(template.getFileName() + "-project");
            try (Stream<Path> paths = Files.walk(template)) {
                for (Path entry : (Iterable<Path>) paths.filter(p -> !template.relativize(p).startsWith(".git"))::iterator) {
                    Files.copy(entry, project.resolve(template.relativize(entry).toString()));
                }
            }
            List<String> created = listTree(project);        // [README.md, src, src/App.java]
            show("created", created);
        }
        {
            Path layout = Files.createTempDirectory("layout");
            Files.createDirectories(layout.resolve("2026/jan"));
            Files.writeString(layout.resolve("2026/jan/report.txt"), "data");
            Path empty = layout.resolveSibling(layout.getFileName() + "-empty");
            try (Stream<Path> paths = Files.walk(layout)) {
                for (Path dir : (Iterable<Path>) paths.filter(Files::isDirectory)::iterator) {
                    Files.createDirectories(empty.resolve(layout.relativize(dir).toString()));
                }
            }
            List<String> folders = listTree(empty);          // [2026, 2026/jan]
            show("folders", folders);
        }
        {
            Path site = Files.createTempDirectory("site");
            Path release = Files.createDirectories(site.resolve("v2"));
            Files.writeString(release.resolve("index.html"), "v2");
            Files.createSymbolicLink(site.resolve("current"), Path.of("v2"));
            Path siteCopy = site.resolveSibling(site.getFileName() + "-copy");
            copyDirectory(site, siteCopy, LinkOption.NOFOLLOW_LINKS);
            boolean stillLink = Files.isSymbolicLink(siteCopy.resolve("current"));   // true
            show("stillLink", stillLink);
        }
        {
            Path inbox = Files.createTempDirectory("inbox");
            Files.createDirectories(inbox.resolve("old"));
            Files.writeString(inbox.resolve("a.txt"), "a");
            Path flat = Files.createTempDirectory("flat");
            try (Stream<Path> entries = Files.list(inbox)) {
                for (Path file : (Iterable<Path>) entries.filter(Files::isRegularFile)::iterator) {
                    Files.copy(file, flat.resolve(file.getFileName().toString()));
                }
            }
            List<String> flatFiles = listTree(flat);         // [a.txt]
            show("flatFiles", flatFiles);
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

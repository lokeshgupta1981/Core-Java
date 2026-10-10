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
 * Examples for the tutorial "Java Delete Directory Recursively: Files.walk() and More".
 * https://howtodoinjava.com/java/io/delete-directory-recursively/
 */
public class DeleteDirectoryRecursively {
    static void deleteRecursively(Path dir) throws IOException {
        try (Stream<Path> tree = Files.walk(dir)) {
            for (Path path : tree.sorted(Comparator.reverseOrder()).toList()) {
                Files.delete(path);
            }
        }
    }
    static void deleteTree(Path root) throws IOException {
        Files.walkFileTree(root, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                if (exc != null) {
                    throw exc;                    // a folder could not be read
                }
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }
    static void deleteContents(Path dir) throws IOException {
        try (Stream<Path> tree = Files.walk(dir)) {
            for (Path path : tree.sorted(Comparator.reverseOrder()).toList()) {
                if (!path.equals(dir)) {
                    Files.delete(path);
                }
            }
        }
    }
    static void deleteOlderThan(Path dir, Instant cutoff) throws IOException {
        try (Stream<Path> tree = Files.walk(dir)) {
            for (Path file : tree.filter(Files::isRegularFile).toList()) {
                if (Files.getLastModifiedTime(file).toInstant().isBefore(cutoff)) {
                    Files.delete(file);
                }
            }
        }
    }
    public static void main(String[] args) throws Exception {
        {
            Path cache = Files.createTempDirectory("cache");
            Path thumbs = Files.createDirectories(cache.resolve("thumbs/2026"));    // nested folders
            show("thumbs", thumbs);
            Path image = Files.writeString(thumbs.resolve("cat.jpg"), "bytes");      // a file inside
            show("image", image);
            try (Stream<Path> tree = Files.walk(cache)) {
                for (Path path : tree.sorted(Comparator.reverseOrder()).toList()) {
                    Files.delete(path);                                              // children first
                }
            }
            boolean gone = Files.notExists(cache);                                   // true
            show("gone", gone);
        }
        {
            Path build = Files.createTempDirectory("build");
            Path classes = Files.createDirectories(build.resolve("classes/com/shop"));
            Path main = Files.writeString(classes.resolve("Main.class"), "x");
            Path jar = Files.writeString(build.resolve("shop.jar"), "x");
            deleteRecursively(build);
            boolean buildGone = Files.notExists(build);                  // true
            show("buildGone", buildGone);
        }
        {
            Path account = Files.createTempDirectory("account-7");
            Path album = Files.createDirectories(account.resolve("albums/summer"));
            Path photo = Files.writeString(album.resolve("beach.jpg"), "x");
            deleteTree(account);
            boolean accountGone = Files.notExists(account);              // true
            show("accountGone", accountGone);
        }
        {
            Path base = Files.createTempDirectory("links");
            Path keep = Files.createDirectories(base.resolve("outside"));
            Path kept = Files.writeString(keep.resolve("keep.txt"), "important");
            Path tmp = Files.createDirectories(base.resolve("tmp"));
            Path link = Files.createSymbolicLink(tmp.resolve("shared"), keep);
            deleteRecursively(tmp);
            boolean tmpGone = Files.notExists(tmp);                       // true
            show("tmpGone", tmpGone);
            boolean targetSafe = Files.exists(kept);                      // true
            show("targetSafe", targetSafe);
        }
        {
            Path dir = Files.createTempDirectory("docs");
            Path draft = Files.writeString(dir.resolve("draft.txt"), "v1");
            Files.delete(draft);                                            // removes the file
            boolean deletedAgain = Files.deleteIfExists(draft);             // false, already gone
            show("deletedAgain", deletedAgain);
            Path ghost = dir.resolve("ghost.txt");
            try { Files.delete(ghost);  } catch (Throwable _t) { System.out.println("-> " + _t); }
        }
        {
            Path dir = Files.createTempDirectory("docs");
            Path notes = Files.writeString(dir.resolve("notes.txt"), "x");
            try { Files.delete(dir);  } catch (Throwable _t) { System.out.println("-> " + _t); }
        }
        {
            File folder = Files.createTempDirectory("old").toFile();
            File log = new File(folder, "app.log");
            boolean created = log.createNewFile();                          // true
            show("created", created);
            boolean folderDeleted = folder.delete();                        // false, not empty
            show("folderDeleted", folderDeleted);
            boolean logDeleted = log.delete();                              // true
            show("logDeleted", logDeleted);
            boolean nowDeleted = folder.delete();                           // true
            show("nowDeleted", nowDeleted);
        }
        {
            Path incoming = Files.createTempDirectory("incoming");
            Path batch = Files.createDirectories(incoming.resolve("batch-1"));
            Path csv = Files.writeString(batch.resolve("orders.csv"), "x");
            deleteContents(incoming);
            boolean folderKept = Files.isDirectory(incoming);              // true
            show("folderKept", folderKept);
            long entries = incoming.toFile().list().length;                // 0
            show("entries", entries);
        }
        {
            Path reports = Files.createTempDirectory("reports");
            Path stale = Files.writeString(reports.resolve("q1.pdf"), "x");
            Path touched = Files.setLastModifiedTime(stale, FileTime.from(Instant.parse("2026-01-01T00:00:00Z"))); // January
            show("touched", touched);
            Path fresh = Files.writeString(reports.resolve("q3.pdf"), "x");
            deleteOlderThan(reports, Instant.parse("2026-06-01T00:00:00Z"));
            boolean staleGone = Files.notExists(stale);                     // true
            show("staleGone", staleGone);
            boolean freshKept = Files.exists(fresh);                        // true
            show("freshKept", freshKept);
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

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
 * Examples for the tutorial "Symbolic Links in Java: Create, Read and Detect Symlinks".
 * https://howtodoinjava.com/java/io/working-with-symbolic-links/
 */
public class SymbolicLinks {
    static void switchRelease(Path currentLink, Path newTarget) throws IOException {
        Path temp = currentLink.resolveSibling(currentLink.getFileName() + ".next");
        Files.deleteIfExists(temp);
        Files.createSymbolicLink(temp, newTarget);
        Files.move(temp, currentLink, StandardCopyOption.ATOMIC_MOVE);
    }
    public static void main(String[] args) throws Exception {
        {
            Path releases = Files.createTempDirectory("releases");
            Path v2 = Files.createDirectories(releases.resolve("v2"));
            Path current = Files.createSymbolicLink(releases.resolve("current"), Path.of("v2"));
            boolean isLink = Files.isSymbolicLink(current);                           // true
            show("isLink", isLink);
            Path target = Files.readSymbolicLink(current);                            // v2
            show("target", target);
            boolean isDir = Files.isDirectory(current);                               // true
            show("isDir", isDir);
            boolean linkItself = Files.isDirectory(current, LinkOption.NOFOLLOW_LINKS);   // false
            show("linkItself", linkItself);
        }
        {
            Path data = Files.createTempDirectory("data");
            Path settings = Files.writeString(data.resolve("settings.json"), "{}");
            Path absoluteLink = Files.createSymbolicLink(data.resolve("abs-link.json"), settings);
            Path relativeLink = Files.createSymbolicLink(data.resolve("rel-link.json"), Path.of("settings.json"));
            String viaRelative = Files.readString(relativeLink);                      // "{}"
            show("viaRelative", viaRelative);
            try { Path again = Files.createSymbolicLink(relativeLink, Path.of("settings.json")); show("again", again); } catch (Throwable _t) { System.out.println("again -> " + _t); }
        }
        {
            Path docs = Files.createTempDirectory("docs");
            Path guide = Files.writeString(docs.resolve("guide.txt"), "read me");
            Path shortcut = Files.createSymbolicLink(docs.resolve("shortcut.txt"), Path.of("guide.txt"));
            boolean onLink = Files.isSymbolicLink(shortcut);                          // true
            show("onLink", onLink);
            boolean onFile = Files.isSymbolicLink(guide);                             // false
            show("onFile", onFile);
            boolean onMissing = Files.isSymbolicLink(docs.resolve("nothing.txt"));    // false
            show("onMissing", onMissing);
            boolean regularTarget = Files.isRegularFile(shortcut);                    // true
            show("regularTarget", regularTarget);
        }
        {
            Path shop = Files.createTempDirectory("shop");
            Path catalog = Files.createDirectories(shop.resolve("catalog-2026"));
            Path latest = Files.createSymbolicLink(shop.resolve("latest"), Path.of("catalog-2026"));
            Path stored = Files.readSymbolicLink(latest);                             // catalog-2026
            show("stored", stored);
            Path resolved = latest.resolveSibling(stored);
            boolean sameDir = Files.isSameFile(resolved, catalog);                    // true
            show("sameDir", sameDir);
            String realName = latest.toRealPath().getFileName().toString();           // "catalog-2026"
            show("realName", realName);
            try { Path notALink = Files.readSymbolicLink(catalog); show("notALink", notALink); } catch (Throwable _t) { System.out.println("notALink -> " + _t); }
        }
        {
            Path media = Files.createTempDirectory("media");
            Path song = Files.writeString(media.resolve("song.mp3"), "mp3");
            Path favorite = Files.createSymbolicLink(media.resolve("favorite.mp3"), Path.of("song.mp3"));
            Path copiedFile = Files.copy(favorite, media.resolve("copy1.mp3"));
            Path copiedLink = Files.copy(favorite, media.resolve("copy2.mp3"), LinkOption.NOFOLLOW_LINKS);
            boolean firstIsLink = Files.isSymbolicLink(copiedFile);                   // false
            show("firstIsLink", firstIsLink);
            boolean secondIsLink = Files.isSymbolicLink(copiedLink);                  // true
            show("secondIsLink", secondIsLink);
            Files.delete(favorite);
            boolean songKept = Files.exists(song);                                    // true
            show("songKept", songKept);
        }
        {
            Path project = Files.createTempDirectory("project");
            Path nestedDir = Files.createDirectories(project.resolve("src"));
            Path backLink = Files.createSymbolicLink(nestedDir.resolve("up"), project);
            long entries;
            try (Stream<Path> paths = Files.walk(project)) {
                entries = paths.count();
            }
            long counted = entries;                                                   // 3
            show("counted", counted);
            try (Stream<Path> paths = Files.walk(project, FileVisitOption.FOLLOW_LINKS)) {
                try { long looped = paths.count();  } catch (Throwable _t) { System.out.println("-> " + _t); }
            }
        }
        {
            Path cache = Files.createTempDirectory("cache");
            Path tempFile = Files.writeString(cache.resolve("page.html"), "html");
            Path cached = Files.createSymbolicLink(cache.resolve("index.html"), Path.of("page.html"));
            Files.delete(tempFile);
            boolean targetThere = Files.exists(cached);                               // false
            show("targetThere", targetThere);
            boolean linkThere = Files.exists(cached, LinkOption.NOFOLLOW_LINKS);      // true
            show("linkThere", linkThere);
            boolean broken = Files.isSymbolicLink(cached) && Files.notExists(cached); // true
            show("broken", broken);
            try { String content = Files.readString(cached); show("content", content); } catch (Throwable _t) { System.out.println("content -> " + _t); }
        }
        {
            Path photos = Files.createTempDirectory("photos");
            Path original = Files.writeString(photos.resolve("beach.jpg"), "jpg");
            Path second = Files.createLink(photos.resolve("beach-copy.jpg"), original);
            boolean sameData = Files.isSameFile(original, second);                    // true
            show("sameData", sameData);
            Files.writeString(original, "edited");
            String throughSecond = Files.readString(second);                          // "edited"
            show("throughSecond", throughSecond);
            Files.delete(original);
            String survived = Files.readString(second);                               // "edited"
            show("survived", survived);
        }
        {
            Path app = Files.createTempDirectory("app");
            Files.createDirectories(app.resolve("release-1"));
            Files.createDirectories(app.resolve("release-2"));
            Path live = Files.createSymbolicLink(app.resolve("current"), Path.of("release-1"));
            switchRelease(live, Path.of("release-2"));
            Path nowServing = Files.readSymbolicLink(live);                           // release-2
            show("nowServing", nowServing);
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

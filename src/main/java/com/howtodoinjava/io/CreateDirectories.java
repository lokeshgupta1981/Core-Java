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
 * Examples for the tutorial "Java Create Directory: createDirectories() vs mkdirs()".
 * https://howtodoinjava.com/java/io/create-directories/
 */
public class CreateDirectories {
    static boolean tryLock(Path lockDir) throws IOException {
        try {
            Files.createDirectory(lockDir);
            return true;
        } catch (FileAlreadyExistsException e) {
            return false;                         // another instance holds the lock
        }
    }
    static Path ensureDirectory(Path dir) throws IOException {
        try {
            return Files.createDirectory(dir);
        } catch (FileAlreadyExistsException e) {
            if (Files.isDirectory(dir)) {
                return dir;                         // already there, fine
            }
            throw e;                                // a file has that name
        }
    }
    public static void main(String[] args) throws Exception {
        {
            Path music = Files.createTempDirectory("music");                   // empty folder
            show("music", music);
            Path album = Files.createDirectories(music.resolve("rock/2026/live")); // creates rock, 2026 and live
            show("album", album);
            boolean isDir = Files.isDirectory(album);                              // true
            show("isDir", isDir);
            Path same = Files.createDirectories(album);                            // no error, already there
            show("same", same);
            Path jazz = Files.createDirectory(music.resolve("jazz"));              // creates one folder
            show("jazz", jazz);
            try { Path again = Files.createDirectory(music.resolve("jazz")); show("again", again); } catch (Throwable _t) { System.out.println("again -> " + _t); }
        }
        {
            Path shared = Files.createTempDirectory("shared");
            boolean firstRun = tryLock(shared.resolve("billing.lock"));   // true
            show("firstRun", firstRun);
            boolean secondRun = tryLock(shared.resolve("billing.lock"));  // false
            show("secondRun", secondRun);
        }
        {
            Path uploads = Files.createTempDirectory("uploads");
            LocalDate today = LocalDate.of(2026, 10, 10);
            Path dayDir = uploads.resolve(String.valueOf(today.getYear()))
                    .resolve("%02d".formatted(today.getMonthValue()))
                    .resolve("%02d".formatted(today.getDayOfMonth()));
            Path created = Files.createDirectories(dayDir);
            String relative = uploads.relativize(created).toString();      // "2026/10/10"
            show("relative", relative);
            Path photo = Files.writeString(created.resolve("cat.jpg"), "bytes");
            boolean saved = Files.exists(photo);                            // true
            show("saved", saved);
        }
        {
            Path uploads = Files.createTempDirectory("uploads");
            Path blocker = Files.writeString(uploads.resolve("2026"), "not a folder");
            try { Path month = Files.createDirectories(uploads.resolve("2026/10")); show("month", month); } catch (Throwable _t) { System.out.println("month -> " + _t); }
        }
        {
            Path app = Files.createTempDirectory("app");
            Path logs = ensureDirectory(app.resolve("logs"));
            Path logsAgain = ensureDirectory(app.resolve("logs"));
            boolean same = logs.equals(logsAgain);                          // true
            show("same", same);
            Path report = Files.writeString(app.resolve("report"), "x");
            try { Path clash = ensureDirectory(report); show("clash", clash); } catch (Throwable _t) { System.out.println("clash -> " + _t); }
        }
        {
            Path home = Files.createTempDirectory("home");
            FileAttribute<Set<PosixFilePermission>> ownerOnly =
            PosixFilePermissions.asFileAttribute(PosixFilePermissions.fromString("rwx------"));
            Path sessions = Files.createDirectories(home.resolve("myapp/sessions"), ownerOnly);
            String appPerms = PosixFilePermissions.toString(Files.getPosixFilePermissions(home.resolve("myapp"))); // "rwx------"
            show("appPerms", appPerms);
            String sessionPerms = PosixFilePermissions.toString(Files.getPosixFilePermissions(sessions));         // "rwx------"
            show("sessionPerms", sessionPerms);
        }
        {
            File base = Files.createTempDirectory("legacy").toFile();
            File nested = new File(base, "photos/2026");
            boolean one = nested.mkdir();                                   // false, parent photos is missing
            show("one", one);
            boolean all = nested.mkdirs();                                  // true, creates photos and 2026
            show("all", all);
            boolean repeat = nested.mkdirs();                               // false, already exists
            show("repeat", repeat);
        }
        {
            File base = Files.createTempDirectory("legacy").toFile();
            File cache = new File(base, "cache/thumbs");
            boolean ok = cache.mkdirs() || cache.isDirectory();            // true
            show("ok", ok);
            boolean okAgain = cache.mkdirs() || cache.isDirectory();       // true
            show("okAgain", okAgain);
        }
        {
            Path work = Files.createTempDirectory("unzip-");
            boolean isDir = Files.isDirectory(work);                         // true
            show("isDir", isDir);
            String name = work.getFileName().toString().substring(0, 6);    // "unzip-"
            show("name", name);
        }
        {
            Path appDir = Path.of(System.getProperty("user.home"), ".myapp");   // for example /home/lokesh/.myapp
            show("appDir", appDir);
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

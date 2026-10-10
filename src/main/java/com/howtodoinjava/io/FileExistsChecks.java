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
 * Examples for the tutorial "Java Check If File Exists: Files.exists() vs notExists()".
 * https://howtodoinjava.com/java/io/how-to-check-if-file-exists-in-java/
 */
public class FileExistsChecks {
    static String status(Path path) {
        if (Files.exists(path)) {
            return "exists";
        }
        if (Files.notExists(path)) {
            return "missing";
        }
        return "unknown, check the permissions";
    }
    static String readOrDefault(Path file, String fallback) throws IOException {
        try {
            return Files.readString(file);
        } catch (NoSuchFileException e) {
            return fallback;
        }
    }
    public static void main(String[] args) throws Exception {
        {
            Path config = Files.createTempFile("app", ".properties");   // a file that exists
            show("config", config);
            boolean exists = Files.exists(config);                       // true
            show("exists", exists);
            boolean isFile = Files.isRegularFile(config);                // true
            show("isFile", isFile);
            boolean isDir = Files.isDirectory(config);                   // false
            show("isDir", isDir);
            Path missing = config.resolveSibling("missing.properties");
            boolean gone = Files.notExists(missing);                     // true
            show("gone", gone);
        }
        {
            Path dir = Files.createTempDirectory("conf");
            Path present = Files.writeString(dir.resolve("app.yml"), "port: 8080");
            String found = status(present);                              // "exists"
            show("found", found);
            String absent = status(dir.resolve("db.yml"));               // "missing"
            show("absent", absent);
        }
        {
            Path data = Files.createTempDirectory("data");
            Path orders = Files.writeString(data.resolve("orders.csv"), "id,total");
            boolean folderIsFile = Files.isRegularFile(data);            // false
            show("folderIsFile", folderIsFile);
            boolean ordersIsFile = Files.isRegularFile(orders);          // true
            show("ordersIsFile", ordersIsFile);
            boolean missingIsDir = Files.isDirectory(data.resolve("x")); // false, no exception
            show("missingIsDir", missingIsDir);
        }
        {
            Path releases = Files.createTempDirectory("releases");
            Path current = Files.createSymbolicLink(releases.resolve("current"), releases.resolve("v42"));
            boolean targetExists = Files.exists(current);                              // false, v42 is missing
            show("targetExists", targetExists);
            boolean targetMissing = Files.notExists(current);                          // true
            show("targetMissing", targetMissing);
            boolean linkExists = Files.exists(current, LinkOption.NOFOLLOW_LINKS);     // true
            show("linkExists", linkExists);
            boolean isLink = Files.isSymbolicLink(current);                            // true
            show("isLink", isLink);
        }
        {
            Path dir = Files.createTempDirectory("app");
            Path overrides = dir.resolve("overrides.properties");
            if (Files.exists(overrides)) {                 // the file can vanish right after this line
                String text = Files.readString(overrides);
            }
        }
        {
            Path dir = Files.createTempDirectory("app");
            String settings = readOrDefault(dir.resolve("overrides.properties"), "defaults"); // "defaults"
            show("settings", settings);
            Path saved = Files.writeString(dir.resolve("overrides.properties"), "theme=dark");
            String custom = readOrDefault(saved, "defaults");                                 // "theme=dark"
            show("custom", custom);
        }
        {
            Path scripts = Files.createTempDirectory("scripts");
            Path backup = Files.writeString(scripts.resolve("backup.sh"), "echo ok");
            boolean canRead = Files.isReadable(backup);                  // true
            show("canRead", canRead);
            boolean canRun = Files.isExecutable(backup);                 // false, no x permission yet
            show("canRun", canRun);
            Path made = Files.setPosixFilePermissions(backup, PosixFilePermissions.fromString("rwxr-xr-x")); // adds x
            show("made", made);
            boolean canRunNow = Files.isExecutable(backup);              // true
            show("canRunNow", canRunNow);
        }
        {
            File report = Files.createTempFile("report", ".pdf").toFile();
            boolean exists = report.exists();                            // true
            show("exists", exists);
            boolean isFile = report.isFile();                            // true
            show("isFile", isFile);
            boolean isDir = report.isDirectory();                        // false
            show("isDir", isDir);
            boolean sameAnswer = Files.exists(report.toPath());          // true
            show("sameAnswer", sameAnswer);
        }
        {
            boolean hasLogback = Thread.currentThread().getContextClassLoader().getResource("logback.xml") != null;   // false when missing
            show("hasLogback", hasLogback);
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

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

import java.time.format.DateTimeFormatter;

/**
 * Examples for the tutorial "File Creation Date in Java: BasicFileAttributes Guide".
 * https://howtodoinjava.com/java/io/get-file-creation-timestamp/
 */
public class FileCreationDate {

    public static void main(String[] args) throws Exception {
        {
            Path report = Files.writeString(Files.createTempFile("report", ".txt"), "draft");
            BasicFileAttributes attrs = Files.readAttributes(report, BasicFileAttributes.class);
            FileTime created = attrs.creationTime();                                  // time the file was created, in UTC
            show("created", created);
            LocalDateTime local = LocalDateTime.ofInstant(created.toInstant(), ZoneId.systemDefault());   // same moment in our time zone
            show("local", local);
            Files.setLastModifiedTime(report, FileTime.from(Instant.parse("2020-01-01T00:00:00Z")));
            FileTime createdAgain = Files.readAttributes(report, BasicFileAttributes.class).creationTime();
            boolean unchanged = createdAgain.equals(created);                         // true on Windows, macOS and Linux with JDK 22+
            show("unchanged", unchanged);
        }
        {
            Path upload = Files.write(Files.createTempFile("photo", ".jpg"), new byte[2048]);
            BasicFileAttributes info = Files.readAttributes(upload, BasicFileAttributes.class);
            FileTime createdTime = info.creationTime();                               // creation time of photo.jpg
            show("createdTime", createdTime);
            long uploadSize = info.size();                                            // 2048
            show("uploadSize", uploadSize);
            boolean regular = info.isRegularFile();                                   // true
            show("regular", regular);
        }
        {
            Path invoice = Files.writeString(Files.createTempFile("invoice", ".txt"), "Total 80");
            FileTime byName = (FileTime) Files.getAttribute(invoice, "creationTime");
            FileTime withView = (FileTime) Files.getAttribute(invoice, "basic:creationTime");
            boolean sameValue = byName.equals(withView);                              // true
            show("sameValue", sameValue);
            try { Object typo = Files.getAttribute(invoice, "createdTime"); show("typo", typo); } catch (Throwable _t) { System.out.println("typo -> " + _t); }
        }
        {
            FileTime time = FileTime.from(Instant.parse("2026-03-15T09:30:00Z"));
            Instant instant = time.toInstant();                                       // 2026-03-15T09:30:00Z
            show("instant", instant);
            ZonedDateTime berlin = instant.atZone(ZoneId.of("Europe/Berlin"));        // 2026-03-15T10:30+01:00[Europe/Berlin]
            show("berlin", berlin);
            String shown = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm", Locale.ENGLISH).format(berlin);   // "15 Mar 2026, 10:30"
            show("shown", shown);
            long millis = time.toMillis();                                            // 1773567000000
            show("millis", millis);
            long days = time.to(TimeUnit.DAYS);                                       // 20527
            show("days", days);
        }
        {
            Path restored = Files.writeString(Files.createTempFile("restored", ".txt"), "data");
            FileTime original = FileTime.from(Instant.parse("2024-05-01T12:00:00Z"));
            BasicFileAttributeView view = Files.getFileAttributeView(restored, BasicFileAttributeView.class);
            view.setTimes(null, null, original);
            FileTime after = Files.readAttributes(restored, BasicFileAttributes.class).creationTime();
            boolean applied = after.equals(original);                                 // false on Linux, true on Windows and macOS
            show("applied", applied);
        }
        {
            Path exports = Files.createTempDirectory("exports");
            Path oldExport = Files.writeString(exports.resolve("march.csv"), "a,b");
            Path newExport = Files.writeString(exports.resolve("october.csv"), "c,d");
            Files.setLastModifiedTime(oldExport, FileTime.from(Instant.now().minus(Duration.ofDays(45))));
            Instant cutoff = Instant.now().minus(Duration.ofDays(30));
            List<String> expired;
            try (Stream<Path> found = Files.find(exports, 1,
            (path, a) -> a.isRegularFile() && a.lastModifiedTime().toInstant().isBefore(cutoff))) {
                expired = found.map(p -> p.getFileName().toString()).sorted().toList();
            }
            List<String> toDelete = expired;                                          // [march.csv]
            show("toDelete", toDelete);
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

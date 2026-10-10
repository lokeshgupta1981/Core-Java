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
 * Examples for the tutorial "Java FileFilter: Filter Files by Size, Date and Type".
 * https://howtodoinjava.com/java/io/java-filefilter-example/
 */
public class FileFilterGuide {
    static File[] listMatching(File dir, FileFilter filter) throws IOException {
        File[] entries = dir.listFiles(filter);
        if (entries == null) {
            throw new IOException("Not a readable directory: " + dir);
        }
        Arrays.sort(entries, Comparator.comparing(File::getName));
        return entries;
    }
    static File sampleFolder() throws IOException {
        Path dir = Files.createTempDirectory("reports");
        Files.writeString(dir.resolve("jan.csv"), "a".repeat(2_000));
        Files.writeString(dir.resolve("feb.csv"), "b".repeat(50));
        Files.writeString(dir.resolve("summary.pdf"), "c".repeat(5_000));
        Files.writeString(dir.resolve(".lock"), "");
        Files.createDirectory(dir.resolve("archive"));
        Files.setLastModifiedTime(dir.resolve("jan.csv"),
        FileTime.from(Instant.now().minus(Duration.ofDays(40))));
        return dir.toFile();
    }
    static List<String> deleteOldLogs(File logDir, Duration maxAge) throws IOException {
        long cutoff = Instant.now().minus(maxAge).toEpochMilli();
        FileFilter oldLogs = file -> file.isFile()
                && file.getName().endsWith(".log")
                && file.lastModified() < cutoff;
        List<String> deleted = new ArrayList<>();
        for (File log : listMatching(logDir, oldLogs)) {
            Files.deleteIfExists(log.toPath());
            deleted.add(log.getName());
        }
        return deleted;
    }
    public static void main(String[] args) throws Exception {
        {
            Path folder = Files.createTempDirectory("logs");
            Files.writeString(folder.resolve("app.log"), "started");
            Files.writeString(folder.resolve("notes.txt"), "todo");
            Files.createDirectory(folder.resolve("archive.log"));
            FileFilter logFiles = file -> file.isFile() && file.getName().endsWith(".log");
            File[] matches = folder.toFile().listFiles(logFiles);
            int found = matches.length;                     // 1 (app.log)
            show("found", found);
            String first = matches[0].getName();            // "app.log"
            show("first", first);
        }
        {
            FileFilter anonymous = new FileFilter() {
                @Override
                public boolean accept(File file) {
                    return file.getName().endsWith(".log");
                }
            };
            FileFilter lambda = file -> file.getName().endsWith(".log");
            boolean same = anonymous.accept(new File("app.log")) == lambda.accept(new File("app.log"));   // true
            show("same", same);
        }
        {
            File missing = new File("no-such-folder");
            File[] entries = missing.listFiles(File::isFile);    // null
            show("entries", entries);
            try { int size = entries.length; show("size", size); } catch (Throwable _t) { System.out.println("size -> " + _t); }
        }
        {
            Path reports = Files.createTempDirectory("reports");
            Files.writeString(reports.resolve("b.csv"), "1,2");
            Files.writeString(reports.resolve("a.csv"), "3,4");
            File[] csv = listMatching(reports.toFile(), f -> f.getName().endsWith(".csv"));
            String names = Arrays.stream(csv).map(File::getName).toList().toString();   // "[a.csv, b.csv]"
            show("names", names);
            try { File[] none = listMatching(new File("no-such-folder"), File::isFile); show("none", none); } catch (Throwable _t) { System.out.println("none -> " + _t); }
        }
        {
            FileFilter csvFiles = file -> file.isFile()
                    && file.getName().toLowerCase(Locale.ROOT).endsWith(".csv");
            File[] csv = listMatching(sampleFolder(), csvFiles);
            String csvNames = Arrays.stream(csv).map(File::getName).toList().toString();   // "[feb.csv, jan.csv]"
            show("csvNames", csvNames);
        }
        {
            File folder = sampleFolder();
            File[] dirs = listMatching(folder, File::isDirectory);
            File[] hidden = listMatching(folder, File::isHidden);
            String dirNames = Arrays.stream(dirs).map(File::getName).toList().toString();     // "[archive]"
            show("dirNames", dirNames);
            String hiddenNames = Arrays.stream(hidden).map(File::getName).toList().toString(); // "[.lock]"
            show("hiddenNames", hiddenNames);
        }
        {
            File folder = sampleFolder();
            FileFilter largerThan1Kb = file -> file.isFile() && file.length() > 1_024;
            long cutoff = Instant.now().minus(Duration.ofDays(30)).toEpochMilli();
            FileFilter olderThan30Days = file -> file.isFile() && file.lastModified() < cutoff;
            String large = Arrays.stream(listMatching(folder, largerThan1Kb)).map(File::getName).toList().toString();  // "[jan.csv, summary.pdf]"
            show("large", large);
            String old = Arrays.stream(listMatching(folder, olderThan30Days)).map(File::getName).toList().toString();  // "[jan.csv]"
            show("old", old);
        }
        {
            File folder = sampleFolder();
            Predicate<File> isCsv = f -> f.getName().endsWith(".csv");
            Predicate<File> isLarge = f -> f.length() > 1_024;
            Predicate<File> visibleFile = Predicate.not(File::isHidden).and(File::isFile);
            FileFilter largeCsv = isCsv.and(isLarge).and(visibleFile)::test;
            FileFilter csvOrPdf = isCsv.or(f -> f.getName().endsWith(".pdf"))::test;
            String both = Arrays.stream(listMatching(folder, largeCsv)).map(File::getName).toList().toString();   // "[jan.csv]"
            show("both", both);
            String either = Arrays.stream(listMatching(folder, csvOrPdf)).map(File::getName).toList().toString(); // "[feb.csv, jan.csv, summary.pdf]"
            show("either", either);
        }
        {
            Path logDir = Files.createTempDirectory("orders");
            Files.writeString(logDir.resolve("orders-old.log"), "x");
            Files.writeString(logDir.resolve("orders-today.log"), "y");
            Files.setLastModifiedTime(logDir.resolve("orders-old.log"), FileTime.from(Instant.now().minus(Duration.ofDays(45))));
            List<String> removed = deleteOldLogs(logDir.toFile(), Duration.ofDays(30));   // [orders-old.log]
            show("removed", removed);
            boolean todayKept = Files.exists(logDir.resolve("orders-today.log"));        // true
            show("todayKept", todayKept);
        }
        {
            Path folder = sampleFolder().toPath();
            DirectoryStream.Filter<Path> largeFiles = p -> Files.isRegularFile(p) && Files.size(p) > 1_024;
            List<String> names = new ArrayList<>();
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(folder, largeFiles)) {
                for (Path p : stream) {
                    names.add(p.getFileName().toString());
                }
            }
            Collections.sort(names);
            String large = names.toString();                 // "[jan.csv, summary.pdf]"
            show("large", large);
        }
        {
            Path nowhere = Path.of("no-such-folder");
            try { DirectoryStream<Path> stream = Files.newDirectoryStream(nowhere, Files::isRegularFile); show("stream", stream); } catch (Throwable _t) { System.out.println("stream -> " + _t); }
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

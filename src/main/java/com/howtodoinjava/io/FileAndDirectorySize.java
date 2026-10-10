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
 * Examples for the tutorial "Get File Size in Java: Files.size(), Folder Size, KB and MB".
 * https://howtodoinjava.com/java/io/file-directory-size/
 */
public class FileAndDirectorySize {
    static void checkUploadSize(Path upload, long maxBytes) throws IOException {
        long size = Files.size(upload);
        if (size > maxBytes) {
            throw new IllegalArgumentException("File too large: " + size + " bytes");
        }
    }
    static long sizeOf(Path file) {
        try {
            return Files.size(file);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
    static long directorySize(Path dir) throws IOException {
        try (Stream<Path> paths = Files.walk(dir)) {
            return paths.filter(Files::isRegularFile)
                    .mapToLong(p -> sizeOf(p))
                    .sum();
        }
    }
    static long readableSize(Path dir) throws IOException {
        AtomicLong total = new AtomicLong();
        Files.walkFileTree(dir, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                if (attrs.isRegularFile()) {
                    total.addAndGet(attrs.size());
                }
                return FileVisitResult.CONTINUE;
            }
            @Override
            public FileVisitResult visitFileFailed(Path file, IOException e) {
                return FileVisitResult.CONTINUE;
            }
        });
        return total.get();
    }
    static String humanReadable(long bytes) {
        if (bytes < 1024) {
            return bytes + " B";
        }
        int unit = (63 - Long.numberOfLeadingZeros(bytes)) / 10;
        double value = (double) bytes / (1L << (unit * 10));
        return String.format(Locale.ROOT, "%.1f %cB", value, "KMGTPE".charAt(unit - 1));
    }
    public static void main(String[] args) throws Exception {
        {
            Path log = Files.writeString(Files.createTempFile("app", ".log"), "x".repeat(2560));
            long bytes = Files.size(log);                                             // 2560
            show("bytes", bytes);
            long legacyBytes = log.toFile().length();                                 // 2560
            show("legacyBytes", legacyBytes);
            double kb = bytes / 1024.0;                                               // 2.5
            show("kb", kb);
            String label = String.format(Locale.ROOT, "%.1f KB", kb);                 // "2.5 KB"
            show("label", label);
        }
        {
            long fiveMb = 5L * 1024 * 1024;
            Path small = Files.write(Files.createTempFile("cv", ".pdf"), new byte[300_000]);
            Path big = Files.write(Files.createTempFile("video", ".mp4"), new byte[6_000_000]);
            checkUploadSize(small, fiveMb);
            try { checkUploadSize(big, fiveMb);  } catch (Throwable _t) { System.out.println("-> " + _t); }
            try { long missing = Files.size(small.resolveSibling("deleted.pdf")); show("missing", missing); } catch (Throwable _t) { System.out.println("missing -> " + _t); }
        }
        {
            File empty = Files.createTempFile("empty", ".txt").toFile();
            File gone = new File(empty.getParentFile(), "no-such-file.txt");
            long emptyLength = empty.length();                                        // 0
            show("emptyLength", emptyLength);
            long goneLength = gone.length();                                          // 0
            show("goneLength", goneLength);
            try { long goneSize = Files.size(gone.toPath()); show("goneSize", goneSize); } catch (Throwable _t) { System.out.println("goneSize -> " + _t); }
        }
        {
            Path user = Files.createTempDirectory("user-docs");
            Files.write(user.resolve("cv.pdf"), new byte[1500]);
            Path photos = Files.createDirectories(user.resolve("photos/2026"));
            Files.write(photos.resolve("beach.jpg"), new byte[4000]);
            Files.write(photos.resolve("city.jpg"), new byte[2500]);
            long used = directorySize(user);                                          // 8000
            show("used", used);
        }
        {
            Path shared = Files.createTempDirectory("shared");
            Files.write(shared.resolve("a.txt"), new byte[700]);
            Files.createDirectories(shared.resolve("old"));
            Files.write(shared.resolve("old/b.txt"), new byte[300]);
            long readable = readableSize(shared);                                     // 1000
            show("readable", readable);
        }
        {
            long size = 2_333_444L;
            double sizeKb = size / 1024.0;                                            // 2278.75390625
            show("sizeKb", sizeKb);
            double sizeMb = size / (1024.0 * 1024);                                   // 2.2253456115722656
            show("sizeMb", sizeMb);
            String mb = String.format(Locale.ROOT, "%.2f MB", sizeMb);                // "2.23 MB"
            show("mb", mb);
            String grouped = String.format(Locale.ROOT, "%,d bytes", size);           // "2,333,444 bytes"
            show("grouped", grouped);
        }
        {
            String tiny = humanReadable(512);                                        // "512 B"
            show("tiny", tiny);
            String photo = humanReadable(2_333_444);                                  // "2.2 MB"
            show("photo", photo);
            String movie = humanReadable(4_700_000_000L);                             // "4.4 GB"
            show("movie", movie);
        }
        {
            Path sparse = Files.createTempFile("disk", ".img");
            try (RandomAccessFile raf = new RandomAccessFile(sparse.toFile(), "rw")) {
                raf.setLength(10L * 1024 * 1024);
            }
            long logical = Files.size(sparse);                                        // 10485760
            show("logical", logical);
        }
        {
            Path exportDir = Files.createTempDirectory("export");
            FileStore store = Files.getFileStore(exportDir);
            long usable = store.getUsableSpace();                                     // free bytes for this JVM
            show("usable", usable);
            long totalSpace = store.getTotalSpace();                                  // size of the partition
            show("totalSpace", totalSpace);
            boolean fits = usable > 4L * 1024 * 1024 * 1024;                          // true when 4 GB fit
            show("fits", fits);
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

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
 * Examples for the tutorial "Copy a File in Java: Files.copy() and Overwrite Options".
 * https://howtodoinjava.com/java/io/copy-files-in-java/
 */
public class CopyFileExamples {
    static long copyWithChannels(Path source, Path target) throws IOException {
        try (FileChannel in = FileChannel.open(source, StandardOpenOption.READ);
        FileChannel out = FileChannel.open(target, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE)) {
            long size = in.size();
            long position = 0;
            while (position < size) {
                position += in.transferTo(position, size - position, out);
            }
            return position;
        }
    }
    static void copyAtomically(Path source, Path target) throws IOException {
        Path temp = Files.createTempFile(target.getParent(), "copy-", ".tmp");
        try {
            Files.copy(source, temp, StandardCopyOption.REPLACE_EXISTING);
            Files.move(temp, target, StandardCopyOption.ATOMIC_MOVE);
        } finally {
            Files.deleteIfExists(temp);
        }
    }
    public static void main(String[] args) throws Exception {
        {
            Path dir = Files.createTempDirectory("invoices");
            Path source = Files.writeString(dir.resolve("invoice.txt"), "Total 120");
            Path target = dir.resolve("invoice-copy.txt");
            String name = Files.copy(source, target).getFileName().toString();       // "invoice-copy.txt"
            show("name", name);
            String text = Files.readString(target);                                   // "Total 120"
            show("text", text);
            try { Path again = Files.copy(source, target); show("again", again); } catch (Throwable _t) { System.out.println("again -> " + _t); }
            Path replaced = Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);   // overwrites the copy
            show("replaced", replaced);
            boolean same = Files.mismatch(source, replaced) == -1;                    // true
            show("same", same);
        }
        {
            Path reports = Files.createTempDirectory("reports");
            Path today = Files.writeString(reports.resolve("report-10-10.csv"), "day,total\n10,42");
            Path latest = Files.writeString(reports.resolve("latest-report.csv"), "old");
            Files.copy(today, latest, StandardCopyOption.REPLACE_EXISTING);
            String content = Files.readString(latest);                                // "day,total\n10,42"
            show("content", content);
        }
        {
            Path docs = Files.createTempDirectory("docs");
            Path original = Files.writeString(docs.resolve("notes.txt"), "draft");
            Files.setLastModifiedTime(original, FileTime.from(Instant.parse("2026-01-15T10:00:00Z")));
            Path plain = Files.copy(original, docs.resolve("plain.txt"));
            Path kept = Files.copy(original, docs.resolve("kept.txt"), StandardCopyOption.COPY_ATTRIBUTES);
            FileTime plainTime = Files.getLastModifiedTime(plain);                    // time of the copy
            show("plainTime", plainTime);
            FileTime keptTime = Files.getLastModifiedTime(kept);                      // 2026-01-15T10:00:00Z
            show("keptTime", keptTime);
        }
        {
            Path inbox = Files.createTempDirectory("inbox");
            Path invoice = Files.writeString(inbox.resolve("invoice-7.txt"), "Total 80");
            Path archive = inbox.resolveSibling(inbox.getFileName() + "-archive").resolve("2026");
            try { Path missing = Files.copy(invoice, archive.resolve(invoice.getFileName())); show("missing", missing); } catch (Throwable _t) { System.out.println("missing -> " + _t); }
            Files.createDirectories(archive);
            Path stored = Files.copy(invoice, archive.resolve(invoice.getFileName()));
            boolean exists = Files.exists(stored);                                    // true
            show("exists", exists);
        }
        {
            Path uploads = Files.createTempDirectory("uploads");
            byte[] picture = {1, 2, 3, 4, 5};
            long saved;
            try (InputStream in = new ByteArrayInputStream(picture)) {
                saved = Files.copy(in, uploads.resolve("avatar.png"), StandardCopyOption.REPLACE_EXISTING);
            }
            long bytes = saved;                                                       // 5
            show("bytes", bytes);
        }
        {
            Path page = Files.writeString(Files.createTempFile("page", ".html"), "<h1>Hi</h1>");
            ByteArrayOutputStream response = new ByteArrayOutputStream();
            long sent = Files.copy(page, response);                                   // 11
            show("sent", sent);
            String body = response.toString(StandardCharsets.UTF_8);                  // "<h1>Hi</h1>"
            show("body", body);
        }
        {
            Path logs = Files.createTempDirectory("logs");
            Path log = Files.writeString(logs.resolve("app.log"), "started\nstopped\n");
            long moved = copyWithChannels(log, logs.resolve("app-copy.log"));        // 16
            show("moved", moved);
        }
        {
            File sourceFile = Files.writeString(Files.createTempFile("menu", ".txt"), "soup, salad").toFile();
            File targetFile = new File(sourceFile.getParentFile(), "menu-copy-" + sourceFile.getName());
            long copied;
            try (InputStream in = new FileInputStream(sourceFile);
            OutputStream out = new FileOutputStream(targetFile)) {
                copied = in.transferTo(out);
            }
            long total = copied;                                                      // 11
            show("total", total);
        }
        {
            Path config = Files.createTempDirectory("config");
            Path draft = Files.writeString(config.resolve("app.draft"), "port=8080");
            Path live = Files.writeString(config.resolve("app.properties"), "port=80");
            copyAtomically(draft, live);
            String active = Files.readString(live);                                   // "port=8080"
            show("active", active);
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

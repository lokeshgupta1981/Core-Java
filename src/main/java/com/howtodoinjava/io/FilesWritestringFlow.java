package com.howtodoinjava.io;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.time.*;
import java.nio.file.*;
import java.math.*;
import java.io.*;

import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.stream.*;
import java.util.function.*;
import java.io.*;
import java.nio.file.*;
import java.nio.charset.*;
import java.time.*;
import java.math.*;

/**
 * Examples for the tutorial "Java Write String to File With Files.writeString()".
 * https://howtodoinjava.com/java11/write-string-to-file/
 */
public class FilesWritestringFlow {
    static void saveAtomically(Path target, String content) throws IOException {
        Path folder = target.toAbsolutePath().getParent();
        Path temp = Files.createTempFile(folder, "save-", ".tmp");
        try {
            Files.writeString(temp, content);
            Files.move(temp, target, StandardCopyOption.ATOMIC_MOVE);
        } finally {
            Files.deleteIfExists(temp);
        }
    }
    public static void main(String[] args) throws Exception {
        {
            Path file = Path.of("notes.txt");
            Path written = Files.writeString(file, "Hello World !!");                               // notes.txt
            show("written", written);
            String content = Files.readString(file);                                                  // "Hello World !!"
            show("content", content);
            Path appended = Files.writeString(file, " Bye", StandardOpenOption.APPEND);             // notes.txt
            show("appended", appended);
            String afterAppend = Files.readString(file);                                              // "Hello World !! Bye"
            show("afterAppend", afterAppend);
            Path latin = Files.writeString(Path.of("latin.txt"), "caf\u00e9", StandardCharsets.ISO_8859_1);   // latin.txt
            show("latin", latin);
        }
        {
            String report = """
            date=2026-10-10
            orders=42
            """;
            Path reportFile = Path.of("daily-report.txt");
            Path saved = Files.writeString(reportFile, report);                 // daily-report.txt
            show("saved", saved);
            long size = Files.size(reportFile);                                 // 26
            show("size", size);
            List<String> lines = Files.readAllLines(reportFile);                // [date=2026-10-10, orders=42]
            show("lines", lines);
        }
        {
            Path log = Path.of("audit.log");
            try { Path failed = Files.writeString(log, "login\n", StandardOpenOption.APPEND); show("failed", failed); } catch (Throwable _t) { System.out.println("failed -> " + _t); }
            Path created = Files.writeString(log, "login\n", StandardOpenOption.CREATE, StandardOpenOption.APPEND);        // audit.log
            show("created", created);
            Path again = Files.writeString(log, "logout\n", StandardOpenOption.CREATE, StandardOpenOption.APPEND);         // audit.log
            show("again", again);
            try { Path once = Files.writeString(log, "reset\n", StandardOpenOption.CREATE_NEW); show("once", once); } catch (Throwable _t) { System.out.println("once -> " + _t); }
        }
        {
            Path ascii = Path.of("ascii.txt");
            Path utf8 = Files.writeString(ascii, "caf\u00e9");                                    // ascii.txt
            show("utf8", utf8);
            try { Path notAscii = Files.writeString(ascii, "caf\u00e9", StandardCharsets.US_ASCII); show("notAscii", notAscii); } catch (Throwable _t) { System.out.println("notAscii -> " + _t); }
            long utf8Size = Files.size(ascii);                                                    // 5
            show("utf8Size", utf8Size);
        }
        {
            Path summary = Path.of("reports", "2026", "summary.txt");
            try { Path noFolder = Files.writeString(summary, "total=42"); show("noFolder", noFolder); } catch (Throwable _t) { System.out.println("noFolder -> " + _t); }
            Path folder = Files.createDirectories(summary.getParent());           // reports/2026
            show("folder", folder);
            Path inFolder = Files.writeString(summary, "total=42");              // reports/2026/summary.txt
            show("inFolder", inFolder);
        }
        {
            Path settings = Path.of("settings.properties");
            saveAtomically(settings, "theme=dark");
            saveAtomically(settings, "theme=light");
            String current = Files.readString(settings);                 // "theme=light"
            show("current", current);
        }
        {
            Path legacy = Files.write(Path.of("legacy.txt"), "Hello World !!".getBytes(StandardCharsets.UTF_8));    // legacy.txt
            show("legacy", legacy);
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

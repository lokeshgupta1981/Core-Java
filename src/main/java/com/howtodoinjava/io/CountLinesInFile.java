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
 * Examples for the tutorial "Count Lines in a File in Java (Fast and Correct Ways)".
 * https://howtodoinjava.com/java/io/count-file-lines/
 */
public class CountLinesInFile {
    static long countLines(Path file) throws IOException {
        try (Stream<String> lines = Files.lines(file)) {
            return lines.count();
        }
    }
    static int countWithLineNumberReader(Path file) throws IOException {
        try (LineNumberReader reader = new LineNumberReader(Files.newBufferedReader(file))) {
            reader.skip(Long.MAX_VALUE);
            return reader.getLineNumber();
        }
    }
    static long countLineBreaks(Path file) throws IOException {
        long lines = 0;
        byte last = '\n';
        byte[] buffer = new byte[64 * 1024];
        try (InputStream in = Files.newInputStream(file)) {
            int read;
            while ((read = in.read(buffer)) != -1) {
                for (int i = 0; i < read; i++) {
                    if (buffer[i] == '\n') {
                        lines++;
                    }
                }
                if (read > 0) {
                    last = buffer[read - 1];
                }
            }
        }
        return last == '\n' ? lines : lines + 1;             // count a last line without newline
    }
    static record ImportPlan(Path file, long totalRows) {}
    static ImportPlan planImport(Path feed) throws IOException {
        long rows = countLineBreaks(feed) - 1;               // minus the header line
        if (rows <= 0) {
            throw new IllegalArgumentException("Feed has no data rows: " + feed.getFileName());
        }
        return new ImportPlan(feed, rows);
    }
    public static void main(String[] args) throws Exception {
        {
            Path orders = Files.writeString(Files.createTempFile("orders", ".csv"), "id,item\n1,tea\n2,cake\n");
            try (Stream<String> lines = Files.lines(orders)) {
                long count = lines.count();                       // 3
            }
        }
        {
            Path report = Files.writeString(Files.createTempFile("report", ".txt"), "a\nb\nc");
            long count = countLines(report);                      // 3, the last line has no newline
            show("count", count);
        }
        {
            Path legacy = Files.write(Files.createTempFile("legacy", ".txt"), new byte[] {'c', 'a', 'f', (byte) 0xE9, '\n'});
            try { long strict = countLines(legacy); show("strict", strict); } catch (Throwable _t) { System.out.println("strict -> " + _t); }
            long lenient = Files.lines(legacy, StandardCharsets.ISO_8859_1).count(); // 1
            show("lenient", lenient);
        }
        {
            Path noNewline = Files.writeString(Files.createTempFile("a", ".txt"), "a\nb\nc");
            Path withNewline = Files.writeString(Files.createTempFile("b", ".txt"), "a\nb\nc\n");
            int first = countWithLineNumberReader(noNewline);       // 3
            show("first", first);
            int second = countWithLineNumberReader(withNewline);    // 3
            show("second", second);
        }
        {
            Path students = Files.writeString(Files.createTempFile("students", ".txt"), "Alex\nMaria\nJohn\n");
            List<String> names = Files.readAllLines(students);
            int count = names.size();                              // 3
            show("count", count);
            String last = names.get(count - 1);                    // "John"
            show("last", last);
        }
        {
            Path big = Files.writeString(Files.createTempFile("big", ".log"), "x\n".repeat(100_000) + "end");
            long count = countLineBreaks(big);                     // 100001
            show("count", count);
            Path empty = Files.createTempFile("empty", ".log");
            long none = countLineBreaks(empty);                    // 0
            show("none", none);
        }
        {
            Path export = Files.writeString(Files.createTempFile("export", ".csv"), "id,item\n1,tea\n\n2,cake\n# end\n");
            try (Stream<String> lines = Files.lines(export)) {
                long records = lines.skip(1)                                   // skip the header
                        .filter(line -> !line.isBlank())
                        .filter(line -> !line.startsWith("#"))
                        .count();                                  // 2
            }
        }
        {
            Path feed = Files.writeString(Files.createTempFile("feed", ".csv"), "sku,price\n" + "p1,10\n".repeat(1_200));
            ImportPlan plan = planImport(feed);
            long total = plan.totalRows();                           // 1200
            show("total", total);
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

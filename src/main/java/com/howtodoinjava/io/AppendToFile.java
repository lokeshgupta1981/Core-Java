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
 * Examples for the tutorial "Java Append to File: Files, BufferedWriter and FileWriter".
 * https://howtodoinjava.com/java/io/java-append-to-file/
 */
public class AppendToFile {
    static void appendLine(Path file, String line) throws IOException {
        boolean needsBreak = false;
        try (FileChannel channel = FileChannel.open(file, StandardOpenOption.READ)) {
            long size = channel.size();
            if (size > 0) {
                ByteBuffer last = ByteBuffer.allocate(1);
                channel.read(last, size - 1);
                needsBreak = last.get(0) != '\n';
            }
        }
        String text = (needsBreak ? System.lineSeparator() : "") + line + System.lineSeparator();
        Files.writeString(file, text, StandardOpenOption.APPEND);
    }
    public static void main(String[] args) throws Exception {
        {
            Path log = Files.createTempFile("visits", ".log");

            Files.writeString(log, "lokesh logged in\n", StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            Files.writeString(log, "alex logged in\n", StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            List<String> twoLines = Files.readAllLines(log);     // [lokesh logged in, alex logged in]
            show("twoLines", twoLines);

            try (FileWriter writer = new FileWriter(log.toFile(), StandardCharsets.UTF_8, true)) {
                writer.write("maria logged in\n");
            }
            List<String> threeLines = Files.readAllLines(log);   // [lokesh logged in, alex logged in, maria logged in]
            show("threeLines", threeLines);
        }
        {
            Path audit = Files.createTempDirectory("shop").resolve("audit.log");

            try { Path missing = Files.writeString(audit, "apple 5 -> 6\n", StandardOpenOption.APPEND); show("missing", missing); } catch (Throwable _t) { System.out.println("missing -> " + _t); }

            Files.writeString(audit, "apple 5 -> 6\n", StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            Files.write(audit, List.of("banana 3 -> 4", "milk 2 -> 3"), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            List<String> changes = Files.readAllLines(audit);   // [apple 5 -> 6, banana 3 -> 4, milk 2 -> 3]
            show("changes", changes);
        }
        {
            Path report = Files.createTempFile("import", ".txt");
            Files.writeString(report, "run 1: ok\n");
            List<String> results = List.of("prices.csv: 120 rows", "stock.csv: 80 rows");

            try (BufferedWriter writer = Files.newBufferedWriter(report, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
                writer.write("run 2:");
                writer.newLine();
                for (String result : results) {
                    writer.write(result);
                    writer.newLine();
                }
            }
            List<String> lines = Files.readAllLines(report);   // [run 1: ok, run 2:, prices.csv: 120 rows, stock.csv: 80 rows]
            show("lines", lines);
        }
        {
            Path log = Files.createTempFile("visits", ".log");
            File file = log.toFile();

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8, true))) {
                writer.write("lokesh logged in");
                writer.newLine();
            }

            try (PrintWriter out = new PrintWriter(new FileWriter(file, StandardCharsets.UTF_8, true))) {
                out.printf("%s logged in at %d%n", "alex", 9);
            }

            try (FileOutputStream out = new FileOutputStream(file, true)) {
                out.write("maria logged in\n".getBytes(StandardCharsets.UTF_8));
            }
            List<String> visits = Files.readAllLines(log);   // [lokesh logged in, alex logged in at 9, maria logged in]
            show("visits", visits);
        }
        {
            Path list = Files.createTempFile("groceries", ".txt");
            Files.writeString(list, "apple\nbanana");

            Files.writeString(list, "milk\n", StandardOpenOption.APPEND);
            List<String> joined = Files.readAllLines(list);   // [apple, bananamilk]
            show("joined", joined);
        }
        {
            Path list = Files.createTempFile("groceries", ".txt");
            Files.writeString(list, "apple\nbanana");

            appendLine(list, "milk");
            appendLine(list, "eggs");
            List<String> fixed = Files.readAllLines(list);   // [apple, banana, milk, eggs]
            show("fixed", fixed);
        }
        {
            Path log = Files.createTempFile("jobs", ".log");

            try (FileChannel channel = FileChannel.open(log, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            FileLock lock = channel.lock()) {
                channel.write(ByteBuffer.wrap("job 42 done\n".getBytes(StandardCharsets.UTF_8)));
            }
            List<String> jobs = Files.readAllLines(log);   // [job 42 done]
            show("jobs", jobs);
        }
        {
            Path file = Files.createTempFile("notes", ".txt");
            Files.writeString(file, "first" + System.lineSeparator(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            Files.write(file, List.of("second"), StandardOpenOption.APPEND);
            List<String> notes = Files.readAllLines(file);   // [first, second]
            show("notes", notes);
        }
        {
            Path file = Files.createTempFile("todo", ".txt");
            Files.writeString(file, "milk\n");

            Path tmp = Files.createTempFile(file.getParent(), "todo", ".tmp");
            Files.writeString(tmp, "eggs\n" + Files.readString(file));
            Files.move(tmp, file, StandardCopyOption.REPLACE_EXISTING);
            List<String> todo = Files.readAllLines(file);   // [eggs, milk]
            show("todo", todo);
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

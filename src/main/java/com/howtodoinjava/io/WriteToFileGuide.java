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
 * Examples for the tutorial "Java Write to File: Files.writeString, BufferedWriter and More".
 * https://howtodoinjava.com/java/io/java-write-to-file/
 */
public class WriteToFileGuide {

    public static void main(String[] args) throws Exception {
        {
            Path file = Files.createTempFile("groceries", ".txt");

            Files.writeString(file, "apple\nbanana\n");
            List<String> fromString = Files.readAllLines(file);      // [apple, banana]
            show("fromString", fromString);

            Files.write(file, List.of("milk", "eggs", "bread"));
            List<String> fromList = Files.readAllLines(file);        // [milk, eggs, bread]
            show("fromList", fromList);

            try (BufferedWriter writer = Files.newBufferedWriter(file, StandardOpenOption.APPEND)) {
                writer.write("butter");
                writer.newLine();
            }
            List<String> appended = Files.readAllLines(file);        // [milk, eggs, bread, butter]
            show("appended", appended);

            Files.write(file, new byte[] {104, 105});
            String fromBytes = Files.readString(file);               // "hi"
            show("fromBytes", fromBytes);
        }
        {
            Path file = Files.createTempFile("note", ".txt");

            Path saved = Files.writeString(file, "buy apples");
            String first = Files.readString(saved);                     // "buy apples"
            show("first", first);

            StringBuilder report = new StringBuilder("apple=5").append(", banana=3");
            Files.writeString(file, report);
            String second = Files.readString(file);                     // "apple=5, banana=3"
            show("second", second);

            Files.writeString(file, "caf\u00e9", StandardCharsets.UTF_8);
            long size = Files.size(file);                               // 5
            show("size", size);
        }
        {
            Path file = Files.createTempFile("price", ".txt");
            try { Path ascii = Files.writeString(file, "\u20ac5", StandardCharsets.US_ASCII); show("ascii", ascii); } catch (Throwable _t) { System.out.println("ascii -> " + _t); }
        }
        {
            Path file = Files.createTempFile("groceries", ".txt");
            List<String> items = List.of("apple", "banana", "milk");

            Files.write(file, items);
            List<String> lines = Files.readAllLines(file);                 // [apple, banana, milk]
            show("lines", lines);
            long bytes = Files.size(file);                                  // 18 on Linux, 21 on Windows
            show("bytes", bytes);

            Stream<String> upper = items.stream().map(String::toUpperCase);
            Iterable<String> lazyLines = upper::iterator;
            Files.write(file, lazyLines);
            List<String> upperLines = Files.readAllLines(file);            // [APPLE, BANANA, MILK]
            show("upperLines", upperLines);
        }
        {
            Path dir = Files.createTempDirectory("reports");
            Path report = dir.resolve("2026-10-10.txt");
            Path log = dir.resolve("visits.log");

            Files.writeString(report, "apple,5", StandardOpenOption.CREATE_NEW);
            try { Path again = Files.writeString(report, "apple,6", StandardOpenOption.CREATE_NEW); show("again", again); } catch (Throwable _t) { System.out.println("again -> " + _t); }

            try { Path missing = Files.writeString(log, "lokesh\n", StandardOpenOption.APPEND); show("missing", missing); } catch (Throwable _t) { System.out.println("missing -> " + _t); }

            Files.writeString(log, "lokesh\n", StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            Files.writeString(log, "alex\n", StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            List<String> visits = Files.readAllLines(log);                                    // [lokesh, alex]
            show("visits", visits);
        }
        {
            Path file = Files.createTempFile("orders", ".csv");

            try (BufferedWriter writer = Files.newBufferedWriter(file)) {
                writer.write("item,qty");
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file.toFile(), StandardCharsets.UTF_8, true))) {
                writer.newLine();
                writer.write("apple,5");
            }
            List<String> rows = Files.readAllLines(file);   // [item,qty, apple,5]
            show("rows", rows);
        }
        {
            Path file = Files.createTempFile("orders", ".csv");
            Map<String, Integer> orders = new LinkedHashMap<>();
            orders.put("apple", 5);
            orders.put("banana", 3);
            orders.put("milk", 2);

            try (BufferedWriter writer = Files.newBufferedWriter(file)) {
                writer.write("item,qty");
                writer.newLine();
                for (Map.Entry<String, Integer> order : orders.entrySet()) {
                    writer.write(order.getKey() + "," + order.getValue());
                    writer.newLine();
                }
            }
            List<String> csv = Files.readAllLines(file);   // [item,qty, apple,5, banana,3, milk,2]
            show("csv", csv);
        }
        {
            Path file = Files.createTempFile("orders", ".csv");
            BufferedWriter writer = Files.newBufferedWriter(file);
            writer.write("item,qty");
            long beforeClose = Files.size(file);   // 0
            show("beforeClose", beforeClose);

            writer.close();
            long afterClose = Files.size(file);    // 8
            show("afterClose", afterClose);
        }
        {
            Path file = Files.createTempFile("big", ".txt");

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file.toFile(), StandardCharsets.UTF_8), 65536)) {
                writer.write("apple");
            }
            String content = Files.readString(file);   // "apple"
            show("content", content);
        }
        {
            Path file = Files.createTempFile("receipt", ".txt");

            try (PrintWriter out = new PrintWriter(Files.newBufferedWriter(file))) {
                out.printf("%-8s%5d%n", "apple", 5);
                out.printf("%-8s%5d%n", "banana", 3);
                out.println("total: 8");
            }
            List<String> receipt = Files.readAllLines(file);   // [apple       5, banana      3, total: 8]
            show("receipt", receipt);
        }
        {
            Path file = Files.createTempFile("receipt", ".txt");
            PrintWriter out = new PrintWriter(Files.newBufferedWriter(file));
            out.close();

            out.println("apple");
            boolean failed = out.checkError();   // true
            show("failed", failed);
        }
        {
            Path file = Files.createTempFile("notes", ".txt");

            try (FileWriter writer = new FileWriter(file.toFile(), StandardCharsets.UTF_8)) {
                writer.write("call the bank");
            }
            String note = Files.readString(file);   // "call the bank"
            show("note", note);
        }
        {
            Path file = Files.createTempFile("scores", ".bin");

            try (DataOutputStream out = new DataOutputStream(new BufferedOutputStream(Files.newOutputStream(file)))) {
                out.writeInt(2);
                out.writeInt(90);
                out.writeDouble(7.5);
            }
            long size = Files.size(file);   // 16
            show("size", size);

            try (DataInputStream in = new DataInputStream(new BufferedInputStream(Files.newInputStream(file)))) {
                int count = in.readInt();          // 2
                int score = in.readInt();          // 90
                double rating = in.readDouble();   // 7.5
            }
        }
        {
            Charset defaultCharset = Charset.defaultCharset();             // UTF-8
            show("defaultCharset", defaultCharset);
            String utf8 = Arrays.toString("caf\u00e9".getBytes(StandardCharsets.UTF_8));          // [99, 97, 102, -61, -87]
            show("utf8", utf8);
            String latin1 = Arrays.toString("caf\u00e9".getBytes(StandardCharsets.ISO_8859_1));    // [99, 97, 102, -23]
            show("latin1", latin1);
        }
        {
            Path base = Files.createTempDirectory("app");
            Path settings = base.resolve("config/settings.properties");

            try { Path failed = Files.writeString(settings, "theme=dark"); show("failed", failed); } catch (Throwable _t) { System.out.println("failed -> " + _t); }

            Files.createDirectories(settings.getParent());
            Files.writeString(settings, "theme=dark");
            boolean exists = Files.exists(settings);                   // true
            show("exists", exists);
        }
        {
            Path dir = Files.createTempDirectory("app");
            Path settings = dir.resolve("settings.properties");
            Files.writeString(settings, "theme=light");

            Path tmp = Files.createTempFile(dir, "settings", ".tmp");
            Files.writeString(tmp, "theme=dark");
            Files.move(tmp, settings, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            String saved = Files.readString(settings);   // "theme=dark"
            show("saved", saved);
        }
        {
            Path file = Files.createTempFile("todo", ".txt");
            Files.writeString(file, "milk\n");
            Files.writeString(file, "eggs\n", StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            List<String> todo = Files.readAllLines(file);   // [milk, eggs]
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

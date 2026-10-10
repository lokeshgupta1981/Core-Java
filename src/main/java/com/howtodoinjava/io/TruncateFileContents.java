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
 * Examples for the tutorial "Delete the Contents of a File in Java Without Deleting It".
 * https://howtodoinjava.com/java/io/how-to-delete-the-contents-of-a-file/
 */
public class TruncateFileContents {

    public static void main(String[] args) throws Exception {
        {
            Path cache = Files.createTempFile("cache", ".txt");
            Files.writeString(cache, "apple=5\nbanana=3\n");

            Files.write(cache, new byte[0]);
            long afterWrite = Files.size(cache);          // 0
            show("afterWrite", afterWrite);

            Files.writeString(cache, "apple=5\n");
            try (FileChannel channel = FileChannel.open(cache, StandardOpenOption.WRITE)) {
                channel.truncate(0);
            }
            long afterTruncate = Files.size(cache);       // 0
            show("afterTruncate", afterTruncate);
            boolean stillThere = Files.exists(cache);     // true
            show("stillThere", stillThere);
        }
        {
            Path dir = Files.createTempDirectory("app");
            Path log = Files.writeString(dir.resolve("app.log"), "started\nstopped\n");

            Files.write(log, new byte[0], StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
            long size = Files.size(log);                                                               // 0
            show("size", size);

            try { Path typo = Files.write(dir.resolve("ap.log"), new byte[0], StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING); show("typo", typo); } catch (Throwable _t) { System.out.println("typo -> " + _t); }

            try (OutputStream out = Files.newOutputStream(log)) {
                out.write("restarted\n".getBytes(StandardCharsets.UTF_8));
            }
            List<String> content = Files.readAllLines(log);                                           // [restarted]
            show("content", content);
        }
        {
            Path csv = Files.createTempFile("orders", ".csv");
            Files.writeString(csv, "item,qty\napple,5\nbanana,3\n");
            long headerBytes = "item,qty\n".getBytes(StandardCharsets.UTF_8).length;

            try (FileChannel channel = FileChannel.open(csv, StandardOpenOption.WRITE)) {
                channel.truncate(headerBytes);
            }
            List<String> left = Files.readAllLines(csv);   // [item,qty]
            show("left", left);

            FileChannel readOnly = FileChannel.open(csv, StandardOpenOption.READ);
            try { FileChannel same = readOnly.truncate(0); show("same", same); } catch (Throwable _t) { System.out.println("same -> " + _t); }
            readOnly.close();
        }
        {
            Path scratch = Files.createTempFile("scratch", ".dat");
            Files.writeString(scratch, "temporary data");

            try (RandomAccessFile file = new RandomAccessFile(scratch.toFile(), "rw")) {
                file.setLength(0);
            }
            long size = Files.size(scratch);   // 0
            show("size", size);
        }
        {
            Path notes = Files.createTempFile("notes", ".txt");
            File file = notes.toFile();

            Files.writeString(notes, "old note");
            new FileWriter(file, StandardCharsets.UTF_8).close();
            long afterFileWriter = Files.size(notes);    // 0
            show("afterFileWriter", afterFileWriter);

            Files.writeString(notes, "old note");
            new PrintWriter(file, StandardCharsets.UTF_8).close();
            long afterPrintWriter = Files.size(notes);   // 0
            show("afterPrintWriter", afterPrintWriter);

            Files.writeString(notes, "old note");
            new FileOutputStream(file).close();
            long afterStream = Files.size(notes);        // 0
            show("afterStream", afterStream);
        }
        {
            Path greeting = Files.createTempFile("greeting", ".txt");
            Files.writeString(greeting, "hello world");

            Files.writeString(greeting, "Jim", StandardOpenOption.WRITE);
            String broken = Files.readString(greeting);   // "Jimlo world"
            show("broken", broken);

            Files.writeString(greeting, "Jim", StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
            String fixed = Files.readString(greeting);    // "Jim"
            show("fixed", fixed);
        }
        {
            Path log = Files.createTempFile("server", ".log");

            try (OutputStream logger = new FileOutputStream(log.toFile())) {
                logger.write("12345".getBytes(StandardCharsets.UTF_8));
                try (FileChannel channel = FileChannel.open(log, StandardOpenOption.WRITE)) {
                    channel.truncate(0);
                }
                logger.write("678".getBytes(StandardCharsets.UTF_8));
            }
            String withGap = Arrays.toString(Files.readAllBytes(log));   // [0, 0, 0, 0, 0, 54, 55, 56]
            show("withGap", withGap);

            try (OutputStream logger = new FileOutputStream(log.toFile(), true)) {
                logger.write("12345".getBytes(StandardCharsets.UTF_8));
                try (FileChannel channel = FileChannel.open(log, StandardOpenOption.WRITE)) {
                    channel.truncate(0);
                }
                logger.write("678".getBytes(StandardCharsets.UTF_8));
            }
            String clean = Files.readString(log);                        // "678"
            show("clean", clean);
        }
        {
            Path todo = Files.createTempFile("todo", ".txt");
            Files.write(todo, List.of("milk", "eggs", "bread"));

            List<String> kept = Files.readAllLines(todo).stream().filter(line -> !line.equals("eggs")).toList();
            Path tmp = Files.createTempFile(todo.getParent(), "todo", ".tmp");
            Files.write(tmp, kept);
            Files.move(tmp, todo, StandardCopyOption.REPLACE_EXISTING);
            List<String> result = Files.readAllLines(todo);   // [milk, bread]
            show("result", result);
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

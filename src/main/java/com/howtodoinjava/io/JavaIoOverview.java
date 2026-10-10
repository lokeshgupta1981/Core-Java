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
 * Examples for the tutorial "Java IO Tutorial: Files, Streams and NIO in Java 25".
 * https://howtodoinjava.com/java/io/java-io/
 */
public class JavaIoOverview {

    public static void main(String[] args) throws Exception {
        {
            Path file = Files.createTempDirectory("notes").resolve("todo.txt");
            Path saved = Files.writeString(file, "buy milk\n");                          // creates or replaces todo.txt
            show("saved", saved);
            Path appended = Files.writeString(file, "call mom\n", StandardOpenOption.APPEND);
            List<String> lines = Files.readAllLines(file);                               // [buy milk, call mom]
            show("lines", lines);
            long size = Files.size(file);                                                // 18
            show("size", size);
            Path copy = Files.copy(file, file.resolveSibling("todo-copy.txt"));
            Path moved = Files.move(copy, file.resolveSibling("done.txt"));
            String movedName = moved.getFileName().toString();                           // "done.txt"
            show("movedName", movedName);
            boolean deleted = Files.deleteIfExists(moved);                               // true
            show("deleted", deleted);

            byte[] data;
            try (InputStream in = Files.newInputStream(file)) {
                data = in.readAllBytes();
            }
            int length = data.length;                                                    // 18
            show("length", length);
        }
        {
            byte[] utf8 = "Caf\u00e9".getBytes(StandardCharsets.UTF_8);
            int byteCount = utf8.length;                                                 // 5
            show("byteCount", byteCount);

            String decoded;
            try (Reader reader = new InputStreamReader(new ByteArrayInputStream(utf8), StandardCharsets.UTF_8)) {
                decoded = new BufferedReader(reader).readLine();
            }
            int charCount = decoded.length();                                            // 4
            show("charCount", charCount);

            ByteArrayOutputStream sink = new ByteArrayOutputStream();
            long copied = new ByteArrayInputStream(utf8).transferTo(sink);               // 5
            show("copied", copied);
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

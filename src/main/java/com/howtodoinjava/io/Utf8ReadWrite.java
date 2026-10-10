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
 * Examples for the tutorial "Read and Write UTF-8 Files in Java: Charsets, JEP 400, BOM".
 * https://howtodoinjava.com/java/io/read-write-utf8-data-file/
 */
public class Utf8ReadWrite {

    public static void main(String[] args) throws Exception {
        {
            Path menu = Files.createTempFile("menu", ".txt");
            String text = "caf\u00e9 \u20ac3\ncr\u00e8me br\u00fbl\u00e9e \u20ac5\n";

            Files.writeString(menu, text, StandardCharsets.UTF_8);
            String back = Files.readString(menu, StandardCharsets.UTF_8);
            boolean same = back.equals(text);                       // true
            show("same", same);

            int chars = text.length();                              // 24
            show("chars", chars);
            long bytes = Files.size(menu);                          // 32
            show("bytes", bytes);

            List<String> lines = Files.readAllLines(menu, StandardCharsets.UTF_8);
            int lineCount = lines.size();                           // 2
            show("lineCount", lineCount);
        }
        {
            int ascii = "a".getBytes(StandardCharsets.UTF_8).length;              // 1
            show("ascii", ascii);
            int accented = "\u00e9".getBytes(StandardCharsets.UTF_8).length;      // 2
            show("accented", accented);
            int euro = "\u20ac".getBytes(StandardCharsets.UTF_8).length;          // 3
            show("euro", euro);
            int emoji = "\uD83D\uDE00".getBytes(StandardCharsets.UTF_8).length;  // 4
            show("emoji", emoji);
            int emojiChars = "\uD83D\uDE00".length();                             // 2
            show("emojiChars", emojiChars);
        }
        {
            Path file = Files.createTempFile("menu", ".txt");
            String item = "cr\u00e8me br\u00fbl\u00e9e";

            Files.writeString(file, item + "\n", StandardCharsets.UTF_8);

            Files.write(file, List.of(item, "caf\u00e9"), StandardCharsets.UTF_8);

            try (BufferedWriter writer = Files.newBufferedWriter(file, StandardCharsets.UTF_8)) {
                writer.write(item);
                writer.newLine();
            }

            try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file.toFile()), StandardCharsets.UTF_8))) {
                writer.write(item);
            }
            long size = Files.size(file);   // 15
            show("size", size);
        }
        {
            Path file = Files.createTempFile("menu", ".txt");
            Files.writeString(file, "caf\u00e9\ncr\u00e8me br\u00fbl\u00e9e\n");

            String all = Files.readString(file);
            List<String> lines = Files.readAllLines(file);
            int count = lines.size();                                       // 2
            show("count", count);

            try (BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
                String first = reader.readLine();
                boolean match = first.equals("caf\u00e9");                  // true
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file.toFile()), StandardCharsets.UTF_8))) {
                long items = reader.lines().count();                        // 2
            }
        }
        {
            Charset charset = Charset.defaultCharset();                    // UTF-8
            show("charset", charset);
            String nativeEncoding = System.getProperty("native.encoding");   // the operating system charset, e.g. Cp1252 on Windows
            show("nativeEncoding", nativeEncoding);
        }
        {
            Path csv = Files.createTempFile("prices", ".csv");
            Files.write(csv, "caf\u00e9;3\n".getBytes(StandardCharsets.ISO_8859_1));

            try { String strict = Files.readString(csv); show("strict", strict); } catch (Throwable _t) { System.out.println("strict -> " + _t); }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(csv.toFile()), StandardCharsets.UTF_8))) {
                String line = reader.readLine();
                boolean replaced = line.contains("\ufffd");                                  // true
            }

            String decoded = Files.readString(csv, StandardCharsets.ISO_8859_1);
            boolean correct = decoded.equals("caf\u00e9;3\n");                              // true
            show("correct", correct);
        }
        {
            Path file = Files.createTempFile("price", ".txt");

            try { Path strictWrite = Files.writeString(file, "\u20ac5", StandardCharsets.US_ASCII); show("strictWrite", strictWrite); } catch (Throwable _t) { System.out.println("strictWrite -> " + _t); }

            byte[] lossy = "\u20ac5".getBytes(StandardCharsets.US_ASCII);
            String stored = new String(lossy, StandardCharsets.US_ASCII);                      // "?5"
            show("stored", stored);
        }
        {
            Path csv = Files.createTempFile("export", ".csv");
            Files.write(csv, new byte[] {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF, 'i', 'd', '\n', '1', '\n'});

            String header = Files.readAllLines(csv).get(0);
            boolean looksRight = header.startsWith("id");                    // false
            show("looksRight", looksRight);
            String cleaned = header.replace("\ufeff", "");
            boolean fixed = cleaned.equals("id");                            // true
            show("fixed", fixed);
        }
        {
            Path csv = Files.createTempFile("excel", ".csv");
            Files.writeString(csv, "\ufeffitem;price\ncaf\u00e9;3\n", StandardCharsets.UTF_8);
            long size = Files.size(csv);   // 22
            show("size", size);
        }
        {
            Path latin1 = Files.createTempFile("old", ".txt");
            Files.write(latin1, "caf\u00e9\n".getBytes(StandardCharsets.ISO_8859_1));
            Path utf8 = Files.createTempFile("new", ".txt");

            try (BufferedReader in = Files.newBufferedReader(latin1, StandardCharsets.ISO_8859_1);
            BufferedWriter out = Files.newBufferedWriter(utf8, StandardCharsets.UTF_8)) {
                in.transferTo(out);
            }
            long oldSize = Files.size(latin1);   // 5
            show("oldSize", oldSize);
            long newSize = Files.size(utf8);     // 6
            show("newSize", newSize);
        }
        {
            Path file = Files.createTempFile("data", ".bin");
            try (DataOutputStream out = new DataOutputStream(Files.newOutputStream(file))) {
                out.writeUTF("caf\u00e9");
            }
            long size = Files.size(file);   // 7
            show("size", size);
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

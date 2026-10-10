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
 * Examples for the tutorial "Java FileWriter: Constructors, Append Mode and Charset".
 * https://howtodoinjava.com/java/io/java-filewriter/
 */
public class FileWriterGuide {

    public static void main(String[] args) throws Exception {
        {
            Path notes = Files.createTempFile("notes", ".txt");
            File file = notes.toFile();

            try (FileWriter writer = new FileWriter(file, StandardCharsets.UTF_8)) {
                writer.write("buy milk");
            }
            String first = Files.readString(notes);           // "buy milk"
            show("first", first);

            try (FileWriter writer = new FileWriter(file, StandardCharsets.UTF_8, true)) {
                writer.append('\n').append("call the bank");
            }
            List<String> lines = Files.readAllLines(notes);   // [buy milk, call the bank]
            show("lines", lines);
        }
        {
            Path dir = Files.createTempDirectory("notes");

            try { FileWriter missingDir = new FileWriter(dir.resolve("2026/oct.txt").toFile(), StandardCharsets.UTF_8); show("missingDir", missingDir); } catch (Throwable _t) { System.out.println("missingDir -> " + _t); }
            try { FileWriter isDirectory = new FileWriter(dir.toFile(), StandardCharsets.UTF_8); show("isDirectory", isDirectory); } catch (Throwable _t) { System.out.println("isDirectory -> " + _t); }
        }
        {
            Path file = Files.createTempFile("letters", ".txt");
            char[] word = {'h', 'e', 'l', 'l', 'o'};

            try (FileWriter writer = new FileWriter(file.toFile(), StandardCharsets.UTF_8)) {
                writer.write(72);
                writer.write(word, 1, 4);
                writer.write(" world", 0, 6);
                writer.append('!').append(" bye", 0, 4);
            }
            String content = Files.readString(file);   // "Hello world! bye"
            show("content", content);
        }
        {
            Path journal = Files.createTempFile("journal", ".txt");
            File file = journal.toFile();

            try (FileWriter writer = new FileWriter(file, StandardCharsets.UTF_8, true)) {
                writer.write("mon: ran 5 km\n");
            }
            try (FileWriter writer = new FileWriter(file, StandardCharsets.UTF_8, true)) {
                writer.write("tue: rest day\n");
            }
            List<String> kept = Files.readAllLines(journal);    // [mon: ran 5 km, tue: rest day]
            show("kept", kept);

            try (FileWriter writer = new FileWriter(file, StandardCharsets.UTF_8)) {
                writer.write("wed: swim\n");
            }
            List<String> wiped = Files.readAllLines(journal);   // [wed: swim]
            show("wiped", wiped);
        }
        {
            Path file = Files.createTempFile("menu", ".txt");

            try (FileWriter writer = new FileWriter(file.toFile(), StandardCharsets.UTF_8)) {
                writer.write("caf\u00e9");
            }
            long utf8Size = Files.size(file);     // 5
            show("utf8Size", utf8Size);

            try (FileWriter writer = new FileWriter(file.toFile(), StandardCharsets.ISO_8859_1)) {
                writer.write("caf\u00e9");
            }
            long latin1Size = Files.size(file);   // 4
            show("latin1Size", latin1Size);
        }
        {
            Path file = Files.createTempFile("draft", ".txt");
            FileWriter writer = new FileWriter(file.toFile(), StandardCharsets.UTF_8);
            writer.write("draft text");
            long beforeFlush = Files.size(file);   // 0
            show("beforeFlush", beforeFlush);

            writer.flush();
            long afterFlush = Files.size(file);    // 10
            show("afterFlush", afterFlush);
            writer.close();
        }
        {
            Path file = Files.createTempFile("export", ".csv");

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file.toFile(), StandardCharsets.UTF_8))) {
                for (int day = 1; day <= 3; day++) {
                    writer.write("day" + day + ",ok");
                    writer.newLine();
                }
            }
            List<String> rows = Files.readAllLines(file);   // [day1,ok, day2,ok, day3,ok]
            show("rows", rows);
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

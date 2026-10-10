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
 * Examples for the tutorial "Java FileReader: Constructors, Charset and Examples".
 * https://howtodoinjava.com/java/io/java-filereader/
 */
public class FileReaderGuide {
    static String readAll(Path file) throws IOException {
        StringBuilder text = new StringBuilder();
        char[] buffer = new char[1024];
        try (FileReader reader = new FileReader(file.toFile(), StandardCharsets.UTF_8)) {
            int count;
            while ((count = reader.read(buffer)) != -1) {
                text.append(buffer, 0, count);       // only the chars read in this call
            }
        }
        return text.toString();
    }
    static Properties loadSettings(File file) throws IOException {
        Properties settings = new Properties();
        try (FileReader reader = new FileReader(file, StandardCharsets.UTF_8)) {
            settings.load(reader);
        }
        return settings;
    }
    public static void main(String[] args) throws Exception {
        {
            Path notes = Files.writeString(Files.createTempFile("notes", ".txt"), "milk\neggs\n");
            try (BufferedReader reader = new BufferedReader(new FileReader(notes.toFile(), StandardCharsets.UTF_8))) {
                String first = reader.readLine();            // "milk"
                String second = reader.readLine();           // "eggs"
                String end = reader.readLine();              // null, end of file
            }
        }
        {
            Path todo = Files.writeString(Files.createTempFile("todo", ".txt"), "call Alex");
            try (FileReader reader = new FileReader(todo.toString(), StandardCharsets.UTF_8)) {
                int firstChar = reader.read();               // 99, the code of 'c'
            }
        }
        {
            Path todo = Files.writeString(Files.createTempFile("todo", ".txt"), "call Alex");
            File file = todo.toFile();
            try (FileReader reader = new FileReader(file, StandardCharsets.UTF_8)) {
                boolean ready = reader.ready();              // true
            }
        }
        {
            Path menu = Files.write(Files.createTempFile("menu", ".txt"), new byte[] {'c', 'a', 'f', (byte) 0xC3, (byte) 0xA9});
            String utf8 = Files.readString(menu, StandardCharsets.UTF_8);            // "caf\u00e9", 4 chars
            show("utf8", utf8);
            String latin1 = Files.readString(menu, StandardCharsets.ISO_8859_1);     // 5 chars, "caf" plus two wrong chars
            show("latin1", latin1);
            try (FileReader reader = new FileReader(menu.toFile(), StandardCharsets.UTF_8)) {
                String encoding = reader.getEncoding();      // "UTF8", the historical name
            }
        }
        {
            Path list = Files.writeString(Files.createTempFile("list", ".txt"), "milk\neggs\nbread");
            String content = readAll(list);
            List<String> items = content.lines().toList();   // [milk, eggs, bread]
            show("items", items);
        }
        {
            Path code = Files.writeString(Files.createTempFile("code", ".txt"), "a1b2");
            int digits = 0;
            try (FileReader reader = new FileReader(code.toFile(), StandardCharsets.UTF_8)) {
                int ch;
                while ((ch = reader.read()) != -1) {
                    if (Character.isDigit((char) ch)) {
                        digits++;
                    }
                }
            }
            int digitCount = digits;                         // 2
            show("digitCount", digitCount);
        }
        {
            Path list = Files.writeString(Files.createTempFile("list", ".txt"), "milk\neggs\nbread\n");
            List<String> lines = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader(list.toFile(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    lines.add(line);
                }
            }
            List<String> result = lines;                     // [milk, eggs, bread]
            show("result", result);
        }
        {
            Path list = Files.writeString(Files.createTempFile("list", ".txt"), "milk\neggs");
            StringWriter copy = new StringWriter();
            try (FileReader reader = new FileReader(list.toFile(), StandardCharsets.UTF_8)) {
                long copied = reader.transferTo(copy);       // 9
            }
            int length = copy.toString().length();           // 9
            show("length", length);
        }
        {
            try { FileReader missing = new FileReader("missing.txt"); show("missing", missing); } catch (Throwable _t) { System.out.println("missing -> " + _t); }
            try { FileReader folder = new FileReader(System.getProperty("java.io.tmpdir")); show("folder", folder); } catch (Throwable _t) { System.out.println("folder -> " + _t); }
        }
        {
            Path legacy = Files.write(Files.createTempFile("legacy", ".txt"), new byte[] {'c', 'a', 'f', (byte) 0xE9});
            StringWriter decoded = new StringWriter();
            try (FileReader reader = new FileReader(legacy.toFile(), StandardCharsets.UTF_8)) {
                reader.transferTo(decoded);
            }
            char last = decoded.toString().charAt(3);        // '\ufffd', the replacement char
            show("last", last);
            try { String strict = Files.readString(legacy); show("strict", strict); } catch (Throwable _t) { System.out.println("strict -> " + _t); }
        }
        {
            Path config = Files.writeString(Files.createTempFile("app", ".properties"), "company=Caf\u00e9 Lumi\u00e8re\ncurrency=EUR\n");
            Properties settings = loadSettings(config.toFile());
            String currency = settings.getProperty("currency");        // "EUR"
            show("currency", currency);
            int nameLength = settings.getProperty("company").length(); // 12
            show("nameLength", nameLength);
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

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
 * Examples for the tutorial "Java BufferedReader: readLine(), lines() and LineNumberReader".
 * https://howtodoinjava.com/java/io/java-bufferedreader-example/
 */
public class BufferedReaderGuide {
    static List<String> validate(Path settings) throws IOException {
        List<String> problems = new ArrayList<>();
        try (LineNumberReader reader = new LineNumberReader(Files.newBufferedReader(settings))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isBlank() && !line.contains("=")) {
                    problems.add("line " + reader.getLineNumber() + ": missing '=' in \"" + line + "\"");
                }
            }
        }
        return problems;
    }
    public static void main(String[] args) throws Exception {
        {
            Path scores = Files.writeString(Files.createTempFile("scores", ".txt"), "name,points\nAlex,40\nMaria,55\n");
            try (BufferedReader reader = Files.newBufferedReader(scores)) {
                String header = reader.readLine();               // "name,points"
                List<String> rows = reader.lines().toList();     // [Alex,40, Maria,55]
            }
        }
        {
            Path notes = Files.writeString(Files.createTempFile("notes", ".txt"), "first line\n");
            try (BufferedReader utf8 = Files.newBufferedReader(notes);
            BufferedReader latin1 = Files.newBufferedReader(notes, StandardCharsets.ISO_8859_1)) {
                String line = utf8.readLine();                   // "first line"
            }
        }
        {
            File file = Files.writeString(Files.createTempFile("todo", ".txt"), "pay rent\n").toFile();
            InputStream bytes = new ByteArrayInputStream("ok\n".getBytes(StandardCharsets.UTF_8));
            try (BufferedReader fromFile = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8));
            BufferedReader fromStream = new BufferedReader(new InputStreamReader(bytes, StandardCharsets.UTF_8));
            BufferedReader fromString = new BufferedReader(new StringReader("a\nb"))) {
                String task = fromFile.readLine();               // "pay rent"
                String status = fromStream.readLine();           // "ok"
                String letter = fromString.readLine();           // "a"
            }
        }
        {
            BufferedReader large = new BufferedReader(new StringReader("data"), 65_536);  // 64K chars
            show("large", large);
            try { BufferedReader none = new BufferedReader(new StringReader("data"), 0); show("none", none); } catch (Throwable _t) { System.out.println("none -> " + _t); }
        }
        {
            Path mixed = Files.writeString(Files.createTempFile("mixed", ".txt"), "a\rb\r\nc\nd");
            List<String> lines = new ArrayList<>();
            try (BufferedReader reader = Files.newBufferedReader(mixed)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    lines.add(line);
                }
            }
            List<String> result = lines;                         // [a, b, c, d]
            show("result", result);
        }
        {
            Path log = Files.writeString(Files.createTempFile("app", ".log"), "INFO start\nERROR db down\nINFO retry\nERROR timeout\n");
            try (BufferedReader reader = Files.newBufferedReader(log)) {
                long errors = reader.lines().filter(l -> l.startsWith("ERROR")).count();  // 2
            }
        }
        {
            try (BufferedReader reader = new BufferedReader(new StringReader("abcdef"))) {
                long skipped = reader.skip(2);                   // 2
                int next = reader.read();                        // 99, the code of 'c'
                char[] chunk = new char[4];
                int count = reader.read(chunk, 0, 4);            // 3, only "def" was left
                String rest = new String(chunk, 0, count);       // "def"
            }
        }
        {
            Path upload = Files.writeString(Files.createTempFile("upload", ".csv"), "id,name\n1,Alex\n");
            try (BufferedReader reader = Files.newBufferedReader(upload)) {
                reader.mark(1_000);                              // up to 1,000 chars can be re-read
                String firstLine = reader.readLine();            // "id,name"
                boolean hasHeader = firstLine.startsWith("id");  // true
                reader.reset();                                  // back to the start
                String again = reader.readLine();                // "id,name"
            }
        }
        {
            InputStream in = new ByteArrayInputStream("Lokesh\n".getBytes());  // stands in for System.in
            show("in", in);
            BufferedReader console = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            String name = console.readLine();                    // "Lokesh"
            show("name", name);
            String after = console.readLine();                   // null, input ended
            show("after", after);
        }
        {
            Path settings = Files.writeString(Files.createTempFile("app", ".conf"), "firstName=Lokesh\nlastName Gupta\nblog=howtodoinjava\n");
            List<String> problems = validate(settings);          // [line 2: missing '=' in "lastName Gupta"]
            show("problems", problems);
        }
        {
            LineNumberReader reader = new LineNumberReader(new StringReader("x\ny"));
            int start = reader.getLineNumber();                  // 0
            show("start", start);
            reader.setLineNumber(10);
            String first = reader.readLine();                    // "x"
            show("first", first);
            int afterFirst = reader.getLineNumber();             // 11
            show("afterFirst", afterFirst);
            String second = reader.readLine();                   // "y"
            show("second", second);
            int afterSecond = reader.getLineNumber();            // 12
            show("afterSecond", afterSecond);
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

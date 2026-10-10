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
 * Examples for the tutorial "Read a Specific Line From a File in Java (by Line Number)".
 * https://howtodoinjava.com/java/io/read-given-line-from-file/
 */
public class ReadSpecificLine {
    static Optional<String> lineOf(List<String> lines, int lineNumber) {
        if (lineNumber < 1 || lineNumber > lines.size()) {
            return Optional.empty();
        }
        return Optional.of(lines.get(lineNumber - 1));
    }
    static Optional<String> readLine(Path file, int lineNumber) throws IOException {
        if (lineNumber < 1) {
            throw new IllegalArgumentException("Line numbers start at 1: " + lineNumber);
        }
        try (Stream<String> lines = Files.lines(file)) {
            return lines.skip(lineNumber - 1).findFirst();
        }
    }
    static Optional<String> readLineWithReader(Path file, int lineNumber) throws IOException {
        try (LineNumberReader reader = new LineNumberReader(Files.newBufferedReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (reader.getLineNumber() == lineNumber) {
                    return Optional.of(line);
                }
            }
        }
        return Optional.empty();
    }
    static long[] indexLines(Path file) throws IOException {
        List<Long> starts = new ArrayList<>(List.of(0L));
        long position = 0;
        try (InputStream in = new BufferedInputStream(Files.newInputStream(file))) {
            int b;
            while ((b = in.read()) != -1) {
                position++;
                if (b == '\n') {
                    starts.add(position);               // the next line starts after '\n'
                }
            }
        }
        starts.add(position + 1);                       // end marker for the last line
        return starts.stream().mapToLong(Long::longValue).toArray();
    }
    static String lineAt(Path file, long[] index, int lineNumber) throws IOException {
        long start = index[lineNumber - 1];
        int length = (int) (index[lineNumber] - start - 1);   // without the '\n'
        try (FileChannel channel = FileChannel.open(file, StandardOpenOption.READ)) {
            ByteBuffer bytes = ByteBuffer.allocate(Math.max(length, 0));
            while (bytes.hasRemaining() && channel.read(bytes, start + bytes.position()) > 0) {
                // a positional read can return fewer bytes than requested
            }
            return new String(bytes.array(), StandardCharsets.UTF_8).stripTrailing();
        }
    }
    static String errorContext(Path file, int badLine) throws IOException {
        int from = Math.max(1, badLine - 1);
        try (Stream<String> lines = Files.lines(file)) {
            List<String> nearby = lines.skip(from - 1).limit(3).toList();
            StringBuilder message = new StringBuilder();
            for (int i = 0; i < nearby.size(); i++) {
                int number = from + i;
                message.append(number == badLine ? "> " : "  ").append(number).append(": ").append(nearby.get(i)).append('\n');
            }
            return message.toString();
        }
    }
    public static void main(String[] args) throws Exception {
        {
            Path songs = Files.writeString(Files.createTempFile("songs", ".txt"), "Yesterday\nImagine\nHey Jude\nLet It Be\nHelp\n");
            int lineNumber = 3;
            try (Stream<String> lines = Files.lines(songs)) {
                Optional<String> line = lines.skip(lineNumber - 1).findFirst();   // Optional[Hey Jude]
            }
        }
        {
            Path songs = Files.writeString(Files.createTempFile("songs", ".txt"), "Yesterday\nImagine\nHey Jude\n");
            List<String> lines = Files.readAllLines(songs);
            String second = lines.get(1);                                  // "Imagine"
            show("second", second);
            try { String tenth = lines.get(9); show("tenth", tenth); } catch (Throwable _t) { System.out.println("tenth -> " + _t); }
        }
        {
            List<String> lines = List.of("Yesterday", "Imagine", "Hey Jude");
            Optional<String> third = lineOf(lines, 3);                      // Optional[Hey Jude]
            show("third", third);
            Optional<String> tenth = lineOf(lines, 10);                     // Optional.empty
            show("tenth", tenth);
            String shown = lineOf(lines, 10).orElse("(no such line)");      // "(no such line)"
            show("shown", shown);
        }
        {
            Path log = Files.writeString(Files.createTempFile("app", ".log"), "line 1\n".repeat(50_000) + "the target\n");
            Optional<String> target = readLine(log, 50_001);              // Optional[the target]
            show("target", target);
            Optional<String> missing = readLine(log, 60_000);             // Optional.empty
            show("missing", missing);
            try { Optional<String> zero = readLine(log, 0); show("zero", zero); } catch (Throwable _t) { System.out.println("zero -> " + _t); }
        }
        {
            Path songs = Files.writeString(Files.createTempFile("songs", ".txt"), "Yesterday\nImagine\nHey Jude\nLet It Be\nHelp\nSomething\n");
            int from = 3;
            int to = 5;
            try (Stream<String> lines = Files.lines(songs)) {
                List<String> range = lines.skip(from - 1).limit(to - from + 1).toList();  // [Hey Jude, Let It Be, Help]
            }
        }
        {
            Path songs = Files.writeString(Files.createTempFile("songs", ".txt"), "Yesterday\nImagine\nHey Jude\n");
            Optional<String> second = readLineWithReader(songs, 2);        // Optional[Imagine]
            show("second", second);
            Optional<String> fifth = readLineWithReader(songs, 5);         // Optional.empty
            show("fifth", fifth);
        }
        {
            Path log = Files.writeString(Files.createTempFile("app", ".log"), "first\nsecond\nthird");
            long[] index = indexLines(log);
            String line2 = lineAt(log, index, 2);                           // "second"
            show("line2", line2);
            String line3 = lineAt(log, index, 3);                           // "third"
            show("line3", line3);
        }
        {
            Path payroll = Files.writeString(Files.createTempFile("payroll", ".csv"), "name,amount\nAlex,1200\nMaria,1500\nJohn,12OO\nRita,1100\n");
            String context = errorContext(payroll, 4);
            List<String> shown = context.lines().toList();                // [  3: Maria,1500, > 4: John,12OO,   5: Rita,1100]
            show("shown", shown);
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

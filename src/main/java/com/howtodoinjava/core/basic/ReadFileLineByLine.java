package com.howtodoinjava.core.basic;

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
 * Examples for the tutorial "Read a File Line by Line in Java (Files.lines, BufferedReader)".
 * https://howtodoinjava.com/java8/read-file-line-by-line/
 */
public class ReadFileLineByLine {
    static Path createLog() throws IOException {
        String text = """
        INFO app started
        WARN disk almost full
        ERROR db timeout
        INFO order saved
        ERROR mail failed
        """;
        return Files.writeString(Files.createTempFile("app", ".log"), text);
    }
    public static void main(String[] args) throws Exception {
        {
            Path log = createLog();

            try (Stream<String> lines = Files.lines(log)) {
                List<String> errors = lines.filter(line -> line.startsWith("ERROR")).toList();   // [ERROR db timeout, ERROR mail failed]
            }

            int warnings = 0;
            try (BufferedReader reader = Files.newBufferedReader(log)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("WARN")) {
                        warnings++;
                    }
                }
            }
            int warnCount = warnings;                      // 1
            show("warnCount", warnCount);

            List<String> allLines = Files.readAllLines(log);   // [INFO app started, WARN disk almost full, ERROR db timeout, INFO order saved, ERROR mail failed]
            show("allLines", allLines);
        }
        {
            Path log = createLog();

            try (Stream<String> lines = Files.lines(log)) {
                long errorCount = lines.filter(line -> line.startsWith("ERROR")).count();   // 2
            }

            try (Stream<String> lines = Files.lines(log)) {
                Optional<String> firstError = lines.filter(line -> line.startsWith("ERROR")).findFirst();   // Optional[ERROR db timeout]
            }

            try (Stream<String> lines = Files.lines(log)) {
                List<String> messages = lines.skip(1).limit(2).map(line -> line.substring(line.indexOf(' ') + 1)).toList();   // [disk almost full, db timeout]
            }
        }
        {
            Path log = createLog();
            String firstError = "none";
            int lineNumber = 0;

            try (BufferedReader reader = Files.newBufferedReader(log)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    lineNumber++;
                    if (line.startsWith("ERROR")) {
                        firstError = lineNumber + ": " + line;
                        break;
                    }
                }
            }
            String found = firstError;   // "3: ERROR db timeout"
            show("found", found);
        }
        {
            Path log = createLog();

            try (BufferedReader reader = Files.newBufferedReader(log)) {
                List<String> infoLines = reader.lines().filter(line -> line.startsWith("INFO")).toList();   // [INFO app started, INFO order saved]
            }
        }
        {
            Path log = createLog();

            List<String> lines = Files.readAllLines(log);
            int count = lines.size();                  // 5
            show("count", count);
            String third = lines.get(2);               // "ERROR db timeout"
            show("third", third);
            String last = lines.getLast();             // "ERROR mail failed"
            show("last", last);

            List<String> fromString = Files.readString(log).lines().toList();   // [INFO app started, WARN disk almost full, ERROR db timeout, INFO order saved, ERROR mail failed]
            show("fromString", fromString);
        }
        {
            Path log = createLog();
            List<String> levels = new ArrayList<>();

            try (Scanner scanner = new Scanner(log)) {
                while (scanner.hasNextLine()) {
                    String level = scanner.next();
                    scanner.nextLine();   // skip the rest of the line
                    levels.add(level);
                }
            }
            List<String> result = levels;   // [INFO, WARN, ERROR, INFO, ERROR]
            show("result", result);
        }
        {
            Path latin1 = Files.write(Files.createTempFile("menu", ".txt"), "caf\u00e9\n".getBytes(StandardCharsets.ISO_8859_1));

            try { List<String> strict = Files.readAllLines(latin1); show("strict", strict); } catch (Throwable _t) { System.out.println("strict -> " + _t); }
            Stream<String> lazy = Files.lines(latin1);
            try { List<String> lazyLines = lazy.toList(); show("lazyLines", lazyLines); } catch (Throwable _t) { System.out.println("lazyLines -> " + _t); }

            try (Stream<String> lines = Files.lines(latin1, StandardCharsets.ISO_8859_1)) {
                boolean decoded = lines.findFirst().orElseThrow().equals("caf\u00e9");   // true
            }
        }
        {
            Path log = createLog();

            try (Stream<String> lines = Files.lines(log)) {
                Map<String, Long> perLevel = lines.collect(Collectors.groupingBy(line -> line.substring(0, line.indexOf(' ')), TreeMap::new, Collectors.counting()));   // {ERROR=2, INFO=2, WARN=1}
            }
        }
        {
            Path csv = Files.writeString(Files.createTempFile("prices", ".csv"), "item,price\napple,5\nbanana,3\n");

            try (Stream<String> lines = Files.lines(csv)) {
                List<String> items = lines.skip(1).map(line -> line.split(",")[0]).toList();   // [apple, banana]
            }
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

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

import java.util.regex.Pattern;

/**
 * Examples for the tutorial "Java FilenameFilter: Find Files Matching a Name Pattern".
 * https://howtodoinjava.com/java/io/java-filenamefilter-example/
 */
public class FilenameFilterGuide {
    static record ExtensionFilter(String extension) implements FilenameFilter {
        @Override
        public boolean accept(File dir, String name) {
            return name.toLowerCase(Locale.ROOT).endsWith("." + extension);
        }
    }
    static File exportFolder() throws IOException {
        Path dir = Files.createTempDirectory("export");
        for (String name : List.of("report.csv", "report2.csv", "Summary.PDF",
        "export-01.tmp", "export-02.tmp", "readme.txt")) {
            Files.writeString(dir.resolve(name), name);
        }
        return dir.toFile();
    }
    static int deleteTempFiles(File workDir) throws IOException {
        File[] temps = workDir.listFiles((dir, name) -> name.endsWith(".tmp"));
        if (temps == null) {
            throw new IOException("Cannot list " + workDir);
        }
        for (File temp : temps) {
            Files.deleteIfExists(temp.toPath());
        }
        return temps.length;
    }
    public static void main(String[] args) throws Exception {
        {
            Path folder = Files.createTempDirectory("app");
            Files.writeString(folder.resolve("server.log"), "ok");
            Files.writeString(folder.resolve("GC.LOG"), "ok");
            Files.writeString(folder.resolve("config.yaml"), "port: 80");
            FilenameFilter logs = (dir, name) -> name.toLowerCase(Locale.ROOT).endsWith(".log");
            String[] names = folder.toFile().list(logs);
            Arrays.sort(names);
            String found = Arrays.toString(names);          // "[GC.LOG, server.log]"
            show("found", found);
        }
        {
            FilenameFilter csv = new ExtensionFilter("csv");
            boolean match = csv.accept(new File("."), "Sales.CSV");      // true
            show("match", match);
            boolean noMatch = csv.accept(new File("."), "sales.csv.bak"); // false
            show("noMatch", noMatch);
        }
        {
            Path folder = Files.createTempDirectory("imports");
            Files.writeString(folder.resolve("orders.csv"), "1");
            String[] names = folder.toFile().list((d, n) -> n.endsWith(".csv"));
            File[] files = folder.toFile().listFiles((d, n) -> n.endsWith(".csv"));
            String name = names[0];                               // "orders.csv"
            show("name", name);
            boolean absolute = files[0].isAbsolute();             // true
            show("absolute", absolute);
            String[] none = new File("no-such-folder").list((d, n) -> true);   // null
            show("none", none);
        }
        {
            File folder = exportFolder();
            FilenameFilter pdf = (dir, name) -> name.toLowerCase(Locale.ROOT).endsWith(".pdf");
            String[] pdfs = folder.list(pdf);
            String result = Arrays.toString(pdfs);           // "[Summary.PDF]"
            show("result", result);
        }
        {
            File folder = exportFolder();
            Pattern lettersOnly = Pattern.compile("[a-zA-Z]+\\.[a-zA-Z]+");
            FilenameFilter noDigits = (dir, name) -> lettersOnly.matcher(name).matches();
            String[] matched = folder.list(noDigits);
            Arrays.sort(matched);
            String result = Arrays.toString(matched);        // "[Summary.PDF, readme.txt, report.csv]"
            show("result", result);
        }
        {
            File folder = exportFolder();
            int deleted = deleteTempFiles(folder);                    // 2
            show("deleted", deleted);
            int left = folder.list().length;                          // 4
            show("left", left);
        }
        {
            Path folder = exportFolder().toPath();
            List<String> reports = new ArrayList<>();
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(folder, "report*.{csv,pdf}")) {
                for (Path p : stream) {
                    reports.add(p.getFileName().toString());
                }
            }
            Collections.sort(reports);
            String result = reports.toString();              // "[report.csv, report2.csv]"
            show("result", result);
        }
        {
            PathMatcher tmp = FileSystems.getDefault().getPathMatcher("glob:*.tmp");
            PathMatcher numbered = FileSystems.getDefault().getPathMatcher("regex:export-\\d+\\.tmp");
            Path chunk = Path.of("/data/work/export-01.tmp");
            boolean onName = tmp.matches(chunk.getFileName());    // true
            show("onName", onName);
            boolean onFullPath = tmp.matches(chunk);              // false
            show("onFullPath", onFullPath);
            boolean regex = numbered.matches(chunk.getFileName()); // true
            show("regex", regex);
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

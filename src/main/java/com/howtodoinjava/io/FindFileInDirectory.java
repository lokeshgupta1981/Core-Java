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
 * Examples for the tutorial "Find a File in a Directory and Subdirectories in Java".
 * https://howtodoinjava.com/java/io/find-file-in-directory-subdirectories/
 */
public class FindFileInDirectory {
    static Path projectTree() throws IOException {
        Path root = Files.createTempDirectory("project");
        Files.createDirectories(root.resolve("src/main"));
        Files.createDirectories(root.resolve("src/test"));
        Files.createDirectories(root.resolve("docs/old"));
        Files.writeString(root.resolve("README.md"), "top");
        Files.writeString(root.resolve("src/main/App.java"), "class App {}");
        Files.writeString(root.resolve("src/test/AppTest.java"), "class AppTest {}");
        Files.writeString(root.resolve("docs/guide.pdf"), "a".repeat(3_000));
        Files.writeString(root.resolve("docs/old/README.md"), "old");
        return root;
    }
    static Optional<Path> findFirstByName(Path start, String fileName) throws IOException {
        Path[] result = new Path[1];
        Files.walkFileTree(start, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                if (file.getFileName().toString().equals(fileName)) {
                    result[0] = file;
                    return FileVisitResult.TERMINATE;
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException e) {
                System.err.println("Skipped " + file + ": " + e);
                return FileVisitResult.CONTINUE;
            }
        });
        return Optional.ofNullable(result[0]);
    }
    static void findByName(File dir, String fileName, List<File> found) {
        File[] entries = dir.listFiles();
        if (entries == null) {
            return;                        // not a directory, or not readable
        }
        for (File entry : entries) {
            if (entry.isDirectory()) {
                findByName(entry, fileName, found);
            } else if (entry.getName().equalsIgnoreCase(fileName)) {
                found.add(entry);
            }
        }
    }
    public static void main(String[] args) throws Exception {
        {
            Path root = Files.createTempDirectory("project");
            Files.createDirectories(root.resolve("docs/old"));
            Files.writeString(root.resolve("README.md"), "top");
            Files.writeString(root.resolve("docs/old/README.md"), "old");
            Files.writeString(root.resolve("docs/guide.pdf"), "pdf");
            List<Path> found;
            try (Stream<Path> matches = Files.find(root, Integer.MAX_VALUE,
            (path, attrs) -> attrs.isRegularFile() && path.getFileName().toString().equals("README.md"))) {
                found = matches.map(root::relativize).sorted().toList();
            }
            String result = found.toString();               // "[README.md, docs/old/README.md]"
            show("result", result);
        }
        {
            Path root = projectTree();
            List<String> large;
            try (Stream<Path> s = Files.find(root, Integer.MAX_VALUE,
            (p, attrs) -> attrs.isRegularFile() && attrs.size() > 1_024)) {
                large = s.map(p -> p.getFileName().toString()).toList();
            }
            String result = large.toString();                // "[guide.pdf]"
            show("result", result);
        }
        {
            Path root = projectTree();
            List<String> javaFiles;
            try (Stream<Path> walk = Files.walk(root)) {
                javaFiles = walk.filter(Files::isRegularFile)
                        .map(p -> p.getFileName().toString())
                        .filter(name -> name.endsWith(".java"))
                        .sorted()
                        .toList();
            }
            String result = javaFiles.toString();            // "[App.java, AppTest.java]"
            show("result", result);
        }
        {
            Path root = projectTree();
            BiPredicate<Path, BasicFileAttributes> readme = (p, a) -> p.getFileName().toString().equals("README.md");
            long topLevel;
            long upToThree;
            try (Stream<Path> s = Files.find(root, 1, readme)) {
                topLevel = s.count();
            }
            try (Stream<Path> s = Files.find(root, 3, readme)) {
                upToThree = s.count();
            }
            long depthOne = topLevel;                        // 1 (docs/old/README.md is at depth 3)
            show("depthOne", depthOne);
            long depthThree = upToThree;                     // 2
            show("depthThree", depthThree);
        }
        {
            Path root = projectTree();
            Optional<Path> pdf;
            try (Stream<Path> s = Files.find(root, Integer.MAX_VALUE,
            (p, a) -> a.isRegularFile() && p.getFileName().toString().endsWith(".pdf"))) {
                pdf = s.findFirst();
            }
            String name = pdf.map(p -> p.getFileName().toString()).orElse("not found");   // "guide.pdf"
            show("name", name);
        }
        {
            Path root = projectTree();
            PathMatcher sources = FileSystems.getDefault().getPathMatcher("glob:*.{java,kt}");
            PathMatcher testsOnly = FileSystems.getDefault().getPathMatcher("glob:src/test/**");
            List<String> all;
            try (Stream<Path> s = Files.find(root, Integer.MAX_VALUE, (p, a) -> sources.matches(p.getFileName()))) {
                all = s.map(p -> p.getFileName().toString()).sorted().toList();
            }
            List<String> tests;
            try (Stream<Path> s = Files.find(root, Integer.MAX_VALUE,
            (p, a) -> a.isRegularFile() && testsOnly.matches(root.relativize(p)))) {
                tests = s.map(p -> p.getFileName().toString()).toList();
            }
            String allSources = all.toString();              // "[App.java, AppTest.java]"
            show("allSources", allSources);
            String testSources = tests.toString();           // "[AppTest.java]"
            show("testSources", testSources);
        }
        {
            Path root = projectTree();
            Files.writeString(root.resolve("docs/Readme.MD"), "mixed");
            long ignoringCase;
            try (Stream<Path> s = Files.find(root, Integer.MAX_VALUE,
            (p, a) -> a.isRegularFile() && p.getFileName().toString().equalsIgnoreCase("readme.md"))) {
                ignoringCase = s.count();
            }
            long readmeCount = ignoringCase;                 // 3
            show("readmeCount", readmeCount);
        }
        {
            Path root = projectTree();
            Optional<Path> test = findFirstByName(root, "AppTest.java");
            Optional<Path> missing = findFirstByName(root, "pom.xml");
            String found = test.map(p -> root.relativize(p).toString()).orElse("none");   // "src/test/AppTest.java"
            show("found", found);
            boolean absent = missing.isEmpty();                                          // true
            show("absent", absent);
        }
        {
            Path root = projectTree();
            List<File> readmes = new ArrayList<>();
            findByName(root.toFile(), "readme.md", readmes);
            int count = readmes.size();                      // 2
            show("count", count);
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

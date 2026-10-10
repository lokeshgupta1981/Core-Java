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
 * Examples for the tutorial "Java Check If Directory Is Empty: Files.list() and More".
 * https://howtodoinjava.com/java/io/check-empty-directory/
 */
public class EmptyDirectoryCheck {
    static boolean isEmpty(Path dir) throws IOException {
        try (DirectoryStream<Path> entries = Files.newDirectoryStream(dir)) {
            return !entries.iterator().hasNext();
        }
    }
    static boolean isEmptyDir(Path dir) throws IOException {
        try (Stream<Path> entries = Files.list(dir)) {
            return entries.findAny().isEmpty();
        }
    }
    static boolean hasNoEntries(Path dir) throws IOException {
        if (!Files.isDirectory(dir)) {
            return true;                          // missing or not a folder
        }
        try (Stream<Path> entries = Files.list(dir)) {
            return entries.findAny().isEmpty();
        }
    }
    static boolean hasCsvFiles(Path dir) throws IOException {
        try (DirectoryStream<Path> csvFiles = Files.newDirectoryStream(dir, "*.csv")) {
            return csvFiles.iterator().hasNext();
        }
    }
    static List<Path> emptyFolders(Path root) throws IOException {
        try (Stream<Path> tree = Files.walk(root)) {
            List<Path> result = new ArrayList<>();
            for (Path path : tree.filter(Files::isDirectory).toList()) {
                if (isEmpty(path)) {
                    result.add(root.relativize(path));
                }
            }
            result.sort(Comparator.naturalOrder());
            return result;
        }
    }
    public static void main(String[] args) throws Exception {
        {
            Path inbox = Files.createTempDirectory("inbox");          // new, empty folder
            show("inbox", inbox);
            Optional<Path> first;
            try (Stream<Path> entries = Files.list(inbox)) {
                first = entries.findFirst();
            }
            boolean empty = first.isEmpty();                           // true
            show("empty", empty);
        }
        {
            Path uploads = Files.createTempDirectory("uploads");
            boolean before = isEmpty(uploads);                         // true
            show("before", before);
            Path photo = Files.writeString(uploads.resolve("cat.jpg"), "x");
            boolean after = isEmpty(uploads);                          // false
            show("after", after);
            Path sub = Files.createDirectory(Files.createTempDirectory("music").resolve("rock"));
            boolean parentEmpty = isEmpty(sub.getParent());            // false, a subfolder counts too
            show("parentEmpty", parentEmpty);
        }
        {
            Path reports = Files.createTempDirectory("reports");
            boolean noReports = isEmptyDir(reports);                   // true
            show("noReports", noReports);
            Path q3 = Files.writeString(reports.resolve("q3.pdf"), "x");
            boolean hasReports = !isEmptyDir(reports);                 // true
            show("hasReports", hasReports);
        }
        {
            Path dir = Files.createTempDirectory("data");
            Path file = Files.writeString(dir.resolve("orders.csv"), "x");
            try { boolean fileCheck = isEmpty(file); show("fileCheck", fileCheck); } catch (Throwable _t) { System.out.println("fileCheck -> " + _t); }
            try { boolean missingCheck = isEmpty(dir.resolve("archive")); show("missingCheck", missingCheck); } catch (Throwable _t) { System.out.println("missingCheck -> " + _t); }
        }
        {
            Path base = Files.createTempDirectory("base");
            boolean nothingToDo = hasNoEntries(base.resolve("not-mounted"));   // true
            show("nothingToDo", nothingToDo);
        }
        {
            Path shared = Files.createTempDirectory("shared");
            Path junk = Files.writeString(shared.resolve(".DS_Store"), "x");
            boolean strictlyEmpty = isEmpty(shared);                   // false, .DS_Store counts
            show("strictlyEmpty", strictlyEmpty);
            boolean work = hasCsvFiles(shared);                        // false, no CSV files
            show("work", work);
            Path orders = Files.writeString(shared.resolve("orders.csv"), "id");
            boolean workNow = hasCsvFiles(shared);                     // true
            show("workNow", workNow);
        }
        {
            File folder = Files.createTempDirectory("legacy").toFile();
            String[] names = folder.list();
            boolean empty = names != null && names.length == 0;        // true
            show("empty", empty);
            String[] fromFile = new File(folder, "missing").list();    // null, not an exception
            show("fromFile", fromFile);
        }
        {
            Path dir = Files.createTempDirectory("files");
            Path blank = Files.createFile(dir.resolve("blank.txt"));
            Path notes = Files.writeString(dir.resolve("notes.txt"), "buy milk");
            boolean blankEmpty = Files.isRegularFile(blank) && Files.size(blank) == 0;   // true
            show("blankEmpty", blankEmpty);
            boolean notesEmpty = Files.isRegularFile(notes) && Files.size(notes) == 0;   // false
            show("notesEmpty", notesEmpty);
        }
        {
            Path archive = Files.createTempDirectory("archive");
            Path oct = Files.createDirectories(archive.resolve("2026/10"));
            Path nov = Files.createDirectories(archive.resolve("2026/11"));
            Path kept = Files.writeString(oct.resolve("orders.csv"), "x");
            List<Path> leftovers = emptyFolders(archive);              // [2026/11]
            show("leftovers", leftovers);
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

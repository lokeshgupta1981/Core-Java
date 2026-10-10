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
 * Examples for the tutorial "Java Create New File: Files.createFile() and CREATE_NEW".
 * https://howtodoinjava.com/java/io/how-to-create-a-new-file-in-java/
 */
public class CreateNewFile {
    static boolean createIfMissing(Path file) throws IOException {
        try {
            Files.createFile(file);
            return true;
        } catch (FileAlreadyExistsException e) {
            return false;                       // someone created it first
        }
    }
    public static void main(String[] args) throws Exception {
        {
            Path notes = Files.createTempDirectory("notes");        // new empty folder
            show("notes", notes);
            Path todo = Files.createFile(notes.resolve("todo.txt"));  // creates todo.txt
            show("todo", todo);
            boolean created = Files.exists(todo);                     // true
            show("created", created);
            long size = Files.size(todo);                             // 0
            show("size", size);
            try { Path again = Files.createFile(todo); show("again", again); } catch (Throwable _t) { System.out.println("again -> " + _t); }
        }
        {
            Path inbox = Files.createTempDirectory("inbox");
            boolean first = createIfMissing(inbox.resolve("orders.csv"));   // true
            show("first", first);
            boolean second = createIfMissing(inbox.resolve("orders.csv"));  // false
            show("second", second);
        }
        {
            Path base = Files.createTempDirectory("reports");
            Path summary = base.resolve("2026/10/summary.txt");
            try { Path missing = Files.createFile(summary); show("missing", missing); } catch (Throwable _t) { System.out.println("missing -> " + _t); }
        }
        {
            Path base = Files.createTempDirectory("reports");
            Path summary = base.resolve("2026/10/summary.txt");
            Path parent = Files.createDirectories(summary.getParent()); // creates 2026/10
            show("parent", parent);
            Path file = Files.createFile(summary);                    // creates summary.txt
            show("file", file);
            boolean isFile = Files.isRegularFile(file);               // true
            show("isFile", isFile);
        }
        {
            Path exports = Files.createTempDirectory("exports");
            Path csv = exports.resolve("invoices-2026-10-10.csv");
            Path written = Files.writeString(csv, "id,amount\n1,40\n", StandardOpenOption.CREATE_NEW);
            long rows = Files.readAllLines(written).size();           // 2
            show("rows", rows);
            try { Path rerun = Files.writeString(csv, "id,amount\n", StandardOpenOption.CREATE_NEW); show("rerun", rerun); } catch (Throwable _t) { System.out.println("rerun -> " + _t); }
        }
        {
            Path exports = Files.createTempDirectory("exports");
            Path log = exports.resolve("import.log");
            try (BufferedWriter out = Files.newBufferedWriter(log, StandardOpenOption.CREATE_NEW)) {
                out.write("started");
                out.newLine();
            }
            long lines = Files.readAllLines(log).size();              // 1
            show("lines", lines);
        }
        {
            Path secrets = Files.createTempDirectory("secrets");
            FileAttribute<Set<PosixFilePermission>> ownerOnly =
            PosixFilePermissions.asFileAttribute(PosixFilePermissions.fromString("rw-------"));
            Path key = Files.createFile(secrets.resolve("api.key"), ownerOnly);
            String perms = PosixFilePermissions.toString(Files.getPosixFilePermissions(key)); // "rw-------"
            show("perms", perms);
        }
        {
            File folder = Files.createTempDirectory("legacy").toFile();
            File notes = new File(folder, "notes.txt");
            boolean createdNow = notes.createNewFile();               // true
            show("createdNow", createdNow);
            boolean createdAgain = notes.createNewFile();             // false, already exists
            show("createdAgain", createdAgain);
            Path asPath = notes.toPath();                             // switch to the Files API
            show("asPath", asPath);
        }
        {
            Path dir = Files.createTempDirectory("photos");
            Path meta = Files.writeString(dir.resolve("meta.txt"), "camera=x100");
            try (FileOutputStream out = new FileOutputStream(meta.toFile())) {
                // nothing written
            }
            long sizeAfter = Files.size(meta);                        // 0, old content is gone
            show("sizeAfter", sizeAfter);
        }
        {
            Path base = Path.of("data");                  // relative to the working directory
            show("base", base);
            Path file = base.resolve("todo.txt").toAbsolutePath(); // full path, starts with the working directory
            show("file", file);
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

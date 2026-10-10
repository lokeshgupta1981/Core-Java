package com.howtodoinjava.core.exceptions;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.time.*;
import java.nio.file.*;
import java.math.*;
import java.io.*;

import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.stream.*;
import java.util.function.*;
import java.io.*;
import java.nio.file.*;
import java.nio.charset.*;
import java.time.*;
import java.math.*;

/**
 * Examples for the tutorial "throw vs throws in Java: Differences and Examples".
 * https://howtodoinjava.com/java/exception-handling/throw-vs-throws/
 */
public class ThrowVsThrows {
    // throws: IOException may reach the caller
    static String loadCatalog(Path file) throws IOException {
        if (!file.toString().endsWith(".csv")) {
            throw new IllegalArgumentException("not a CSV: " + file);   // throw: raised here
        }
        return Files.readString(file);
    }
    static class Library {
        private final Set<String> onLoan = new HashSet<>(Set.of("dune"));

        String lend(String bookId) {
            Objects.requireNonNull(bookId, "bookId");
            if (onLoan.contains(bookId)) {
                throw new IllegalStateException("already on loan: " + bookId);
            }
            onLoan.add(bookId);
            return "lent " + bookId;
        }
    }
    static List<String> readMembers(Path file) throws IOException {
        return Files.readAllLines(file);
    }
    static String findTitle(String bookId) throws IOException {
        throw new FileNotFoundException("catalog.csv");
    }

    static String describeBook(String bookId) throws IOException {
        return "Title: " + findTitle(bookId);
    }

    static String showBook(String bookId) {
        try {
            return describeBook(bookId);
        } catch (IOException e) {
            return "Catalog unavailable: " + e.getMessage();
        }
    }
    static String titleOrFail(String bookId) {
        try {
            return findTitle(bookId);
        } catch (IOException e) {
            throw new IllegalStateException("Cannot load book " + bookId, e);
        }
    }
    static class FileSource {
        String read() throws IOException { return "file"; }
    }
    static class MemorySource extends FileSource {
        @Override
        String read() { return "memory"; }            // no throws clause needed
    }
    static int loanDays(String format) {
        return switch (format) {
            case "hardcover" -> 21;
            case "magazine" -> 7;
            default -> throw new IllegalArgumentException("unknown format: " + format);
        };
    }
    public static void main(String[] args) throws Exception {
        {
            try { String wrongType = loadCatalog(Path.of("books.txt")); show("wrongType", wrongType); } catch (Throwable _t) { System.out.println("wrongType -> " + _t); }
            try { String missing = loadCatalog(Path.of("books.csv")); show("missing", missing); } catch (Throwable _t) { System.out.println("missing -> " + _t); }
        }
        {
            Library library = new Library();
            String loan = library.lend("emma");        // "lent emma"
            show("loan", loan);
            try { String twice = library.lend("dune"); show("twice", twice); } catch (Throwable _t) { System.out.println("twice -> " + _t); }
        }
        {
            RuntimeException notSet = null;
            String thrown;
            try {
                throw notSet;
            } catch (NullPointerException e) {
                thrown = e.getClass().getSimpleName();
            }
            try { String result = thrown; show("result", result); } catch (Throwable _t) { System.out.println("result -> " + _t); }
        }
        {
            try { List<String> members = readMembers(Path.of("members.csv")); show("members", members); } catch (Throwable _t) { System.out.println("members -> " + _t); }
        }
        {
            String page = showBook("emma");    // "Catalog unavailable: catalog.csv"
            show("page", page);
        }
        {
            try { String wrapped = titleOrFail("emma"); show("wrapped", wrapped); } catch (Throwable _t) { System.out.println("wrapped -> " + _t); }
        }
        {
            String fromMemory = new MemorySource().read();     // "memory"
            show("fromMemory", fromMemory);
        }
        {
            int magazine = loanDays("magazine");          // 7
            show("magazine", magazine);
            try { int ebook = loanDays("ebook"); show("ebook", ebook); } catch (Throwable _t) { System.out.println("ebook -> " + _t); }
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

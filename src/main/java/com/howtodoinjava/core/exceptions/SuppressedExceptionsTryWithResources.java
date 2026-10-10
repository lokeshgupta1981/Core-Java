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
 * Examples for the tutorial "Java Suppressed Exceptions with try-with-resources".
 * https://howtodoinjava.com/java/exception-handling/java-suppressed-exceptions/
 */
public class SuppressedExceptionsTryWithResources {
    static class Upload implements AutoCloseable {
        private final String name;

        Upload(String name) {
            this.name = name;
        }

        void send() throws IOException {
            throw new IOException("upload interrupted: " + name);
        }

        @Override
        public void close() throws IOException {
            throw new IOException("close failed: " + name);
        }
    }
    static void backupWithFinally(String album) throws IOException {
        Upload upload = new Upload(album);
        try {
            upload.send();
        } finally {
            upload.close();
        }
    }
    static void backup(String album) throws IOException {
        try (Upload upload = new Upload(album)) {
            upload.send();
        }
    }
    static void backupAlbumAndThumbnails() throws IOException {
        try (Upload photos = new Upload("photos"); Upload thumbs = new Upload("thumbs")) {
            photos.send();
        }
    }
    static void deleteAll(List<Path> files) throws IOException {
        IOException failure = null;
        for (Path file : files) {
            try {
                Files.delete(file);
            } catch (IOException e) {
                if (failure == null) {
                    failure = new IOException("cleanup failed");
                }
                failure.addSuppressed(e);
            }
        }
        if (failure != null) {
            throw failure;
        }
    }
    static class QuietException extends RuntimeException {
        QuietException(String message) {
            super(message, null, false, false);
        }
    }
    public static void main(String[] args) throws Exception {
        {
            IOException primary = new IOException("upload interrupted");
            primary.addSuppressed(new IOException("close failed"));
            int count = primary.getSuppressed().length;               // 1
            show("count", count);
            String attached = primary.getSuppressed()[0].getMessage(); // "close failed"
            show("attached", attached);
        }
        {
            IOException lost = null;
            try {
                backupWithFinally("photos");
            } catch (IOException e) {
                lost = e;
            }
            String seen = lost.getMessage();                       // "close failed: photos"
            show("seen", seen);
            int attached = lost.getSuppressed().length;            // 0
            show("attached", attached);
        }
        {
            IOException failure = null;
            try {
                backup("photos");
            } catch (IOException e) {
                failure = e;
            }
            String primaryMessage = failure.getMessage();                    // "upload interrupted: photos"
            show("primaryMessage", primaryMessage);
            String hiddenMessage = failure.getSuppressed()[0].getMessage();  // "close failed: photos"
            show("hiddenMessage", hiddenMessage);
        }
        {
            List<String> closeErrors = new ArrayList<>();
            try {
                backupAlbumAndThumbnails();
            } catch (IOException e) {
                for (Throwable s : e.getSuppressed()) {
                    closeErrors.add(s.getMessage());
                }
            }
            List<String> order = closeErrors;          // [close failed: thumbs, close failed: photos]
            show("order", order);
        }
        {
            int failedFiles = 0;
            try {
                deleteAll(List.of(Path.of("a.tmp"), Path.of("b.tmp")));
            } catch (IOException e) {
                failedFiles = e.getSuppressed().length;
            }
            try { int failed = failedFiles; show("failed", failed); } catch (Throwable _t) { System.out.println("failed -> " + _t); }
        }
        {
            IOException report = new IOException("report");
            try { report.addSuppressed(report);  } catch (Throwable _t) { System.out.println("-> " + _t); }
            try { report.addSuppressed(null);  } catch (Throwable _t) { System.out.println("-> " + _t); }
        }
        {
            QuietException quiet = new QuietException("cache miss");
            quiet.addSuppressed(new IOException("ignored"));
            int kept = quiet.getSuppressed().length;   // 0
            show("kept", kept);
            int frames = quiet.getStackTrace().length; // 0
            show("frames", frames);
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

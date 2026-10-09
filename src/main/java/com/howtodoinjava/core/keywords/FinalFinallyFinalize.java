package com.howtodoinjava.core.keywords;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.time.*;
import java.nio.file.*;
import java.math.*;
import java.io.*;

import java.lang.ref.Cleaner;

/**
 * Examples for the tutorial "final vs finally vs finalize in Java".
 * https://howtodoinjava.com/java/keywords/final-finally-finalize/
 */
public class FinalFinallyFinalize {
    static final class Money {
        private final long cents;           // blank final, set in the constructor

        Money(long cents) {
            this.cents = cents;
        }

        final long cents() {
            return cents;
        }
    }
    static String readConfig(boolean fail) {
        StringBuilder log = new StringBuilder("open;");
        try {
            if (fail) {
                throw new IllegalStateException("bad file");
            }
            log.append("read;");
        } catch (IllegalStateException e) {
            log.append("error;");
        } finally {
            log.append("close");
        }
        return log.toString();
    }
    @SuppressWarnings("finally")
    static int lostException() {
        try {
            throw new IllegalStateException("lost");
        } finally {
            return -1;                      // the exception disappears
        }
    }
    static class TempFile implements AutoCloseable {
        private static final Cleaner CLEANER = Cleaner.create();
        private final Cleaner.Cleanable cleanable;
        static final List<String> LOG = new ArrayList<>();

        TempFile(String name) {
            cleanable = CLEANER.register(this, () -> LOG.add("deleted " + name));
        }

        @Override
        public void close() {
            cleanable.clean();               // runs the action once
        }
    }
    public static void main(String[] args) throws Exception {
        {
            final int maxRetries = 3;               // final: cannot be reassigned
            show("maxRetries", maxRetries);
            String status;
            try {
                status = "attempts allowed: " + maxRetries;
            } finally {
                status = "checked";                 // finally: runs after try
            }
            String result = status;                 // "checked"
            show("result", result);
            // finalize(): never called here, and deprecated for removal since Java 18
        }
        {
            Money price = new Money(1999);
            long cents = price.cents();             // 1999
            show("cents", cents);
        }
        {
            final List<String> tags = new ArrayList<>();
            tags.add("sale");                       // allowed, the list changes
            int tagCount = tags.size();             // 1
            show("tagCount", tagCount);
            final List<String> fixed = List.of("new");
            try { fixed.add("sale");  } catch (Throwable _t) { System.out.println("-> " + _t); }
        }
        {
            String ok = readConfig(false);          // "open;read;close"
            show("ok", ok);
            String failed = readConfig(true);       // "open;error;close"
            show("failed", failed);
        }
        {
            int code = lostException();             // -1, no exception reaches the caller
            show("code", code);
        }
        {
            try (TempFile file = new TempFile("report.tmp")) {
                TempFile.LOG.add("writing");
            }
            String log = TempFile.LOG.toString();   // "[writing, deleted report.tmp]"
            show("log", log);
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

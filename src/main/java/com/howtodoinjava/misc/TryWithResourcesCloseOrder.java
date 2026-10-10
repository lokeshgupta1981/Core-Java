package com.howtodoinjava.misc;

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
import java.sql.*;
import java.util.concurrent.locks.*;

/**
 * Examples for the tutorial "Java try-with-resources: Syntax, Close Order and Examples".
 * https://howtodoinjava.com/java/try-with-resources/
 */
public class TryWithResourcesCloseOrder {
    static class TrackedResource implements AutoCloseable {
        private final String name;
        private final List<String> log;
        private final boolean failOnClose;

        TrackedResource(String name, List<String> log, boolean failOnClose) {
            this.name = name;
            this.log = log;
            this.failOnClose = failOnClose;
            log.add("open " + name);
        }

        TrackedResource(String name, List<String> log) {
            this(name, log, false);
        }

        static TrackedResource openBroken(String name, List<String> log) {
            log.add("open " + name + " failed");
            throw new IllegalStateException("cannot open " + name);
        }

        @Override
        public void close() {
            log.add("close " + name);
            if (failOnClose) {
                throw new IllegalStateException("close failed: " + name);
            }
        }
    }
    static void exportOldStyle(List<String> log) {
        TrackedResource csv = new TrackedResource("csv", log, true);
        try {
            throw new IllegalStateException("disk full");
        } finally {
            csv.close();
        }
    }
    static void exportNewStyle(List<String> log) {
        try (TrackedResource csv = new TrackedResource("csv", log, true)) {
            throw new IllegalStateException("disk full");
        }
    }
    static String messageOf(Runnable action) {
        try {
            action.run();
            return "no exception";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }
    static record LockGuard(Lock lock) implements AutoCloseable {
        static LockGuard acquire(Lock lock) {
            lock.lock();
            return new LockGuard(lock);
        }

        @Override
        public void close() {
            lock.unlock();
        }
    }
    public static void main(String[] args) throws Exception {
        {
            Path path = Path.of("pantry.csv");
            String first;
            try (BufferedReader reader = Files.newBufferedReader(path)) {
                first = reader.readLine();
            }
            String firstLine = first;                       // "apple,5"
            show("firstLine", firstLine);
        }
        {
            String oldStyle = messageOf(() -> exportOldStyle(new ArrayList<>()));   // "close failed: csv"
            show("oldStyle", oldStyle);
            String newStyle = messageOf(() -> exportNewStyle(new ArrayList<>()));   // "disk full"
            show("newStyle", newStyle);
        }
        {
            BufferedReader reader = Files.newBufferedReader(Path.of("pantry.csv"));
            String header;
            try (reader) {
                header = reader.readLine();
            }
            String readFirst = header;                      // "apple,5"
            show("readFirst", readFirst);
        }
        {
            List<String> log = new ArrayList<>();
            TrackedResource optionalCache = null;
            try (optionalCache) {
                log.add("body");
            }
            List<String> steps = log;                       // [body]
            show("steps", steps);
        }
        {
            List<String> log = new ArrayList<>();
            try (TrackedResource db = new TrackedResource("db", log);
            TrackedResource csv = new TrackedResource("csv", log)) {
                log.add("export recipes");
                throw new IllegalStateException("disk full");
            } catch (IllegalStateException e) {
                log.add("catch " + e.getMessage());
            } finally {
                log.add("finally");
            }
            List<String> order = log;   // [open db, open csv, export recipes, close csv, close db, catch disk full, finally]
            show("order", order);
        }
        {
            List<String> log = new ArrayList<>();
            try (TrackedResource db = new TrackedResource("db", log);
            TrackedResource csv = TrackedResource.openBroken("csv", log)) {
                log.add("export recipes");
            } catch (IllegalStateException e) {
                log.add("catch " + e.getMessage());
            }
            List<String> order = log;   // [open db, open csv failed, close db, catch cannot open csv]
            show("order", order);
        }
        {
            List<String> log = new ArrayList<>();
            Throwable caught = null;
            try (TrackedResource db = new TrackedResource("db", log, true);
            TrackedResource csv = new TrackedResource("csv", log, true)) {
                throw new IllegalStateException("disk full");
            } catch (IllegalStateException e) {
                caught = e;
            }
            String primary = caught.getMessage();                          // "disk full"
            show("primary", primary);
            int suppressedCount = caught.getSuppressed().length;            // 2
            show("suppressedCount", suppressedCount);
            String firstSuppressed = caught.getSuppressed()[0].getMessage(); // "close failed: csv"
            show("firstSuppressed", firstSuppressed);
        }
        {
            int stock;
            try (Connection con = DriverManager.getConnection("jdbc:h2:mem:pantry");
            Statement setup = con.createStatement()) {
                setup.execute("create table item(name varchar(20), qty int)");
                setup.execute("insert into item values ('apple', 5), ('banana', 3)");
                try (PreparedStatement query = con.prepareStatement("select qty from item where name = ?")) {
                    query.setString(1, "apple");
                    try (ResultSet rs = query.executeQuery()) {
                        rs.next();
                        stock = rs.getInt("qty");
                    }
                }
            }
            int appleStock = stock;                         // 5
            show("appleStock", appleStock);
        }
        {
            long count;
            try (Stream<String> lines = Files.lines(Path.of("pantry.csv"))) {
                count = lines.filter(line -> line.startsWith("b")).count();
            }
            long itemsWithB = count;                        // 1
            show("itemsWithB", itemsWithB);
        }
        {
            AtomicInteger done = new AtomicInteger();
            try (ExecutorService pool = Executors.newVirtualThreadPerTaskExecutor()) {
                for (int i = 0; i < 3; i++) {
                    pool.submit(done::incrementAndGet);
                }
            }
            int finished = done.get();                      // 3
            show("finished", finished);
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

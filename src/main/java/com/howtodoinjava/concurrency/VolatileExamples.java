package com.howtodoinjava.concurrency;

import java.util.concurrent.locks.*;
import java.lang.management.*;
import java.time.format.*;
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
 * Examples for the tutorial "Java volatile Keyword: Thread Safety, Visibility and Atomicity".
 * https://howtodoinjava.com/java/multi-threading/volatile-variables-and-thread-safety/
 */
public class VolatileExamples {
    static class ImportWorker implements Runnable {
        private volatile boolean running = true;
        private int imported;

        @Override
        public void run() {
            while (running) {                        // fresh read on every pass
                imported++;                          // only this thread writes it
                LockSupport.parkNanos(Duration.ofMillis(1).toNanos());
            }
        }

        void stop() {
            running = false;
        }

        int imported() {
            return imported;
        }
    }
    static class PageCounter {
        volatile int views;                          // visible, but ++ is not atomic
        final AtomicInteger atomicViews = new AtomicInteger();
    }
    static record MailConfig(String host, int port, List<String> admins) {
        MailConfig {
            admins = List.copyOf(admins);            // immutable copy
        }
    }
    static class ConfigHolder {
        private volatile MailConfig current = new MailConfig("localhost", 25, List.of("lokesh"));

        MailConfig get() {
            return current;
        }

        void reload(MailConfig fresh) {
            current = fresh;                         // one atomic reference write
        }
    }
    static class TemplateEngine {
        private static volatile TemplateEngine instance;
        private final Map<String, String> templates;

        private TemplateEngine() {
            templates = Map.of("welcome", "Hello {name}");   // expensive in real code
        }

        static TemplateEngine getInstance() {
            TemplateEngine local = instance;                 // one volatile read
            if (local == null) {
                synchronized (TemplateEngine.class) {
                    local = instance;
                    if (local == null) {
                        local = new TemplateEngine();
                        instance = local;                    // publishes a fully built object
                    }
                }
            }
            return local;
        }

        String template(String name) {
            return templates.get(name);
        }
    }
    static class ReportCache {
        private ReportCache() {
        }

        private static class Holder {
            static final ReportCache INSTANCE = new ReportCache();
        }

        static ReportCache getInstance() {
            return Holder.INSTANCE;
        }
    }
    public static void main(String[] args) throws Exception {
        {
            ImportWorker worker = new ImportWorker();
            Thread importer = Thread.ofPlatform().start(worker);
            LockSupport.parkNanos(Duration.ofMillis(50).toNanos());   // let it work for a moment
            worker.stop();                                          // volatile write
            importer.join();
            boolean stopped = !importer.isAlive();                  // true
            show("stopped", stopped);
        }
        {
            PageCounter counter = new PageCounter();
            Runnable visit = () -> {
                for (int i = 0; i < 1_000_000; i++) {
                    counter.views++;
                    counter.atomicViews.incrementAndGet();
                }
            };
            Thread visitor1 = Thread.ofPlatform().start(visit);
            Thread visitor2 = Thread.ofPlatform().start(visit);
            visitor1.join();
            visitor2.join();
            int volatileViews = counter.views;               // less than 2000000 in most runs
            show("volatileViews", volatileViews);
            int atomicTotal = counter.atomicViews.get();     // 2000000
            show("atomicTotal", atomicTotal);
        }
        {
            ConfigHolder holder = new ConfigHolder();
            Thread reloader = Thread.ofPlatform().start(() -> holder.reload(new MailConfig("smtp.local", 587, List.of("lokesh", "alex"))));
            reloader.join();
            int port = holder.get().port();                  // 587
            show("port", port);
            int adminCount = holder.get().admins().size();   // 2
            show("adminCount", adminCount);
        }
        {
            TemplateEngine engine1 = TemplateEngine.getInstance();
            TemplateEngine engine2 = TemplateEngine.getInstance();
            boolean sameEngine = engine1 == engine2;                 // true
            show("sameEngine", sameEngine);
            String welcome = engine1.template("welcome");            // "Hello {name}"
            show("welcome", welcome);
        }
        {
            boolean sameCache = ReportCache.getInstance() == ReportCache.getInstance();   // true
            show("sameCache", sameCache);
        }
        {
            AtomicIntegerArray slots = new AtomicIntegerArray(3);    // volatile semantics per element
            show("slots", slots);
            slots.set(1, 7);
            int slot = slots.get(1);                                  // 7
            show("slot", slot);
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

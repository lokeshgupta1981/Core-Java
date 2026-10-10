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
 * Examples for the tutorial "Java UncaughtExceptionHandler: Log and Restart Failed Threads".
 * https://howtodoinjava.com/java/multi-threading/restart-thread-uncaughtexceptionhandler/
 */
public class UncaughtExceptionHandlerExamples {
    static Runnable parseQuantities(List<String> quantities, List<Integer> parsed) {
        return () -> {
            for (String quantity : quantities) {
                parsed.add(Integer.parseInt(quantity));
            }
        };
    }
    static class ImportFailureHandler implements Thread.UncaughtExceptionHandler {
        final List<String> log = new CopyOnWriteArrayList<>();

        @Override
        public void uncaughtException(Thread t, Throwable e) {
            log.add(t.getName() + " " + t.getState() + " " + e.getClass().getSimpleName());
        }
    }
    static void runWithRestarts(Runnable task, int maxStarts, AtomicInteger starts, List<String> log) {
        int attempt = starts.incrementAndGet();
        Thread.ofPlatform()
                .name("consumer-" + attempt)
                .uncaughtExceptionHandler((t, e) -> {
            log.add(t.getName() + " failed: " + e.getMessage());
            if (attempt < maxStarts) {
                runWithRestarts(task, maxStarts, starts, log);     // new thread, same task
            }
        })
                .start(task);
    }
    public static void main(String[] args) throws Exception {
        {
            List<String> failures = new CopyOnWriteArrayList<>();
            Thread importer = Thread.ofPlatform()
                    .name("importer")
                    .uncaughtExceptionHandler((t, e) -> failures.add(t.getName() + ": " + e.getClass().getSimpleName()))
                    .start(() -> Integer.parseInt("12x"));
            importer.join();
            boolean alive = importer.isAlive();         // false, the thread still ends
            show("alive", alive);
            try { String logged = failures.get(0); show("logged", logged); } catch (Throwable _t) { System.out.println("logged -> " + _t); }
        }
        {
            List<Integer> parsed = new CopyOnWriteArrayList<>();
            Thread importer = new Thread(parseQuantities(List.of("123", "234", "345", "XYZ", "456"), parsed), "importer");
            importer.start();
            importer.join();
            List<Integer> done = List.copyOf(parsed);           // [123, 234, 345]
            show("done", done);
            boolean alive = importer.isAlive();                 // false
            show("alive", alive);
        }
        {
            ImportFailureHandler handler = new ImportFailureHandler();
            List<Integer> parsed = new CopyOnWriteArrayList<>();
            Thread importer = new Thread(parseQuantities(List.of("123", "XYZ"), parsed), "importer");
            importer.setUncaughtExceptionHandler(handler);
            importer.start();
            importer.join();
            List<Integer> done = List.copyOf(parsed);           // [123]
            show("done", done);
            try { String entry = handler.log.get(0); show("entry", entry); } catch (Throwable _t) { System.out.println("entry -> " + _t); }
        }
        {
            List<String> appLog = new CopyOnWriteArrayList<>();
            Thread.setDefaultUncaughtExceptionHandler((t, e) -> appLog.add(t.getName() + ": " + e.getClass().getSimpleName()));
            Thread job = Thread.ofPlatform().name("nightly-job").start(() -> List.of().getFirst());
            job.join();
            try { String logged = appLog.get(0); show("logged", logged); } catch (Throwable _t) { System.out.println("logged -> " + _t); }
        }
        {
            Thread once = Thread.ofPlatform().start(() -> {});
            once.join();
            try { once.start();  } catch (Throwable _t) { System.out.println("-> " + _t); }
        }
        {
            AtomicInteger starts = new AtomicInteger();
            List<String> log = new CopyOnWriteArrayList<>();
            CountDownLatch connected = new CountDownLatch(1);
            Runnable consumer = () -> {
                if (starts.get() < 3) {
                    throw new IllegalStateException("broker not ready");
                }
                connected.countDown();
            };
            runWithRestarts(consumer, 5, starts, log);
            boolean ok = connected.await(2, TimeUnit.SECONDS);      // true
            show("ok", ok);
            int attempts = starts.get();                            // 3
            show("attempts", attempts);
            String firstFailure = log.get(0);                       // "consumer-1 failed: broker not ready"
            show("firstFailure", firstFailure);
        }
        {
            List<String> seen = new CopyOnWriteArrayList<>();
            CountDownLatch reported = new CountDownLatch(1);
            ThreadFactory factory = Thread.ofPlatform().name("job-", 1).uncaughtExceptionHandler((t, e) -> {
                seen.add(e.getMessage());
                reported.countDown();
            }).factory();
            Future<?> submitted;
            try (ExecutorService pool = Executors.newFixedThreadPool(2, factory)) {
                pool.execute(() -> Integer.parseInt("from execute"));
                submitted = pool.submit(() -> Integer.parseInt("from submit"));
                reported.await(1, TimeUnit.SECONDS);
            }
            int handledCount = seen.size();                                 // 1
            show("handledCount", handledCount);
            Future.State state = submitted.state();                         // FAILED
            show("state", state);
            try { String stored = submitted.exceptionNow().getClass().getSimpleName(); show("stored", stored); } catch (Throwable _t) { System.out.println("stored -> " + _t); }
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

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
 * Examples for the tutorial "Thread Name in Java: Set and Get the Name of a Thread".
 * https://howtodoinjava.com/java/multi-threading/set-get-thread-name/
 */
public class ThreadNames {
    static <T> T withThreadName(String name, Callable<T> task) throws Exception {
        Thread current = Thread.currentThread();
        String original = current.getName();
        current.setName(name);
        try {
            return task.call();
        } finally {
            current.setName(original);
        }
    }
    public static void main(String[] args) throws Exception {
        {
            Thread mailer = new Thread(() -> {}, "invoice-mailer");    // name in the constructor
            show("mailer", mailer);
            String given = mailer.getName();                             // "invoice-mailer"
            show("given", given);

            mailer.setName("receipt-mailer");                            // rename at any time
            String renamed = mailer.getName();                           // "receipt-mailer"
            show("renamed", renamed);

            Thread built = Thread.ofPlatform().name("export-", 1).unstarted(() -> {});
            String fromBuilder = built.getName();                        // "export-1"
            show("fromBuilder", fromBuilder);

            String current = Thread.currentThread().getName();           // "main"
            show("current", current);
        }
        {
            Thread plain = new Thread(() -> {});
            boolean counterName = plain.getName().startsWith("Thread-");          // true
            show("counterName", counterName);

            Future<String> pooled;
            try (ExecutorService pool = Executors.newFixedThreadPool(3)) {
                pooled = pool.submit(() -> Thread.currentThread().getName());
            }
            boolean poolName = pooled.resultNow().matches("pool-\\d+-thread-1");    // true
            show("poolName", poolName);

            String common = CompletableFuture.supplyAsync(() -> Thread.currentThread().getName()).join();   // "ForkJoinPool.commonPool-worker-1", the number varies
            show("common", common);
            String virtual = Thread.ofVirtual().unstarted(() -> {}).getName();   // ""
            show("virtual", virtual);
        }
        {
            AtomicReference<String> seen = new AtomicReference<>();
            Thread worker = new Thread(() -> seen.set(Thread.currentThread().getName()), "report-builder");
            worker.start();
            worker.join();
            String inside = seen.get();                       // "report-builder"
            show("inside", inside);
            String outside = worker.getName();                // "report-builder"
            show("outside", outside);
        }
        {
            Thread billing = new Thread(() -> {}, "billing-sync");
            String billingName = billing.getName();           // "billing-sync"
            show("billingName", billingName);
        }
        {
            Thread cleaner = new Thread(() -> {});
            cleaner.setName("cache-cleaner");
            String cleanerName = cleaner.getName();           // "cache-cleaner"
            show("cleanerName", cleanerName);
            try { cleaner.setName(null);  } catch (Throwable _t) { System.out.println("-> " + _t); }
        }
        {
            Thread.Builder builder = Thread.ofVirtual().name("upload-", 1);
            Thread first = builder.unstarted(() -> {});
            Thread second = builder.unstarted(() -> {});
            String firstName = first.getName();               // "upload-1"
            show("firstName", firstName);
            String secondName = second.getName();             // "upload-2"
            show("secondName", secondName);

            Thread single = Thread.ofPlatform().name("scheduler").unstarted(() -> {});
            String singleName = single.getName();             // "scheduler"
            show("singleName", singleName);
        }
        {
            ThreadFactory pdfThreads = Thread.ofPlatform().name("invoice-pdf-", 1).factory();
            Future<String> rendered;
            try (ExecutorService pool = Executors.newFixedThreadPool(4, pdfThreads)) {
                rendered = pool.submit(() -> Thread.currentThread().getName());
            }
            String pdfThread = rendered.resultNow();          // "invoice-pdf-1"
            show("pdfThread", pdfThread);

            ThreadFactory fetchThreads = Thread.ofVirtual().name("fetch-", 1).factory();
            Future<String> fetched;
            try (ExecutorService executor = Executors.newThreadPerTaskExecutor(fetchThreads)) {
                fetched = executor.submit(() -> Thread.currentThread().getName());
            }
            String fetchThread = fetched.resultNow();         // "fetch-1"
            show("fetchThread", fetchThread);
        }
        {
            Future<String> later;
            try (ExecutorService single = Executors.newSingleThreadExecutor()) {
                single.submit(() -> Thread.currentThread().setName("order-42"));
                later = single.submit(() -> Thread.currentThread().getName());
            }
            String leaked = later.resultNow();                // "order-42", from the previous task
            show("leaked", leaked);
        }
        {
            String during = withThreadName("order-43", () -> Thread.currentThread().getName());   // "order-43"
            show("during", during);
            String restored = Thread.currentThread().getName();                                   // "main"
            show("restored", restored);
        }
        {
            Thread a = new Thread(() -> {}, "worker");
            Thread b = new Thread(() -> {}, "worker");
            boolean sameName = a.getName().equals(b.getName());   // true
            show("sameName", sameName);
            boolean sameId = a.threadId() == b.threadId();        // false
            show("sameId", sameId);
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

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
 * Examples for the tutorial "Java Thread Pool: ThreadPoolExecutor and Fixed Pools".
 * https://howtodoinjava.com/java/multi-threading/java-thread-pool-executor-example/
 */
public class ThreadPoolGuide {
    static int poolSize(int cores, long waitMs, long computeMs) {
        return (int) (cores * (1 + (double) waitMs / computeMs));
    }
    public static void main(String[] args) throws Exception {
        {
            Set<String> workers = ConcurrentHashMap.newKeySet();
            ThreadFactory named = Thread.ofPlatform().name("worker-", 1).factory();
            try (ExecutorService pool = Executors.newFixedThreadPool(3, named)) {
                for (int i = 0; i < 6; i++) {
                    pool.execute(() -> workers.add(Thread.currentThread().getName()));
                }
            }
            int threadsUsed = workers.size();               // 3
            show("threadsUsed", threadsUsed);
        }
        {
            ThreadPoolExecutor fixed = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
            int fixedCore = fixed.getCorePoolSize();        // 2
            show("fixedCore", fixedCore);
            fixed.close();
            try { ThreadPoolExecutor single = (ThreadPoolExecutor) Executors.newSingleThreadExecutor(); show("single", single); } catch (Throwable _t) { System.out.println("single -> " + _t); }
        }
        {
            CountDownLatch gate = new CountDownLatch(1);
            ThreadPoolExecutor reports = (ThreadPoolExecutor) Executors.newFixedThreadPool(4);
            for (int i = 1; i <= 10; i++) {
                reports.execute(() -> {
                    try {
                        gate.await();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });
            }
            int poolSize = reports.getPoolSize();           // 4
            show("poolSize", poolSize);
            int waiting = reports.getQueue().size();        // 6
            show("waiting", waiting);
            gate.countDown();
            reports.shutdown();
            boolean finished = reports.awaitTermination(5, TimeUnit.SECONDS);   // true
            show("finished", finished);
            long completed = reports.getCompletedTaskCount();   // 10
            show("completed", completed);
            int largest = reports.getLargestPoolSize();     // 4
            show("largest", largest);
        }
        {
            ThreadPoolExecutor mailer = new ThreadPoolExecutor(
            2, 4, 30, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(100),
            Thread.ofPlatform().name("mailer-", 1).factory(),
            new ThreadPoolExecutor.CallerRunsPolicy());
            int capacity = mailer.getQueue().remainingCapacity();   // 100
            show("capacity", capacity);
            int maxThreads = mailer.getMaximumPoolSize();   // 4
            show("maxThreads", maxThreads);
            mailer.close();
        }
        {
            int cpuBound = poolSize(8, 0, 10);              // 8
            show("cpuBound", cpuBound);
            int ioBound = poolSize(8, 90, 10);              // 80
            show("ioBound", ioBound);
            int cores = Runtime.getRuntime().availableProcessors();
        }
        {
            AtomicInteger handled = new AtomicInteger();
            try (ExecutorService perTask = Executors.newVirtualThreadPerTaskExecutor()) {
                for (int i = 0; i < 1_000; i++) {
                    perTask.submit(() -> handled.incrementAndGet());
                }
            }
            int requests = handled.get();                   // 1000
            show("requests", requests);
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

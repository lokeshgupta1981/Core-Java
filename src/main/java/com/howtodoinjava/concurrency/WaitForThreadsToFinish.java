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
 * Examples for the tutorial "Java Wait for All Threads to Finish: 7 Ways With Examples".
 * https://howtodoinjava.com/java/multi-threading/wait-for-threads-to-finish/
 */
public class WaitForThreadsToFinish {
    static void pause(long ms) {
        LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(ms));
    }
    static int resize(int photoId) {
        pause(10L * (5 - photoId % 5));     // later photos finish first
        return photoId * 100;
    }
    static boolean shutdownAndAwaitTermination(ExecutorService pool) {
        pool.shutdown();
        try {
            if (!pool.awaitTermination(60, TimeUnit.SECONDS)) {
                pool.shutdownNow();
                return pool.awaitTermination(60, TimeUnit.SECONDS);
            }
            return true;
        } catch (InterruptedException e) {
            pool.shutdownNow();
            Thread.currentThread().interrupt();
            return false;
        }
    }
    public static void main(String[] args) throws Exception {
        {
            List<Integer> thumbnails = Collections.synchronizedList(new ArrayList<>());
            try (ExecutorService pool = Executors.newVirtualThreadPerTaskExecutor()) {
                for (int i = 1; i <= 3; i++) {
                    int photo = i;
                    pool.submit(() -> thumbnails.add(photo));
                }
            }                                       // close() waits for all three tasks
            int ready = thumbnails.size();          // 3
            show("ready", ready);
        }
        {
            ExecutorService pool = Executors.newFixedThreadPool(4);
            List<Future<Integer>> futures = new ArrayList<>();
            for (int id = 1; id <= 5; id++) {
                int photoId = id;
                futures.add(pool.submit(() -> resize(photoId)));
            }
            List<Integer> sizes = new ArrayList<>();
            for (Future<Integer> future : futures) {
                sizes.add(future.get(5, TimeUnit.SECONDS));
            }
            pool.shutdown();
            List<Integer> allSizes = List.copyOf(sizes);   // [100, 200, 300, 400, 500]
            show("allSizes", allSizes);
        }
        {
            ExecutorService pool = Executors.newFixedThreadPool(4);
            AtomicInteger resized = new AtomicInteger();
            for (int id = 1; id <= 8; id++) {
                int photoId = id;
                pool.submit(() -> resized.addAndGet(resize(photoId) > 0 ? 1 : 0));
            }
            boolean finished = shutdownAndAwaitTermination(pool);   // true
            show("finished", finished);
            int count = resized.get();                               // 8
            show("count", count);
        }
        {
            ExecutorService pool = Executors.newFixedThreadPool(3);
            List<Callable<Integer>> tasks = List.of(() -> resize(1), () -> resize(2), () -> resize(3));
            List<Future<Integer>> done = pool.invokeAll(tasks, 5, TimeUnit.SECONDS);
            List<Integer> sizes = done.stream().map(Future::resultNow).toList();   // [100, 200, 300]
            show("sizes", sizes);
            pool.close();
        }
        {
            List<String> checks = List.of("network", "cache", "database");
            CountDownLatch latch = new CountDownLatch(checks.size());
            Set<String> passed = ConcurrentHashMap.newKeySet();
            for (String check : checks) {
                Thread.ofVirtual().start(() -> {
                    try {
                        passed.add(check);
                    } finally {
                        latch.countDown();
                    }
                });
            }
            boolean allChecked = latch.await(2, TimeUnit.SECONDS);   // true
            show("allChecked", allChecked);
            int healthy = passed.size();                             // 3
            show("healthy", healthy);
        }
        {
            AtomicInteger uploaded = new AtomicInteger();
            try (ExecutorService pool = Executors.newVirtualThreadPerTaskExecutor()) {
                for (int id = 1; id <= 100; id++) {
                    int photoId = id;
                    pool.submit(() -> uploaded.addAndGet(resize(photoId) > 0 ? 1 : 0));
                }
            }
            int total = uploaded.get();             // 100
            show("total", total);
        }
        {
            ExecutorService pool = Executors.newVirtualThreadPerTaskExecutor();
            List<CompletableFuture<Integer>> quotes = List.of(
            CompletableFuture.supplyAsync(() -> resize(1), pool),
            CompletableFuture.supplyAsync(() -> resize(2), pool),
            CompletableFuture.supplyAsync(() -> resize(3), pool));
            CompletableFuture.allOf(quotes.toArray(CompletableFuture[]::new)).get(5, TimeUnit.SECONDS);
            int cheapest = quotes.stream().mapToInt(CompletableFuture::join).min().orElseThrow();   // 100
            show("cheapest", cheapest);
            pool.close();
        }
        {
            List<Thread> workers = new ArrayList<>();
            AtomicInteger processed = new AtomicInteger();
            for (int id = 1; id <= 4; id++) {
                int photoId = id;
                workers.add(Thread.ofPlatform().start(() -> processed.addAndGet(resize(photoId) > 0 ? 1 : 0)));
            }
            for (Thread worker : workers) {
                worker.join();
            }
            int count = processed.get();            // 4
            show("count", count);
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

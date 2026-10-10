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
 * Examples for the tutorial "Daemon Thread in Java: JVM Exit Rules and Examples".
 * https://howtodoinjava.com/java/multi-threading/daemon-threads/
 */
public class DaemonThreads {
    static void pause(long ms) {
        LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(ms));
    }
    public static void main(String[] args) throws Exception {
        {
            Thread sweeper = new Thread(() -> {}, "cache-sweeper");
            sweeper.setDaemon(true);                          // must happen before start()
            boolean daemon = sweeper.isDaemon();              // true
            show("daemon", daemon);

            Thread importer = new Thread(() -> {}, "csv-import");
            boolean user = importer.isDaemon();               // false, inherited from main
            show("user", user);

            Thread virtual = Thread.ofVirtual().unstarted(() -> {});
            boolean alwaysDaemon = virtual.isDaemon();        // true
            show("alwaysDaemon", alwaysDaemon);
        }
        {
            CountDownLatch stop = new CountDownLatch(1);
            Thread reporter = new Thread(() -> {
                try {
                    stop.await();                             // runs until told to stop
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }, "metrics-reporter");
            reporter.setDaemon(true);
            reporter.start();
            boolean marked = reporter.isDaemon();             // true
            show("marked", marked);
            try { reporter.setDaemon(false);  } catch (Throwable _t) { System.out.println("-> " + _t); }
            stop.countDown();
            reporter.join();
        }
        {
            Thread heartbeat = Thread.ofPlatform().name("heartbeat").daemon(true).start(() -> {});
            boolean builderDaemon = heartbeat.isDaemon();     // true
            show("builderDaemon", builderDaemon);

            ThreadFactory daemonFactory = Thread.ofPlatform().name("sweeper-", 1).daemon(true).factory();
            boolean fromFactory = daemonFactory.newThread(() -> {}).isDaemon();   // true
            show("fromFactory", fromFactory);
        }
        {
            AtomicBoolean childFlag = new AtomicBoolean();
            Thread parent = new Thread(() -> childFlag.set(new Thread(() -> {}).isDaemon()));
            parent.setDaemon(true);
            parent.start();
            parent.join();
            boolean inherited = childFlag.get();              // true, child of a daemon thread
            show("inherited", inherited);
        }
        {
            boolean mainDaemon = Thread.currentThread().isDaemon();   // false
            show("mainDaemon", mainDaemon);
            try { Thread.currentThread().setDaemon(true);  } catch (Throwable _t) { System.out.println("-> " + _t); }
        }
        {
            Thread fetcher = Thread.ofVirtual().unstarted(() -> {});
            fetcher.setDaemon(true);                          // allowed, no effect
            boolean stillDaemon = fetcher.isDaemon();         // true
            show("stillDaemon", stillDaemon);
            try { fetcher.setDaemon(false);  } catch (Throwable _t) { System.out.println("-> " + _t); }
        }
        {
            Runtime.getRuntime().addShutdownHook(new Thread(() -> System.out.println("shutdown hook runs")));

            Thread flusher = new Thread(() -> {
                try {
                    for (int batch = 1; batch <= 10; batch++) {
                        System.out.println("flushed batch " + batch);
                        pause(100);
                    }
                } finally {
                    System.out.println("flusher finally");    // never printed
                }
            });
            flusher.setDaemon(true);
            flusher.start();
            pause(250);
            System.out.println("main ends");
        }
        {
            boolean poolDaemon = Executors.defaultThreadFactory().newThread(() -> {}).isDaemon();   // false
            show("poolDaemon", poolDaemon);
            boolean commonDaemon = CompletableFuture.supplyAsync(() -> Thread.currentThread().isDaemon()).join();   // true
            show("commonDaemon", commonDaemon);
        }
        {
            ScheduledExecutorService sweeperPool = Executors.newSingleThreadScheduledExecutor(
            Thread.ofPlatform().name("price-cache-sweeper").daemon(true).factory());
            sweeperPool.scheduleAtFixedRate(() -> {}, 30, 30, TimeUnit.SECONDS);
            boolean sweeperIsDaemon = sweeperPool.submit(() -> Thread.currentThread().isDaemon()).get();   // true
            show("sweeperIsDaemon", sweeperIsDaemon);
            sweeperPool.shutdown();
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

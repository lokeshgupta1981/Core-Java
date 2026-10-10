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
 * Examples for the tutorial "Create and Start a Thread in Java: Runnable, Builders, Executors".
 * https://howtodoinjava.com/java/multi-threading/create-start-threads/
 */
public class CreateAndStartThreads {
    static String thumbnailName(String photo) {
        return photo.replace(".jpg", "-200px.jpg");
    }
    static class ThumbnailThread extends Thread {
        private final String photo;
        private volatile String thumbnail;

        ThumbnailThread(String photo) {
            super("thumb-" + photo);
            this.photo = photo;
        }

        @Override
        public void run() {
            thumbnail = photo.replace(".jpg", "-200px.jpg");
        }

        String thumbnail() {
            return thumbnail;
        }
    }
    static class NamedThreadFactory implements ThreadFactory {
        private final String prefix;
        private final AtomicInteger counter = new AtomicInteger(1);

        NamedThreadFactory(String prefix) {
            this.prefix = prefix;
        }

        @Override
        public Thread newThread(Runnable task) {
            Thread thread = new Thread(task, prefix + counter.getAndIncrement());
            thread.setDaemon(false);
            thread.setUncaughtExceptionHandler(
            (t, e) -> System.err.println(t.getName() + " failed: " + e));
            return thread;
        }
    }
    public static void main(String[] args) throws Exception {
        {
            AtomicReference<String> ranOn = new AtomicReference<>();
            Runnable task = () -> ranOn.set(Thread.currentThread().getName());

            Thread worker = new Thread(task, "thumbnail-1");
            worker.start();                                   // run() executes on the new thread
            worker.join();                                    // the caller waits until it ends
            String name = ranOn.get();                        // "thumbnail-1"
            show("name", name);

            Thread virtual = Thread.ofVirtual().start(task);  // Java 21+ virtual thread
            show("virtual", virtual);
            virtual.join();
        }
        {
            AtomicReference<String> caller = new AtomicReference<>();
            Thread worker = new Thread(() -> caller.set(Thread.currentThread().getName()), "worker");

            worker.run();                                     // ordinary call on the current thread
            String afterRun = caller.get();                   // "main"
            show("afterRun", afterRun);

            worker.start();
            worker.join();
            String afterStart = caller.get();                 // "worker"
            show("afterStart", afterStart);

            try { worker.start();  } catch (Throwable _t) { System.out.println("-> " + _t); }
        }
        {
            AtomicReference<String> result = new AtomicReference<>();
            Thread thread = new Thread(() -> result.set(thumbnailName("beach.jpg")), "resize-beach");
            thread.start();
            thread.join();
            String thumbnail = result.get();                  // "beach-200px.jpg"
            show("thumbnail", thumbnail);
        }
        {
            ThumbnailThread job = new ThumbnailThread("forest.jpg");
            job.start();
            job.join();
            String done = job.thumbnail();                    // "forest-200px.jpg"
            show("done", done);
            String jobName = job.getName();                   // "thumb-forest.jpg"
            show("jobName", jobName);
        }
        {
            Thread.Builder.OfPlatform builder = Thread.ofPlatform()
                    .name("thumbnail-", 1)
                    .daemon(false)
                    .priority(Thread.NORM_PRIORITY);

            Thread first = builder.start(() -> thumbnailName("city.jpg"));
            Thread second = builder.unstarted(() -> thumbnailName("lake.jpg"));
            first.join();
            String firstName = first.getName();               // "thumbnail-1"
            show("firstName", firstName);
            String secondName = second.getName();             // "thumbnail-2"
            show("secondName", secondName);
            Thread.State waiting = second.getState();         // NEW
            show("waiting", waiting);
        }
        {
            Thread named = Thread.ofVirtual().name("upload-", 1).start(() -> thumbnailName("park.jpg"));
            Thread quick = Thread.startVirtualThread(() -> thumbnailName("road.jpg"));
            named.join();
            quick.join();
            boolean isVirtual = named.isVirtual();            // true
            show("isVirtual", isVirtual);
            String nameOfNamed = named.getName();             // "upload-1"
            show("nameOfNamed", nameOfNamed);
            String nameOfQuick = quick.getName();             // "" (no name by default)
            show("nameOfQuick", nameOfQuick);
            boolean daemon = quick.isDaemon();                // true, always
            show("daemon", daemon);
        }
        {
            Future<String> resized;
            try (ExecutorService pool = Executors.newFixedThreadPool(4)) {
                resized = pool.submit(() -> thumbnailName("snow.jpg"));
            }                                                 // close() waits for submitted tasks
            String fromPool = resized.resultNow();            // "snow-200px.jpg"
            show("fromPool", fromPool);

            CompletableFuture<String> async = CompletableFuture.supplyAsync(() -> thumbnailName("dune.jpg"));
            String fromAsync = async.join();                  // "dune-200px.jpg"
            show("fromAsync", fromAsync);
        }
        {
            ThreadFactory factory = new NamedThreadFactory("invoice-pdf-");
            String firstThread = factory.newThread(() -> {}).getName();    // "invoice-pdf-1"
            show("firstThread", firstThread);
            String secondThread = factory.newThread(() -> {}).getName();   // "invoice-pdf-2"
            show("secondThread", secondThread);

            Future<String> job;
            try (ExecutorService pool = Executors.newFixedThreadPool(4, new NamedThreadFactory("report-"))) {
                job = pool.submit(() -> Thread.currentThread().getName());
            }
            String poolThread = job.resultNow();                           // "report-1"
            show("poolThread", poolThread);
        }
        {
            ThreadFactory platformFactory = Thread.ofPlatform().name("export-", 1).daemon(true).factory();
            ThreadFactory virtualFactory = Thread.ofVirtual().name("fetch-", 1).factory();
            String exportName = platformFactory.newThread(() -> {}).getName();   // "export-1"
            show("exportName", exportName);
            String fetchName = virtualFactory.newThread(() -> {}).getName();     // "fetch-1"
            show("fetchName", fetchName);

            ExecutorService namedVirtual = Executors.newThreadPerTaskExecutor(virtualFactory);
            namedVirtual.close();
        }
        {
            Thread report = new Thread(() -> {});
            int inherited = report.getPriority();             // 5, same as the creating thread
            show("inherited", inherited);
            report.setPriority(Thread.MIN_PRIORITY);
            int lowered = report.getPriority();               // 1
            show("lowered", lowered);

            Thread virtual = Thread.ofVirtual().unstarted(() -> {});
            virtual.setPriority(Thread.MAX_PRIORITY);
            int fixed = virtual.getPriority();                // 5, virtual threads ignore it
            show("fixed", fixed);

            try { report.setPriority(11);  } catch (Throwable _t) { System.out.println("-> " + _t); }
        }
        {
            List<String> photos = List.of("beach.jpg", "forest.jpg", "city.jpg");
            List<Future<String>> jobs = new ArrayList<>();

            try (ExecutorService executor = Executors.newThreadPerTaskExecutor(
            Thread.ofVirtual().name("thumb-", 1).factory())) {
                for (String photo : photos) {
                    jobs.add(executor.submit(() -> thumbnailName(photo)));
                }
            }
            List<String> thumbnails = jobs.stream().map(Future::resultNow).toList();   // [beach-200px.jpg, forest-200px.jpg, city-200px.jpg]
            show("thumbnails", thumbnails);
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

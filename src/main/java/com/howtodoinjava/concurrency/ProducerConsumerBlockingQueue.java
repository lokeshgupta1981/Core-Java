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
 * Examples for the tutorial "Producer Consumer Problem in Java Using BlockingQueue".
 * https://howtodoinjava.com/java/multi-threading/producer-consumer-problem-using-blockingqueue/
 */
public class ProducerConsumerBlockingQueue {
    static record Job(String image) {
        static final Job POISON = new Job("");
    }
    static int produce(BlockingQueue<Job> queue, List<String> images) throws InterruptedException {
        for (String image : images) {
            queue.put(new Job(image));              // blocks while the queue is full
        }
        return images.size();
    }
    static List<String> consume(BlockingQueue<Job> queue) throws InterruptedException {
        List<String> thumbnails = new ArrayList<>();
        while (true) {
            Job job = queue.take();                 // blocks while the queue is empty
            if (job == Job.POISON) {
                return thumbnails;
            }
            thumbnails.add("thumb-" + job.image());
        }
    }
    static class Buffer {
        private final Deque<Job> items = new ArrayDeque<>();
        private final int capacity;

        Buffer(int capacity) {
            this.capacity = capacity;
        }

        synchronized void put(Job job) throws InterruptedException {
            while (items.size() == capacity) {
                wait();                             // releases the monitor while waiting
            }
            items.addLast(job);
            notifyAll();
        }

        synchronized Job take() throws InterruptedException {
            while (items.isEmpty()) {
                wait();
            }
            Job job = items.removeFirst();
            notifyAll();
            return job;
        }
    }
    public static void main(String[] args) throws Exception {
        {
            BlockingQueue<String> queue = new ArrayBlockingQueue<>(2);
            ExecutorService pool = Executors.newVirtualThreadPerTaskExecutor();
            Future<?> producer = pool.submit(() -> {
                for (String image : List.of("a.png", "b.png", "c.png")) {
                    queue.put(image);                       // waits while 2 images are queued
                }
                queue.put("DONE");                          // end marker (poison pill)
                return null;
            });
            Future<List<String>> consumer = pool.submit(() -> {
                List<String> resized = new ArrayList<>();
                for (String item = queue.take(); !item.equals("DONE"); item = queue.take()) {
                    resized.add(item);                      // take() waits while the queue is empty
                }
                return resized;
            });
            List<String> images = consumer.get();           // [a.png, b.png, c.png]
            show("images", images);
            pool.close();
        }
        {
            BlockingQueue<Job> queue = new ArrayBlockingQueue<>(2);
            ExecutorService pool = Executors.newVirtualThreadPerTaskExecutor();
            Future<Integer> producer = pool.submit(() -> produce(queue, List.of("a.png", "b.png", "c.png")));
            Future<List<String>> consumer = pool.submit(() -> consume(queue));
            int sent = producer.get();                      // 3
            show("sent", sent);
            queue.put(Job.POISON);                          // the producer is done
            List<String> thumbnails = consumer.get();       // [thumb-a.png, thumb-b.png, thumb-c.png]
            show("thumbnails", thumbnails);
            pool.close();
        }
        {
            BlockingQueue<Job> queue = new LinkedBlockingQueue<>(10);
            ExecutorService pool = Executors.newVirtualThreadPerTaskExecutor();
            List<Future<Integer>> producers = List.of(
            pool.submit(() -> produce(queue, List.of("a.png", "b.png", "c.png"))),
            pool.submit(() -> produce(queue, List.of("d.png", "e.png"))));
            List<Future<List<String>>> consumers = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                consumers.add(pool.submit(() -> consume(queue)));
            }
            for (Future<Integer> p : producers) {
                p.get();                                    // wait for every producer
            }
            for (int i = 0; i < consumers.size(); i++) {
                queue.put(Job.POISON);                      // one pill per consumer
            }
            List<Integer> perConsumer = new ArrayList<>();
            for (Future<List<String>> c : consumers) {
                perConsumer.add(c.get().size());
            }
            List<Integer> split = perConsumer;              // varies per run, e.g. [2, 2, 1]
            show("split", split);
            int handled = split.stream().mapToInt(Integer::intValue).sum();   // 5
            show("handled", handled);
            pool.close();
        }
        {
            BlockingQueue<Job> idle = new LinkedBlockingQueue<>(10);
            ExecutorService workers = Executors.newVirtualThreadPerTaskExecutor();
            Future<List<String>> waiting = workers.submit(() -> consume(idle));
            List<Runnable> notStarted = workers.shutdownNow();      // [], and the blocked take() is interrupted
            show("notStarted", notStarted);
            boolean stopped = workers.awaitTermination(1, TimeUnit.SECONDS);   // true
            show("stopped", stopped);
            Future.State state = waiting.state();                   // FAILED
            show("state", state);
            try { Throwable cause = waiting.exceptionNow(); show("cause", cause); } catch (Throwable _t) { System.out.println("cause -> " + _t); }
        }
        {
            BlockingQueue<Job> small = new ArrayBlockingQueue<>(1);
            boolean first = small.offer(new Job("a.png"));                              // true
            show("first", first);
            boolean second = small.offer(new Job("b.png"), 50, TimeUnit.MILLISECONDS);  // false, still full after 50 ms
            show("second", second);
            Job next = small.poll();                                                    // Job[image=a.png]
            show("next", next);
            Job none = small.poll(50, TimeUnit.MILLISECONDS);                           // null, still empty after 50 ms
            show("none", none);
            boolean added = small.add(new Job("c.png"));                                // true
            show("added", added);
            try { boolean overflow = small.add(new Job("d.png")); show("overflow", overflow); } catch (Throwable _t) { System.out.println("overflow -> " + _t); }
            try { boolean rejected = small.offer(null); show("rejected", rejected); } catch (Throwable _t) { System.out.println("rejected -> " + _t); }
        }
        {
            BlockingQueue<Job> uploads = new ArrayBlockingQueue<>(10);
            ExecutorService pool = Executors.newVirtualThreadPerTaskExecutor();
            List<Future<?>> uploaders = new ArrayList<>();
            for (int i = 0; i < 1_000; i++) {
                Job job = new Job(i + ".png");
                uploaders.add(pool.submit(() -> { uploads.put(job); return null; }));   // most of them wait in put()
            }
            List<Future<List<String>>> resizers = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                resizers.add(pool.submit(() -> consume(uploads)));
            }
            for (Future<?> f : uploaders) {
                f.get();
            }
            for (int i = 0; i < resizers.size(); i++) {
                uploads.put(Job.POISON);
            }
            int resized = 0;
            for (Future<List<String>> r : resizers) {
                resized += r.get().size();
            }
            int total = resized;                            // 1000
            show("total", total);
            pool.close();
        }
        {
            Buffer buffer = new Buffer(1);
            ExecutorService pool = Executors.newVirtualThreadPerTaskExecutor();
            Future<?> writer = pool.submit(() -> {
                buffer.put(new Job("a.png"));
                buffer.put(new Job("b.png"));               // waits until the first job is taken
                return null;
            });
            Job firstJob = buffer.take();                   // Job[image=a.png]
            show("firstJob", firstJob);
            Job secondJob = buffer.take();                  // Job[image=b.png]
            show("secondJob", secondJob);
            pool.close();
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

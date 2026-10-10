package com.howtodoinjava.core.collections.queue;

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
 * Examples for the tutorial "Java ArrayBlockingQueue: Bounded Blocking Queue Examples".
 * https://howtodoinjava.com/java/collections/java-arrayblockingqueue/
 */
public class ArrayBlockingQueueExamples {

    public static void main(String[] args) throws Exception {
        {
            ArrayBlockingQueue<String> uploads = new ArrayBlockingQueue<>(2);
            boolean first = uploads.offer("cat.png");       // true
            show("first", first);
            uploads.put("dog.png");                         // fits, the queue is now full
            boolean third = uploads.offer("fox.png");       // false (full, returns at once)
            show("third", third);
            boolean waited = uploads.offer("fox.png", 50, TimeUnit.MILLISECONDS);   // false (still full after 50 ms)
            show("waited", waited);
            int free = uploads.remainingCapacity();         // 0
            show("free", free);
            String oldest = uploads.take();                 // "cat.png" (FIFO)
            show("oldest", oldest);
            boolean added = uploads.add("fox.png");         // true, there is room again
            show("added", added);
            try { boolean overflow = uploads.add("owl.png"); show("overflow", overflow); } catch (Throwable _t) { System.out.println("overflow -> " + _t); }
        }
        {
            ArrayBlockingQueue<String> resizeJobs = new ArrayBlockingQueue<>(1);
            resizeJobs.put("a.jpg");
            boolean accepted = resizeJobs.offer("b.jpg", 20, TimeUnit.MILLISECONDS);   // false (answer 503 to the client)
            show("accepted", accepted);
            String peeked = resizeJobs.peek();              // "a.jpg"
            show("peeked", peeked);
            String polled = resizeJobs.poll();              // "a.jpg"
            show("polled", polled);
            String emptyPoll = resizeJobs.poll(20, TimeUnit.MILLISECONDS);   // null (nothing arrived in 20 ms)
            show("emptyPoll", emptyPoll);
        }
        {
            ArrayBlockingQueue<String> buffer = new ArrayBlockingQueue<>(2);
            List<String> resized = new CopyOnWriteArrayList<>();

            Thread producer = Thread.ofVirtual().start(() -> {
                try {
                    for (int i = 1; i <= 5; i++) {
                        buffer.put("img-" + i);             // waits while 2 images are queued
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
            Thread consumer = Thread.ofVirtual().start(() -> {
                try {
                    for (int i = 1; i <= 5; i++) {
                        resized.add(buffer.take());         // waits while the queue is empty
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
            producer.join();
            consumer.join();
            List<String> result = List.copyOf(resized);     // [img-1, img-2, img-3, img-4, img-5]
            show("result", result);
            int leftOver = buffer.size();                   // 0
            show("leftOver", leftOver);
        }
        {
            ArrayBlockingQueue<Integer> fairQueue = new ArrayBlockingQueue<>(3, true, List.of(1, 2));
            int size = fairQueue.size();                    // 2
            show("size", size);
            int room = fairQueue.remainingCapacity();       // 1
            show("room", room);
            try { ArrayBlockingQueue<Integer> tooSmall = new ArrayBlockingQueue<>(2, false, List.of(1, 2, 3)); show("tooSmall", tooSmall); } catch (Throwable _t) { System.out.println("tooSmall -> " + _t); }
        }
        {
            ArrayBlockingQueue<String> auditLines = new ArrayBlockingQueue<>(10, false, List.of("login", "view", "logout"));
            List<String> rows = new ArrayList<>();
            int written = auditLines.drainTo(rows);         // 3
            show("written", written);
            List<String> drained = rows;                    // [login, view, logout]
            show("drained", drained);
            boolean emptyNow = auditLines.isEmpty();        // true
            show("emptyNow", emptyNow);
        }
        {
            ArrayBlockingQueue<Integer> events = new ArrayBlockingQueue<>(10, false, List.of(1, 2, 3, 4, 5));
            List<Integer> batch = new ArrayList<>();
            int firstBatch = events.drainTo(batch, 3);      // 3
            show("firstBatch", firstBatch);
            List<Integer> batchOne = List.copyOf(batch);    // [1, 2, 3]
            show("batchOne", batchOne);
            batch.clear();
            int secondBatch = events.drainTo(batch, 3);     // 2
            show("secondBatch", secondBatch);
            List<Integer> batchTwo = List.copyOf(batch);    // [4, 5]
            show("batchTwo", batchTwo);
        }
        {
            ArrayBlockingQueue<String> logQueue = new ArrayBlockingQueue<>(100);
            Thread web = Thread.ofVirtual().start(() -> {
                for (int i = 0; i < 3; i++) {
                    logQueue.offer("web-" + i);
                }
            });
            Thread api = Thread.ofVirtual().start(() -> {
                for (int i = 0; i < 3; i++) {
                    logQueue.offer("api-" + i);
                }
            });
            web.join();
            api.join();
            List<String> dbBatch = new ArrayList<>();
            int batchSize = logQueue.drainTo(dbBatch, 4);   // 4 (which lines come first depends on thread timing)
            show("batchSize", batchSize);
            int stillQueued = logQueue.size();              // 2
            show("stillQueued", stillQueued);
        }
        {
            ArrayBlockingQueue<String> selfTarget = new ArrayBlockingQueue<>(2, false, List.of("a"));
            try { int bad = selfTarget.drainTo(selfTarget); show("bad", bad); } catch (Throwable _t) { System.out.println("bad -> " + _t); }
        }
        {
            ArrayBlockingQueue<Integer> small = new ArrayBlockingQueue<>(2, false, List.of(1, 2));
            ArrayBlockingQueue<Integer> bigger = new ArrayBlockingQueue<>(10);
            int copied = small.drainTo(bigger);             // 2
            show("copied", copied);
            int newRoom = bigger.remainingCapacity();       // 8
            show("newRoom", newRoom);
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

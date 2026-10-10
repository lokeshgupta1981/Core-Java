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
 * Examples for the tutorial "Java ConcurrentLinkedDeque: Non-blocking Thread-safe Deque".
 * https://howtodoinjava.com/java/multi-threading/non-blocking-thread-safe-list-concurrentlinkeddeque-example/
 */
public class ConcurrentLinkedDequeExamples {
    static class AddTask implements Runnable {
        private final ConcurrentLinkedDeque<String> deque;

        AddTask(ConcurrentLinkedDeque<String> deque) {
            this.deque = deque;
        }

        @Override
        public void run() {
            String name = Thread.currentThread().getName();
            for (int i = 0; i < 10_000; i++) {
                deque.add(name + ": Element " + i);
            }
        }
    }
    static class RemoveTask implements Runnable {
        private final ConcurrentLinkedDeque<String> deque;

        RemoveTask(ConcurrentLinkedDeque<String> deque) {
            this.deque = deque;
        }

        @Override
        public void run() {
            for (int i = 0; i < 5_000; i++) {
                deque.pollFirst();
                deque.pollLast();
            }
        }
    }
    public static void main(String[] args) throws Exception {
        {
            ConcurrentLinkedDeque<String> jobs = new ConcurrentLinkedDeque<>();
            jobs.offerLast("job-1");
            jobs.offerLast("job-2");
            jobs.offerFirst("urgent");
            String head = jobs.peekFirst();                  // "urgent"
            show("head", head);
            String tail = jobs.pollLast();                   // "job-2"
            show("tail", tail);
            String next = jobs.pollFirst();                  // "urgent"
            show("next", next);
            int remaining = jobs.size();                     // 1
            show("remaining", remaining);
            String nothing = new ConcurrentLinkedDeque<String>().pollFirst();   // null
            show("nothing", nothing);
        }
        {
            ConcurrentLinkedDeque<String> empty = new ConcurrentLinkedDeque<>();
            String polled = empty.pollFirst();               // null
            show("polled", polled);
            String peeked = empty.peekLast();                // null
            show("peeked", peeked);
            try { String first = empty.getFirst(); show("first", first); } catch (Throwable _t) { System.out.println("first -> " + _t); }
            try { String popped = empty.pop(); show("popped", popped); } catch (Throwable _t) { System.out.println("popped -> " + _t); }
        }
        {
            ConcurrentLinkedDeque<String> steps = new ConcurrentLinkedDeque<>(List.of("open", "edit", "save"));
            Deque<String> undoOrder = steps.reversed();
            String lastStep = undoOrder.peekFirst();         // "save"
            show("lastStep", lastStep);
            String undoList = undoOrder.toString();          // "[save, edit, open]"
            show("undoList", undoList);
        }
        {
            ConcurrentLinkedDeque<String> deque = new ConcurrentLinkedDeque<>();
            try (ExecutorService adders = Executors.newFixedThreadPool(100)) {
                for (int i = 0; i < 100; i++) {
                    adders.submit(new AddTask(deque));
                }
            }
            int sizeAfterAdding = deque.size();              // 1000000
            show("sizeAfterAdding", sizeAfterAdding);
            try (ExecutorService removers = Executors.newFixedThreadPool(100)) {
                for (int i = 0; i < 100; i++) {
                    removers.submit(new RemoveTask(deque));
                }
            }
            int sizeAfterRemoving = deque.size();            // 0
            show("sizeAfterRemoving", sizeAfterRemoving);
        }
        {
            ConcurrentLinkedDeque<Integer> orders = new ConcurrentLinkedDeque<>(List.of(1, 2, 3));
            for (Integer order : orders) {
                if (order == 1) {
                    orders.addLast(4);                       // no ConcurrentModificationException
                }
            }
            int orderCount = orders.size();                  // 4
            show("orderCount", orderCount);
        }
        {
            ConcurrentLinkedDeque<Integer> thumbnails = IntStream.range(0, 10_000).boxed()
                    .collect(Collectors.toCollection(ConcurrentLinkedDeque::new));
            LongAdder processed = new LongAdder();
            Thread owner = Thread.ofPlatform().start(() -> {
                while (thumbnails.pollFirst() != null) {
                    processed.increment();
                }
            });
            Thread thief = Thread.ofPlatform().start(() -> {
                while (thumbnails.pollLast() != null) {
                    processed.increment();
                }
            });
            owner.join();
            thief.join();
            long done = processed.sum();                     // 10000
            show("done", done);
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

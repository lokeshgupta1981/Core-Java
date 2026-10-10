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
 * Examples for the tutorial "Java wait(), notify() and notifyAll(): Producer Consumer Guide".
 * https://howtodoinjava.com/java/multi-threading/wait-notify-and-notifyall-methods/
 */
public class WaitNotifyExamples {
    static class TaskQueue {
        private final List<Integer> tasks = new ArrayList<>();
        private final int capacity;

        TaskQueue(int capacity) {
            this.capacity = capacity;
        }

        synchronized void put(int task) throws InterruptedException {
            while (tasks.size() == capacity) {
                wait();                      // queue full, the producer waits
            }
            tasks.add(task);
            notifyAll();                     // wakes a waiting consumer
        }

        synchronized int take() throws InterruptedException {
            while (tasks.isEmpty()) {
                wait();                      // queue empty, the consumer waits
            }
            int task = tasks.remove(0);
            notifyAll();                     // wakes a waiting producer
            return task;
        }
    }
    static class BoundedBuffer {
        private final Deque<Integer> items = new ArrayDeque<>();
        private final ReentrantLock lock = new ReentrantLock();
        private final Condition notEmpty = lock.newCondition();
        private final Condition notFull = lock.newCondition();

        void put(int item) throws InterruptedException {
            lock.lock();
            try {
                while (items.size() == 2) {
                    notFull.await();
                }
                items.addLast(item);
                notEmpty.signal();
            } finally {
                lock.unlock();
            }
        }

        int take() throws InterruptedException {
            lock.lock();
            try {
                while (items.isEmpty()) {
                    notEmpty.await();
                }
                int item = items.removeFirst();
                notFull.signal();
                return item;
            } finally {
                lock.unlock();
            }
        }
    }
    public static void main(String[] args) throws Exception {
        {
            List<Integer> taskQueue = new ArrayList<>();
            Thread.ofPlatform().start(() -> {
                synchronized (taskQueue) {
                    taskQueue.add(7);
                    taskQueue.notifyAll();          // wakes the threads waiting on taskQueue
                }
            });
            int task;
            synchronized (taskQueue) {
                while (taskQueue.isEmpty()) {
                    taskQueue.wait();               // releases the monitor until notified
                }
                task = taskQueue.remove(0);
            }
            int received = task;                    // 7
            show("received", received);
        }
        {
            TaskQueue queue = new TaskQueue(2);
            List<Integer> consumed = Collections.synchronizedList(new ArrayList<>());

            Thread producer = Thread.ofPlatform().name("producer").start(() -> {
                try {
                    for (int i = 1; i <= 5; i++) {
                        queue.put(i);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });

            Thread consumer = Thread.ofPlatform().name("consumer").start(() -> {
                try {
                    for (int i = 1; i <= 5; i++) {
                        consumed.add(queue.take());
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });

            producer.join();
            consumer.join();
            List<Integer> result = List.copyOf(consumed);   // [1, 2, 3, 4, 5]
            show("result", result);
        }
        {
            BlockingQueue<Integer> jobs = new ArrayBlockingQueue<>(2);
            jobs.put(1);
            jobs.put(2);
            boolean added = jobs.offer(3, 50, TimeUnit.MILLISECONDS);   // false, queue full
            show("added", added);
            int first = jobs.take();                                     // 1
            show("first", first);
        }
        {
            BoundedBuffer buffer = new BoundedBuffer();
            buffer.put(4);
            int item = buffer.take();               // 4
            show("item", item);
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

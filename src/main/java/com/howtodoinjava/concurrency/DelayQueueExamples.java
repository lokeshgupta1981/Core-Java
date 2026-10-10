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
 * Examples for the tutorial "Java DelayQueue Tutorial with Delayed Element Examples".
 * https://howtodoinjava.com/java/multi-threading/java-delayqueue/
 */
public class DelayQueueExamples {
    static record Reminder(String text, long dueAt) implements Delayed {

        static Reminder in(String text, long delayMs) {
            return new Reminder(text, System.nanoTime() + TimeUnit.MILLISECONDS.toNanos(delayMs));
        }

        @Override
        public long getDelay(TimeUnit unit) {
            return unit.convert(dueAt - System.nanoTime(), TimeUnit.NANOSECONDS);
        }

        @Override
        public int compareTo(Delayed other) {
            return Long.compare(getDelay(TimeUnit.NANOSECONDS), other.getDelay(TimeUnit.NANOSECONDS));
        }
    }
    static record PendingOrder(String id, long dueAt) implements Delayed {

        static PendingOrder expiresIn(String id, long delayMs) {
            return new PendingOrder(id, System.nanoTime() + TimeUnit.MILLISECONDS.toNanos(delayMs));
        }

        @Override
        public long getDelay(TimeUnit unit) {
            return unit.convert(dueAt - System.nanoTime(), TimeUnit.NANOSECONDS);
        }

        @Override
        public int compareTo(Delayed other) {
            return Long.compare(getDelay(TimeUnit.NANOSECONDS), other.getDelay(TimeUnit.NANOSECONDS));
        }
    }
    static Runnable canceller(DelayQueue<PendingOrder> pending, List<String> cancelled, CountDownLatch progress) {
        return () -> {
            try {
                while (true) {
                    PendingOrder order = pending.take();    // blocks until an order expires
                    cancelled.add(order.id());
                    progress.countDown();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();         // shutdown requested
            }
        };
    }
    public static void main(String[] args) throws Exception {
        {
            DelayQueue<Reminder> queue = new DelayQueue<>();
            queue.put(Reminder.in("call mom", 300));
            queue.put(Reminder.in("stretch", 50));
            Reminder early = queue.poll();                  // null, nothing has expired yet
            show("early", early);
            String next = queue.peek().text();              // "stretch", the next to expire
            show("next", next);
            String due = queue.take().text();               // "stretch", after about 50 ms
            show("due", due);
            int left = queue.size();                        // 1
            show("left", left);
        }
        {
            Reminder soon = Reminder.in("tea", 1000);
            boolean waiting = soon.getDelay(TimeUnit.MILLISECONDS) > 0;     // true
            show("waiting", waiting);
            Reminder past = Reminder.in("late", -10);
            boolean expired = past.getDelay(TimeUnit.NANOSECONDS) <= 0;     // true
            show("expired", expired);
        }
        {
            DelayQueue<Reminder> rules = new DelayQueue<>();
            rules.put(Reminder.in("later", 5000));
            int size = rules.size();                        // 1
            show("size", size);
            Reminder none = rules.poll();                   // null
            show("none", none);
            try { Reminder removed = rules.remove(); show("removed", removed); } catch (Throwable _t) { System.out.println("removed -> " + _t); }
            try { boolean added = rules.offer(null); show("added", added); } catch (Throwable _t) { System.out.println("added -> " + _t); }
        }
        {
            DelayQueue<PendingOrder> pending = new DelayQueue<>();
            List<String> cancelled = new CopyOnWriteArrayList<>();
            CountDownLatch progress = new CountDownLatch(3);
            Thread consumer = Thread.ofPlatform().name("order-canceller").start(canceller(pending, cancelled, progress));
            pending.put(PendingOrder.expiresIn("order-1", 300));
            pending.put(PendingOrder.expiresIn("order-2", 100));
            pending.put(PendingOrder.expiresIn("order-3", 200));
            progress.await();
            consumer.interrupt();
            consumer.join();
            List<String> result = List.copyOf(cancelled);       // [order-2, order-3, order-1]
            show("result", result);
        }
        {
            DelayQueue<PendingOrder> batch = new DelayQueue<>();
            batch.put(PendingOrder.expiresIn("a", -5));
            batch.put(PendingOrder.expiresIn("b", -1));
            batch.put(PendingOrder.expiresIn("c", 5000));
            List<PendingOrder> due = new ArrayList<>();
            int moved = batch.drainTo(due);                     // 2
            show("moved", moved);
            int stillPending = batch.size();                    // 1
            show("stillPending", stillPending);
        }
        {
            DelayQueue<PendingOrder> open = new DelayQueue<>();
            PendingOrder order = PendingOrder.expiresIn("order-7", 60_000);
            open.put(order);
            boolean paid = open.remove(order);                  // true, the customer paid
            show("paid", paid);
            boolean byId = open.removeIf(o -> o.id().equals("order-8"));    // false, not in the queue
            show("byId", byId);
            int remaining = open.size();                        // 0
            show("remaining", remaining);
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

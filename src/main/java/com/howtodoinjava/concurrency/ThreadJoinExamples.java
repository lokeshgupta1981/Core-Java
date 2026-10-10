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
 * Examples for the tutorial "Java Thread join(): Timeouts, Interrupts and join vs yield".
 * https://howtodoinjava.com/java/multi-threading/thread-join/
 */
public class ThreadJoinExamples {
    static Thread startAfter(Thread previous, Runnable step) {
        return Thread.ofPlatform().start(() -> {
            try {
                previous.join();
                step.run();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }
    static void pause(long ms) {
        LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(ms));
    }
    public static void main(String[] args) throws Exception {
        {
            List<String> steps = Collections.synchronizedList(new ArrayList<>());
            Thread printer = Thread.ofPlatform().start(() -> steps.add("cards printed"));
            printer.join();                         // main waits here until printer ends
            steps.add("cards sent");
            List<String> order = List.copyOf(steps);   // [cards printed, cards sent]
            show("order", order);
        }
        {
            List<String> plan = Collections.synchronizedList(new ArrayList<>());
            Thread venue = Thread.ofPlatform().start(() -> plan.add("venue booked"));
            Thread printing = startAfter(venue, () -> plan.add("cards printed"));
            Thread delivery = startAfter(printing, () -> plan.add("cards delivered"));
            delivery.join();
            List<String> done = List.copyOf(plan);   // [venue booked, cards printed, cards delivered]
            show("done", done);
        }
        {
            Thread idle = Thread.ofPlatform().unstarted(() -> {});
            idle.join();                            // returns at once, idle was never started
            boolean alive = idle.isAlive();         // false
            show("alive", alive);
        }
        {
            Thread export = Thread.ofPlatform().start(() -> pause(500));
            export.join(100);
            boolean stillRunning = export.isAlive();   // true, join gave up after 100 ms
            show("stillRunning", stillRunning);
            export.join();
            boolean finished = !export.isAlive();      // true
            show("finished", finished);
        }
        {
            Thread upload = Thread.ofVirtual().start(() -> pause(300));
            boolean quick = upload.join(Duration.ofMillis(50));      // false
            show("quick", quick);
            boolean done = upload.join(Duration.ofSeconds(2));       // true
            show("done", done);
            Thread draft = Thread.ofPlatform().unstarted(() -> {});
            try { boolean never = draft.join(Duration.ofMillis(10)); show("never", never); } catch (Throwable _t) { System.out.println("never -> " + _t); }
        }
        {
            AtomicReference<String> outcome = new AtomicReference<>();
            Thread worker = Thread.ofPlatform().start(() -> pause(300));
            Thread coordinator = Thread.ofPlatform().start(() -> {
                try {
                    worker.join();
                    outcome.set("joined");
                } catch (InterruptedException e) {
                    outcome.set("interrupted");
                    Thread.currentThread().interrupt();
                }
            });
            pause(50);
            coordinator.interrupt();
            coordinator.join();
            String result = outcome.get();              // "interrupted"
            show("result", result);
            boolean workerAlive = worker.isAlive();     // true, the worker is not affected
            show("workerAlive", workerAlive);
            worker.join();
        }
        {
            boolean selfJoined = Thread.currentThread().join(Duration.ofMillis(100));   // false
            show("selfJoined", selfJoined);
        }
        {
            int[] total = new int[1];
            Thread adder = Thread.ofPlatform().start(() -> total[0] = 42);
            adder.join();
            int seen = total[0];                    // 42
            show("seen", seen);
        }
        {
            Thread.yield();                         // a hint only, the thread may keep running
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

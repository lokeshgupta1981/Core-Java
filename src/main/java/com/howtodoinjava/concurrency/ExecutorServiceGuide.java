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
 * Examples for the tutorial "ExecutorService in Java: Guide With Java 25 Examples".
 * https://howtodoinjava.com/java/multi-threading/executor-service-example/
 */
public class ExecutorServiceGuide {
    static void pause(long ms) {
        LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(ms));
    }
    static int quote(String supplier, long delayMs) {
        pause(delayMs);
        return supplier.length() * 10;
    }
    public static void main(String[] args) throws Exception {
        {
            Future<Integer> price;
            try (ExecutorService pool = Executors.newFixedThreadPool(2)) {
                price = pool.submit(() -> 40 + 2);          // runs on a pool thread
            }                                               // close() waits for the task
            int result = price.resultNow();                 // 42
            show("result", result);
        }
        {
            ExecutorService fixed = Executors.newFixedThreadPool(4);
            ExecutorService single = Executors.newSingleThreadExecutor();
            ExecutorService cached = Executors.newCachedThreadPool();
            ExecutorService virtual = Executors.newVirtualThreadPerTaskExecutor();
            ScheduledExecutorService timer = Executors.newScheduledThreadPool(1);
            fixed.close();
            single.close();
            cached.close();
            virtual.close();
            timer.close();
        }
        {
            AtomicInteger visits = new AtomicInteger();
            Future<?> logged;
            Future<String> tagged;
            Future<Integer> sum;
            List<Future<Integer>> all;
            try (ExecutorService pool = Executors.newFixedThreadPool(2)) {
                pool.execute(() -> visits.incrementAndGet());
                logged = pool.submit(() -> { visits.incrementAndGet(); });
                tagged = pool.submit(() -> { visits.incrementAndGet(); }, "done");
                sum = pool.submit(() -> 2 + 3);
                all = pool.invokeAll(List.of(() -> 1, () -> 2, () -> 3));
            }
            int count = visits.get();                        // 3
            show("count", count);
            Object nothing = logged.resultNow();             // null
            show("nothing", nothing);
            String tag = tagged.resultNow();                 // "done"
            show("tag", tag);
            int five = sum.resultNow();                      // 5
            show("five", five);
            int size = all.size();                           // 3
            show("size", size);
        }
        {
            Future<Integer> failed;
            try (ExecutorService pool = Executors.newSingleThreadExecutor()) {
                failed = pool.submit(() -> 10 / 0);
            }
            Future.State state = failed.state();             // FAILED
            show("state", state);
            String message = failed.exceptionNow().getMessage();   // "/ by zero"
            show("message", message);
            try { int value = failed.get(); show("value", value); } catch (Throwable _t) { System.out.println("value -> " + _t); }
        }
        {
            Future<String> report;
            String text;
            try (ExecutorService pool = Executors.newFixedThreadPool(2)) {
                report = pool.submit(() -> "ready");
                text = report.get(2, TimeUnit.SECONDS);
            }
            String received = text;                          // "ready"
            show("received", received);
            boolean done = report.isDone();                  // true
            show("done", done);
        }
        {
            ExecutorService pool = Executors.newFixedThreadPool(2);
            Future<Integer> answer = pool.submit(() -> 6 * 7);
            pool.close();
            boolean terminated = pool.isTerminated();        // true
            show("terminated", terminated);
            int answerValue = answer.resultNow();            // 42
            show("answerValue", answerValue);
            try { Future<Integer> late = pool.submit(() -> 1); show("late", late); } catch (Throwable _t) { System.out.println("late -> " + _t); }
        }
        {
            List<Future<Integer>> lengths = new ArrayList<>();
            try (ExecutorService vexec = Executors.newVirtualThreadPerTaskExecutor()) {
                for (String city : List.of("Rome", "Oslo", "Lima")) {
                    lengths.add(vexec.submit(() -> city.length()));
                }
            }
            int total = lengths.stream().mapToInt(Future::resultNow).sum();   // 12
            show("total", total);
        }
        {
            List<Callable<Integer>> calls = List.of(
            () -> quote("acme", 50),
            () -> quote("globex", 80),
            () -> quote("initech", 3_000));
            List<Future<Integer>> quotes;
            try (ExecutorService pool = Executors.newFixedThreadPool(3)) {
                quotes = pool.invokeAll(calls, 500, TimeUnit.MILLISECONDS);
            }
            int acme = quotes.get(0).resultNow();            // 40
            show("acme", acme);
            int globex = quotes.get(1).resultNow();          // 60
            show("globex", globex);
            Future.State slow = quotes.get(2).state();       // CANCELLED
            show("slow", slow);
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

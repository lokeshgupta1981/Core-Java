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
 * Examples for the tutorial "Java ExecutorService invokeAll() and invokeAny() with Examples".
 * https://howtodoinjava.com/java/multi-threading/executorservice-invokeall/
 */
public class InvokeAllInvokeAny {
    static void pause(long ms) {
        LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(ms));
    }
    static Callable<String> widget(String name, long ms) {
        return () -> {
            pause(ms);
            return name;
        };
    }
    static Callable<String> failing(String message) {
        return () -> {
            throw new IllegalStateException(message);
        };
    }
    static String fastest(List<Callable<String>> tasks, long timeoutMs) throws Exception {
        try (ExecutorService pool = Executors.newVirtualThreadPerTaskExecutor()) {
            return pool.invokeAny(tasks, timeoutMs, TimeUnit.MILLISECONDS);
        }
    }
    public static void main(String[] args) throws Exception {
        {
            List<Callable<String>> widgets = List.of(() -> "weather", () -> "orders", () -> "news");
            List<Future<String>> futures;
            try (ExecutorService pool = Executors.newFixedThreadPool(3)) {
                futures = pool.invokeAll(widgets);                   // blocks until all 3 are done
            }
            boolean allDone = futures.stream().allMatch(Future::isDone);    // true
            show("allDone", allDone);
            String first = futures.get(0).resultNow();                      // "weather"
            show("first", first);
            int count = futures.size();                                     // 3
            show("count", count);
        }
        {
            List<Callable<String>> tasks = List.of(
            widget("weather", 300),
            widget("orders", 100),
            widget("news", 200));
            List<Future<String>> results;
            try (ExecutorService pool = Executors.newFixedThreadPool(3)) {
                results = pool.invokeAll(tasks);
            }
            List<String> page = results.stream().map(Future::resultNow).toList();   // [weather, orders, news]
            show("page", page);
        }
        {
            List<Callable<String>> mixed = List.of(widget("weather", 50), failing("orders down"), widget("news", 50));
            List<Future<String>> outcome;
            try (ExecutorService pool = Executors.newFixedThreadPool(3)) {
                outcome = pool.invokeAll(mixed);
            }
            Future.State second = outcome.get(1).state();                       // FAILED
            show("second", second);
            String reason = outcome.get(1).exceptionNow().getMessage();         // "orders down"
            show("reason", reason);
            try { String value = outcome.get(1).get(); show("value", value); } catch (Throwable _t) { System.out.println("value -> " + _t); }
            List<String> ready = outcome.stream().filter(f -> f.state() == Future.State.SUCCESS).map(Future::resultNow).toList();   // [weather, news]
            show("ready", ready);
        }
        {
            List<Callable<String>> slow = List.of(widget("price", 20), widget("stock", 30), widget("reviews", 2000));
            List<Future<String>> partial;
            try (ExecutorService pool = Executors.newFixedThreadPool(3)) {
                partial = pool.invokeAll(slow, 150, TimeUnit.MILLISECONDS);
            }
            Future.State reviews = partial.get(2).state();                  // CANCELLED
            show("reviews", reviews);
            boolean cancelled = partial.get(2).isCancelled();               // true
            show("cancelled", cancelled);
            try { String missing = partial.get(2).get(); show("missing", missing); } catch (Throwable _t) { System.out.println("missing -> " + _t); }
            String price = partial.get(0).resultNow();                      // "price"
            show("price", price);
        }
        {
            List<Callable<String>> mirrors = List.of(
            widget("eu-mirror", 300),
            failing("us-mirror refused"),
            widget("asia-mirror", 100));
            String winner;
            try (ExecutorService pool = Executors.newFixedThreadPool(3)) {
                winner = pool.invokeAny(mirrors);
            }
            String used = winner;                                           // "asia-mirror"
            show("used", used);
        }
        {
            try { String allFail = fastest(List.of(failing("a down"), failing("b down")), 500); show("allFail", allFail); } catch (Throwable _t) { System.out.println("allFail -> " + _t); }
            try { String tooSlow = fastest(List.of(widget("late", 1000)), 50); show("tooSlow", tooSlow); } catch (Throwable _t) { System.out.println("tooSlow -> " + _t); }
            try { String noTasks = fastest(List.of(), 50); show("noTasks", noTasks); } catch (Throwable _t) { System.out.println("noTasks -> " + _t); }
        }
        {
            Runnable cleanup = () -> {};
            List<Callable<Object>> jobs = List.of(Executors.callable(cleanup));
            List<Future<Object>> done;
            try (ExecutorService pool = Executors.newSingleThreadExecutor()) {
                done = pool.invokeAll(jobs);
            }
            Object result = done.get(0).resultNow();         // null
            show("result", result);
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

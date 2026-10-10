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
 * Examples for the tutorial "Java Callable and Future Tutorial with Examples (Java 25)".
 * https://howtodoinjava.com/java/multi-threading/java-callable-future-example/
 */
public class CallableAndFuture {
    static record Invoices(String customer, List<Integer> amounts) {}
    static void pause(long ms) {
        LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(ms));
    }
    static Callable<Integer> totalOf(Invoices invoices, long loadMs) {
        return () -> {
            pause(loadMs);                      // simulates the database call
            return invoices.amounts().stream().mapToInt(Integer::intValue).sum();
        };
    }
    static int totalOrZero(Future<Integer> future, long timeoutMs) throws InterruptedException {
        try {
            return future.get(timeoutMs, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            future.cancel(true);                // stop the slow task
            return 0;
        } catch (ExecutionException e) {
            return 0;                           // the task threw, see section 4
        }
    }
    static String describe(Future<Integer> future) {
        return switch (future.state()) {
            case RUNNING -> "still loading";
            case SUCCESS -> "total " + future.resultNow();
            case FAILED -> "error: " + future.exceptionNow().getMessage();
            case CANCELLED -> "cancelled";
        };
    }
    public static void main(String[] args) throws Exception {
        {
            Callable<Integer> countWords = () -> "the quick brown fox".split(" ").length;
            Future<Integer> future;
            try (ExecutorService pool = Executors.newVirtualThreadPerTaskExecutor()) {
                future = pool.submit(countWords);       // returns at once, the task runs in the pool
                // the current thread can do other work here
            }
            Integer words = future.get();               // 4
            show("words", words);
            Future.State state = future.state();        // SUCCESS
            show("state", state);
            boolean done = future.isDone();             // true
            show("done", done);
        }
        {
            Invoices anna = new Invoices("anna", List.of(120, 80, 50));
            Invoices ben = new Invoices("ben", List.of(300, 25));
            Future<Integer> annaTotal;
            Future<Integer> benTotal;
            try (ExecutorService pool = Executors.newFixedThreadPool(2)) {
                annaTotal = pool.submit(totalOf(anna, 100));
                benTotal = pool.submit(totalOf(ben, 50));
            }
            int a = annaTotal.get();                    // 250
            show("a", a);
            int b = benTotal.get();                     // 325
            show("b", b);
            int sum = a + b;                            // 575
            show("sum", sum);
        }
        {
            Invoices anna = new Invoices("anna", List.of(120, 80, 50));
            Invoices ben = new Invoices("ben", List.of(300, 25));
            int onTime;
            int late;
            try (ExecutorService pool = Executors.newFixedThreadPool(2)) {
                onTime = totalOrZero(pool.submit(totalOf(anna, 20)), 500);
                late = totalOrZero(pool.submit(totalOf(ben, 2000)), 50);
            }
            int fast = onTime;                          // 250
            show("fast", fast);
            int slow = late;                            // 0, cancelled after 50 ms
            show("slow", slow);
        }
        {
            Callable<Integer> broken = () -> Integer.parseInt("12x");
            Future<Integer> failed;
            try (ExecutorService pool = Executors.newSingleThreadExecutor()) {
                failed = pool.submit(broken);
            }
            Future.State outcome = failed.state();                              // FAILED
            show("outcome", outcome);
            try { String cause = failed.exceptionNow().getClass().getSimpleName(); show("cause", cause); } catch (Throwable _t) { System.out.println("cause -> " + _t); }
            try { Integer value = failed.get(); show("value", value); } catch (Throwable _t) { System.out.println("value -> " + _t); }
        }
        {
            Invoices anna = new Invoices("anna", List.of(120, 80, 50));
            Future<Integer> statement;
            boolean stopped;
            try (ExecutorService pool = Executors.newSingleThreadExecutor()) {
                statement = pool.submit(totalOf(anna, 5000));
                stopped = statement.cancel(true);
            }
            boolean result = stopped;                           // true
            show("result", result);
            boolean isCancelled = statement.isCancelled();      // true
            show("isCancelled", isCancelled);
            Future.State after = statement.state();             // CANCELLED
            show("after", after);
            try { Integer total = statement.get(); show("total", total); } catch (Throwable _t) { System.out.println("total -> " + _t); }
        }
        {
            Future<Integer> ok = CompletableFuture.completedFuture(250);
            Future<Integer> bad = CompletableFuture.failedFuture(new IllegalStateException("db down"));
            String first = describe(ok);                // "total 250"
            show("first", first);
            String second = describe(bad);              // "error: db down"
            show("second", second);
            try { Integer early = new CompletableFuture<Integer>().resultNow(); show("early", early); } catch (Throwable _t) { System.out.println("early -> " + _t); }
        }
        {
            Invoices ben = new Invoices("ben", List.of(300, 25));
            FutureTask<Integer> task = new FutureTask<>(totalOf(ben, 10));
            Thread worker = Thread.ofVirtual().start(task);
            int benSum = task.get();                    // 325
            show("benSum", benSum);
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

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
 * Examples for the tutorial "Runnable vs Callable in Java: Differences With Examples".
 * https://howtodoinjava.com/java/multi-threading/java-runnable-vs-callable/
 */
public class RunnableVsCallable {
    static int quoteFrom(String carrier) throws IOException {
        Integer rate = Map.of("dhl", 12, "ups", 15).get(carrier);
        if (rate == null) {
            throw new IOException(carrier + " is offline");
        }
        return rate;
    }
    static class QuoteRunnable implements Runnable {
        volatile int rate = -1;

        @Override
        public void run() {
            try {
                rate = quoteFrom("dhl");
            } catch (IOException e) {
                rate = -1;
            }
        }
    }
    static class QuoteCallable implements Callable<Integer> {
        @Override
        public Integer call() throws IOException {
            return quoteFrom("dhl");
        }
    }
    public static void main(String[] args) throws Exception {
        {
            List<String> auditLog = new CopyOnWriteArrayList<>();
            Runnable audit = () -> auditLog.add("quote requested");
            Callable<Integer> quote = () -> 12;

            Future<?> logged;
            Future<Integer> price;
            try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
                logged = executor.submit(audit);
                price = executor.submit(quote);
            }                                                 // close() waits for both tasks
            Object nothing = logged.get();                    // null, Runnable has no result
            show("nothing", nothing);
            Integer cost = price.get();                       // 12
            show("cost", cost);
        }
        {
            QuoteRunnable runnable = new QuoteRunnable();
            Thread thread = new Thread(runnable);
            thread.start();
            thread.join();
            int fromThread = runnable.rate;                   // 12
            show("fromThread", fromThread);
        }
        {
            Future<Integer> submitted;
            try (ExecutorService executor = Executors.newFixedThreadPool(2)) {
                submitted = executor.submit(new QuoteCallable());
            }
            Integer fromExecutor = submitted.get();           // 12
            show("fromExecutor", fromExecutor);

            FutureTask<Integer> futureTask = new FutureTask<>(() -> quoteFrom("ups"));
            new Thread(futureTask).start();
            Integer fromFutureTask = futureTask.get();        // 15
            show("fromFutureTask", fromFutureTask);

            Integer direct = new QuoteCallable().call();      // 12, on the current thread
            show("direct", direct);
        }
        {
            Future<Integer> quoteFuture;
            try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
                quoteFuture = executor.submit(() -> quoteFrom("ups"));
            }
            Future.State finished = quoteFuture.state();      // SUCCESS
            show("finished", finished);
            Integer now = quoteFuture.resultNow();            // 15
            show("now", now);
            Integer timed = quoteFuture.get(1, TimeUnit.SECONDS);   // 15
            show("timed", timed);
        }
        {
            Future<Integer> failed;
            try (ExecutorService executor = Executors.newSingleThreadExecutor()) {
                failed = executor.submit(() -> quoteFrom("fedex"));
            }
            boolean done = failed.isDone();                   // true, the task has ended
            show("done", done);
            Future.State state = failed.state();              // FAILED
            show("state", state);
            try { Throwable cause = failed.exceptionNow(); show("cause", cause); } catch (Throwable _t) { System.out.println("cause -> " + _t); }
            try { Integer value = failed.get(); show("value", value); } catch (Throwable _t) { System.out.println("value -> " + _t); }
        }
        {
            Runnable crashing = () -> {
                throw new IllegalStateException("no carrier");
            };
            Future<?> crashed;
            try (ExecutorService executor = Executors.newSingleThreadExecutor()) {
                crashed = executor.submit(crashing);
            }
            Future.State crashState = crashed.state();        // FAILED
            show("crashState", crashState);
        }
        {
            Runnable cleanup = () -> {};
            Callable<Object> asCallable = Executors.callable(cleanup);
            Callable<String> withResult = Executors.callable(cleanup, "done");
            Object empty = asCallable.call();                 // null
            show("empty", empty);
            String fixed = withResult.call();                 // "done"
            show("fixed", fixed);
        }
        {
            List<Callable<Integer>> requests = Stream.of("dhl", "ups", "fedex")
                    .map(carrier -> (Callable<Integer>) () -> quoteFrom(carrier))
                    .toList();

            List<Future<Integer>> answers;
            try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
                answers = executor.invokeAll(requests, 2, TimeUnit.SECONDS);
            }
            List<Integer> prices = answers.stream()
                    .filter(f -> f.state() == Future.State.SUCCESS)
                    .map(Future::resultNow)
                    .toList();
            int cheapest = Collections.min(prices);           // 12
            show("cheapest", cheapest);
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

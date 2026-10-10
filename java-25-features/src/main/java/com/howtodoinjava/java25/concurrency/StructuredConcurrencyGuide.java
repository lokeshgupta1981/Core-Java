package com.howtodoinjava.java25.concurrency;

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

import java.util.concurrent.StructuredTaskScope.*;
import java.util.concurrent.StructuredTaskScope.Subtask;

/**
 * Examples for the tutorial "Java Structured Concurrency with StructuredTaskScope (Java 25)".
 * https://howtodoinjava.com/java/multi-threading/structured-concurrency/
 */
public class StructuredConcurrencyGuide {
    static String dashboardUnstructured(ExecutorService executor, int userId) throws Exception {
        Future<String> rewards = executor.submit(() -> loadRewards(userId));
        Future<Integer> orders = executor.submit(() -> countOrders(userId));
        int count = orders.get();             // ExecutionException if orders fail
        return count + " orders, " + rewards.get();
    }
    static void pause(long ms) throws InterruptedException {
        new CountDownLatch(1).await(ms, TimeUnit.MILLISECONDS);   // interruptible wait
    }
    static String loadProfile(int userId) throws InterruptedException {
        pause(50);
        return "Lokesh";
    }
    static int countOrders(int userId) throws InterruptedException {
        pause(80);
        return 3;
    }
    static String loadRewards(int userId) throws InterruptedException {
        pause(120);
        return "120 points";
    }
    static int countOrdersDown(int userId) {
        throw new IllegalStateException("orders service down");
    }
    static String loadRewardsSlowly(int userId, AtomicBoolean interrupted) throws InterruptedException {
        try {
            pause(2_000);
            return "120 points";
        } catch (InterruptedException e) {
            interrupted.set(true);
            throw e;
        }
    }
    static class SuccessfulResults<T> implements Joiner<T, List<T>> {
        private final Queue<T> results = new ConcurrentLinkedQueue<>();

        @Override
        public boolean onComplete(Subtask<? extends T> subtask) {
            if (subtask.state() == Subtask.State.SUCCESS) {
                results.add(subtask.get());
            }
            return false;                        // never cancel the scope
        }

        @Override
        public List<T> result() {
            return List.copyOf(results);
        }
    }
    public static void main(String[] args) throws Exception {
        {
            String summary;
            try (var scope = StructuredTaskScope.open()) {
                Subtask<String> profile = scope.fork(() -> loadProfile(7));   // new virtual thread
                Subtask<Integer> orders = scope.fork(() -> countOrders(7));   // new virtual thread
                scope.join();                                                 // waits for both, throws if one fails
                summary = profile.get() + " has " + orders.get() + " orders";
            }
            String result = summary;                                          // "Lokesh has 3 orders"
            show("result", result);
        }
        {
            Subtask.State before;
            Subtask.State after;
            String rewards;
            try (var scope = StructuredTaskScope.open()) {
                Subtask<String> task = scope.fork(() -> loadRewards(7));
                before = task.state();
                scope.join();
                after = task.state();
                rewards = task.get();
            }
            Subtask.State stateBeforeJoin = before;      // UNAVAILABLE
            show("stateBeforeJoin", stateBeforeJoin);
            Subtask.State stateAfterJoin = after;        // SUCCESS
            show("stateAfterJoin", stateAfterJoin);
            String points = rewards;                     // "120 points"
            show("points", points);
        }
        {
            AtomicBoolean interrupted = new AtomicBoolean();
            String outcome;
            try (var scope = StructuredTaskScope.open()) {
                scope.fork(() -> loadRewardsSlowly(7, interrupted));
                scope.fork(() -> countOrdersDown(7));
                scope.join();
                outcome = "ok";
            } catch (StructuredTaskScope.FailedException e) {
                outcome = e.getCause().getMessage();
            }
            String failure = outcome;                              // "orders service down"
            show("failure", failure);
            boolean rewardsCancelled = interrupted.get();          // true
            show("rewardsCancelled", rewardsCancelled);
        }
        {
            String fastest;
            try (var scope = StructuredTaskScope.open(Joiner.<String>anySuccessfulResultOrThrow())) {
                scope.fork(() -> { pause(300); return "mirror-a"; });
                scope.fork(() -> { pause(20); return "mirror-b"; });
                fastest = scope.join();
            }
            String winner = fastest;                     // "mirror-b"
            show("winner", winner);
        }
        {
            List<Integer> stock;
            try (var scope = StructuredTaskScope.open(Joiner.<Integer>allSuccessfulOrThrow())) {
                for (int warehouse = 1; warehouse <= 3; warehouse++) {
                    int id = warehouse;
                    scope.fork(() -> id * 10);
                }
                stock = scope.join().map(Subtask::get).toList();
            }
            List<Integer> perWarehouse = stock;          // [10, 20, 30]
            show("perWarehouse", perWarehouse);
        }
        {
            String status;
            try (var scope = StructuredTaskScope.open(Joiner.<String>awaitAllSuccessfulOrThrow(),
            cf -> cf.withTimeout(Duration.ofMillis(100)))) {
                scope.fork(() -> { pause(1_000); return "late report"; });
                scope.join();
                status = "done";
            } catch (StructuredTaskScope.TimeoutException e) {
                status = "timed out";
            }
            String report = status;                      // "timed out"
            show("report", report);
        }
        {
            List<String> offers;
            try (var scope = StructuredTaskScope.open(new SuccessfulResults<String>())) {
                scope.fork(() -> "hotel-a: 90");
                scope.fork(() -> { throw new IllegalStateException("hotel-b down"); });
                scope.fork(() -> "hotel-c: 85");
                offers = scope.join();
            }
            int offerCount = offers.size();              // 2
            show("offerCount", offerCount);
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

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
 * Examples for the tutorial "Concurrency vs Parallelism in Java with Examples".
 * https://howtodoinjava.com/java/multi-threading/concurrency-vs-parallelism/
 */
public class ConcurrencyVsParallelism {
    static String callService(String name) {
        LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(100));   // waiting for the reply
        return name + " ok";
    }
    static boolean isPrime(long n) {
        if (n < 2) {
            return false;
        }
        for (long d = 2; d * d <= n; d++) {
            if (n % d == 0) {
                return false;
            }
        }
        return true;
    }
    public static void main(String[] args) throws Exception {
        {
            // Concurrency: three calls that mostly wait, overlapped
            List<Callable<String>> calls = List.of(
            () -> callService("payment"), () -> callService("stock"), () -> callService("shipping"));
            List<Future<String>> replies;
            try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
                replies = executor.invokeAll(calls);
            }
            String firstReply = replies.get(0).resultNow();                                // "payment ok"
            show("firstReply", firstReply);

            // Parallelism: one CPU-bound job split across cores
            long primes = LongStream.rangeClosed(2, 200_000).parallel().filter(n -> isPrime(n)).count(); // 17984
            show("primes", primes);
        }
        {
            List<Callable<String>> orderCalls = List.of(
            () -> callService("payment"), () -> callService("stock"), () -> callService("shipping"));
            long start = System.nanoTime();
            List<String> results = new ArrayList<>();
            try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
                for (Future<String> reply : executor.invokeAll(orderCalls)) {
                    results.add(reply.resultNow());
                }
            }
            long elapsedMs = (System.nanoTime() - start) / 1_000_000;
            String allReplies = String.join(", ", results);       // "payment ok, stock ok, shipping ok"
            show("allReplies", allReplies);
            boolean overlapped = elapsedMs < 250;                 // true, not 300 ms in sequence
            show("overlapped", overlapped);
        }
        {
            int cores = Runtime.getRuntime().availableProcessors();               // depends on the machine, e.g. 8
            show("cores", cores);
            long sequential = LongStream.rangeClosed(2, 200_000).filter(n -> isPrime(n)).count();        // 17984
            show("sequential", sequential);
            long parallel = LongStream.rangeClosed(2, 200_000).parallel().filter(n -> isPrime(n)).count(); // 17984
            show("parallel", parallel);
            boolean sameResult = sequential == parallel;                          // true
            show("sameResult", sameResult);
        }
        {
            int[] counter = {0};
            IntStream.range(0, 100_000).parallel().forEach(i -> counter[0]++);
            int unsafeTotal = counter[0];                                  // less than 100000 in most runs
            show("unsafeTotal", unsafeTotal);
        }
        {
            int safeTotal = IntStream.range(0, 100_000).parallel().map(i -> 1).sum();   // 100000
            show("safeTotal", safeTotal);
            List<Integer> evens = IntStream.range(0, 10).parallel()
                    .filter(i -> i % 2 == 0).boxed().toList();
            List<Integer> ordered = evens;                                             // [0, 2, 4, 6, 8]
            show("ordered", ordered);
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

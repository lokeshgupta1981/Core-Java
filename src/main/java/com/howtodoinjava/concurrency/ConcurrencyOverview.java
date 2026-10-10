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
 * Examples for the tutorial "Java Concurrency Guide: Threads, Executors and Virtual Threads".
 * https://howtodoinjava.com/java/multi-threading/java-concurrency/
 */
public class ConcurrencyOverview {
    static class VisitCounter {
        private int count;

        void increment() {
            count++;                                // read, add, write: not atomic
        }

        int get() {
            return count;
        }
    }
    static List<Integer> primeFactors(int number) {
        List<Integer> factors = new ArrayList<>();  // local, one list per call
        for (int p = 2; number > 1; p++) {
            while (number % p == 0) {
                factors.add(p);
                number /= p;
            }
        }
        return factors;
    }
    public static void main(String[] args) throws Exception {
        {
            Future<String> user;
            Future<Integer> cartSize;
            try (ExecutorService pool = Executors.newVirtualThreadPerTaskExecutor()) {
                user = pool.submit(() -> "lokesh");                 // runs on its own virtual thread
                cartSize = pool.submit(() -> 3);                    // runs at the same time
            }                                                       // close() waits for both tasks
            String name = user.resultNow();                         // "lokesh"
            show("name", name);
            int items = cartSize.resultNow();                       // 3
            show("items", items);
        }
        {
            VisitCounter visits = new VisitCounter();
            ExecutorService pool = Executors.newFixedThreadPool(2);
            for (int t = 0; t < 2; t++) {
                pool.submit(() -> {
                    for (int i = 0; i < 100_000; i++) {
                        visits.increment();
                    }
                });
            }
            pool.close();
            int counted = visits.get();                     // less than 200000 in most runs
            show("counted", counted);
        }
        {
            AtomicInteger views = new AtomicInteger();
            ExecutorService pool = Executors.newFixedThreadPool(2);
            for (int t = 0; t < 2; t++) {
                pool.submit(() -> {
                    for (int i = 0; i < 100_000; i++) {
                        views.incrementAndGet();
                    }
                });
            }
            pool.close();
            int total = views.get();                        // 200000
            show("total", total);
        }
        {
            List<Integer> factors = primeFactors(84);       // [2, 2, 3, 7]
            show("factors", factors);
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

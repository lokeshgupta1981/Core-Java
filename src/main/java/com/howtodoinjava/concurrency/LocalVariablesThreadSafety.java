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
 * Examples for the tutorial "Are Local Variables Thread Safe in Java? Stack vs Heap".
 * https://howtodoinjava.com/java/multi-threading/local-variables-and-thread-safety/
 */
public class LocalVariablesThreadSafety {
    static void addTag(List<String> tags, int limit) {
        tags.add("java");                    // changes the caller's list
        limit = 0;                           // changes only the local copy
        tags = new ArrayList<>();            // the caller keeps its list
    }
    static String invoiceLine(String item, int quantity, double price) {
        StringBuilder line = new StringBuilder();   // never leaves this call
        line.append(item).append(" x").append(quantity);
        line.append(" = ").append(quantity * price);
        return line.toString();                     // returns an immutable String
    }
    public static void main(String[] args) throws Exception {
        {
            Callable<Integer> sumTo100 = () -> {
                int total = 0;                       // a new copy on each call's stack
                for (int i = 1; i <= 100; i++) {
                    total += i;
                }
                return total;
            };
            ExecutorService pool = Executors.newFixedThreadPool(2);
            Future<Integer> first = pool.submit(sumTo100);
            Future<Integer> second = pool.submit(sumTo100);
            int firstSum = first.get();              // 5050
            show("firstSum", firstSum);
            int secondSum = second.get();            // 5050
            show("secondSum", secondSum);
            pool.close();
        }
        {
            List<String> postTags = new ArrayList<>(List.of("threads"));
            int maxTags = 5;
            addTag(postTags, maxTags);
            String tagsAfter = postTags.toString();  // "[threads, java]"
            show("tagsAfter", tagsAfter);
            int maxAfter = maxTags;                  // 5
            show("maxAfter", maxAfter);
        }
        {
            int[] hits = {0};                        // local reference, shared array
            show("hits", hits);
            Runnable countHits = () -> {
                for (int i = 0; i < 1_000_000; i++) {
                    hits[0]++;                       // read, add, write: not atomic
                }
            };
            Thread worker1 = Thread.ofPlatform().start(countHits);
            Thread worker2 = Thread.ofPlatform().start(countHits);
            worker1.join();
            worker2.join();
            int lostUpdates = hits[0];               // less than 2000000 in most runs
            show("lostUpdates", lostUpdates);
        }
        {
            Callable<Integer> countLocally = () -> {
                int count = 0;                       // private to this call
                for (int i = 0; i < 1_000_000; i++) {
                    count++;
                }
                return count;
            };
            ExecutorService workers = Executors.newFixedThreadPool(2);
            Future<Integer> part1 = workers.submit(countLocally);
            Future<Integer> part2 = workers.submit(countLocally);
            int totalHits = part1.get() + part2.get();   // 2000000
            show("totalHits", totalHits);
            workers.close();
        }
        {
            LongAdder sharedHits = new LongAdder();
            Runnable addHits = () -> {
                for (int i = 0; i < 1_000_000; i++) {
                    sharedHits.increment();
                }
            };
            Thread adder1 = Thread.ofPlatform().start(addHits);
            Thread adder2 = Thread.ofPlatform().start(addHits);
            adder1.join();
            adder2.join();
            long safeHits = sharedHits.sum();        // 2000000
            show("safeHits", safeHits);
        }
        {
            String line = invoiceLine("pen", 3, 1.5);      // "pen x3 = 4.5"
            show("line", line);
        }
        {
            List<Integer> orderAmounts = List.of(10, 20, 30);
            int orderTotal = orderAmounts.parallelStream().mapToInt(Integer::intValue).sum();   // 60
            show("orderTotal", orderTotal);
            List<Integer> doubled = orderAmounts.parallelStream().map(n -> n * 2).toList();       // [20, 40, 60]
            show("doubled", doubled);
        }
        {
            Callable<String> greet = () -> {
                String name = Thread.currentThread().isVirtual() ? "virtual" : "platform";
                return "hello from " + name;
            };
            ExecutorService virtualPool = Executors.newVirtualThreadPerTaskExecutor();
            Future<String> reply = virtualPool.submit(greet);
            String message = reply.get();            // "hello from virtual"
            show("message", message);
            virtualPool.close();
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

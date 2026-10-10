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
 * Examples for the tutorial "Java Executors.newVirtualThreadPerTaskExecutor() Example".
 * https://howtodoinjava.com/java/multi-threading/newvirtualthreadpertaskexecutor-example/
 */
public class VirtualThreadPerTaskExecutor {
    static void pause(long ms) {
        LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(ms));
    }
    static String fetchPage(String url) {
        pause(100);                             // waiting for the remote site
        return url.substring(url.lastIndexOf('/') + 1);
    }
    static String callPriceApi() {
        pause(150);                             // remote pricing service
        return "price=25";
    }
    static String callStockApi() {
        pause(150);                             // remote inventory service
        return "stock=3";
    }
    static String invokeAndCombineResults() throws ExecutionException, InterruptedException {
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            Future<String> price = executor.submit(() -> callPriceApi());
            Future<String> stock = executor.submit(() -> callStockApi());
            return price.get() + ", " + stock.get();
        }
    }
    static class PartnerGateway {
        private final Semaphore permits = new Semaphore(3);
        private final AtomicInteger inFlight = new AtomicInteger();
        private final AtomicInteger maxInFlight = new AtomicInteger();

        String placeOrder(int id) throws InterruptedException {
            permits.acquire();
            try {
                maxInFlight.accumulateAndGet(inFlight.incrementAndGet(), Math::max);
                pause(20);                      // remote call
                return "order-" + id;
            } finally {
                inFlight.decrementAndGet();
                permits.release();
            }
        }

        int maxInFlight() {
            return maxInFlight.get();
        }
    }
    public static void main(String[] args) throws Exception {
        {
            List<Future<Integer>> futures = new ArrayList<>();
            try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
                for (int num = 1; num <= 5; num++) {
                    int n = num;
                    futures.add(executor.submit(() -> n * n));   // one new virtual thread per task
                }
            }                                                    // close() waits for all five tasks
            int first = futures.get(0).resultNow();              // 1
            show("first", first);
            int last = futures.get(4).resultNow();               // 25
            show("last", last);
        }
        {
            ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();
        }
        {
            List<Integer> numList = List.of(1, 2, 3, 4, 5);
            Map<Integer, Integer> squares = new ConcurrentHashMap<>();
            try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
                numList.forEach(num ->
                executor.execute(() -> squares.put(num, num * num)));
            }
            int count = squares.size();                 // 5
            show("count", count);
            int squareOfThree = squares.get(3);         // 9
            show("squareOfThree", squareOfThree);
        }
        {
            List<String> urls = List.of(
            "https://example.com/page1",
            "https://example.com/page2",
            "https://example.com/page3");
            List<Callable<String>> calls = urls.stream()
                    .map(url -> (Callable<String>) () -> fetchPage(url))
                    .toList();
            List<Future<String>> pages;
            try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
                pages = executor.invokeAll(calls);
            }
            String firstPage = pages.get(0).resultNow();     // "page1"
            show("firstPage", firstPage);
            String lastPage = pages.get(2).resultNow();      // "page3"
            show("lastPage", lastPage);
        }
        {
            long start = System.nanoTime();
            String product = invokeAndCombineResults();                   // "price=25, stock=3"
            show("product", product);
            long elapsedMs = (System.nanoTime() - start) / 1_000_000;
            boolean overlapped = elapsedMs < 280;                         // true, calls ran at the same time
            show("overlapped", overlapped);
        }
        {
            PartnerGateway gateway = new PartnerGateway();
            List<Future<String>> orders = new ArrayList<>();
            try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
                for (int i = 0; i < 20; i++) {
                    int id = i;
                    orders.add(executor.submit(() -> gateway.placeOrder(id)));
                }
            }
            int done = orders.size();                          // 20
            show("done", done);
            boolean withinLimit = gateway.maxInFlight() <= 3;  // true
            show("withinLimit", withinLimit);
        }
        {
            ThreadFactory factory = Thread.ofVirtual().name("price-", 0).factory();
            Future<String> named;
            Future<String> unnamed;
            try (ExecutorService executor = Executors.newThreadPerTaskExecutor(factory);
            ExecutorService plain = Executors.newVirtualThreadPerTaskExecutor()) {
                named = executor.submit(() -> Thread.currentThread().getName());
                unnamed = plain.submit(() -> Thread.currentThread().getName());
            }
            String threadName = named.resultNow();               // "price-0"
            show("threadName", threadName);
            String defaultName = unnamed.resultNow();            // ""
            show("defaultName", defaultName);
        }
        {
            Future<String> failed;
            try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
                failed = executor.submit(() -> {
                    throw new IllegalStateException("supplier down");
                });
            }
            Future.State state = failed.state();                 // FAILED
            show("state", state);
            try { Throwable cause = failed.exceptionNow(); show("cause", cause); } catch (Throwable _t) { System.out.println("cause -> " + _t); }
            try { String value = failed.get(); show("value", value); } catch (Throwable _t) { System.out.println("value -> " + _t); }
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

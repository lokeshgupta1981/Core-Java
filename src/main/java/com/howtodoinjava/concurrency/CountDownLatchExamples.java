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
 * Examples for the tutorial "Java CountDownLatch Example: await(), countDown() and Timeout".
 * https://howtodoinjava.com/java/multi-threading/when-to-use-countdownlatch-java-concurrency-example-tutorial/
 */
public class CountDownLatchExamples {
    static abstract class BaseHealthChecker implements Runnable {
        private final CountDownLatch latch;
        private final String serviceName;
        private volatile boolean serviceUp;

        BaseHealthChecker(String serviceName, CountDownLatch latch) {
            this.serviceName = serviceName;
            this.latch = latch;
        }

        @Override
        public void run() {
            try {
                verifyService();
                serviceUp = true;
            } catch (RuntimeException e) {
                serviceUp = false;
            } finally {
                latch.countDown();                          // always, even when the check fails
            }
        }

        String getServiceName() {
            return serviceName;
        }

        boolean isServiceUp() {
            return serviceUp;
        }

        abstract void verifyService();
    }
    static void pause(long ms) {
        LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(ms));
    }
    static class NetworkHealthChecker extends BaseHealthChecker {
        NetworkHealthChecker(CountDownLatch latch) {
            super("Network Service", latch);
        }

        @Override
        void verifyService() {
            pause(70);
        }
    }

    static class CacheHealthChecker extends BaseHealthChecker {
        CacheHealthChecker(CountDownLatch latch) {
            super("Cache Service", latch);
        }

        @Override
        void verifyService() {
            pause(30);
        }
    }

    static class DatabaseHealthChecker extends BaseHealthChecker {
        DatabaseHealthChecker(CountDownLatch latch) {
            super("Database Service", latch);
        }

        @Override
        void verifyService() {
            pause(50);
        }
    }
    static class ApplicationStartup {
        static boolean checkExternalServices() throws InterruptedException {
            CountDownLatch latch = new CountDownLatch(3);
            List<BaseHealthChecker> services = List.of(
            new NetworkHealthChecker(latch),
            new CacheHealthChecker(latch),
            new DatabaseHealthChecker(latch));
            try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
                services.forEach(executor::execute);
                if (!latch.await(5, TimeUnit.SECONDS)) {
                    executor.shutdownNow();                 // interrupt the hanging checks
                    return false;
                }
            }
            return services.stream().allMatch(BaseHealthChecker::isServiceUp);
        }
    }
    public static void main(String[] args) throws Exception {
        {
            CountDownLatch servicesReady = new CountDownLatch(3);    // wait for 3 events
            show("servicesReady", servicesReady);
            long before = servicesReady.getCount();                  // 3
            show("before", before);
            servicesReady.countDown();                               // database is up
            servicesReady.countDown();                               // cache is up
            servicesReady.countDown();                               // network is up
            boolean allUp = servicesReady.await(5, TimeUnit.SECONDS);   // true, returns at once
            show("allUp", allUp);
            long after = servicesReady.getCount();                   // 0
            show("after", after);
        }
        {
            boolean result = ApplicationStartup.checkExternalServices();   // true
            show("result", result);
        }
        {
            CountDownLatch reports = new CountDownLatch(2);
            reports.countDown();                                     // only one of two workers reported
            boolean finished = reports.await(100, TimeUnit.MILLISECONDS);   // false, timed out
            show("finished", finished);
            long missing = reports.getCount();                       // 1
            show("missing", missing);
        }
        {
            CountDownLatch startGate = new CountDownLatch(1);
            CountDownLatch done = new CountDownLatch(5);
            AtomicInteger seatsLeft = new AtomicInteger(1);
            AtomicInteger booked = new AtomicInteger();
            for (int i = 0; i < 5; i++) {
                Thread.ofVirtual().start(() -> {
                    try {
                        startGate.await();                          // every thread waits here
                        if (seatsLeft.getAndUpdate(n -> n > 0 ? n - 1 : n) > 0) {
                            booked.incrementAndGet();
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } finally {
                        done.countDown();
                    }
                });
            }
            startGate.countDown();                                  // opens the gate for all 5
            boolean allDone = done.await(1, TimeUnit.SECONDS);      // true
            show("allDone", allDone);
            int bookings = booked.get();                            // 1
            show("bookings", bookings);
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

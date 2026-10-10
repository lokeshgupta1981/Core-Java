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
 * Examples for the tutorial "Java Virtual Threads Guide with Examples (Java 25)".
 * https://howtodoinjava.com/java/multi-threading/virtual-threads/
 */
public class VirtualThreadsGuide {
    static void pause(long ms) {
        LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(ms));
    }
    static class RoomCounter {
        private int booked;

        synchronized void book() throws InterruptedException {
            booked++;
            wait(1);                              // unmounts on Java 24+, pinned on Java 21
        }

        synchronized int booked() {
            return booked;
        }
    }
    static class PaymentGateway {
        private final Semaphore limit = new Semaphore(10);

        String charge(int bookingId) throws InterruptedException {
            limit.acquire();
            try {
                pause(10);                        // payment API call
                return "charged-" + bookingId;
            } finally {
                limit.release();
            }
        }
    }
    public static void main(String[] args) throws Exception {
        {
            Thread vt = Thread.ofVirtual().name("booking-1").start(() -> System.out.println("checking rooms"));
            vt.join();
            boolean virtual = vt.isVirtual();          // true
            show("virtual", virtual);
            boolean daemon = vt.isDaemon();            // true
            show("daemon", daemon);
            String name = vt.getName();                // "booking-1"
            show("name", name);
        }
        {
            Thread first = Thread.startVirtualThread(() -> {});
            Thread second = Thread.ofVirtual().name("worker-", 0).unstarted(() -> {});
            second.start();
            first.join();
            second.join();
            String secondName = second.getName();                       // "worker-0"
            show("secondName", secondName);
            ThreadFactory factory = Thread.ofVirtual().name("job-", 1).factory();
            Thread fromFactory = factory.newThread(() -> {});
            String factoryName = fromFactory.getName();                 // "job-1"
            show("factoryName", factoryName);
            boolean platformByDefault = new Thread(() -> {}).isVirtual(); // false
            show("platformByDefault", platformByDefault);
        }
        {
            AtomicInteger finished = new AtomicInteger();
            Set<String> carriers = ConcurrentHashMap.newKeySet();
            try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
                for (int i = 0; i < 10_000; i++) {
                    executor.submit(() -> {
                        String self = Thread.currentThread().toString();
                        carriers.add(self.substring(self.indexOf('@') + 1));
                        pause(100);                       // partner API call
                        finished.incrementAndGet();
                    });
                }
            }
            int completed = finished.get();                                      // 10000
            show("completed", completed);
            boolean fewCarriers = carriers.size() <= Runtime.getRuntime().availableProcessors(); // true
            show("fewCarriers", fewCarriers);
        }
        {
            Thread worker = Thread.ofVirtual().unstarted(() -> {});
            worker.setPriority(Thread.MAX_PRIORITY);
            int priority = worker.getPriority();          // 5, the call is ignored
            show("priority", priority);
            boolean isDaemon = worker.isDaemon();         // true
            show("isDaemon", isDaemon);
            try { worker.setDaemon(false);  } catch (Throwable _t) { System.out.println("-> " + _t); }
        }
        {
            RoomCounter counter = new RoomCounter();
            try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
                for (int i = 0; i < 1_000; i++) {
                    executor.submit(() -> { counter.book(); return null; });
                }
            }
            int booked = counter.booked();                // 1000
            show("booked", booked);
        }
        {
            // anti-pattern: a fixed pool of 50 virtual threads used as a concurrency limit
            ExecutorService pooled = Executors.newFixedThreadPool(50, Thread.ofVirtual().factory());
            pooled.close();
        }
        {
            PaymentGateway payments = new PaymentGateway();
            List<Future<String>> charges = new ArrayList<>();
            try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
                for (int id = 1; id <= 100; id++) {
                    int bookingId = id;
                    charges.add(executor.submit(() -> payments.charge(bookingId)));
                }
            }
            String lastCharge = charges.get(99).resultNow();     // "charged-100"
            show("lastCharge", lastCharge);
        }
        {
            DateTimeFormatter checkInFormat = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH);
            String formatted;
            try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
                formatted = executor.submit(() -> checkInFormat.format(LocalDate.of(2026, 10, 12))).get();
            }
            String checkIn = formatted;                   // "12 Oct 2026"
            show("checkIn", checkIn);
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

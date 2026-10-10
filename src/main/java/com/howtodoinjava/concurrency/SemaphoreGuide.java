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
 * Examples for the tutorial "Java Semaphore: acquire(), release(), tryAcquire() Examples".
 * https://howtodoinjava.com/java/multi-threading/control-concurrent-access-to-multiple-copies-of-a-resource-using-semaphore/
 */
public class SemaphoreGuide {
    static class PdfRenderer {
        private final Semaphore slots = new Semaphore(3);
        private final AtomicInteger inside = new AtomicInteger();
        private final AtomicInteger peak = new AtomicInteger();

        String render(String report) throws InterruptedException {
            slots.acquire();
            try {
                int running = inside.incrementAndGet();
                peak.accumulateAndGet(running, Math::max);
                LockSupport.parkNanos(Duration.ofMillis(50).toNanos());   // the remote call
                return report + ".pdf";
            } finally {
                inside.decrementAndGet();
                slots.release();
            }
        }

        int peak() {
            return peak.get();
        }
    }
    static void renderWrong(Semaphore slots, Runnable call) {
        try {
            slots.acquire();
            call.run();
        } catch (InterruptedException e) {
            e.printStackTrace();                            // the interrupt flag is lost
        } finally {
            slots.release();                                // runs even when acquire() failed
        }
    }
    static void renderSafe(Semaphore slots, Runnable call) throws InterruptedException {
        slots.acquire();                                    // throws before we hold a permit
        try {
            call.run();
        } finally {
            slots.release();                                // only after a successful acquire
        }
    }
    static String renderOrBusy(Semaphore slots, String report) throws InterruptedException {
        if (!slots.tryAcquire(200, TimeUnit.MILLISECONDS)) {
            return "busy, retry later";
        }
        try {
            return report + ".pdf";
        } finally {
            slots.release();
        }
    }
    static class ThrottledExecutor {
        private final ExecutorService executor;
        private final Semaphore slots;

        ThrottledExecutor(ExecutorService executor, int maxInFlight) {
            this.executor = executor;
            this.slots = new Semaphore(maxInFlight);
        }

        Future<?> submit(Runnable task) throws InterruptedException {
            slots.acquire();                                // blocks the caller at the limit
            try {
                return executor.submit(() -> {
                    try {
                        task.run();
                    } finally {
                        slots.release();                    // the task has finished
                    }
                });
            } catch (RejectedExecutionException e) {
                slots.release();                            // the task never started
                throw e;
            }
        }
    }
    public static void main(String[] args) throws Exception {
        {
            Semaphore renderSlots = new Semaphore(3);             // 3 permits
            show("renderSlots", renderSlots);
            renderSlots.acquire();                                  // takes a permit, blocks when none is left
            int left = renderSlots.availablePermits();              // 2
            show("left", left);
            try {
                String file = "sales" + ".pdf";                     // call the rendering service here
            } finally {
                renderSlots.release();                              // always gives the permit back
            }
            int afterRelease = renderSlots.availablePermits();      // 3
            show("afterRelease", afterRelease);
            boolean gotOne = renderSlots.tryAcquire(200, TimeUnit.MILLISECONDS);   // true
            show("gotOne", gotOne);
        }
        {
            PdfRenderer renderer = new PdfRenderer();
            List<Future<String>> futures = new ArrayList<>();
            try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
                for (int i = 1; i <= 10; i++) {
                    String report = "report-" + i;
                    futures.add(executor.submit(() -> renderer.render(report)));
                }
            }
            List<String> files = futures.stream().map(Future::resultNow).toList();
            String first = files.getFirst();                        // "report-1.pdf"
            show("first", first);
            int exported = files.size();                            // 10
            show("exported", exported);
            int peak = renderer.peak();                             // 3
            show("peak", peak);
        }
        {
            Semaphore slots = new Semaphore(0);
            Thread.currentThread().interrupt();                     // simulate a cancelled request
            renderWrong(slots, () -> {});
            int leaked = slots.availablePermits();                  // 1
            show("leaked", leaked);
        }
        {
            Semaphore slots = new Semaphore(0);
            Thread.currentThread().interrupt();
            try { renderSafe(slots, () -> {});  } catch (Throwable _t) { System.out.println("-> " + _t); }
            int permits = slots.availablePermits();                 // 0
            show("permits", permits);
        }
        {
            Semaphore slots = new Semaphore(1);
            String first = renderOrBusy(slots, "sales");            // "sales.pdf"
            show("first", first);
            slots.acquire();                                        // another caller holds the only permit
            String second = renderOrBusy(slots, "stock");           // "busy, retry later"
            show("second", second);
            boolean noWait = slots.tryAcquire();                    // false
            show("noWait", noWait);
        }
        {
            Semaphore slots = new Semaphore(3);
            slots.acquire(2);                                       // the yearly export takes 2 slots
            int free = slots.availablePermits();                    // 1
            show("free", free);
            boolean big = slots.tryAcquire(2);                      // false, only 1 left
            show("big", big);
            boolean small = slots.tryAcquire();                     // true
            show("small", small);
            slots.release(3);
            int all = slots.availablePermits();                     // 3
            show("all", all);
        }
        {
            Semaphore fair = new Semaphore(3, true);
            boolean isFair = fair.isFair();                         // true
            show("isFair", isFair);
            boolean waiting = fair.hasQueuedThreads();              // false
            show("waiting", waiting);
            boolean inOrder = fair.tryAcquire(0, TimeUnit.SECONDS); // true, respects the queue
            show("inOrder", inOrder);
        }
        {
            ExecutorService pool = Executors.newFixedThreadPool(2);
            ThrottledExecutor throttled = new ThrottledExecutor(pool, 4);   // 2 running + 2 waiting
            show("throttled", throttled);
            AtomicInteger done = new AtomicInteger();
            for (int i = 0; i < 20; i++) {
                throttled.submit(() -> {
                    LockSupport.parkNanos(Duration.ofMillis(10).toNanos());
                    done.incrementAndGet();
                });
            }
            pool.close();
            int completed = done.get();                             // 20
            show("completed", completed);
        }
        {
            Semaphore dbSlots = new Semaphore(10);
            AtomicInteger inside = new AtomicInteger();
            AtomicInteger peak = new AtomicInteger();
            try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
                for (int i = 0; i < 1_000; i++) {
                    executor.submit(() -> {
                        dbSlots.acquire();
                        try {
                            peak.accumulateAndGet(inside.incrementAndGet(), Math::max);
                            LockSupport.parkNanos(Duration.ofMillis(5).toNanos());   // the query
                        } finally {
                            inside.decrementAndGet();
                            dbSlots.release();
                        }
                        return null;
                    });
                }
            }
            int maxParallel = peak.get();                           // 10
            show("maxParallel", maxParallel);
        }
        {
            Semaphore slots = new Semaphore(2);
            slots.release();                                        // no acquire() before it
            int permits = slots.availablePermits();                 // 3
            show("permits", permits);
            int drained = slots.drainPermits();                     // 3
            show("drained", drained);
            int remaining = slots.availablePermits();               // 0
            show("remaining", remaining);
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

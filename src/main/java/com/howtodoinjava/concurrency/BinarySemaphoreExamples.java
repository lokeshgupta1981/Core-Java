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
 * Examples for the tutorial "Java Binary Semaphore vs Mutex (with Examples)".
 * https://howtodoinjava.com/java/multi-threading/binary-semaphore-tutorial-and-example/
 */
public class BinarySemaphoreExamples {
    static class InvoiceExporter {
        private final Semaphore oneAtATime = new Semaphore(1);
        private final AtomicInteger running = new AtomicInteger();
        private final AtomicInteger peak = new AtomicInteger();

        void export(String month) throws InterruptedException {
            oneAtATime.acquire();
            try {
                peak.accumulateAndGet(running.incrementAndGet(), Math::max);
                LockSupport.parkNanos(Duration.ofMillis(20).toNanos());   // write the invoices
            } finally {
                running.decrementAndGet();
                oneAtATime.release();
            }
        }

        int peak() {
            return peak.get();
        }
    }
    public static void main(String[] args) throws Exception {
        {
            Semaphore cacheRebuild = new Semaphore(1);              // binary semaphore, 1 = free
            show("cacheRebuild", cacheRebuild);
            boolean first = cacheRebuild.tryAcquire();               // true, the count is 0
            show("first", first);
            boolean second = cacheRebuild.tryAcquire();              // false, already taken
            show("second", second);
            cacheRebuild.release();                                  // the count is 1 again
            int permits = cacheRebuild.availablePermits();           // 1
            show("permits", permits);
        }
        {
            InvoiceExporter exporter = new InvoiceExporter();
            try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
                for (String month : List.of("jan", "feb", "mar", "apr", "may")) {
                    executor.submit(() -> { exporter.export(month); return null; });
                }
            }
            int peak = exporter.peak();                              // 1
            show("peak", peak);
        }
        {
            Semaphore configReady = new Semaphore(0);                // 0 = no signal yet
            show("configReady", configReady);
            AtomicReference<String> seen = new AtomicReference<>();
            Thread worker = Thread.ofPlatform().start(() -> {
                configReady.acquireUninterruptibly();                // waits for the signal
                seen.set("processing with config");
            });
            boolean waiting = worker.isAlive();                      // true
            show("waiting", waiting);
            configReady.release();                                   // the main thread gives the permit
            worker.join();
            String result = seen.get();                              // "processing with config"
            show("result", result);
        }
        {
            Semaphore single = new Semaphore(1);
            single.acquire();
            boolean again = single.tryAcquire(100, TimeUnit.MILLISECONDS);   // false, the holder is blocked
            show("again", again);
            ReentrantLock lock = new ReentrantLock();
            lock.lock();
            boolean relock = lock.tryLock();                         // true
            show("relock", relock);
            int holds = lock.getHoldCount();                         // 2
            show("holds", holds);
            lock.unlock();
            lock.unlock();                                           // free again at hold count 0
        }
        {
            Semaphore single = new Semaphore(1);
            single.release();                                        // a bug releases without acquire()
            int permits = single.availablePermits();                 // 2, no longer binary
            show("permits", permits);
            boolean a = single.tryAcquire();                         // true
            show("a", a);
            boolean b = single.tryAcquire();                         // true, two holders at once
            show("b", b);
        }
        {
            Semaphore semaphore = new Semaphore(1);                  // non-fair, the default
            show("semaphore", semaphore);
            Semaphore fairSemaphore = new Semaphore(1, true);        // fair
            show("fairSemaphore", fairSemaphore);
            boolean fair = fairSemaphore.isFair();                   // true
            show("fair", fair);
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

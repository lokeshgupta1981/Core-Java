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
 * Examples for the tutorial "Cancel a Task in ExecutorService With Future.cancel()".
 * https://howtodoinjava.com/java/multi-threading/executor-service-cancel-task/
 */
public class CancelTaskExamples {
    static void await(CountDownLatch latch) {
        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    static String fetchOrDefault(ExecutorService pool, Callable<String> call, String fallback) {
        Future<String> future = pool.submit(call);
        try {
            return future.get(200, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            future.cancel(true);
            return fallback;
        } catch (ExecutionException e) {
            return fallback;
        } catch (InterruptedException e) {
            future.cancel(true);
            Thread.currentThread().interrupt();
            return fallback;
        }
    }
    public static void main(String[] args) throws Exception {
        {
            CountDownLatch busy = new CountDownLatch(1);
            ExecutorService pool = Executors.newSingleThreadExecutor();
            pool.submit(() -> await(busy));
            Future<String> export = pool.submit(() -> "report.pdf");
            boolean cancelled = export.cancel(false);       // true
            show("cancelled", cancelled);
            Future.State state = export.state();            // CANCELLED
            show("state", state);
            busy.countDown();
            pool.close();
        }
        {
            CountDownLatch gate = new CountDownLatch(1);
            ExecutorService exports = Executors.newSingleThreadExecutor();
            exports.submit(() -> await(gate));
            Future<String> oldExport = exports.submit(() -> "march.pdf");
            boolean doneBefore = oldExport.isDone();        // false
            show("doneBefore", doneBefore);
            boolean wasCancelled = oldExport.cancel(false); // true
            show("wasCancelled", wasCancelled);
            boolean doneAfter = oldExport.isDone();         // true
            show("doneAfter", doneAfter);
            boolean secondTry = oldExport.cancel(false);    // false
            show("secondTry", secondTry);
            try { String file = oldExport.get(); show("file", file); } catch (Throwable _t) { System.out.println("file -> " + _t); }
            gate.countDown();
            exports.close();
        }
        {
            AtomicInteger resized = new AtomicInteger();
            CountDownLatch started = new CountDownLatch(1);
            CountDownLatch stopped = new CountDownLatch(1);
            ExecutorService resizer = Executors.newSingleThreadExecutor();
            Future<?> job = resizer.submit(() -> {
                started.countDown();
                for (int i = 0; i < 100_000 && !Thread.currentThread().isInterrupted(); i++) {
                    resized.incrementAndGet();
                    LockSupport.parkNanos(TimeUnit.MICROSECONDS.toNanos(100));
                }
                stopped.countDown();
            });
            started.await();
            boolean interrupted = job.cancel(true);         // true
            show("interrupted", interrupted);
            boolean jobEnded = stopped.await(2, TimeUnit.SECONDS);   // true
            show("jobEnded", jobEnded);
            boolean partial = resized.get() < 100_000;      // true
            show("partial", partial);
            resizer.close();
        }
        {
            CountDownLatch release = new CountDownLatch(1);
            CountDownLatch running = new CountDownLatch(1);
            CountDownLatch finished = new CountDownLatch(1);
            ExecutorService worker = Executors.newSingleThreadExecutor();
            Future<?> upload = worker.submit(() -> {
                running.countDown();
                await(release);
                finished.countDown();
            });
            running.await();
            boolean marked = upload.cancel(false);          // true
            show("marked", marked);
            release.countDown();
            boolean ranToEnd = finished.await(2, TimeUnit.SECONDS);  // true
            show("ranToEnd", ranToEnd);
            worker.close();
        }
        {
            ExecutorService calls = Executors.newFixedThreadPool(2);
            String fast = fetchOrDefault(calls, () -> "phone case", "bestsellers");   // "phone case"
            show("fast", fast);
            Callable<String> stuck = () -> {
                new CountDownLatch(1).await();
                return "never";
            };
            String slow = fetchOrDefault(calls, stuck, "bestsellers");   // "bestsellers"
            show("slow", slow);
            calls.close();
        }
        {
            CountDownLatch hold = new CountDownLatch(1);
            ThreadPoolExecutor tpe = new ThreadPoolExecutor(1, 1, 0, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>());
            tpe.submit(() -> await(hold));
            Future<String> stale = tpe.submit(() -> "stale");
            stale.cancel(false);
            int beforePurge = tpe.getQueue().size();        // 1
            show("beforePurge", beforePurge);
            tpe.purge();
            int afterPurge = tpe.getQueue().size();         // 0
            show("afterPurge", afterPurge);
            hold.countDown();
            tpe.close();
        }
        {
            CountDownLatch never = new CountDownLatch(1);
            ExecutorService batch = Executors.newSingleThreadExecutor();
            batch.submit(() -> await(never));
            Future<String> second = batch.submit(() -> "b");
            Future<String> third = batch.submit(() -> "c");
            List<Runnable> neverStarted = batch.shutdownNow();
            int drained = neverStarted.size();              // 2
            show("drained", drained);
            Future.State leftOver = second.state();         // RUNNING
            show("leftOver", leftOver);
            for (Runnable task : neverStarted) {
                if (task instanceof Future<?> f) {
                    f.cancel(false);
                }
            }
            Future.State fixed = third.state();             // CANCELLED
            show("fixed", fixed);
            batch.close();
        }
        {
            CountDownLatch inside = new CountDownLatch(1);
            CountDownLatch keepGoing = new CountDownLatch(1);
            CountDownLatch completed = new CountDownLatch(1);
            ExecutorService async = Executors.newSingleThreadExecutor();
            CompletableFuture<String> price = CompletableFuture.supplyAsync(() -> {
                inside.countDown();
                await(keepGoing);
                completed.countDown();
                return "9.99";
            }, async);
            inside.await();
            boolean priceCancelled = price.cancel(true);    // true
            show("priceCancelled", priceCancelled);
            keepGoing.countDown();
            boolean stillRan = completed.await(2, TimeUnit.SECONDS);   // true
            show("stillRan", stillRan);
            async.close();
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

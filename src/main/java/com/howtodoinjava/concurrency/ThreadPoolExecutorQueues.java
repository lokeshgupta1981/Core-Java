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
 * Examples for the tutorial "ThreadPoolExecutor BlockingQueue: Queue Choice and Sizing".
 * https://howtodoinjava.com/java/multi-threading/how-to-use-blockingqueue-and-threadpoolexecutor-in-java/
 */
public class ThreadPoolExecutorQueues {
    static void await(CountDownLatch latch) {
        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    static record Job(int priority, String name, List<String> log)
    implements Runnable, Comparable<Job> {
        public void run() {
            log.add(name);
        }
        public int compareTo(Job other) {
            return Integer.compare(priority, other.priority);
        }
    }
    static class CountingPool extends ThreadPoolExecutor {
        final AtomicInteger started = new AtomicInteger();
        final AtomicInteger failed = new AtomicInteger();

        CountingPool(int threads) {
            super(threads, threads, 0, TimeUnit.SECONDS, new ArrayBlockingQueue<>(100));
        }

        @Override
        protected void beforeExecute(Thread t, Runnable r) {
            super.beforeExecute(t, r);
            started.incrementAndGet();
        }

        @Override
        protected void afterExecute(Runnable r, Throwable t) {
            super.afterExecute(r, t);
            boolean futureFailed = r instanceof Future<?> f && f.isDone()
                    && f.state() == Future.State.FAILED;
            if (t != null || futureFailed) {
                failed.incrementAndGet();
            }
        }
    }
    public static void main(String[] args) throws Exception {
        {
            CountDownLatch hold = new CountDownLatch(1);
            ThreadPoolExecutor pool = new ThreadPoolExecutor(2, 4, 30, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(2));
            for (int i = 0; i < 6; i++) {
                pool.execute(() -> await(hold));
            }
            int threads = pool.getPoolSize();               // 4
            show("threads", threads);
            int queued = pool.getQueue().size();            // 2
            show("queued", queued);
            hold.countDown();
            pool.close();
        }
        {
            CountDownLatch handoffGate = new CountDownLatch(1);
            ThreadPoolExecutor handoff = new ThreadPoolExecutor(1, 3, 30, TimeUnit.SECONDS,
            new SynchronousQueue<>());
            for (int i = 0; i < 3; i++) {
                handoff.execute(() -> await(handoffGate));
            }
            int handoffThreads = handoff.getPoolSize();     // 3
            show("handoffThreads", handoffThreads);
            try { handoff.execute(() -> await(handoffGate));  } catch (Throwable _t) { System.out.println("-> " + _t); }
            handoffGate.countDown();
            handoff.close();
        }
        {
            CountDownLatch openGate = new CountDownLatch(1);
            ThreadPoolExecutor open = new ThreadPoolExecutor(2, 10, 30, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>());
            for (int i = 0; i < 6; i++) {
                open.execute(() -> await(openGate));
            }
            int openThreads = open.getPoolSize();           // 2
            show("openThreads", openThreads);
            int openQueued = open.getQueue().size();        // 4
            show("openQueued", openQueued);
            openGate.countDown();
            open.close();
        }
        {
            List<String> order = new CopyOnWriteArrayList<>();
            CountDownLatch busy = new CountDownLatch(1);
            ThreadPoolExecutor ranked = new ThreadPoolExecutor(1, 1, 0, TimeUnit.SECONDS,
            new PriorityBlockingQueue<>());
            ranked.execute(() -> await(busy));
            ranked.execute(new Job(3, "batch", order));
            ranked.execute(new Job(1, "urgent", order));
            ranked.execute(new Job(2, "normal", order));
            busy.countDown();
            ranked.close();
            String runOrder = order.toString();             // "[urgent, normal, batch]"
            show("runOrder", runOrder);
        }
        {
            CountDownLatch slow = new CountDownLatch(1);
            ThreadPoolExecutor exporter = new ThreadPoolExecutor(1, 1, 0, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(1), new ThreadPoolExecutor.CallerRunsPolicy());
            exporter.execute(() -> await(slow));
            exporter.execute(() -> await(slow));
            AtomicReference<String> runner = new AtomicReference<>();
            exporter.execute(() -> runner.set(Thread.currentThread().getName()));
            boolean ranInCaller = runner.get().equals(Thread.currentThread().getName());   // true
            show("ranInCaller", ranInCaller);
            slow.countDown();
            exporter.close();
        }
        {
            ThreadPoolExecutor warm = new ThreadPoolExecutor(3, 6, 1, TimeUnit.MINUTES,
            new ArrayBlockingQueue<>(50));
            int started = warm.prestartAllCoreThreads();    // 3
            show("started", started);
            warm.allowCoreThreadTimeOut(true);
            boolean coreTimeout = warm.allowsCoreThreadTimeOut();   // true
            show("coreTimeout", coreTimeout);
            warm.close();
        }
        {
            CountingPool counting = new CountingPool(2);
            counting.submit(() -> 10 / 2);
            counting.submit(() -> 10 / 0);
            counting.submit(() -> 10 / 5);
            counting.close();
            int startedTasks = counting.started.get();      // 3
            show("startedTasks", startedTasks);
            int failedTasks = counting.failed.get();        // 1
            show("failedTasks", failedTasks);
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

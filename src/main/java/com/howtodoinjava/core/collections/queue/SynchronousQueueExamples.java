package com.howtodoinjava.core.collections.queue;

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
 * Examples for the tutorial "Java SynchronousQueue: Direct Handoff Between Threads".
 * https://howtodoinjava.com/java/collections/synchronousqueue-class/
 */
public class SynchronousQueueExamples {

    public static void main(String[] args) throws Exception {
        {
            SynchronousQueue<String> rides = new SynchronousQueue<>();
            boolean offered = rides.offer("ride-1");        // false (no consumer waiting)
            show("offered", offered);
            String polled = rides.poll();                   // null (no producer waiting)
            show("polled", polled);
            String peeked = rides.peek();                   // null (always)
            show("peeked", peeked);
            int size = rides.size();                        // 0 (always)
            show("size", size);
            boolean empty = rides.isEmpty();                // true (always)
            show("empty", empty);
            int capacity = rides.remainingCapacity();       // 0 (always)
            show("capacity", capacity);
        }
        {
            SynchronousQueue<String> requests = new SynchronousQueue<>();
            boolean assigned = requests.offer("ride-7", 50, TimeUnit.MILLISECONDS);   // false (no driver took it)
            show("assigned", assigned);
            String taken = requests.poll(50, TimeUnit.MILLISECONDS);                   // null (no request arrived)
            show("taken", taken);
        }
        {
            SynchronousQueue<String> dispatch = new SynchronousQueue<>();
            List<String> accepted = new CopyOnWriteArrayList<>();

            Thread driver = Thread.ofVirtual().start(() -> {
                try {
                    for (int i = 0; i < 3; i++) {
                        accepted.add(dispatch.take());      // waits for the next request
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
            dispatch.put("ride-1");                         // returns when the driver took ride-1
            dispatch.put("ride-2");
            dispatch.put("ride-3");
            driver.join();
            List<String> rideLog = List.copyOf(accepted);   // [ride-1, ride-2, ride-3]
            show("rideLog", rideLog);
        }
        {
            CountDownLatch release = new CountDownLatch(1);
            ThreadPoolExecutor pool = new ThreadPoolExecutor(1, 1, 60, TimeUnit.SECONDS, new SynchronousQueue<>());
            pool.execute(() -> {
                try {
                    release.await();                        // keeps the only worker busy
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
            try { Future<?> second = pool.submit(() -> {}); show("second", second); } catch (Throwable _t) { System.out.println("second -> " + _t); }
            release.countDown();
            pool.shutdown();
            boolean finished = pool.awaitTermination(1, TimeUnit.SECONDS);   // true
            show("finished", finished);
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

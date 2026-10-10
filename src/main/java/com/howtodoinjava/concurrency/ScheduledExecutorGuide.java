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
 * Examples for the tutorial "Java ScheduledExecutorService: Fixed Rate vs Fixed Delay".
 * https://howtodoinjava.com/java/multi-threading/scheduledexecutorservice/
 */
public class ScheduledExecutorGuide {
    static long secondsUntil(LocalTime time, ZonedDateTime now) {
        ZonedDateTime next = now.with(time);
        if (!next.isAfter(now)) {
            next = next.plusDays(1);
        }
        return Duration.between(now, next).toSeconds();
    }
    public static void main(String[] args) throws Exception {
        {
            AtomicInteger pings = new AtomicInteger();
            ScheduledFuture<String> reminder;
            ScheduledFuture<?> heartbeat;
            try (ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor()) {
                reminder = scheduler.schedule(() -> "sent", 100, TimeUnit.MILLISECONDS);
                heartbeat = scheduler.scheduleAtFixedRate(pings::incrementAndGet, 0, 20, TimeUnit.MILLISECONDS);
                reminder.get();
            }
            String result = reminder.resultNow();           // "sent"
            show("result", result);
            boolean beating = pings.get() >= 3;             // true
            show("beating", beating);
            boolean stopped = heartbeat.isCancelled();      // true
            show("stopped", stopped);
        }
        {
            ScheduledFuture<String> cartReminder;
            ScheduledFuture<?> discarded;
            try (ScheduledExecutorService mailer = Executors.newScheduledThreadPool(1)) {
                cartReminder = mailer.schedule(() -> "reminder for cart 7", 50, TimeUnit.MILLISECONDS);
                discarded = mailer.schedule(() -> System.out.println("never printed"), 30, TimeUnit.MINUTES);
                discarded.cancel(false);
            }
            String mail = cartReminder.resultNow();         // "reminder for cart 7"
            show("mail", mail);
            boolean cancelled = discarded.isCancelled();    // true
            show("cancelled", cancelled);
        }
        {
            CountDownLatch threeRuns = new CountDownLatch(3);
            ScheduledExecutorService poller = Executors.newSingleThreadScheduledExecutor();
            ScheduledFuture<?> polling = poller.scheduleWithFixedDelay(
            threeRuns::countDown, 0, 10, TimeUnit.MILLISECONDS);
            boolean reached = threeRuns.await(2, TimeUnit.SECONDS);   // true
            show("reached", reached);
            polling.cancel(false);
            poller.close();
        }
        {
            AtomicInteger refreshes = new AtomicInteger();
            ScheduledExecutorService cache = Executors.newSingleThreadScheduledExecutor();
            ScheduledFuture<?> refresh = cache.scheduleAtFixedRate(() -> {
                if (refreshes.incrementAndGet() == 3) {
                    throw new IllegalStateException("source down");
                }
            }, 0, 10, TimeUnit.MILLISECONDS);
            try { Object done = refresh.get(); show("done", done); } catch (Throwable _t) { System.out.println("done -> " + _t); }
            cache.close();
            int runs = refreshes.get();                     // 3
            show("runs", runs);
            Future.State state = refresh.state();           // FAILED
            show("state", state);
        }
        {
            AtomicInteger attempts = new AtomicInteger();
            CountDownLatch fiveRuns = new CountDownLatch(5);
            ScheduledExecutorService safeCache = Executors.newSingleThreadScheduledExecutor();
            safeCache.scheduleAtFixedRate(() -> {
                try {
                    if (attempts.incrementAndGet() == 3) {
                        throw new IllegalStateException("source down");
                    }
                } catch (RuntimeException e) {
                    System.err.println("refresh failed, retrying on the next run");
                } finally {
                    fiveRuns.countDown();
                }
            }, 0, 10, TimeUnit.MILLISECONDS);
            boolean keptRunning = fiveRuns.await(2, TimeUnit.SECONDS);   // true
            show("keptRunning", keptRunning);
            safeCache.close();
        }
        {
            ScheduledThreadPoolExecutor timeouts = new ScheduledThreadPoolExecutor(1);
            timeouts.schedule(() -> {}, 1, TimeUnit.HOURS).cancel(false);
            int keptByDefault = timeouts.getQueue().size();     // 1
            show("keptByDefault", keptByDefault);
            timeouts.setRemoveOnCancelPolicy(true);
            timeouts.schedule(() -> {}, 1, TimeUnit.HOURS).cancel(false);
            int afterPolicy = timeouts.getQueue().size();       // 1
            show("afterPolicy", afterPolicy);
            timeouts.shutdownNow();
        }
        {
            ZoneId paris = ZoneId.of("Europe/Paris");
            ZonedDateTime evening = ZonedDateTime.of(2026, 10, 10, 22, 30, 0, 0, paris);
            long untilReport = secondsUntil(LocalTime.of(2, 0), evening);       // 12600
            show("untilReport", untilReport);
            ZonedDateTime beforeDst = ZonedDateTime.of(2026, 10, 24, 3, 0, 0, 0, paris);
            long acrossDst = secondsUntil(LocalTime.of(3, 0), beforeDst);       // 90000
            show("acrossDst", acrossDst);
        }
        {
            AtomicInteger syncs = new AtomicInteger();
            CountDownLatch twoSyncs = new CountDownLatch(2);
            ExecutorService workers = Executors.newVirtualThreadPerTaskExecutor();
            ScheduledExecutorService clock = Executors.newSingleThreadScheduledExecutor();
            clock.scheduleAtFixedRate(() -> workers.submit(() -> {
                syncs.incrementAndGet();
                twoSyncs.countDown();
            }), 0, 10, TimeUnit.MILLISECONDS);
            boolean synced = twoSyncs.await(2, TimeUnit.SECONDS);   // true
            show("synced", synced);
            clock.close();
            workers.close();
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

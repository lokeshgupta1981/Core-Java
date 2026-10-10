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
 * Examples for the tutorial "Sleep vs Wait in Java: Lock, State and Wake-Up Differences".
 * https://howtodoinjava.com/java/multi-threading/sleep-vs-wait/
 */
public class SleepVsWait {
    static void pause(long ms) {
        LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(ms));
    }
    static class Kitchen {
        private boolean orderReady;

        synchronized void markReady() {
            orderReady = true;
            notifyAll();
        }

        synchronized String awaitOrder() throws InterruptedException {
            while (!orderReady) {
                wait();
            }
            return "served";
        }
    }
    public static void main(String[] args) throws Exception {
        {
            Object lock = new Object();
            synchronized (lock) {
                lock.wait(100);              // releases lock for 100 ms, then takes it back
            }
            synchronized (lock) {
                pause(100);                  // keeps lock for the whole 100 ms
            }
            boolean held = Thread.holdsLock(lock);   // false
            show("held", held);
        }
        {
            Object menu = new Object();
            Thread cook = Thread.ofPlatform().start(() -> {
                synchronized (menu) {
                    pause(300);
                }
            });
            pause(50);
            Thread guest = Thread.ofPlatform().start(() -> {
                synchronized (menu) {
                    // reads the menu
                }
            });
            pause(50);
            Thread.State cookState = cook.getState();     // TIMED_WAITING
            show("cookState", cookState);
            Thread.State guestState = guest.getState();   // BLOCKED
            show("guestState", guestState);
            cook.join();
            guest.join();
        }
        {
            Kitchen kitchen = new Kitchen();
            try (ExecutorService pool = Executors.newVirtualThreadPerTaskExecutor()) {
                Future<String> order = pool.submit(kitchen::awaitOrder);
                pause(50);
                kitchen.markReady();
                String dish = order.get(1, TimeUnit.SECONDS);   // "served"
            }
        }
        {
            Object table = new Object();
            Thread holder = Thread.ofPlatform().start(() -> {
                synchronized (table) {
                    try {
                        table.wait(300);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            });
            pause(50);
            CountDownLatch entered = new CountDownLatch(1);
            Thread visitor = Thread.ofPlatform().start(() -> {
                synchronized (table) {
                    entered.countDown();
                }
            });
            boolean visitorGotIn = entered.await(100, TimeUnit.MILLISECONDS);   // true
            show("visitorGotIn", visitorGotIn);
            Thread.State holderState = holder.getState();                       // TIMED_WAITING
            show("holderState", holderState);
            holder.join();
            visitor.join();
        }
        {
            Object bell = new Object();
            try { bell.wait(10);  } catch (Throwable _t) { System.out.println("-> " + _t); }
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

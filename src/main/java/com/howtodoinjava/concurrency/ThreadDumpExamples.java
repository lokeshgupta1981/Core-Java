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
 * Examples for the tutorial "Java Thread Dump: Capture with jcmd, jstack and Analyze It".
 * https://howtodoinjava.com/java/multi-threading/create-analyze-java-thread-dumps/
 */
public class ThreadDumpExamples {
    static long[] findDeadlocks() {
        ThreadMXBean threads = ManagementFactory.getThreadMXBean();
        long[] ids = threads.findDeadlockedThreads();
        return ids == null ? new long[0] : ids;
    }
    static void lockBoth(Object first, Object second, CountDownLatch bothLocked) {
        synchronized (first) {
            bothLocked.countDown();
            try {
                bothLocked.await();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
            synchronized (second) {
                bothLocked.countDown();             // never reached
            }
        }
    }
    public static void main(String[] args) throws Exception {
        {
            Object inventory = new Object();
            Object payment = new Object();
            CountDownLatch bothLocked = new CountDownLatch(2);
            Thread order = Thread.ofPlatform().daemon().name("order-worker")
                    .start(() -> lockBoth(inventory, payment, bothLocked));
            Thread refund = Thread.ofPlatform().daemon().name("refund-worker")
                    .start(() -> lockBoth(payment, inventory, bothLocked));
            while (order.getState() != Thread.State.BLOCKED || refund.getState() != Thread.State.BLOCKED) {
                Thread.onSpinWait();
            }
            int stuck = findDeadlocks().length;                                         // 2
            show("stuck", stuck);
            ThreadInfo info = ManagementFactory.getThreadMXBean().getThreadInfo(order.threadId());
            Thread.State state = info.getThreadState();                                 // BLOCKED
            show("state", state);
            String owner = info.getLockOwnerName();                                     // "refund-worker"
            show("owner", owner);
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

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
 * Examples for the tutorial "IllegalMonitorStateException in Java: Causes and Fixes".
 * https://howtodoinjava.com/java/multi-threading/java-illegalmonitorstateexception/
 */
public class IllegalMonitorStateExamples {
    static void notifyWrongObject(Object inbox, Object outbox) {
        synchronized (inbox) {
            outbox.notifyAll();
        }
    }
    static class StatusBoard {
        private Object lock = new Object();

        void reset() {
            synchronized (lock) {
                lock = new Object();
                lock.notifyAll();
            }
        }
    }
    static class OrderStatus {
        private final Object lock = new Object();
        private String status = "NEW";

        void update(String newStatus) {
            synchronized (lock) {
                status = newStatus;
                lock.notifyAll();
            }
        }

        String awaitStatus(String expected) throws InterruptedException {
            synchronized (lock) {
                while (!status.equals(expected)) {
                    lock.wait();
                }
                return status;
            }
        }
    }
    public static void main(String[] args) throws Exception {
        {
            Object lock = new Object();
            try { lock.notify();  } catch (Throwable _t) { System.out.println("-> " + _t); }
            synchronized (lock) {
                lock.notify();                      // works, the thread owns the monitor of lock
            }
            boolean owner = Thread.holdsLock(lock); // false, the block has ended
            show("owner", owner);
        }
        {
            Object mailbox = new Object();
            try { mailbox.wait(100);  } catch (Throwable _t) { System.out.println("-> " + _t); }
        }
        {
            Object inbox = new Object();
            Object outbox = new Object();
            try { notifyWrongObject(inbox, outbox);  } catch (Throwable _t) { System.out.println("-> " + _t); }
        }
        {
            StatusBoard board = new StatusBoard();
            try { board.reset();  } catch (Throwable _t) { System.out.println("-> " + _t); }
        }
        {
            ReentrantLock lock = new ReentrantLock();
            try { lock.unlock();  } catch (Throwable _t) { System.out.println("-> " + _t); }
            lock.lock();
            lock.unlock();
            try { lock.unlock();  } catch (Throwable _t) { System.out.println("-> " + _t); }
        }
        {
            ReentrantLock lock = new ReentrantLock();
            Condition ready = lock.newCondition();
            try { ready.signal();  } catch (Throwable _t) { System.out.println("-> " + _t); }
        }
        {
            OrderStatus order = new OrderStatus();
            CompletableFuture<String> delivery = CompletableFuture.supplyAsync(() -> {
                try {
                    return order.awaitStatus("PACKED");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return "interrupted";
                }
            });
            order.update("PACKED");
            String seen = delivery.get(1, TimeUnit.SECONDS);   // "PACKED"
            show("seen", seen);
        }
        {
            ReentrantLock lock = new ReentrantLock();
            lock.lock();
            try {
                // update the shared state
            } finally {
                lock.unlock();
            }
            boolean stillHeld = lock.isHeldByCurrentThread();   // false
            show("stillHeld", stillHeld);
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

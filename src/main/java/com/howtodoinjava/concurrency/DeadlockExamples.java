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
 * Examples for the tutorial "Deadlock in Java: Create, Detect and Resolve It".
 * https://howtodoinjava.com/java/multi-threading/writing-a-deadlock-and-resolving-in-java/
 */
public class DeadlockExamples {
    static Thread lockBoth(String name, Object first, Object second, CountDownLatch bothHoldFirst) {
        Thread thread = new Thread(() -> {
            synchronized (first) {
                bothHoldFirst.countDown();
                try {
                    bothHoldFirst.await();                  // until the other thread holds its first lock
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
                synchronized (second) {                     // never succeeds
                    System.out.println(name + " finished");
                }
            }
        }, name);
        thread.setDaemon(true);                             // lets the demo JVM exit
        thread.start();
        return thread;
    }
    static class Account {
        final int id;
        int balance;

        Account(int id, int balance) {
            this.id = id;
            this.balance = balance;
        }
    }
    static void transfer(Account from, Account to, int amount) {
        Account first = from.id < to.id ? from : to;        // always lock the lower id first
        Account second = first == from ? to : from;
        synchronized (first) {
            synchronized (second) {
                from.balance -= amount;
                to.balance += amount;
            }
        }
    }
    static boolean transferWithTimeout(ReentrantLock fromLock, ReentrantLock toLock, Runnable move) throws InterruptedException {
        for (int attempt = 0; attempt < 3; attempt++) {
            if (fromLock.tryLock(50, TimeUnit.MILLISECONDS)) {
                try {
                    if (toLock.tryLock(50, TimeUnit.MILLISECONDS)) {
                        try {
                            move.run();
                            return true;
                        } finally {
                            toLock.unlock();
                        }
                    }
                } finally {
                    fromLock.unlock();                      // released before the next attempt
                }
            }
            long backOff = ThreadLocalRandom.current().nextLong(10, 50);
            LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(backOff));
        }
        return false;
    }
    static Thread worker(String name, ReentrantLock first, ReentrantLock second, CountDownLatch bothHoldFirst, Map<String, String> results) {
        return Thread.ofPlatform().name(name).start(() -> {
            try {
                first.lockInterruptibly();
                try {
                    bothHoldFirst.countDown();
                    bothHoldFirst.await();
                    second.lockInterruptibly();             // the deadlock forms here
                    try {
                        results.put(name, "completed");
                    } finally {
                        second.unlock();
                    }
                } finally {
                    first.unlock();
                }
            } catch (InterruptedException e) {
                results.put(name, "interrupted, locks released");
            }
        });
    }
    public static void main(String[] args) throws Exception {
        {
            Object inventory = new Object();
            Object wallet = new Object();
            Runnable checkout = () -> { synchronized (inventory) { synchronized (wallet) { } } };   // inventory, then wallet
            show("checkout", checkout);
            Runnable refund = () -> { synchronized (wallet) { synchronized (inventory) { } } };     // wallet, then inventory
            show("refund", refund);
        }
        {
            Object inventory = new Object();
            Object wallet = new Object();
            CountDownLatch bothHoldFirst = new CountDownLatch(2);
            Thread checkout = lockBoth("checkout", inventory, wallet, bothHoldFirst);
            Thread refund = lockBoth("refund", wallet, inventory, bothHoldFirst);
            checkout.join(500);                                     // gives up waiting after 500 ms
            Thread.State state = checkout.getState();               // BLOCKED
            show("state", state);
        }
        {
            ThreadMXBean mxBean = ManagementFactory.getThreadMXBean();
            long[] ids = mxBean.findDeadlockedThreads();
            int deadlocked = ids == null ? 0 : ids.length;          // 2
            show("deadlocked", deadlocked);
            List<String> names = Arrays.stream(mxBean.getThreadInfo(ids)).map(ThreadInfo::getThreadName).sorted().toList();   // [checkout, refund]
            show("names", names);
        }
        {
            Account alice = new Account(1, 100);
            Account bob = new Account(2, 100);
            Thread toBob = Thread.ofPlatform().start(() -> { for (int i = 0; i < 10_000; i++) transfer(alice, bob, 1); });
            Thread toAlice = Thread.ofPlatform().start(() -> { for (int i = 0; i < 10_000; i++) transfer(bob, alice, 1); });
            toBob.join();
            toAlice.join();
            int total = alice.balance + bob.balance;                // 200
            show("total", total);
            int aliceBalance = alice.balance;                       // 100
            show("aliceBalance", aliceBalance);
        }
        {
            ReentrantLock aliceLock = new ReentrantLock();
            ReentrantLock bobLock = new ReentrantLock();
            CountDownLatch bobLocked = new CountDownLatch(1);
            CountDownLatch releaseBob = new CountDownLatch(1);
            Thread other = Thread.ofPlatform().start(() -> {
                bobLock.lock();
                try {
                    bobLocked.countDown();
                    releaseBob.await();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    bobLock.unlock();
                }
            });
            bobLocked.await();
            boolean moved = transferWithTimeout(aliceLock, bobLock, () -> {});        // false, Bob's lock stayed busy
            show("moved", moved);
            boolean aliceFree = !aliceLock.isLocked();                              // true, released after each attempt
            show("aliceFree", aliceFree);
            releaseBob.countDown();
            other.join();
            boolean movedLater = transferWithTimeout(aliceLock, bobLock, () -> {});   // true
            show("movedLater", movedLater);
        }
        {
            ReentrantLock stock = new ReentrantLock();
            ReentrantLock payment = new ReentrantLock();
            CountDownLatch bothHoldFirst = new CountDownLatch(2);
            Map<String, String> results = new ConcurrentHashMap<>();
            Thread order = worker("order", stock, payment, bothHoldFirst, results);
            Thread cancel = worker("cancel", payment, stock, bothHoldFirst, results);
            ThreadMXBean mxBean = ManagementFactory.getThreadMXBean();
            boolean cancelStuck = false;
            while (!cancelStuck) {                                  // wait until the cycle forms
                long[] ids = mxBean.findDeadlockedThreads();
                cancelStuck = ids != null && Arrays.stream(ids).anyMatch(id -> id == cancel.threadId());
                LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(10));
            }
            cancel.interrupt();                                     // break the cycle
            order.join();
            cancel.join();
            String orderResult = results.get("order");              // "completed"
            show("orderResult", orderResult);
            String cancelResult = results.get("cancel");            // "interrupted, locks released"
            show("cancelResult", cancelResult);
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

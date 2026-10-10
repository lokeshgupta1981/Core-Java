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
 * Examples for the tutorial "Object Level Lock vs Class Level Lock in Java".
 * https://howtodoinjava.com/java/multi-threading/object-vs-class-level-locking/
 */
public class ObjectVsClassLevelLock {
    static class Wallet {
        private int balance;                               // per wallet, guarded by the object lock
        private static int walletCount;                    // shared, guarded by the class lock

        synchronized void deposit(int amount) {            // locks this wallet
            balance += amount;
        }

        static synchronized void register() {              // locks Wallet.class
            walletCount++;
        }
    }
    static class MethodLockWallet {
        private int balance;

        synchronized void deposit(int amount) {            // 1. synchronized method, locks this
            balance += amount;
        }
    }

    static class ThisBlockWallet {
        private int balance;

        void deposit(int amount) {
            synchronized (this) {                          // 2. block on this
                balance += amount;
            }
        }
    }

    static class PrivateLockWallet {
        private final Object lock = new Object();
        private int balance;

        void deposit(int amount) {
            synchronized (lock) {                          // 3. private lock object
                balance += amount;
            }
        }

        int balance() {
            synchronized (lock) {
                return balance;
            }
        }
    }
    static class IdGenerator {
        private static long nextId = 1;

        static synchronized long next() {                  // 1. static synchronized method
            return nextId++;
        }

        static long nextWithBlock() {
            synchronized (IdGenerator.class) {             // 2. block on the Class object
                return nextId++;
            }
        }
    }

    static class AuditLog {
        private static final Object LOCK = new Object();   // 3. private static lock object
        private static int entries;

        static void record() {
            synchronized (LOCK) {
                entries++;
            }
        }
    }
    static class LockProbe {
        synchronized boolean instanceMethodLocks() {
            return Thread.holdsLock(this) && !Thread.holdsLock(LockProbe.class);
        }

        static synchronized boolean staticMethodLocks() {
            return Thread.holdsLock(LockProbe.class);
        }

        static synchronized void holdClassLock(CountDownLatch entered, CountDownLatch release)
        throws InterruptedException {
            entered.countDown();
            release.await();                               // keeps the class lock until released
        }
    }
    static class BrokenWallet {
        static int totalDeposits;

        synchronized boolean deposit(CountDownLatch bothInside) {   // locks this, not BrokenWallet.class
            totalDeposits++;
            bothInside.countDown();
            try {
                return bothInside.await(1, TimeUnit.SECONDS);       // true when the other thread is inside too
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        }
    }
    static void lockOn(Object lock) {
        synchronized (lock) {
            // guarded code
        }
    }
    public static void main(String[] args) throws Exception {
        {
            PrivateLockWallet shared = new PrivateLockWallet();
            Thread first = Thread.ofPlatform().start(() -> { for (int i = 0; i < 1_000; i++) shared.deposit(1); });
            Thread second = Thread.ofPlatform().start(() -> { for (int i = 0; i < 1_000; i++) shared.deposit(1); });
            first.join();
            second.join();
            int balance = shared.balance();                         // 2000
            show("balance", balance);
        }
        {
            Set<Long> ids = ConcurrentHashMap.newKeySet();
            Thread a = Thread.ofPlatform().start(() -> { for (int i = 0; i < 500; i++) ids.add(IdGenerator.next()); });
            Thread b = Thread.ofPlatform().start(() -> { for (int i = 0; i < 500; i++) ids.add(IdGenerator.nextWithBlock()); });
            a.join();
            b.join();
            int uniqueIds = ids.size();                             // 1000
            show("uniqueIds", uniqueIds);
        }
        {
            LockProbe probe = new LockProbe();
            boolean objectOnly = probe.instanceMethodLocks();       // true, this and not the class
            show("objectOnly", objectOnly);
            boolean classLock = LockProbe.staticMethodLocks();      // true
            show("classLock", classLock);
        }
        {
            CountDownLatch entered = new CountDownLatch(1);
            CountDownLatch release = new CountDownLatch(1);
            Thread auditor = Thread.ofPlatform().start(() -> {
                try {
                    LockProbe.holdClassLock(entered, release);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
            entered.await();                                        // the auditor holds LockProbe.class
            LockProbe other = new LockProbe();
            boolean notBlocked = other.instanceMethodLocks();       // true, returns at once
            show("notBlocked", notBlocked);
            boolean auditorWaiting = auditor.isAlive();             // true
            show("auditorWaiting", auditorWaiting);
            release.countDown();
            auditor.join();
        }
        {
            BrokenWallet w1 = new BrokenWallet();
            BrokenWallet w2 = new BrokenWallet();
            CountDownLatch bothInside = new CountDownLatch(2);
            Thread t1 = Thread.ofPlatform().start(() -> w1.deposit(bothInside));
            boolean overlapped = w2.deposit(bothInside);            // true, both threads inside at once
            show("overlapped", overlapped);
            t1.join();
        }
        {
            try { lockOn(null);  } catch (Throwable _t) { System.out.println("-> " + _t); }
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

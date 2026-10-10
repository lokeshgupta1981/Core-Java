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
 * Examples for the tutorial "Semaphore vs ReentrantLock in Java: Key Differences".
 * https://howtodoinjava.com/java/multi-threading/semaphore-vs-reentrantlock/
 */
public class SemaphoreVsReentrantLock {
    static class PaymentClient {
        private final Semaphore slots = new Semaphore(5);

        String pay(String booking) throws InterruptedException {
            slots.acquire();
            try {
                return booking + " paid";                   // call the payment gateway
            } finally {
                slots.release();
            }
        }
    }
    static class SeatMap {
        private final ReentrantLock lock = new ReentrantLock();
        private final Set<String> booked = new HashSet<>();

        boolean book(String seat) {
            lock.lock();
            try {
                return booked.add(seat);                    // check and add as one step
            } finally {
                lock.unlock();
            }
        }
    }
    public static void main(String[] args) throws Exception {
        {
            Semaphore paymentSlots = new Semaphore(5);              // 5 threads at a time, no owner
            show("paymentSlots", paymentSlots);
            ReentrantLock seatLock = new ReentrantLock();           // 1 thread at a time, has an owner
            show("seatLock", seatLock);
            paymentSlots.acquire();
            int freeSlots = paymentSlots.availablePermits();        // 4
            show("freeSlots", freeSlots);
            seatLock.lock();
            seatLock.lock();                                        // the owner enters again
            int holds = seatLock.getHoldCount();                    // 2
            show("holds", holds);
            seatLock.unlock();
            seatLock.unlock();
            paymentSlots.release();
            boolean locked = seatLock.isLocked();                   // false
            show("locked", locked);
        }
        {
            Semaphore binarySemaphore = new Semaphore(1);
            binarySemaphore.acquire();
            int duringWork = binarySemaphore.availablePermits();    // 0
            show("duringWork", duringWork);
            binarySemaphore.release();
            int afterWork = binarySemaphore.availablePermits();     // 1
            show("afterWork", afterWork);
        }
        {
            PaymentClient client = new PaymentClient();
            String receipt = client.pay("booking-7");               // "booking-7 paid"
            show("receipt", receipt);
        }
        {
            ReentrantLock reentrantLock = new ReentrantLock();
            reentrantLock.lock();                                   // hold count 1
            reentrantLock.lock();                                   // hold count 2
            boolean locked = reentrantLock.isLocked();              // true
            show("locked", locked);
            boolean mine = reentrantLock.isHeldByCurrentThread();   // true
            show("mine", mine);
            reentrantLock.unlock();
            int holdCount = reentrantLock.getHoldCount();           // 1
            show("holdCount", holdCount);
            boolean stillLocked = reentrantLock.isLocked();         // true
            show("stillLocked", stillLocked);
            reentrantLock.unlock();
            int finalCount = reentrantLock.getHoldCount();          // 0
            show("finalCount", finalCount);
            boolean lockedAtEnd = reentrantLock.isLocked();         // false
            show("lockedAtEnd", lockedAtEnd);
        }
        {
            SeatMap seats = new SeatMap();
            boolean firstUser = seats.book("12A");                  // true
            show("firstUser", firstUser);
            boolean secondUser = seats.book("12A");                 // false, already booked
            show("secondUser", secondUser);
        }
        {
            Semaphore single = new Semaphore(1);
            single.acquire();
            boolean again = single.tryAcquire(100, TimeUnit.MILLISECONDS);   // false, blocks itself
            show("again", again);
            ReentrantLock lock = new ReentrantLock();
            lock.lock();
            boolean relock = lock.tryLock();                        // true, the owner enters again
            show("relock", relock);
            lock.unlock();
            lock.unlock();
        }
        {
            ReentrantLock lock = new ReentrantLock();
            try { lock.unlock();  } catch (Throwable _t) { System.out.println("-> " + _t); }
            Semaphore permits = new Semaphore(0);
            Thread other = Thread.ofPlatform().start(permits::release);
            other.join();
            int count = permits.availablePermits();                 // 1, released by another thread
            show("count", count);
        }
        {
            ReentrantLock lock = new ReentrantLock();
            Condition seatFreed = lock.newCondition();
            lock.lock();
            boolean signaled = seatFreed.await(50, TimeUnit.MILLISECONDS);   // false, nobody signaled
            show("signaled", signaled);
            lock.unlock();
            boolean stillLocked = lock.isLocked();                  // false
            show("stillLocked", stillLocked);
        }
        {
            Semaphore fairSlots = new Semaphore(5, true);
            ReentrantLock fairLock = new ReentrantLock(true);
            boolean slot = fairSlots.tryAcquire(100, TimeUnit.MILLISECONDS);  // true
            show("slot", slot);
            boolean entered = fairLock.tryLock(100, TimeUnit.MILLISECONDS);   // true
            show("entered", entered);
            boolean bothFair = fairSlots.isFair() && fairLock.isFair();       // true
            show("bothFair", bothFair);
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

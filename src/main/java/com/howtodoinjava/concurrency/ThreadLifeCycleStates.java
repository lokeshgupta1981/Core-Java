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
 * Examples for the tutorial "Thread Life Cycle in Java: 6 Thread States With Examples".
 * https://howtodoinjava.com/java/multi-threading/java-thread-life-cycle-and-thread-states/
 */
public class ThreadLifeCycleStates {
    static Thread.State awaitState(Thread thread, Thread.State expected) {
        while (thread.getState() != expected && thread.isAlive()) {
            Thread.onSpinWait();
        }
        return thread.getState();
    }
    public static void main(String[] args) throws Exception {
        {
            CountDownLatch go = new CountDownLatch(1);
            Thread worker = new Thread(() -> {
                try {
                    go.await();                               // waits for the signal
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
            Thread.State before = worker.getState();          // NEW
            show("before", before);
            worker.start();
            Thread.State parked = awaitState(worker, Thread.State.WAITING);   // WAITING
            show("parked", parked);
            go.countDown();
            worker.join();
            Thread.State after = worker.getState();           // TERMINATED
            show("after", after);
        }
        {
            Thread thread = new Thread(() -> {});
            Thread.State created = thread.getState();         // NEW
            show("created", created);
            boolean alive = thread.isAlive();                 // false
            show("alive", alive);
        }
        {
            Thread busy = new Thread(() -> {
                while (!Thread.currentThread().isInterrupted()) {
                    Thread.onSpinWait();                      // keeps the CPU busy
                }
            });
            busy.start();
            Thread.State running = busy.getState();           // RUNNABLE
            show("running", running);
            busy.interrupt();
            busy.join();
        }
        {
            ServerSocket server = new ServerSocket(0);
            Thread acceptor = new Thread(() -> {
                try (Socket client = server.accept()) {
                    // read the request
                } catch (IOException e) {
                    // the server socket was closed
                }
            });
            acceptor.start();
            LockSupport.parkNanos(Duration.ofMillis(100).toNanos());
            Thread.State duringIo = acceptor.getState();      // RUNNABLE, waiting in accept()
            show("duringIo", duringIo);
            server.close();
            acceptor.join();
        }
        {
            Object invoiceLock = new Object();
            Thread waiter = new Thread(() -> {
                synchronized (invoiceLock) {
                    // update the invoice
                }
            });
            Thread.State whileHeld;
            synchronized (invoiceLock) {                      // the current thread holds the monitor
                waiter.start();
                whileHeld = awaitState(waiter, Thread.State.BLOCKED);
            }
            waiter.join();
            Thread.State blockedState = whileHeld;            // BLOCKED
            show("blockedState", blockedState);
        }
        {
            ReentrantLock lock = new ReentrantLock();
            Thread contender = new Thread(() -> {
                lock.lock();
                try {
                    // update the invoice
                } finally {
                    lock.unlock();
                }
            });
            lock.lock();
            contender.start();
            Thread.State onLock = awaitState(contender, Thread.State.WAITING);   // WAITING, not BLOCKED
            show("onLock", onLock);
            lock.unlock();
            contender.join();
        }
        {
            CountDownLatch report = new CountDownLatch(1);
            Thread poller = new Thread(() -> {
                try {
                    report.await(5, TimeUnit.SECONDS);        // waits at most 5 seconds
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
            poller.start();
            Thread.State timed = awaitState(poller, Thread.State.TIMED_WAITING);   // TIMED_WAITING
            show("timed", timed);
            report.countDown();
            poller.join();
        }
        {
            Thread failing = new Thread(() -> {
                throw new IllegalStateException("bad invoice");
            });
            failing.setUncaughtExceptionHandler((t, e) -> {});   // keeps the console quiet
            failing.start();
            failing.join();
            Thread.State ended = failing.getState();          // TERMINATED
            show("ended", ended);
            try { failing.start();  } catch (Throwable _t) { System.out.println("-> " + _t); }
        }
        {
            CountDownLatch signal = new CountDownLatch(1);
            Thread virtual = Thread.ofVirtual().start(() -> {
                try {
                    signal.await();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
            Thread.State virtualWaiting = awaitState(virtual, Thread.State.WAITING);   // WAITING
            show("virtualWaiting", virtualWaiting);
            signal.countDown();
            virtual.join();
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

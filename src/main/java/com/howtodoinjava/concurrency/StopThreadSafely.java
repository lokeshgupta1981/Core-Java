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
 * Examples for the tutorial "How to Kill a Thread in Java: interrupt(), Flags, Thread.stop".
 * https://howtodoinjava.com/java/multi-threading/killing-java-threads/
 */
public class StopThreadSafely {
    static void pause(long ms) {
        LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(ms));
    }
    static class ReportJob implements Runnable {
        private volatile boolean running = true;
        private final AtomicInteger pages = new AtomicInteger();

        void stop() {
            running = false;
        }

        int pages() {
            return pages.get();
        }

        @Override
        public void run() {
            while (running) {
                pages.incrementAndGet();    // writes one page
                pause(10);
            }
            // close the report file here
        }
    }
    static class InboxPoller implements Runnable {
        private final BlockingQueue<String> inbox;
        private final List<String> handled = new CopyOnWriteArrayList<>();

        InboxPoller(BlockingQueue<String> inbox) {
            this.inbox = inbox;
        }

        List<String> handled() {
            return handled;
        }

        @Override
        public void run() {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    String message = inbox.poll(100, TimeUnit.MILLISECONDS);
                    if (message != null) {
                        handled.add(message);
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();   // keep the flag for the caller
            }
            // close the mail connection here
        }
    }
    public static void main(String[] args) throws Exception {
        {
            Thread worker = Thread.ofPlatform().start(() -> {
                while (!Thread.currentThread().isInterrupted()) {
                    // process the next batch
                }
            });
            worker.interrupt();                     // asks the worker to stop
            worker.join();
            boolean alive = worker.isAlive();       // false
            show("alive", alive);
        }
        {
            Thread job = Thread.ofPlatform().unstarted(() -> {});
            try { job.stop();  } catch (Throwable _t) { System.out.println("-> " + _t); }
        }
        {
            ReportJob job = new ReportJob();
            Thread thread = Thread.ofPlatform().start(job);
            pause(50);
            job.stop();
            boolean stopped = thread.join(Duration.ofSeconds(1));   // true
            show("stopped", stopped);
            boolean wrotePages = job.pages() > 0;                   // true
            show("wrotePages", wrotePages);
        }
        {
            BlockingQueue<String> orders = new LinkedBlockingQueue<>();
            AtomicBoolean keepGoing = new AtomicBoolean(true);
            Thread listener = Thread.ofPlatform().start(() -> {
                try {
                    while (keepGoing.get()) {
                        orders.take();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
            pause(50);
            keepGoing.set(false);
            boolean endedByFlag = listener.join(Duration.ofMillis(200));   // false, take() still blocks
            show("endedByFlag", endedByFlag);
            listener.interrupt();
            boolean endedByInterrupt = listener.join(Duration.ofSeconds(1));   // true
            show("endedByInterrupt", endedByInterrupt);
        }
        {
            BlockingQueue<String> inbox = new LinkedBlockingQueue<>(List.of("a", "b"));
            InboxPoller poller = new InboxPoller(inbox);
            Thread sync = Thread.ofPlatform().start(poller);
            pause(50);
            sync.interrupt();
            boolean ended = sync.join(Duration.ofSeconds(1));   // true
            show("ended", ended);
            List<String> mails = poller.handled();              // [a, b]
            show("mails", mails);
        }
        {
            Thread.currentThread().interrupt();
            boolean first = Thread.interrupted();    // true, and clears the flag
            show("first", first);
            boolean second = Thread.interrupted();   // false
            show("second", second);
        }
        {
            ExecutorService pool = Executors.newSingleThreadExecutor();
            Future<?> sync = pool.submit(new InboxPoller(new LinkedBlockingQueue<>()));
            pause(50);
            boolean cancelled = sync.cancel(true);  // true, interrupts the task thread
            show("cancelled", cancelled);
            Future.State state = sync.state();      // CANCELLED
            show("state", state);
            pool.close();
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

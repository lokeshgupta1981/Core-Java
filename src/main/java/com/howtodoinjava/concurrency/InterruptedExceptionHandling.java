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
 * Examples for the tutorial "How to Handle InterruptedException in Java (with Examples)".
 * https://howtodoinjava.com/java/multi-threading/java-interruptedexception/
 */
public class InterruptedExceptionHandling {
    static Optional<String> nextMessage(BlockingQueue<String> inbox) {
        try {
            return Optional.of(inbox.take());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();     // restore the flag for the caller
            return Optional.empty();
        }
    }
    static String awaitReply(BlockingQueue<String> replies, long timeoutMs) throws InterruptedException {
        String reply = replies.poll(timeoutMs, TimeUnit.MILLISECONDS);
        return reply == null ? "no reply" : reply;
    }
    static Runnable mailWorker(BlockingQueue<String> outbox, List<String> sent, CountDownLatch progress) {
        return () -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    String mail = outbox.take();
                    sent.add(mail);
                    progress.countDown();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();     // the loop condition sees it and exits
                }
            }
        };
    }
    static void swallowing(BlockingQueue<String> outbox) {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                outbox.take();
            } catch (InterruptedException e) {
                System.err.println("interrupted");      // wrong: flag lost, loop continues
            }
        }
    }
    static void spinning(BlockingQueue<String> outbox) {
        while (true) {
            try {
                outbox.take();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();     // wrong: no exit, take() throws again
            }
        }
    }
    static String wrapping(BlockingQueue<String> outbox) {
        try {
            return outbox.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);              // wrong: flag lost
        }
    }
    static Callable<Integer> exportRows(BlockingQueue<String> rows) {
        return () -> {
            int written = 0;
            while (true) {
                rows.take();                            // throws when the user cancels
                written++;
            }
        };
    }
    public static void main(String[] args) throws Exception {
        {
            Thread.currentThread().interrupt();                                     // someone asks us to stop
            Optional<String> message = nextMessage(new LinkedBlockingQueue<>());    // Optional.empty
            show("message", message);
            boolean stillFlagged = Thread.interrupted();                            // true, and clears the flag
            show("stillFlagged", stillFlagged);
        }
        {
            Thread self = Thread.currentThread();
            self.interrupt();
            boolean first = self.isInterrupted();       // true
            show("first", first);
            boolean second = Thread.interrupted();      // true, and clears the flag
            show("second", second);
            boolean third = self.isInterrupted();       // false
            show("third", third);
        }
        {
            BlockingQueue<String> replies = new LinkedBlockingQueue<>(List.of("ok"));
            String answer = awaitReply(replies, 100);           // "ok"
            show("answer", answer);
            String none = awaitReply(replies, 50);              // "no reply"
            show("none", none);
            Thread.currentThread().interrupt();
            try { String stopped = awaitReply(replies, 50); show("stopped", stopped); } catch (Throwable _t) { System.out.println("stopped -> " + _t); }
            boolean flag = Thread.interrupted();                // false, poll() cleared it
            show("flag", flag);
        }
        {
            BlockingQueue<String> outbox = new LinkedBlockingQueue<>(List.of("welcome", "invoice"));
            List<String> sent = new CopyOnWriteArrayList<>();
            CountDownLatch progress = new CountDownLatch(2);
            Thread worker = Thread.ofPlatform().name("mailer").start(mailWorker(outbox, sent, progress));
            progress.await();                                   // both mails are sent
            worker.interrupt();                                 // take() throws, the loop ends
            worker.join();
            boolean alive = worker.isAlive();                   // false
            show("alive", alive);
            List<String> delivered = List.copyOf(sent);         // [welcome, invoice]
            show("delivered", delivered);
        }
        {
            Future<Integer> export;
            try (ExecutorService pool = Executors.newVirtualThreadPerTaskExecutor()) {
                export = pool.submit(exportRows(new LinkedBlockingQueue<>()));
                export.cancel(true);                            // user clicks Cancel
            }
            boolean cancelled = export.isCancelled();           // true
            show("cancelled", cancelled);
            Future.State state = export.state();                // CANCELLED
            show("state", state);
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

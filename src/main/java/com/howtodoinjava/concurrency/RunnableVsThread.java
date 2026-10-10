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
 * Examples for the tutorial "Runnable vs Thread in Java: Differences and When to Use Each".
 * https://howtodoinjava.com/java/multi-threading/java-runnable-vs-thread/
 */
public class RunnableVsThread {
    static class ReceiptThread extends Thread {
        private final String orderId;

        ReceiptThread(String orderId) {
            super("receipt-" + orderId);
            this.orderId = orderId;
        }

        @Override
        public void run() {
            ReceiptLog.record(orderId + " sent by " + getName());
        }
    }
    static class ReceiptLog {
        static final List<String> LINES = new CopyOnWriteArrayList<>();

        static void record(String line) {
            LINES.add(line);
        }
    }
    static class SendReceiptTask implements Runnable {
        private final String orderId;

        SendReceiptTask(String orderId) {
            this.orderId = orderId;
        }

        @Override
        public void run() {
            ReceiptLog.record(orderId + " sent by " + Thread.currentThread().getName());
        }
    }
    static class PageViewCounter implements Runnable {
        final AtomicInteger views = new AtomicInteger();

        @Override
        public void run() {
            views.incrementAndGet();
        }
    }
    public static void main(String[] args) throws Exception {
        {
            AtomicInteger sent = new AtomicInteger();
            Runnable sendReceipt = sent::incrementAndGet;     // a task, no thread yet
            show("sendReceipt", sendReceipt);

            Thread worker = new Thread(sendReceipt);          // Runnable handed to a Thread
            show("worker", worker);
            worker.start();
            worker.join();

            Thread subclass = new Thread() {                  // Thread subclass overriding run()
                @Override
                public void run() {
                    sent.incrementAndGet();
                }
            };
            subclass.start();
            subclass.join();
            int total = sent.get();                           // 2
            show("total", total);
        }
        {
            ReceiptThread thread = new ReceiptThread("A7");
            thread.start();
            thread.join();
            String line = ReceiptLog.LINES.getLast();         // "A7 sent by receipt-A7"
            show("line", line);
        }
        {
            Thread platform = new Thread(new SendReceiptTask("B2"), "mailer");
            platform.start();
            platform.join();
            String first = ReceiptLog.LINES.getLast();        // "B2 sent by mailer"
            show("first", first);

            Thread virtual = Thread.ofVirtual().name("vmail").start(new SendReceiptTask("C3"));
            virtual.join();
            String second = ReceiptLog.LINES.getLast();       // "C3 sent by vmail"
            show("second", second);
        }
        {
            PageViewCounter counter = new PageViewCounter();
            List<Thread> threads = List.of(new Thread(counter), new Thread(counter), new Thread(counter));
            for (Thread t : threads) {
                t.start();
            }
            for (Thread t : threads) {
                t.join();
            }
            int views = counter.views.get();                  // 3, one shared task
            show("views", views);
        }
        {
            int before = ReceiptLog.LINES.size();
            try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
                executor.submit(new SendReceiptTask("D4"));
                executor.submit(new SendReceiptTask("E5"));
            }                                                 // close() waits for both tasks
            int added = ReceiptLog.LINES.size() - before;     // 2
            show("added", added);
        }
        {
            SendReceiptTask task = new SendReceiptTask("F6");
            task.run();                                       // runs on the calling thread
            String inTest = ReceiptLog.LINES.getLast();       // "F6 sent by main"
            show("inTest", inTest);
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

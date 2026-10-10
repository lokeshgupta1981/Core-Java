package com.howtodoinjava.core.collections.queue;

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
 * Examples for the tutorial "Java TransferQueue and LinkedTransferQueue with Examples".
 * https://howtodoinjava.com/java/collections/transferqueue-linkedtransferqueue/
 */
public class TransferQueueExamples {

    public static void main(String[] args) throws Exception {
        {
            LinkedTransferQueue<String> messages = new LinkedTransferQueue<>();
            messages.put("order-1");                        // queued, returns at once
            boolean handed = messages.tryTransfer("order-2");   // false (no consumer waiting, not queued)
            show("handed", handed);
            boolean timed = messages.tryTransfer("order-3", 50, TimeUnit.MILLISECONDS);   // false after 50 ms, not queued
            show("timed", timed);
            int queued = messages.size();                   // 1 (only order-1)
            show("queued", queued);
            boolean waiting = messages.hasWaitingConsumer();    // false
            show("waiting", waiting);
            String head = messages.poll();                  // "order-1"
            show("head", head);
        }
        {
            LinkedTransferQueue<String> outbox = new LinkedTransferQueue<>(List.of("order-1"));
            boolean offered = outbox.offer("order-2");      // true
            show("offered", offered);
            String next = outbox.poll();                    // "order-1"
            show("next", next);
            String last = outbox.poll();                    // "order-2"
            show("last", last);
            String none = outbox.poll();                    // null
            show("none", none);
        }
        {
            LinkedTransferQueue<String> handoff = new LinkedTransferQueue<>();
            List<String> notified = new CopyOnWriteArrayList<>();

            Thread worker = Thread.ofVirtual().start(() -> {
                try {
                    for (int i = 0; i < 3; i++) {
                        notified.add(handoff.take());       // waits for the next order
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
            handoff.transfer("order-1");                    // returns after the worker took order-1
            handoff.transfer("order-2");
            handoff.transfer("order-3");
            int afterTransfers = handoff.size();            // 0 (every order was received)
            show("afterTransfers", afterTransfers);
            worker.join();
            List<String> received = List.copyOf(notified);  // [order-1, order-2, order-3]
            show("received", received);
        }
        {
            LinkedTransferQueue<String> direct = new LinkedTransferQueue<>();
            Thread listener = Thread.ofVirtual().start(() -> {
                try {
                    direct.take();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
            while (!direct.hasWaitingConsumer()) {
                Thread.onSpinWait();                        // wait until the listener blocks in take()
            }
            int consumers = direct.getWaitingConsumerCount();   // 1
            show("consumers", consumers);
            boolean delivered = direct.tryTransfer("order-9");  // true
            show("delivered", delivered);
            listener.join();
        }
        {
            TransferQueue<String> notifications = new LinkedTransferQueue<>();
            List<String> retryLater = new ArrayList<>();
            String confirmation = "order-42";
            boolean pickedUp = notifications.tryTransfer(confirmation, 200, TimeUnit.MILLISECONDS);   // false (no worker running)
            show("pickedUp", pickedUp);
            if (!pickedUp) {
                retryLater.add(confirmation);
            }
            List<String> retries = retryLater;              // [order-42]
            show("retries", retries);
            int stuckInQueue = notifications.size();        // 0
            show("stuckInQueue", stuckInQueue);
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

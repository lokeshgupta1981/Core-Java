package com.howtodoinjava.core.keywords;

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
 * Examples for the tutorial "Java synchronized Keyword".
 * https://howtodoinjava.com/java/keywords/java-synchronized/
 */
public class SynchronizedKeyword {
    static class Counter {
        private int count;

        synchronized void increment() {
            count++;                         // read, add, write as one step
        }

        synchronized int get() {
            return count;
        }
    }
    static class OrderBook {
        private final Object lock = new Object();
        private final List<String> orders = new ArrayList<>();

        void add(String rawOrder) {
            String order = rawOrder.trim().toUpperCase();   // no shared state, no lock
            synchronized (lock) {
                orders.add(order);
            }
        }

        int size() {
            synchronized (lock) {
                return orders.size();
            }
        }
    }
    static class Account {
        private long balance;

        synchronized void deposit(long amount) {
            balance += amount;
        }

        synchronized void depositTwice(long amount) {
            deposit(amount);                 // same thread takes the lock again
            deposit(amount);
        }

        synchronized long balance() {
            return balance;
        }
    }
    static record Wallet(int id) {}
    static String transfer(Wallet from, Wallet to) {
        Wallet first = from.id() < to.id() ? from : to;
        Wallet second = first == from ? to : from;
        synchronized (first) {
            synchronized (second) {
                return "moved from " + from.id() + " to " + to.id();
            }
        }
    }
    public static void main(String[] args) throws Exception {
        {
            Counter counter = new Counter();
            Thread t1 = Thread.ofPlatform().start(() -> { for (int i = 0; i < 10_000; i++) counter.increment(); });
            Thread t2 = Thread.ofPlatform().start(() -> { for (int i = 0; i < 10_000; i++) counter.increment(); });
            t1.join();
            t2.join();
            int total = counter.get();              // 20000, every time
            show("total", total);
        }
        {
            OrderBook book = new OrderBook();
            book.add("  buy 10 acme ");
            int orderCount = book.size();           // 1
            show("orderCount", orderCount);
        }
        {
            Account account = new Account();
            account.depositTwice(50);
            long balance = account.balance();       // 100
            show("balance", balance);
        }
        {
            String move = transfer(new Wallet(2), new Wallet(1));   // "moved from 2 to 1"
            show("move", move);
        }
        {
            Counter shared = new Counter();
            try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
                for (int i = 0; i < 1_000; i++) {
                    executor.submit(shared::increment);
                }
            }
            int virtualTotal = shared.get();        // 1000
            show("virtualTotal", virtualTotal);
        }
        {
            AtomicInteger hits = new AtomicInteger();
            hits.incrementAndGet();
            hits.incrementAndGet();
            int hitCount = hits.get();              // 2
            show("hitCount", hitCount);
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

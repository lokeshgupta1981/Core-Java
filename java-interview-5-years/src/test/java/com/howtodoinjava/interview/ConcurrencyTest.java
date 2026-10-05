package com.howtodoinjava.interview;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;
import org.junit.jupiter.api.Test;

class ConcurrencyTest {

  static class Counter {
    private volatile boolean running = true;   // visibility: readers see the latest write
    private volatile int hits = 0;             // hits++ is still read-modify-write, not atomic
    private final AtomicInteger safeHits = new AtomicInteger();
  }

  @Test
  void volatileIsNotAtomic() throws Exception {
    Counter counter = new Counter();
    java.util.concurrent.CountDownLatch go = new java.util.concurrent.CountDownLatch(1);
    Runnable work = () -> {
      try {
        go.await();
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
      for (int i = 0; i < 1_000_000; i++) {
        counter.hits++;
        counter.safeHits.incrementAndGet();
      }
    };
    Thread t1 = Thread.ofPlatform().start(work);
    Thread t2 = Thread.ofPlatform().start(work);
    go.countDown();
    t1.join();
    t2.join();
    counter.running = false;
    System.out.println("volatile hits = " + counter.hits + ", AtomicInteger = " + counter.safeHits.get());
    assertThat(counter.hits).isLessThanOrEqualTo(2_000_000);
    assertThat(counter.safeHits.get()).isEqualTo(2_000_000);
    assertThat(counter.running).isFalse();
  }

  @Test
  void tryLockWithTimeout() throws InterruptedException {
    int balance = 100;
    ReentrantLock lock = new ReentrantLock();
    boolean acquired = lock.tryLock(500, TimeUnit.MILLISECONDS);   // true, nobody holds the lock
    if (acquired) {
      try {
        balance += 10;                                             // balance = 110
      } finally {
        lock.unlock();
      }
    }
    assertThat(acquired).isTrue();
    assertThat(balance).isEqualTo(110);

    // another thread holds the lock: tryLock gives up after the timeout
    lock.lock();
    try {
      AtomicReference<Boolean> other = new AtomicReference<>();
      Thread t = Thread.ofPlatform().start(() -> {
        try {
          other.set(lock.tryLock(100, TimeUnit.MILLISECONDS));
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
        }
      });
      t.join();
      assertThat(other.get()).isFalse();
    } finally {
      lock.unlock();
    }
  }

  @Test
  void executorCloseAndShutdown() throws Exception {
    try (ExecutorService pool = Executors.newFixedThreadPool(2)) {
      Future<Integer> future = pool.submit(() -> 6 * 7);
      int answer = future.get(1, TimeUnit.SECONDS);      // 42
      assertThat(answer).isEqualTo(42);
    }                                                    // close(): shutdown() + wait for tasks

    ExecutorService workers = Executors.newFixedThreadPool(2);
    workers.submit(() -> 6 * 7);
    workers.shutdown();                                  // no new tasks
    if (!workers.awaitTermination(10, TimeUnit.SECONDS)) {
      workers.shutdownNow();                             // interrupt the stragglers
    }
    assertThat(workers.isTerminated()).isTrue();
  }

  @Test
  void completableFuture() {
    try (ExecutorService pool = Executors.newFixedThreadPool(2)) {
      CompletableFuture<Integer> price = CompletableFuture.supplyAsync(() -> 100, pool);
      CompletableFuture<Integer> tax = CompletableFuture.supplyAsync(() -> 18, pool);
      int total = price.thenCombine(tax, Integer::sum)
          .orTimeout(2, TimeUnit.SECONDS)
          .join();                                       // 118

      int fallback = CompletableFuture.supplyAsync(() -> Integer.parseInt("abc"), pool)
          .exceptionally(ex -> 0)
          .join();                                       // 0

      AtomicReference<Throwable> seen = new AtomicReference<>();
      CompletableFuture.supplyAsync(() -> Integer.parseInt("abc"), pool)
          .exceptionally(ex -> {
            seen.set(ex);
            return 0;
          })
          .join();

      assertThat(total).isEqualTo(118);
      assertThat(fallback).isZero();
      assertThat(seen.get()).isInstanceOf(CompletionException.class)
          .hasCauseInstanceOf(NumberFormatException.class);
      System.out.println("exceptionally() got " + seen.get());
    }
  }
}

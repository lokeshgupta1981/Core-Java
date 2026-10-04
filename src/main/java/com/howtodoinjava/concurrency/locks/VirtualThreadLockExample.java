package com.howtodoinjava.concurrency.locks;

import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 200 virtual threads each block for 100 ms while holding a lock: once inside synchronized,
 * once inside a ReentrantLock. Each task uses its own lock, so there is no contention.
 * On Java 21 to 23 a virtual thread that blocks inside synchronized is pinned to its carrier thread,
 * so the synchronized run is much slower. On Java 24 and later (JEP 491) both runs take about the same time.
 *
 * <p>Run: mvn -q compile exec:java -Dexec.mainClass=com.howtodoinjava.concurrency.locks.VirtualThreadLockExample
 * <p>Run the class with -Djdk.virtualThreadScheduler.parallelism=2 to fix the number of carrier threads.
 */
public class VirtualThreadLockExample {

  static final int TASKS = 200;

  static long runSynchronized() {
    long start = System.nanoTime();
    try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
      for (int i = 0; i < TASKS; i++) {
        Object monitor = new Object();
        executor.submit(() -> {
          synchronized (monitor) {
            TimeUnit.MILLISECONDS.sleep(100);       // blocking while holding the monitor
          }
          return null;
        });
      }
    }
    return Duration.ofNanos(System.nanoTime() - start).toMillis();
  }

  static long runReentrantLock() {
    long start = System.nanoTime();
    try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
      for (int i = 0; i < TASKS; i++) {
        ReentrantLock lock = new ReentrantLock();
        executor.submit(() -> {
          lock.lock();
          try {
            TimeUnit.MILLISECONDS.sleep(100);       // blocking while holding the lock
          } finally {
            lock.unlock();
          }
          return null;
        });
      }
    }
    return Duration.ofNanos(System.nanoTime() - start).toMillis();
  }

  public static void main(String[] args) {
    System.out.println("Java " + Runtime.version().feature());
    runSynchronized();                              // warm-up
    runReentrantLock();
    System.out.println("synchronized : " + runSynchronized() + " ms");
    System.out.println("ReentrantLock: " + runReentrantLock() + " ms");
  }
}

package com.howtodoinjava.concurrency.locks;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Two threads add 1,000,000 page views each to a shared counter. Without a lock some updates are lost;
 * volatile does not help because views++ is still three steps. With a ReentrantLock and the lock()/try/finally/unlock() pattern the count is always 2,000,000.
 *
 * <p>Run: mvn -q compile exec:java -Dexec.mainClass=com.howtodoinjava.concurrency.locks.LockCounterExample
 */
public class LockCounterExample {

  private final Lock lock = new ReentrantLock();
  private volatile int views = 0;      // volatile does not make views++ safe

  void addViewUnsafe() {
    views++;                    // read, add 1, write back: three steps
  }

  void addView() {
    lock.lock();
    try {
      views++;
    } finally {
      lock.unlock();
    }
  }

  static int run(boolean useLock) throws InterruptedException {
    LockCounterExample counter = new LockCounterExample();
    CountDownLatch startGate = new CountDownLatch(1);    // both threads start counting together
    Runnable task = () -> {
      try {
        startGate.await();
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        return;
      }
      for (int i = 0; i < 1_000_000; i++) {
        if (useLock) {
          counter.addView();
        } else {
          counter.addViewUnsafe();
        }
      }
    };
    Thread t1 = new Thread(task, "t1");
    Thread t2 = new Thread(task, "t2");
    t1.start();
    t2.start();
    startGate.countDown();
    t1.join();
    t2.join();
    return counter.views;
  }

  public static void main(String[] args) throws InterruptedException {
    for (int round = 1; round <= 5; round++) {
      System.out.println("Without lock, round " + round + ": " + run(false));
    }
    for (int round = 1; round <= 5; round++) {
      System.out.println("With lock, round " + round + ": " + run(true));
    }
  }
}

package com.howtodoinjava.concurrency.locks;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * tryLock() returns false at once when another thread holds the lock; tryLock(timeout) waits up to the
 * timeout. A "booking" thread holds the seat lock until the main thread releases a latch.
 *
 * <p>Run: mvn -q compile exec:java -Dexec.mainClass=com.howtodoinjava.concurrency.locks.TryLockExample
 */
public class TryLockExample {

  public static void main(String[] args) throws InterruptedException {
    Lock seatLock = new ReentrantLock();
    CountDownLatch locked = new CountDownLatch(1);
    CountDownLatch release = new CountDownLatch(1);

    Thread booking = new Thread(() -> {
      seatLock.lock();
      try {
        locked.countDown();
        release.await();                          // hold the lock until main says so
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      } finally {
        seatLock.unlock();
      }
    }, "booking");
    booking.start();
    locked.await();

    // 1. tryLock() without timeout: no waiting
    boolean got = seatLock.tryLock();
    System.out.println("tryLock(): " + got);

    // 2. tryLock(timeout): waits up to 500 ms
    long start = System.nanoTime();
    got = seatLock.tryLock(500, TimeUnit.MILLISECONDS);
    long waited = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
    System.out.println("tryLock(500 ms): " + got + " after about " + (waited / 100 * 100) + " ms");

    // 3. The other thread releases the lock; now tryLock(timeout) succeeds
    release.countDown();
    if (seatLock.tryLock(1, TimeUnit.SECONDS)) {
      try {
        System.out.println("tryLock(1 s) after release: true, seat booked by main");
      } finally {
        seatLock.unlock();
      }
    } else {
      System.out.println("Seat is busy, try again later");
    }
    booking.join();
  }
}

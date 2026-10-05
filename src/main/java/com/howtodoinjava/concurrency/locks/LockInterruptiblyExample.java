package com.howtodoinjava.concurrency.locks;

import java.util.concurrent.locks.ReentrantLock;

/**
 * A thread waiting in lockInterruptibly() stops waiting when it is interrupted and gets an
 * InterruptedException. A thread waiting in lock() would keep waiting.
 *
 * <p>Run: mvn -q compile exec:java -Dexec.mainClass=com.howtodoinjava.concurrency.locks.LockInterruptiblyExample
 */
public class LockInterruptiblyExample {

  public static void main(String[] args) throws InterruptedException {
    ReentrantLock reportLock = new ReentrantLock();
    reportLock.lock();                             // main holds the lock

    Thread worker = new Thread(() -> {
      try {
        reportLock.lockInterruptibly();
        try {
          System.out.println("worker: building the report");
        } finally {
          reportLock.unlock();
        }
      } catch (InterruptedException e) {
        System.out.println("worker: interrupted while waiting for the lock, giving up");
      }
    }, "worker");
    worker.start();

    while (!reportLock.hasQueuedThread(worker)) {  // wait until worker is waiting for the lock
      Thread.onSpinWait();
    }
    System.out.println("main: worker is waiting = " + reportLock.hasQueuedThread(worker));
    worker.interrupt();
    worker.join();
    reportLock.unlock();
    System.out.println("main: worker state = " + worker.getState());
  }
}

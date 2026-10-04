package com.howtodoinjava.concurrency.locks;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Threads A, B and C wait for a lock that main holds. Then main releases the lock and at once asks
 * for it again. A fair lock puts main at the end of the queue, so the order is A B C main.
 * The default (unfair) lock lets main take the lock back before the waiting threads wake up,
 * so the order is usually main A B C.
 *
 * <p>Run: mvn -q compile exec:java -Dexec.mainClass=com.howtodoinjava.concurrency.locks.FairLockExample
 */
public class FairLockExample {

  static String run(boolean fair) throws InterruptedException {
    ReentrantLock lock = new ReentrantLock(fair);
    List<String> owners = new CopyOnWriteArrayList<>();
    Thread[] threads = new Thread[3];

    lock.lock();                                   // main holds the lock while A, B, C line up
    String[] names = {"A", "B", "C"};
    for (int i = 0; i < 3; i++) {
      String name = names[i];
      threads[i] = new Thread(() -> {
        lock.lock();
        try {
          owners.add(name);
        } finally {
          lock.unlock();
        }
      }, name);
      threads[i].start();
      while (!lock.hasQueuedThread(threads[i])) {
        Thread.onSpinWait();
      }
    }

    lock.unlock();                                 // release ...
    lock.lock();                                   // ... and ask again at once
    try {
      owners.add("main");
    } finally {
      lock.unlock();
    }
    for (Thread t : threads) {
      t.join();
    }
    return String.join(" ", owners);
  }

  public static void main(String[] args) throws InterruptedException {
    System.out.println("fair=true : " + run(true));
    System.out.println("fair=false: " + run(false));
  }
}

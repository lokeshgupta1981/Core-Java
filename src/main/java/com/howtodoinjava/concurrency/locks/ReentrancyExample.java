package com.howtodoinjava.concurrency.locks;

import java.util.concurrent.locks.ReentrantLock;

/**
 * A thread that already holds a ReentrantLock can lock it again. getHoldCount() shows how many
 * times the current thread holds the lock, and every lock() needs its own unlock().
 * Calling unlock() without holding the lock throws IllegalMonitorStateException.
 *
 * <p>Run: mvn -q compile exec:java -Dexec.mainClass=com.howtodoinjava.concurrency.locks.ReentrancyExample
 */
public class ReentrancyExample {

  private final ReentrantLock lock = new ReentrantLock();

  void saveCart() {
    lock.lock();
    try {
      System.out.println("saveCart() hold count: " + lock.getHoldCount());
      saveItem();                                  // locks the same lock again
    } finally {
      lock.unlock();
    }
  }

  void saveItem() {
    lock.lock();
    try {
      System.out.println("saveItem() hold count: " + lock.getHoldCount());
    } finally {
      lock.unlock();
    }
  }

  public static void main(String[] args) {
    ReentrancyExample example = new ReentrancyExample();
    example.saveCart();
    System.out.println("After saveCart() hold count: " + example.lock.getHoldCount());
    System.out.println("isLocked: " + example.lock.isLocked());

    try {
      example.lock.unlock();                       // not held by this thread
    } catch (IllegalMonitorStateException e) {
      System.out.println("unlock() without lock(): " + e);
    }
  }
}

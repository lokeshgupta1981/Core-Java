package com.howtodoinjava.concurrency.locks;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * A price list guarded by a ReentrantReadWriteLock. Many threads can hold the read lock at the same
 * time; the write lock is exclusive. Three readers meet at a barrier while all of them hold the read
 * lock, which proves they are inside together. While they are inside, the write lock is not available.
 *
 * <p>Run: mvn -q compile exec:java -Dexec.mainClass=com.howtodoinjava.concurrency.locks.ReadWriteLockExample
 */
public class ReadWriteLockExample {

  static class PriceList {
    private final Map<String, Integer> prices = new HashMap<>();
    private final ReadWriteLock rwLock = new ReentrantReadWriteLock();
    private final Lock readLock = rwLock.readLock();
    private final Lock writeLock = rwLock.writeLock();

    Integer get(String item) {
      readLock.lock();
      try {
        return prices.get(item);
      } finally {
        readLock.unlock();
      }
    }

    void put(String item, int price) {
      writeLock.lock();
      try {
        prices.put(item, price);
      } finally {
        writeLock.unlock();
      }
    }
  }

  public static void main(String[] args) throws Exception {
    PriceList list = new PriceList();
    list.put("apple", 5);
    list.put("banana", 3);
    System.out.println("apple = " + list.get("apple"));

    // Three readers hold the read lock at the same time
    ReentrantReadWriteLock rw = (ReentrantReadWriteLock) list.rwLock;
    CyclicBarrier allInside = new CyclicBarrier(4);   // 3 readers + main
    CyclicBarrier done = new CyclicBarrier(4);
    for (int i = 1; i <= 3; i++) {
      new Thread(() -> {
        list.readLock.lock();
        try {
          allInside.await(1, TimeUnit.SECONDS);       // times out if readers block each other
          done.await(1, TimeUnit.SECONDS);
        } catch (Exception e) {
          System.out.println("reader failed: " + e);
        } finally {
          list.readLock.unlock();
        }
      }, "reader-" + i).start();
    }
    allInside.await(1, TimeUnit.SECONDS);
    System.out.println("Readers holding the read lock: " + rw.getReadLockCount());
    System.out.println("Write lock while reading, tryLock(): " + list.writeLock.tryLock());
    done.await(1, TimeUnit.SECONDS);

    // After the readers finish, the writer gets the lock
    while (rw.getReadLockCount() > 0) {
      Thread.onSpinWait();
    }
    boolean gotWrite = list.writeLock.tryLock();
    System.out.println("Write lock after readers left, tryLock(): " + gotWrite);
    if (gotWrite) {
      list.writeLock.unlock();
    }
  }
}

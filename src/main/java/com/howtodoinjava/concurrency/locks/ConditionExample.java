package com.howtodoinjava.concurrency.locks;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A small producer-consumer queue with one lock and two Conditions. The producer waits on notFull
 * when 2 orders are queued; the consumer waits on notEmpty when the queue is empty.
 *
 * <p>Run: mvn -q compile exec:java -Dexec.mainClass=com.howtodoinjava.concurrency.locks.ConditionExample
 */
public class ConditionExample {

  static class OrderQueue {
    private final Deque<String> orders = new ArrayDeque<>();
    private final int capacity;
    private final Lock lock = new ReentrantLock();
    private final Condition notFull = lock.newCondition();
    private final Condition notEmpty = lock.newCondition();

    OrderQueue(int capacity) {
      this.capacity = capacity;
    }

    void put(String order) throws InterruptedException {
      lock.lock();
      try {
        while (orders.size() == capacity) {
          System.out.println("producer: queue full, waiting");
          notFull.await();                        // releases the lock while waiting
        }
        orders.addLast(order);
        System.out.println("producer: added " + order);
        notEmpty.signal();                        // wake one waiting consumer
      } finally {
        lock.unlock();
      }
    }

    String take() throws InterruptedException {
      lock.lock();
      try {
        while (orders.isEmpty()) {
          notEmpty.await();
        }
        String order = orders.removeFirst();
        notFull.signal();                         // wake the waiting producer
        return order;
      } finally {
        lock.unlock();
      }
    }
  }

  public static void main(String[] args) throws InterruptedException {
    OrderQueue queue = new OrderQueue(2);

    Thread producer = new Thread(() -> {
      try {
        for (int i = 1; i <= 4; i++) {
          queue.put("pizza-" + i);
        }
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    }, "producer");
    producer.start();

    // Wait until the producer filled the queue and is waiting
    while (producer.getState() != Thread.State.WAITING) {
      Thread.onSpinWait();
    }
    for (int i = 1; i <= 4; i++) {
      String order = queue.take();
      System.out.println("consumer: took " + order);
    }
    producer.join();
  }
}

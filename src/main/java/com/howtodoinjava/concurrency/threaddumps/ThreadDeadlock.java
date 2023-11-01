package com.howtodoinjava.concurrency.threaddumps;

public class ThreadDeadlock {

  private static final Object lock1 = new Object();
  private static final Object lock2 = new Object();

  public static void main(String[] args) {
    Thread thread1 = new Thread(() -> {
      synchronized (lock1) {
        System.out.println("Thread1: Holding lock1...");
        try {
          Thread.sleep(100);
        } catch (InterruptedException ignored) {
        }
        System.out.println("Thread1: Waiting for lock2...");
        synchronized (lock2) {
          System.out.println("Thread1: Acquired lock2.");
        }
      }
    });

    Thread thread2 = new Thread(() -> {
      synchronized (lock2) {
        System.out.println("Thread2: Holding lock2...");
        try {
          Thread.sleep(100);
        } catch (InterruptedException ignored) {
        }
        System.out.println("Thread2: Waiting for lock1...");
        synchronized (lock1) {
          System.out.println("Thread2: Acquired lock1.");
        }
      }
    });

    thread1.start();
    thread2.start();

    try {
      thread1.join();
      thread2.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
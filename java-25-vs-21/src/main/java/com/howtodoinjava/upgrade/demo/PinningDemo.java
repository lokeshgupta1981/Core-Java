package com.howtodoinjava.upgrade.demo;

import java.time.Duration;
import java.util.concurrent.Executors;

/**
 * 100 virtual threads that each hold a monitor while sleeping for 100 ms.
 * Run with -Djdk.virtualThreadScheduler.parallelism=4 on JDK 21 and JDK 25.
 * Compiled with --release 21 so the same class file runs on both JDKs.
 */
public class PinningDemo {

  public static void main(String[] args) {
    int tasks = 100;
    long start = System.nanoTime();
    try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
      for (int i = 0; i < tasks; i++) {
        Object lock = new Object();
        executor.submit(() -> {
          synchronized (lock) {
            pause(Duration.ofMillis(100));
          }
        });
      }
    }
    System.out.println("elapsed ms = " + (System.nanoTime() - start) / 1_000_000);
  }

  private static void pause(Duration duration) {
    try {
      Thread.sleep(duration);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }
}

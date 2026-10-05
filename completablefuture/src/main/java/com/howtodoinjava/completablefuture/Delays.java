package com.howtodoinjava.completablefuture;

import java.time.Duration;
import java.util.concurrent.locks.LockSupport;

/**
 * Simulates a slow call, such as a database query or an HTTP request.
 */
public final class Delays {

  private Delays() {
  }

  public static void simulateDelay(Duration duration) {
    long deadline = System.nanoTime() + duration.toNanos();
    long remaining;
    while ((remaining = deadline - System.nanoTime()) > 0) {
      LockSupport.parkNanos(remaining);
      if (Thread.currentThread().isInterrupted()) {
        return;
      }
    }
  }
}

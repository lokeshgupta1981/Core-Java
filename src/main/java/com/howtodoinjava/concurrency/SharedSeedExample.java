package com.howtodoinjava.concurrency;

import java.security.SecureRandom;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class SharedSeedExample {

  private static final int THREAD_COUNT = 5;
  private static final int ITERATIONS_PER_THREAD = 10;
  private static ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);

  private static final AtomicInteger sharedCounter = new AtomicInteger(0);

  public static void main(String[] args) {
    System.out.println("Using a shared Random instance:");
    java.util.Random sharedRandom = new java.util.Random();

    for (int i = 0; i < THREAD_COUNT; i++) {
      executorService.submit(() -> {
        for (int j = 0; j < ITERATIONS_PER_THREAD; j++) {
          int randomNumber = sharedRandom.nextInt();
          processRandomNumber(randomNumber);
        }
      });
    }

    System.out.println("\nUsing a shared SecureRandom instance:");
    SecureRandom sharedSecureRandom = new SecureRandom();

    for (int i = 0; i < THREAD_COUNT; i++) {
      executorService.submit(() -> {
        for (int j = 0; j < ITERATIONS_PER_THREAD; j++) {
          int randomNumber = sharedSecureRandom.nextInt();
          processRandomNumber(randomNumber);
        }
      });
    }
    shutdownExecutor(executorService);
  }

  private static void processRandomNumber(int randomNumber) {
    // Simulate some processing of the random number
    sharedCounter.incrementAndGet();
  }

  private static void shutdownExecutor(ExecutorService executorService) {
    executorService.shutdown();
    while (!executorService.isTerminated()) {
      // Wait for all threads to finish
    }
    System.out.println(STR."The counter value: \{sharedCounter.get()}");
  }
}

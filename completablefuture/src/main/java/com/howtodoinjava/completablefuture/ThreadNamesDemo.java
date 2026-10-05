package com.howtodoinjava.completablefuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

/**
 * Prints which thread runs a task for each way of creating a CompletableFuture.
 */
public class ThreadNamesDemo {

  public static void main(String[] args) {
    System.out.println("Common pool parallelism: " + ForkJoinPool.getCommonPoolParallelism());

    String defaultThread = CompletableFuture
        .supplyAsync(() -> Thread.currentThread().getName())
        .join();
    System.out.println("supplyAsync(supplier):           " + defaultThread);

    try (ExecutorService pool = Executors.newFixedThreadPool(4)) {
      String poolThread = CompletableFuture
          .supplyAsync(() -> Thread.currentThread().getName(), pool)
          .join();
      System.out.println("supplyAsync(supplier, pool):     " + poolThread);
    }

    try (ExecutorService virtual = Executors.newVirtualThreadPerTaskExecutor()) {
      String virtualThread = CompletableFuture
          .supplyAsync(() -> Thread.currentThread().toString(), virtual)
          .join();
      System.out.println("supplyAsync(supplier, virtual):  " + virtualThread);
    }
  }
}

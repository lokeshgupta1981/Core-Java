package com.howtodoinjava.concurrency;

import lombok.SneakyThrows;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ThreadPoolExample {
  public static void main(String[] args) {

    ExecutorService executor = Executors.newSingleThreadExecutor();

    for (int i = 1; i <= 5; i++) {
      Task task = new Task("Task " + i);
      executor.execute(task);
    }

    shutdownAndAwaitTermination(executor);
  }

  static void shutdownAndAwaitTermination(ExecutorService pool) {
    // Disable new tasks from being submitted
    pool.shutdown();
    try {
      // Wait a while for existing tasks to terminate
      if (!pool.awaitTermination(60, TimeUnit.SECONDS)) {
        // Cancel currently executing tasks forcefully
        pool.shutdownNow();
        // Wait a while for tasks to respond to being cancelled
        if (!pool.awaitTermination(60, TimeUnit.SECONDS))
          System.err.println("Pool did not terminate");
      }
    } catch (InterruptedException ex) {
      // (Re-)Cancel if current thread also interrupted
      pool.shutdownNow();
      // Preserve interrupt status
      Thread.currentThread().interrupt();
    }
  }
}

class Task implements Runnable {
  private final String name;

  public Task(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  @SneakyThrows
  @Override
  public void run() {
    Thread.sleep(2000l);
    System.out.println("Task [" + name + "] executed on : " + LocalDateTime.now().toString());
  }
}
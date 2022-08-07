package com.howtodoinjava.concurrency;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ScheduledExecutorServiceExample {
  public static void main(String[] args) throws InterruptedException,
      ExecutionException {

    //1
    ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    System.out.println("Task scheduled to execute after 10 seconds at : " + LocalDateTime.now().toString());
    Task task = new Task("App-Task");
    ScheduledFuture<?> result = executor.schedule(task, 10, TimeUnit.SECONDS);

    System.out.println("Shutdown and await requested at : " + LocalDateTime.now().toString());
    shutdownAndAwaitTermination(executor);

    //2
    ScheduledExecutorService executor1 = Executors.newScheduledThreadPool(1);

    Task task1 = new Task("App-Task");
    ScheduledFuture<?> result1 = executor1.scheduleWithFixedDelay(task1, 0, 10, TimeUnit.SECONDS);

    //3
    ScheduledExecutorService executor2 = Executors.newScheduledThreadPool(1);

    Task task2 = new Task("App-Task");
    ScheduledFuture<?> result2 = executor1.scheduleAtFixedRate(task1, 0, 10, TimeUnit.SECONDS);
  }

  static void shutdownAndAwaitTermination(ExecutorService executorService) {
    executorService.shutdown();
    try {
      if (!executorService.awaitTermination(1, TimeUnit.HOURS)) {
        executorService.shutdownNow();
      }
    } catch (InterruptedException ie) {
      executorService.shutdownNow();
      Thread.currentThread().interrupt();
    }
  }
}

/*class Task implements Runnable {
  private final String name;

  public Task(String name) {
    this.name = name;
  }

  @Override
  public void run() {
    System.out.println("Task [" + name + "] executed on : " + LocalDateTime.now().toString());
  }
}*/


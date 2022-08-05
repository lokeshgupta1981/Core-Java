package com.howtodoinjava.concurrency;

import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class WaitForAllTasksToComplete {
  public static void main(String[] args) throws Exception {

    //1 - Runnable and Future.get()
    ExecutorService executor = Executors.newFixedThreadPool(5);

    List<Future<?>> futures = new ArrayList<>();

    for (int i = 1; i <= 10; i++) {
      Future<?> f = executor.submit(new DemoRunnable(i));
      futures.add(f);
    }

    System.out.println("###### All tasks are submitted.");

    for (Future<?> f : futures) {
      f.get();
    }

    System.out.println("###### All tasks are completed.");

    //2 - Runnable and ExecutorService.awaitTermination()

    executor = Executors.newFixedThreadPool(5);

    for (int i = 1; i <= 10; i++) {
      executor.submit(new DemoRunnable(i));
    }

    System.out.println("###### All tasks are submitted.");

    shutdownAndAwaitTermination(executor);

    System.out.println("###### All tasks are completed.");

    //3 - Callable

    executor = Executors.newFixedThreadPool(10);

    List<DemoCallable> tasks = Arrays.asList(
        new DemoCallable(1), new DemoCallable(2),
        new DemoCallable(3), new DemoCallable(4),
        new DemoCallable(5), new DemoCallable(6),
        new DemoCallable(7), new DemoCallable(8),
        new DemoCallable(9), new DemoCallable(10));

    System.out.println("###### Submitting all tasks.");

    List<Future<Integer>> listOfFutures = executor.invokeAll(tasks);

    shutdownAndAwaitTermination(executor);

    System.out.println("###### All tasks are completed.");

    for (Future f : listOfFutures) {
      System.out.print(f.get() + " ");
    }

    //4-ExecutorCompletionService

    System.out.println("========4============");

    executor = Executors.newFixedThreadPool(10);
    CompletionService<Integer> service
        = new ExecutorCompletionService<>(executor);

    tasks = Arrays.asList(
        new DemoCallable(1), new DemoCallable(2),
        new DemoCallable(3), new DemoCallable(4),
        new DemoCallable(5), new DemoCallable(6),
        new DemoCallable(7), new DemoCallable(8),
        new DemoCallable(9), new DemoCallable(10));

    for (DemoCallable task : tasks) {
      service.submit(task);
    }

    shutdownAndAwaitTermination(executor);

    System.out.println("###### All tasks are completed.");
  }

  static void shutdownAndAwaitTermination(ExecutorService executorService) {
    executorService.shutdown();
    try {
      if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
        executorService.shutdownNow();
      }
    } catch (InterruptedException ie) {
      executorService.shutdownNow();
      Thread.currentThread().interrupt();
    }
  }
}

class DemoRunnable implements Runnable {

  private Integer jobNum;

  public DemoRunnable(Integer index) {
    this.jobNum = index;
  }

  @SneakyThrows
  @Override
  public void run() {
    Thread.sleep(new Random(0).nextLong(1000));
    System.out.println("DemoRunnable completed for index : " + jobNum);
  }
}

class DemoCallable implements Callable<Integer> {

  private Integer jobNum;

  public DemoCallable(Integer jobNum) {
    this.jobNum = jobNum;
  }

  @SneakyThrows
  @Override
  public Integer call() {
    Thread.sleep(new Random(0).nextLong(1000));
    System.out.println("DemoCallable completed for job number : " + jobNum);
    return jobNum;
  }
}
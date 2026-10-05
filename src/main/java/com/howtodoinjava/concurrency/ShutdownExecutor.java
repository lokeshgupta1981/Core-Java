package com.howtodoinjava.concurrency;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * shutdown() followed by awaitTermination() on a ScheduledExecutorService, and the same program
 * with shutdownNow(). Three tasks run after 10, 20 and 30 seconds; the executor is shut down after 15 seconds.
 *
 * <p>Run with shutdown(): mvn -q compile exec:java -Dexec.mainClass=com.howtodoinjava.concurrency.ShutdownExecutor
 * <p>Run with shutdownNow(): add -Dexec.args=now
 */
public class ShutdownExecutor {

  public static void main(String[] args) throws InterruptedException {
    boolean now = args.length > 0 && args[0].equals("now");
    ScheduledExecutorService executor = Executors.newScheduledThreadPool(3);

    System.out.println("WorkerTasks scheduled at : " + time());

    ScheduledFuture<String> result1 = executor.schedule(new WorkerTask("WorkerTask-1"), 10, TimeUnit.SECONDS);
    ScheduledFuture<String> result2 = executor.schedule(new WorkerTask("WorkerTask-2"), 20, TimeUnit.SECONDS);
    ScheduledFuture<String> result3 = executor.schedule(new WorkerTask("WorkerTask-3"), 30, TimeUnit.SECONDS);

    TimeUnit.SECONDS.sleep (15);
    System.out.println("*** Shutting down the executor service at : " + time());
    if (now) {
      List<Runnable> notStarted = executor.shutdownNow();
      System.out.println("Tasks never started : " + notStarted.size());
    } else {
      executor.shutdown();
    }

    System.out.println("Task-1 is done : " + result1.isDone());
    System.out.println("Task-2 is done : " + result2.isDone());
    System.out.println("Task-3 is done : " + result3.isDone());

    System.out.println("*** Waiting for tasks to complete");
    boolean terminated = executor.awaitTermination(1, TimeUnit.MINUTES);
    System.out.println("*** Terminated : " + terminated + " at : " + time());

    System.out.println("Task-1 is done : " + result1.isDone() + ", cancelled : " + result1.isCancelled());
    System.out.println("Task-2 is done : " + result2.isDone() + ", cancelled : " + result2.isCancelled());
    System.out.println("Task-3 is done : " + result3.isDone() + ", cancelled : " + result3.isCancelled());
  }

  static LocalTime time() {
    return LocalTime.now().truncatedTo(ChronoUnit.SECONDS);
  }
}

class WorkerTask implements Callable<String> {
  private final String name;

  public WorkerTask(String name) {
    this.name = name;
  }

  @Override
  public String call() {
    System.out.println("WorkerTask [" + name + "] executed at : " + ShutdownExecutor.time());
    return "WorkerTask [" + name + "] is SUCCESS !!";
  }
}

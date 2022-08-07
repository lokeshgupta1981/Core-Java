package com.howtodoinjava.concurrency;

import java.time.LocalDateTime;
import java.util.concurrent.*;

public class CallableFutureExample {
  public static void main(String[] args) throws ExecutionException,
      InterruptedException, TimeoutException {

    ExecutorService executor = Executors.newFixedThreadPool(1);
    Future<String> future = executor.submit(new Work("Demo-Task"));

    String completedThread = future.get(10, TimeUnit.SECONDS);

    System.out.println("Thread completed : " + completedThread);
  }
}

class Work implements Callable<String> {
  private final String name;

  public Work(String name) {this.name = name;}

  @Override
  public String call() {
    System.out.println("Task [" + name + "] executed on : " + LocalDateTime.now().toString());
    return name;
  }
}
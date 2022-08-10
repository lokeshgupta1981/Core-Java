package com.howtodoinjava.concurrency;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class GetAndSetThreadNames {
  public static void main(String[] args) throws InterruptedException {
    ExecutorService executorService = null;
    try {
      executorService = Executors.newFixedThreadPool(3);

      Thread thread = new Thread(() -> {
        System.out.println("Thread name is : " + Thread.currentThread().getName());
      }, "Demo-Thread");
      thread.start();


      thread = new Thread(() -> {
        System.out.println(Thread.currentThread().getName());
      });
      thread.start();

      executorService.submit(() -> {
        Thread.currentThread().setName("Executor-Thread");
        System.out.println(Thread.currentThread().getName());
      });

      executorService.submit(() -> {
        System.out.println(Thread.currentThread().getName());
      });

      executorService = Executors.newFixedThreadPool(3);

      executorService.submit(() -> {
        System.out.println(Thread.currentThread().getName());
      });
    } finally {
      executorService.shutdown();
    }
  }
}





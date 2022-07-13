package com.howtodoinjava.loom;

import java.util.Random;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.Executors;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class HelloLoom {
  public static void main(String[] args) throws InterruptedException {

    final AtomicInteger atomicInteger = new AtomicInteger();

    Runnable runnable = () -> {
      try {
        Thread.sleep(Duration.ofSeconds(1));
      } catch(Exception e) {
          System.out.println(e);
      }
      System.out.println("Work Done - " + atomicInteger.incrementAndGet());
    };

    Instant start = Instant.now();

    try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
      for(int i = 0; i < 10_000; i++) {
        executor.submit(runnable);
      }
    }

    Instant finish = Instant.now();
    long timeElapsed = Duration.between(start, finish).toMillis();  //in millis
    System.out.println("Total elapsed time : " + timeElapsed);
  }
}

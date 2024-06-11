package com.howtodoinjava.concurrency.virtualThreads;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ExecutorExample {

  void main(String[] args) throws InterruptedException {

    List<Integer> numList = Arrays.asList(1, 2, 3, 4, 5);

    ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

    numList.forEach(num ->
        executor.execute(() -> {
          System.out.println(STR."Square of \{num} is :: \{square(num)}");
        })
    );

    executor.awaitTermination(2, TimeUnit.SECONDS);
    executor.shutdown();
  }

  private Integer square(Integer num) {
    return num * num;
  }
}

package com.howtodoinjava.core.collections.queue;

import lombok.SneakyThrows;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class QueueDrain {
  public static void main(String[] args) {
    BlockingQueue<String> queue = new ArrayBlockingQueue(20);

    ExecutorService executorService = Executors.newFixedThreadPool(3);

    executorService.submit(new Producer(queue));
    executorService.submit(new Producer(queue));
    executorService.submit(new Consumer(queue));
  }
}

class Producer implements Runnable {

  BlockingQueue queue;

  public Producer(BlockingQueue queue){
    this.queue = queue;
  }

  @Override
  @SneakyThrows
  public void run() {
    while (true){
      Thread.sleep(2000);
      System.out.println("Produced new message at : " + LocalDateTime.now());
      queue.offer("Test Message");
    }
  }
}

class Consumer implements Runnable {

  BlockingQueue queue;

  public Consumer(BlockingQueue queue){
    this.queue = queue;
  }

  @Override
  @SneakyThrows
  public void run() {
    while (true) {
      Thread.sleep(10000);

      List<String> messages = new ArrayList<>();

      System.out.println("=========================================");
      System.out.println("Queue size before draining : " + queue.size());

      int messagesCount = queue.drainTo(messages, 20);

      System.out.println("Collection size : " + messagesCount);
      //messages.stream().forEach(System.out::println);

      System.out.println("Queue size after draining : " + queue.size());
      System.out.println("=========================================");
    }
  }
}

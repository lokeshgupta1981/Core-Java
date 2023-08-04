package com.howtodoinjava.core.collections.queue;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedTransferQueue;

public class JavaQueueExamples {

  public static void main(String[] args) {

    Queue<String> messageQueue = new LinkedList<>();

    messageQueue.add("message-1");
    messageQueue.offer("message-2");

    System.out.println(messageQueue.element());  // "message-1"
    System.out.println(messageQueue.peek());  // "message-1"

    System.out.println(messageQueue.poll());  // "message-1"
    System.out.println(messageQueue.remove());  // "message-2"
    //System.out.println( messageQueue.remove() );  // Exception in thread "main" java.util.NoSuchElementException

    //ArrayBlockingQueue Example of blocking nature
    ArrayBlockingQueue<Integer> queue = new ArrayBlockingQueue<>(2);

    // Producer Thread adding 5 items
    Thread producerThread = new Thread(() -> {
      for (int i = 1; i <= 5; i++) {
        try {
          queue.put(i);
          System.out.println("Producer added: " + i);
        } catch (InterruptedException e) {
          System.out.println("Producer Thread interrupted.");
        }
      }
    });

    // Consumer Thread
    Thread consumerThread = new Thread(() -> {
      for (int i = 1; i <= 5; i++) {
        try {
          Thread.sleep(200);
          int element = queue.take();
          System.out.println("Consumer removed: " + element);
        } catch (InterruptedException e) {
          System.out.println("Consumer Thread interrupted.");
        }
      }
    });

    producerThread.start();
    consumerThread.start();

    //LinkedTransferQueue Example
    LinkedTransferQueue<Integer> transferQueue = new LinkedTransferQueue<>();

    // Producer Thread
    Thread producerThread1 = new Thread(() -> {
      for (int i = 1; i <= 5; i++) {
        try {
          System.out.println("Producer is transferring: " + i);
          transferQueue.transfer(i);
          System.out.println("Producer transferred: " + i);
        } catch (InterruptedException e) {
          System.out.println("Producer Thread interrupted.");
        }
      }
    });

    // Consumer Thread
    Thread consumerThread1 = new Thread(() -> {
      for (int i = 1; i <= 5; i++) {
        try {
          System.out.println("Consumer is waiting to receive...");
          int element = transferQueue.take();
          System.out.println("Consumer received: " + element);
        } catch (InterruptedException e) {
          System.out.println("Consumer Thread interrupted.");
        }
      }
    });

    producerThread1.start();
    consumerThread1.start();

    //Deque Example
    Deque<Integer> deque = new ArrayDeque<>();

    // Adding elements to the front of the Deque
    deque.offerFirst(10);
    deque.offerFirst(20);
    deque.offerFirst(30);

    // Adding elements to the back of the Deque
    deque.offerLast(40);
    deque.offerLast(50);

    System.out.println("Deque elements: " + deque); // [30, 20, 10, 40, 50]

    // Peeking elements at both ends of the Deque
    System.out.println("Peek First (Front): " + deque.peekFirst()); // 30
    System.out.println("Peek Last (Back): " + deque.peekLast());   // 50

    // Removing elements from both ends of the Deque
    System.out.println("Popped First (Front): " + deque.pollFirst()); // 30
    System.out.println("Popped Last (Back): " + deque.pollLast());   // 50

    System.out.println("Deque elements after pops: " + deque); // [20, 10, 40]

    //PriorityQueue Example
    PriorityQueue<Task> priorityQueue = new PriorityQueue<>();

    priorityQueue.add(new Task(10001, "Task 1", 5));
    priorityQueue.add(new Task(10002, "Task 2", 1));
    priorityQueue.add(new Task(10003, "Task 3", 10));

    System.out.println(priorityQueue);

    while (!priorityQueue.isEmpty()) {
      System.out.println(priorityQueue.poll());
    }
  }

}

record Task(long id, String name, int priority) implements Comparable<Task> {

  @Override
  public int compareTo(Task other) {
    return Integer.compare(other.priority, this.priority);
  }
}

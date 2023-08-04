package com.howtodoinjava.core.collections.queue;

import java.util.Comparator;
import java.util.PriorityQueue;

public class PriorityQueueExample {

  public static void main(String[] args) {
    // Creating a priority queue
    PriorityQueue<Integer> numbers = new PriorityQueue<>();

    // Add elements with the add() or offer() methods
    numbers.offer(1);
    numbers.add(3);
    numbers.add(2);

    System.out.println("PriorityQueue: " + numbers);

    //Examine the items
    System.out.println("Item: " + numbers.peek());

    //Retrieve the items
    System.out.println("Item: " + numbers.poll());
    System.out.println("Item: " + numbers.poll());
    System.out.println("Item: " + numbers.poll());

    //PriorityQueue Example

    Comparator<Task> idComparator = Comparator.comparing(Task::id);

    PriorityQueue<Task> priorityQueue = new PriorityQueue<>(idComparator);

    priorityQueue.add(new Task(10001, "Task 1", 5));
    priorityQueue.add(new Task(10003, "Task 3", 10));
    priorityQueue.add(new Task(10002, "Task 2", 1));

    while (!priorityQueue.isEmpty()) {
      System.out.println(priorityQueue.poll());
    }
  }
}

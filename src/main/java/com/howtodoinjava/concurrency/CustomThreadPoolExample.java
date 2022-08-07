package com.howtodoinjava.concurrency;

import java.util.concurrent.LinkedBlockingQueue;

public class CustomThreadPoolExample {
  public static void main(String[] args) {
    CustomThreadPool customThreadPool = new CustomThreadPool(2);

    for (int i = 1; i <= 5; i++) {
      Task task = new Task("Task " + i);
      System.out.println("Created : " + task.getName());

      customThreadPool.execute(task);
    }
  }
}

class CustomThreadPool {
  //Thread pool size
  private final int poolSize;

  //Internally pool is an array
  private final WorkerThread[] workers;

  // FIFO ordering
  private final LinkedBlockingQueue<Runnable> queue;

  public CustomThreadPool(int poolSize) {
    this.poolSize = poolSize;
    queue = new LinkedBlockingQueue<Runnable>();
    workers = new WorkerThread[poolSize];

    for (int i = 0; i < poolSize; i++) {
      workers[i] = new WorkerThread();
      workers[i].start();
    }
  }

  public void execute(Runnable task) {
    synchronized (queue) {
      queue.add(task);
      queue.notify();
    }
  }

  private class WorkerThread extends Thread {
    public void run() {
      Runnable task;

      while (true) {
        synchronized (queue) {
          while (queue.isEmpty()) {
            try {
              queue.wait();
            } catch (InterruptedException e) {
              System.out.println("An error occurred while queue is waiting: " + e.getMessage());
            }
          }
          task = (Runnable) queue.poll();
        }

        try {
          task.run();
        } catch (RuntimeException e) {
          System.out.println("Thread pool is interrupted due to an issue: " + e.getMessage());
        }
      }
    }
  }

  public void shutdown() {
    System.out.println("Shutting down thread pool");
    for (int i = 0; i < poolSize; i++) {
      workers[i] = null;
    }
  }
}
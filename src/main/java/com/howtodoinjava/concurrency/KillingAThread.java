package com.howtodoinjava.concurrency;

public class KillingAThread {
  public static void main(String[] args) throws InterruptedException {

    //1

    CustomTask task1 = new CustomTask();
    CustomTask task2 = new CustomTask();

    task1.start();
    task2.start();

    try {
      Thread.sleep(1000);
      task1.stop();
      task2.stop();
    } catch (InterruptedException e) {
      System.out.println("Caught:" + e);
    }

    //2
    CustomTaskV2 taskV21 = new CustomTaskV2();
    CustomTaskV2 taskV22 = new CustomTaskV2();

    taskV21.start();
    taskV22.start();

    try {
      Thread.sleep(1100);
      taskV21.interrupt();
      taskV22.interrupt();
    } catch (InterruptedException e) {
      System.out.println("Caught:" + e);
    }
  }
}

class CustomTaskV2 implements Runnable {
  private Thread worker;

  public void start() {
    worker = new Thread(this);
    worker.start();
  }

  public void interrupt() {
    worker.interrupt();
  }

  @Override
  public void run() {
    while (!Thread.currentThread().isInterrupted()) {
      try {
        Thread.sleep(500);
        System.out.println(Thread.currentThread().getName() + " Running...");
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        System.out.println("Thread was interrupted with reason : " + e.getMessage());
      }
    }
    System.out.println(Thread.currentThread().getName() + " Stopped");
    return;
  }
}

class CustomTask implements Runnable {

  private volatile boolean flag = false;
  private Thread worker;

  public void start() {
    worker = new Thread(this);
    worker.start();
  }

  public void stop() {
    flag = true;
  }

  @Override
  public void run() {
    while (!flag) {
      try {
        Thread.sleep(500);
        System.out.println(Thread.currentThread().getName() + " Running...");
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        System.out.println("Thread was interrupted," + e.getMessage());
      }
    }
    System.out.println(Thread.currentThread().getName() + " Stopped");
    return;
  }
}
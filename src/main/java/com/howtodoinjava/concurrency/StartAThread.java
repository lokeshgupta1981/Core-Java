package com.howtodoinjava.concurrency;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StartAThread {

  public static void main(String[] args) {
    Thread subTask = new SubTask();
    subTask.start();

    Thread subTaskWithRunnable = new Thread(new SubTaskWithRunnable());
    subTaskWithRunnable.start();

    Runnable subTaskWithLambda = () ->
    {
      System.out.println("SubTaskWithLambda started...");
    };

    Thread subTask1 = new Thread(subTaskWithLambda);
    subTask1.start();

    //virtual thread
   /* Runnable runnable = () -> System.out.println("Inside Runnable");
    Thread.startVirtualThread(runnable);

    //or

    Thread.startVirtualThread(() -> {
      //Code to execute in virtual thread
      System.out.println("Inside Runnable");
    });*/

    ExecutorService executorService = Executors.newFixedThreadPool(10);
    executorService.execute(() ->
    {
      System.out.println("SubTaskWithLambda started...");
    });
  }
}

class SubTask extends Thread {

  public void run() {
    System.out.println("SubTask started...");
  }
}

class SubTaskWithRunnable implements Runnable {

  public void run() {
    System.out.println("SubTaskWithRunnable started...");
  }
}

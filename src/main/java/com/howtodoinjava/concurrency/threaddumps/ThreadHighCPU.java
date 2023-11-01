package com.howtodoinjava.concurrency.threaddumps;

public class ThreadHighCPU {

  public static void main(String[] args) {
    // Create a CPU-intensive task and run it in a separate thread
    Runnable cpuIntensiveTask = () -> {
      while (true) {
        // Simulate a CPU-intensive operation (e.g., busy-wait loop)
        double result = 0;
        for (int i = 0; i < 1000000; i++) {
          result += Math.random();
        }
      }
    };

    Thread thread = new Thread(cpuIntensiveTask);
    thread.start();

    // Let the CPU-intensive task run for a while (you may need to manually terminate it)
    try {
      Thread.sleep(60000); // Run for 60 seconds
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}

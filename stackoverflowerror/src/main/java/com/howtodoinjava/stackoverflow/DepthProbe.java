package com.howtodoinjava.stackoverflow;

/**
 * Measures how many frames of a small recursive method fit on the current thread stack.
 * Run it with different -Xss values. Catching StackOverflowError is done here only to measure.
 */
public final class DepthProbe {

  private static int depth;

  private DepthProbe() {
  }

  private static void dive() {
    depth++;
    dive();
  }

  private static int measureOnce() {
    depth = 0;
    try {
      dive();
    } catch (StackOverflowError e) {
      // measurement only
    }
    return depth;
  }

  // The first runs are interpreted (bigger frames); later runs use JIT-compiled code
  public static int measure() {
    int last = 0;
    for (int i = 0; i < 10; i++) {
      last = measureOnce();
    }
    return last;
  }

  public static void main(String[] args) throws InterruptedException {
    System.out.println("main thread depth: " + measure());

    // A platform thread with a 16 MB stack size request
    int[] result = new int[1];
    Thread worker = new Thread(null, () -> result[0] = measure(), "deep-worker", 16L * 1024 * 1024);
    worker.start();
    worker.join();
    System.out.println("16 MB thread depth: " + result[0]);
  }
}

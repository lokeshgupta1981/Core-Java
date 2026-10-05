package com.howtodoinjava.stackoverflow;

/** Recursive sums of 1..n: without a base case, with a base case, and as a loop. */
public final class Sums {

  private Sums() {
  }

  // No base case: the method calls itself until the thread stack is full
  public static long sumToNoBase(int n) {
    return n + sumToNoBase(n - 1);
  }

  // Base case: stops at 0
  public static long sumTo(int n) {
    if (n <= 0) {
      return 0;
    }
    return n + sumTo(n - 1);
  }

  // Tail call: the recursive call is the last action, but the JVM still adds a frame per call
  public static long sumToTail(int n, long total) {
    if (n <= 0) {
      return total;
    }
    return sumToTail(n - 1, total + n);
  }

  // Same result with no recursion, so the depth of n does not matter
  public static long sumToLoop(int n) {
    long total = 0;
    for (int i = 1; i <= n; i++) {
      total += i;
    }
    return total;
  }
}

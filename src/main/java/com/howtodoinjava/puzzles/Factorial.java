package com.howtodoinjava.puzzles;

import java.math.BigInteger;
import java.util.stream.LongStream;

public class Factorial {

  public static void main(String[] args) {
    int n = 5;

    System.out.println(factorialIterative(5));
    System.out.println(factorialRecursive(5));
    System.out.println(factorialStreams(5));
    System.out.println(getFactorialOfLargeNumber(25));
  }

  public static long factorialIterative(long n) {
    long r = 1;
    for (long i = 1; i <= n; i++) {
      r *= i;
    }
    return r;
  }

  public static long factorialRecursive(long n) {
    return n == 1 ? 1 : n * factorialRecursive(n - 1);
  }

  public static long factorialStreams(long n) {
    return LongStream.rangeClosed(1, n)
        .reduce(1, (long a, long b) -> a * b);
  }

  public static BigInteger getFactorialOfLargeNumber(int num) {
    BigInteger result = BigInteger.ONE;
    for (int i = 1; i <= num; i++) {
      result = result.multiply(BigInteger.valueOf(i));
    }
    return result;
  }
}

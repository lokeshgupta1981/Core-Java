package com.howtodoinjava.java26.concurrency;

import java.util.List;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Joiner;

/** JEP 525 (preview): allSuccessfulOrThrow() now returns the results as a List. */
public class StructuredFetch {

  static int stock(String fruit) {
    return fruit.length();
  }

  public static List<Integer> stocks() throws InterruptedException {
    try (var scope = StructuredTaskScope.open(Joiner.<Integer>allSuccessfulOrThrow())) {
      scope.fork(() -> stock("apple"));
      scope.fork(() -> stock("banana"));
      List<Integer> stocks = scope.join();
      return stocks;
    }
  }

  public static void main(String[] args) throws InterruptedException {
    System.out.println("stocks = " + stocks());
  }
}

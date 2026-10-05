package com.howtodoinjava.java27;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Subtask;

/**
 * JEP 533: Structured Concurrency (Seventh Preview). In JDK 27, join() of the
 * default scope throws ExecutionException when a subtask fails.
 */
public class ProfileLoader {

  public static String load(boolean ageServiceUp) throws ExecutionException, InterruptedException {
    try (var scope = StructuredTaskScope.open()) {
      Subtask<String> name = scope.fork(() -> findName());
      Subtask<Integer> age = scope.fork(() -> findAge(ageServiceUp));
      scope.join();
      return name.get() + " is " + age.get();
    }
  }

  static String findName() {
    return "Lokesh";
  }

  static int findAge(boolean up) {
    if (!up) {
      throw new IllegalStateException("age service down");
    }
    return 37;
  }

  public static void main(String[] args) throws InterruptedException {
    try {
      System.out.println(load(true));
      System.out.println(load(false));
    } catch (ExecutionException e) {
      System.out.println("Failed: " + e.getCause());
    }
  }
}

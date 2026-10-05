package com.howtodoinjava.java27;

import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * JEP 531: Lazy Constants (Third Preview). Set.ofLazy() is new in JDK 27.
 */
public class LazyGreetings {

  static final AtomicInteger LOADS = new AtomicInteger();
  static final AtomicInteger FLAG_CHECKS = new AtomicInteger();

  // computed at most once, on the first get()
  private final LazyConstant<String> greeting = LazyConstant.of(LazyGreetings::loadGreeting);

  // membership of each candidate is decided on first access
  private final Set<String> enabledFlags =
      Set.ofLazy(Set.of("dark-mode", "beta"), LazyGreetings::isEnabled);

  static String loadGreeting() {
    LOADS.incrementAndGet();
    return "Hello";
  }

  static boolean isEnabled(String flag) {
    FLAG_CHECKS.incrementAndGet();
    return flag.equals("dark-mode");
  }

  public String greeting() {
    return greeting.get();
  }

  public Set<String> enabledFlags() {
    return enabledFlags;
  }

  public static void main(String[] args) {
    var app = new LazyGreetings();
    System.out.println("Loads before get(): " + LOADS.get());
    String first = app.greeting();
    String second = app.greeting();
    System.out.println(first + " / " + second + ", loads: " + LOADS.get());
    System.out.println("dark-mode: " + app.enabledFlags().contains("dark-mode"));
    System.out.println("beta: " + app.enabledFlags().contains("beta"));
    System.out.println("size: " + app.enabledFlags().size());
  }
}

package com.howtodoinjava.app;

import com.howtodoinjava.greeter.Greeter;

/**
 * Compiled against greeter 1.0.0, where count() returns int.
 */
public class CountApp {

  public static void main(String[] args) {
    Greeter.greet("Lokesh");
    int total = Greeter.count();
    System.out.println("Greetings: " + total);
  }
}

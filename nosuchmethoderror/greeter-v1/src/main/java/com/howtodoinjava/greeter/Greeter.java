package com.howtodoinjava.greeter;

/**
 * Version 1.0.0 of the greeter library.
 */
public class Greeter {

  private static int greetings;

  public static String greet(String name) {
    greetings++;
    return "Hello, " + name;
  }

  public static int count() {
    return greetings;
  }
}

package com.howtodoinjava.app;

import com.howtodoinjava.greeter.Greeter;

/**
 * Compiled against greeter 2.0.0.
 */
public class GreetApp {

  public static void main(String[] args) {
    String message = Greeter.greet("Lokesh", "es");
    System.out.println(message);
  }
}

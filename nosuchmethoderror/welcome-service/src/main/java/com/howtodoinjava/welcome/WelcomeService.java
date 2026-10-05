package com.howtodoinjava.welcome;

import com.howtodoinjava.greeter.Greeter;

/**
 * Built against greeter 2.0.0, uses greet(String, String) that 1.0.0 does not have.
 */
public class WelcomeService {

  public static String welcome(String name, String language) {
    return Greeter.greet(name, language);
  }
}

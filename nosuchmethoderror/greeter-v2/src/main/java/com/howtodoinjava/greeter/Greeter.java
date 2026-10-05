package com.howtodoinjava.greeter;

/**
 * Version 2.0.0 of the greeter library.
 * Adds greet(String, String) and changes the return type of count() from int to long.
 */
public class Greeter {

  private static long greetings;

  public static String greet(String name) {
    return greet(name, "en");
  }

  public static String greet(String name, String language) {
    greetings++;
    String word = switch (language) {
      case "es" -> "Hola";
      case "fr" -> "Bonjour";
      default -> "Hello";
    };
    return word + ", " + name;
  }

  public static long count() {
    return greetings;
  }
}

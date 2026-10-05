package com.howtodoinjava.java26.patterns;

/** JEP 530 (preview): primitive types in patterns, instanceof and switch. */
public class PrimitivePatterns {

  public static String size(int count) {
    return switch (count) {
      case byte b -> "fits in a byte: " + b;
      case int i -> "needs an int: " + i;
    };
  }

  public static String stars(double rating) {
    return rating instanceof int whole ? whole + " stars" : rating + " stars";
  }

  public static void main(String[] args) {
    System.out.println(size(100) + " | " + size(300));
    System.out.println(stars(4.0) + " | " + stars(4.5));
  }
}

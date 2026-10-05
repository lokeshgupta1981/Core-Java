package com.howtodoinjava.java25.language;

/** JEP 507 (third preview): primitive types in patterns, instanceof and switch. */
public class PrimitivePatterns {

  public static String size(int pages) {
    return switch (pages) {
      case 0 -> "empty";
      case int p when p < 100 -> "short";
      case int p when p < 500 -> "medium";
      case int p -> "long (" + p + " pages)";
    };
  }

  public static String rating(double stars) {
    return switch (stars) {
      case 5.0 -> "perfect";
      case double s when s >= 4.0 -> "good";
      case double s -> "read reviews first (" + s + ")";
    };
  }

  public static String availability(boolean inStock) {
    return switch (inStock) {
      case true -> "ready to borrow";
      case false -> "join the waiting list";
    };
  }

  public static String toIntSafely(long copiesSold) {
    if (copiesSold instanceof int copies) {
      return "fits in int: " + copies;
    }
    return "too large for int: " + copiesSold;
  }

  public static boolean fitsInByte(int pages) {
    return pages instanceof byte;
  }
}

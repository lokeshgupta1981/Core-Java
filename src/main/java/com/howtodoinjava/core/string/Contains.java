package com.howtodoinjava.core.string;

import org.junit.jupiter.api.Assertions;

public class Contains {

  public static void main(String[] args) {
    Assertions.assertTrue("Hello World, Java!".contains("World"));
    Assertions.assertFalse("Hello World, Java!".contains("world"));

    Assertions.assertThrows(NullPointerException.class, () -> {
      "Hello World, Java!".contains(null);
    });

    Assertions.assertFalse("Hello World, Java!".contains("\\s"));
  }
}

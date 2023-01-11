package com.howtodoinjava.core.string;


import org.junit.jupiter.api.Assertions;

public class EndsWith {

  public static void main(String[] args) {
    String name = "howtodoinjava";

    Assertions.assertTrue(name.endsWith("java"));
    Assertions.assertFalse(name.endsWith("java$"));

    Assertions.assertThrows(NullPointerException.class, ()->{
      name.endsWith(null);
    });
  }
}

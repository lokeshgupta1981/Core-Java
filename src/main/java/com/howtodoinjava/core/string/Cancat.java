package com.howtodoinjava.core.string;

import org.junit.jupiter.api.Assertions;

public class Cancat {

  public static void main(String[] args) {

    String str = "Hello";
    Assertions.assertEquals("Hello World", str.concat(" World"));

    Assertions.assertEquals("Hello", str.concat(""));

    Assertions.assertThrows(NullPointerException.class, ()->{
      str.concat(null);
    });
  }
}

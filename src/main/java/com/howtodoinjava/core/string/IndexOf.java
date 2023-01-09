package com.howtodoinjava.core.string;

import org.junit.jupiter.api.Assertions;

public class IndexOf {

  public static void main(String[] args) {
    String str = "Hello World";

    Assertions.assertEquals(6, str.indexOf("World"));

    Assertions.assertEquals(7, str.indexOf('o', 5));

    Assertions.assertThrows(NullPointerException.class, () -> {
      str.indexOf(null);
    });
  }
}

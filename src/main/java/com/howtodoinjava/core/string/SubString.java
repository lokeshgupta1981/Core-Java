package com.howtodoinjava.core.string;

import org.junit.jupiter.api.Assertions;

public class SubString {

  public static void main(String[] args) {
    String str = "Hello World";

    Assertions.assertEquals("Hello", str.substring(0, 5));
    Assertions.assertEquals("llo Wo", str.substring(2, 8));

    Assertions.assertEquals("World", str.substring(6));

    Assertions.assertThrows(IndexOutOfBoundsException.class, () -> {
      str.substring(-1);
    });

    Assertions.assertThrows(IndexOutOfBoundsException.class, () -> {
      str.substring(6, 4);
    });
  }
}

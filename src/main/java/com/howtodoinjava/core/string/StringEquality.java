package com.howtodoinjava.core.string;

import org.junit.jupiter.api.Assertions;

public class StringEquality {

  public static void main(String[] args) {

    Assertions.assertFalse("null".equalsIgnoreCase(null));
    Assertions.assertFalse("abc".equalsIgnoreCase("abcd")); //Different strings

    Assertions.assertTrue("abc".equalsIgnoreCase("ABC"));
    Assertions.assertTrue("ABC".equalsIgnoreCase("AbC"));
  }

}

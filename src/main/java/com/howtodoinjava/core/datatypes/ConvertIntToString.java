package com.howtodoinjava.core.datatypes;

import org.junit.jupiter.api.Assertions;

public class ConvertIntToString {

  public static void main(String[] args) {
    Assertions.assertEquals("0", Integer.toString(0));
    Assertions.assertEquals("40", Integer.toString(40));
    Assertions.assertEquals("-40", Integer.toString(-40));

    Assertions.assertEquals("2147483647", Integer.toString(Integer.MAX_VALUE));
    Assertions.assertEquals("-2147483648", Integer.toString(Integer.MAX_VALUE + 1));

    Assertions.assertEquals("-2147483648", Integer.toString(Integer.MIN_VALUE));
    Assertions.assertEquals("2147483647", Integer.toString(Integer.MIN_VALUE - 1));

    //

    Assertions.assertEquals("0", String.valueOf(0));
    Assertions.assertEquals("40", String.valueOf(40));
    Assertions.assertEquals("-40", String.valueOf(-40));

    Assertions.assertEquals("2147483647", String.valueOf(Integer.MAX_VALUE));
    Assertions.assertEquals("-2147483648", String.valueOf(Integer.MAX_VALUE + 1));

    Assertions.assertEquals("-2147483648", String.valueOf(Integer.MIN_VALUE));
    Assertions.assertEquals("2147483647", String.valueOf(Integer.MIN_VALUE - 1));
  }
}

package com.howtodoinjava.core.string;

import org.junit.jupiter.api.Assertions;

public class LastIndexOf {

  public static void main(String[] args) {
    String str = "Hello world Java programmers, welcome to Java world !";

    Assertions.assertEquals(41, str.lastIndexOf("Java"));
    Assertions.assertEquals(41, str.lastIndexOf('J'));

    Assertions.assertEquals(12, str.lastIndexOf("Java", str.indexOf(",")));
    Assertions.assertEquals(12, str.lastIndexOf('J', str.indexOf(",")));

    Assertions.assertThrows(NullPointerException.class, ()->{
      str.lastIndexOf(null);
    });

    Assertions.assertEquals(str.length(), str.lastIndexOf(""));
  }
}

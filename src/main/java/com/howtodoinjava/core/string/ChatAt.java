package com.howtodoinjava.core.string;

import org.junit.jupiter.api.Assertions;

public class ChatAt {

  public static void main(String[] args) {
    String str = "howtodoinjava.com";

    Assertions.assertEquals('h', str.charAt(0));

    Assertions.assertEquals('m', str.charAt(str.length() - 1));

    Assertions.assertEquals('.', str.charAt(13));

    Assertions.assertThrows(StringIndexOutOfBoundsException.class, ()->{
      str.charAt(50);
    });
  }
}

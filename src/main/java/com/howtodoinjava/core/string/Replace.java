package com.howtodoinjava.core.string;

import java.util.regex.PatternSyntaxException;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;

public class Replace {

  public static void main(String[] args) {
    String message = "Hello world !!";

    Assertions.assertEquals("HellO wOrld !!", message.replace('o', 'O'));
    Assertions.assertEquals("Hi world !!", message.replace("Hello", "Hi"));

    Assertions.assertEquals("Hello world !!", message.replace("[H]", "h"));

    Assertions.assertThrows(NullPointerException.class, () -> {
      message.replace(null, "O");
    });

    Assertions.assertThrows(NullPointerException.class, () -> {
      message.replace("o", null);
    });
  }

}

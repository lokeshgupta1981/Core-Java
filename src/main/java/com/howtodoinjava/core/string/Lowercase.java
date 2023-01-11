package com.howtodoinjava.core.string;

import java.util.Locale;
import org.junit.jupiter.api.Assertions;

public class Lowercase {

  public static void main(String[] args) {
    String name = "HowToDoInJava.com";

    Assertions.assertEquals("howtodoinjava.com", name.toLowerCase());

    System.out.println("howtodoinjava.com".toLowerCase(new Locale("tr", "TR")));
  }
}

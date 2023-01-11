package com.howtodoinjava.core.string;

import org.junit.jupiter.api.Assertions;

public class Intern {

  public static void main(String[] args) {
    String strObject = new String("Alex");
    String strLiteral = strObject.intern();

    Assertions.assertFalse(strObject == strLiteral);
    Assertions.assertTrue(strObject.equals(strLiteral));

    String newLiteral = "Alex";
    Assertions.assertTrue(strLiteral == newLiteral);

    String upperCased = "Alex123".toUpperCase();
    System.out.println(upperCased);
  }
}

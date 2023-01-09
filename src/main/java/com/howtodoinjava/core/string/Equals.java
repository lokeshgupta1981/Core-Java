package com.howtodoinjava.core.string;

import org.junit.jupiter.api.Assertions;

public class Equals {

  public static void main(String[] args) {

    String str1 = "alex";
    String str2 = "alex";
    String str3 = "alexa";

    Assertions.assertTrue(str1.equals(str2));
    Assertions.assertFalse(str1.equals(str3));

    Assertions.assertTrue(new String("alex").equals(new String("alex")));

    Assertions.assertThrows(NullPointerException.class, () -> {
      str1.contains(null);
    });

    Assertions.assertTrue(str1.equals("alex"));
    Assertions.assertFalse(str1.equals("Alex"));

    String strObj1 = new String("test");
    String strObj2 = new String("test");

    Assertions.assertFalse(strObj1 == strObj2);
    Assertions.assertTrue(strObj2.equals(strObj2));
  }
}

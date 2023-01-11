package com.howtodoinjava.core.string;

import org.junit.jupiter.api.Assertions;

public class ReverseString {

  public static void main(String[] args) {
    String blogName = "How To Do In Java";
    String reverseString = reverseString(blogName);

    Assertions.assertEquals("avaJ nI oD oT woH", reverseString);

    reverseString = new StringBuilder(blogName).reverse().toString();
    Assertions.assertEquals("avaJ nI oD oT woH", reverseString);

  }

  public static String reverseString(String string) {
    if (string.isEmpty()) {
      return string;
    }
    return reverseString(string.substring(1)) + string.charAt(0);
  }
}

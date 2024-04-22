package com.howtodoinjava.core.security;

import java.util.Base64;

public class Base64Example {

  public static void main(String[] args) {
    String originalInput = "hello world";
    String encodedString = Base64.getEncoder().withoutPadding().encodeToString(originalInput.getBytes());
    System.out.println(encodedString);

    byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
    String decodedString = new String(decodedBytes);
    System.out.println(decodedString);
  }
}

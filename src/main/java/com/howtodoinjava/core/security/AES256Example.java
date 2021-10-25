package com.howtodoinjava.core.security;

public class AES256Example {
  public static void main(String[] args) {
    String originalString = "howtodoinjava.com";

    String encryptedString = AES256.encrypt(originalString);
    String decryptedString = AES256.decrypt(encryptedString);

    System.out.println(originalString);
    System.out.println(encryptedString);
    System.out.println(decryptedString);
  }
}

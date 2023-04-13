package com.howtodoinjava.puzzles;

import org.apache.commons.lang3.StringUtils;

public class Palindrome {

  public static void main(String[] args) {

    String input1 = "howtodoinjava";
    String input2 = "naman";

    System.out.println(isPalindromeUsingStringBuilder(input1));
    System.out.println(isPalindromeUsingStringBuilder(input2));

    System.out.println(isPalindromeUsingStringUtils(input1));
    System.out.println(isPalindromeUsingStringUtils(input2));

    System.out.println(isPalindromeUsingForLoop(input1));
    System.out.println(isPalindromeUsingForLoop(input2));

    System.out.println(isPalindromeUsingRecursion(input1));
    System.out.println(isPalindromeUsingRecursion(input2));
  }

  public static boolean isPalindromeUsingStringBuilder(String originalString) {

    String reverse = new StringBuilder(originalString).reverse().toString();
    return originalString.equals(reverse);
  }

  public static boolean isPalindromeUsingStringUtils(String originalString) {

    String reverse = StringUtils.reverse(originalString);
    return originalString.equals(reverse);
  }

  public static boolean isPalindromeUsingForLoop(String originalString) {

    String reverse = "";

    for (int i = originalString.length() - 1; i >= 0; i--) {
      reverse = reverse + originalString.charAt(i);
    }
    return originalString.equals(reverse);
  }

  public static boolean isPalindromeUsingRecursion(String string) {

    if (string.length() == 0 || string.length() == 1) {
      return true;
    }
    if (string.charAt(0) == string.charAt(string.length() - 1)) {
      return isPalindromeUsingRecursion(string.substring(1, string.length() - 1));
    }
    return false;
  }


}

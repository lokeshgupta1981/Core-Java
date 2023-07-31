package com.howtodoinjava.core.datatypes.decimalandhexadecimal.decimaltohex;

public class ExampleUsingRawMethod {

  public static String convertToHex(int number) {

    if (number <= 0) {
      return "0";
    }

    int remainder;
    StringBuilder result = new StringBuilder();
    String tokens = "0123456789ABCDEF";

    while (number > 0) {
      remainder = number % 16;
      result.insert(0, tokens.charAt(remainder));
      number = number / 16;
    }
    return result.toString();
  }

  public static void main(String[] args) {

    String hex = convertToHex(269);
    System.out.println(hex);  // '10D'
  }
}

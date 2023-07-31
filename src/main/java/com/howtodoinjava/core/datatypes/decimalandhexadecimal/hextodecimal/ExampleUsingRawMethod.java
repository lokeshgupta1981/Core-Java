package com.howtodoinjava.core.datatypes.decimalandhexadecimal.hextodecimal;

public class ExampleUsingRawMethod {

  public static int convertHexToDecimal(String hex) {

    String tokens = "0123456789ABCDEF";
    hex = hex.toUpperCase();
    int result = 0;

    for (int i = 0; i < hex.length(); i++) {

      int n = tokens.indexOf(hex.charAt(i));
      result = result * 16 + n;
    }
    return result;
  }

  public static void main(String[] args) {

    System.out.println(convertHexToDecimal("10D"));
  }
}

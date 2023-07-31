package com.howtodoinjava.core.datatypes.decimalandhexadecimal.decimaltohex;

public class ExampleUsingDouble {

  public static void main(String[] args) {

    double number = 333.44d;

    String hex = Double.toHexString(number);

    System.out.println(hex);

    hex = String.format("%x", number);

    System.out.println(hex);

    hex = String.format("%x", number);

    System.out.println(hex);
  }
}

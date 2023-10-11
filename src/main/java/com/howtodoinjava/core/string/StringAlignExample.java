package com.howtodoinjava.core.string;

import com.howtodoinjava.core.string.StringAlignment.Alignment;

public class StringAlignExample {

  public static void main(String[] args) {
    String input = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt "
        + "ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris "
        + "nisi ut aliquip ex ea commodo consequat.";
    int width = 50;

    StringAlignment leftAlignment = new StringAlignment(width, StringAlignment.Alignment.LEFT);
    String leftAlignedString = leftAlignment.format(input);

    StringAlignment rightAlignment = new StringAlignment(width, Alignment.RIGHT);
    String rightAlignedString = rightAlignment.format(input);

    StringAlignment centerAlignment = new StringAlignment(width, Alignment.CENTER);
    String centerAlignedString = centerAlignment.format(input);

    System.out.println(leftAlignedString);
    System.out.println(rightAlignedString);
    System.out.println(centerAlignedString);
  }
}

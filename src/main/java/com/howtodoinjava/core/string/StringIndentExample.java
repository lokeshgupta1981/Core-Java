package com.howtodoinjava.core.string;

public class StringIndentExample {

  public static void main(String[] args) {
    String line1 = "ABC".indent(8);
    System.out.println(line1.replace(" ", "-"));

    String line2 = "ABC".indent(8).indent(-5);
    System.out.println(line2.replace(" ", "-"));

    String textBlock = """
        Line 1
        Line 2""";

    String indentedBlock = """
        Line 1
        Line 2""".indent(8);

    System.out.println(textBlock.replace(" ", "-"));
    System.out.println(indentedBlock.replace(" ", "-"));
  }

}

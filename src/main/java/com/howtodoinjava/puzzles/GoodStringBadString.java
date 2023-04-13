package com.howtodoinjava.puzzles;

public class GoodStringBadString {

  public static void main(String[] args) {
    String input = "Good Oops, Bad Oops";

    //1 Regex
    String output = input.replaceAll("(?i)(\\p{L})\\1", "$1");
    System.out.println(output);

  }
}

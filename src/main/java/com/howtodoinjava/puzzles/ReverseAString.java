package com.howtodoinjava.puzzles;

public class ReverseAString {

  public static void main(String[] args) {
    String string = "howtodoinjava";
    String reverseString = reverse(string);
    System.out.println(reverseString);
  }

  private static String reverse(String str) {

    char[] chars = str.toCharArray();
    int start = 0;
    int end = chars.length - 1;

    while (start < end) {
      // Swap characters at start and end indices
      char temp = chars[start];
      chars[start] = chars[end];
      chars[end] = temp;

      // Move the indices towards the center
      start++;
      end--;
    }

    return new String(chars);
  }
}

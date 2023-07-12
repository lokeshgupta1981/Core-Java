package com.howtodoinjava.puzzles;

import java.util.HashSet;
import java.util.Set;

public class LongestSubstringLength {

  public static void main(String[] args) {

    String input = "abcabcbb";
    int length = findLongestSubstringLength(input);

    System.out.println(length); //3
  }

  public static int findLongestSubstringLength(String str) {

    int maxLength = 0;
    int left = 0;
    int right = 0;
    Set<Character> slidingWindow = new HashSet<>();

    while (right < str.length()) {

      char currentChar = str.charAt(right);

      if (slidingWindow.contains(currentChar)) {
        slidingWindow.remove(str.charAt(left));
        left++;
      } else {
        slidingWindow.add(currentChar);
        maxLength = Math.max(maxLength, right - left + 1);
        right++;
      }
    }

    return maxLength;
  }
}

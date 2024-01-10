package com.howtodoinjava.algorithms;

public class IsomorphicStrings {

  public boolean areIsomorphic(String s1, String s2) {
    if (s1 == null || s2 == null
        || s1.length() != s2.length()) {
      return false;
    }

    int[] arr1 = new int[256];
    int[] arr2 = new int[256];

    for (int i = 0; i < s1.length(); i++) {

      char c1 = s1.charAt(i);
      char c2 = s2.charAt(i);

      if (arr1[c1] != arr2[c2]) {
        return false;
      }

      arr1[c1] = i + 1;
      arr2[c2] = i + 1;
    }
    return true;
  }

  public static void main(String[] args) {
    IsomorphicStrings iso = new IsomorphicStrings();
    System.out.println(iso.areIsomorphic("abbcdd", "qwwcrr")); // true
    System.out.println(iso.areIsomorphic("aab", "que")); // false
  }
}

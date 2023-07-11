package com.howtodoinjava.core.array;

public class SortStringAlphabetically {

  public static void main(String[] args) {
    String string = "adcbgekhs";

    String sortedString = sortWithArray(string);

    System.out.println(sortedString);
  }

  static String sortWithArray(String str) {
    char arr[] = str.toCharArray();
    char temp;

    int i = 0;
    while (i < arr.length) {
      int j = i + 1;
      while (j < arr.length) {
        if (arr[j] < arr[i]) {
          temp = arr[i];
          arr[i] = arr[j];
          arr[j] = temp;
        }
        j += 1;
      }
      i += 1;
    }
    return String.copyValueOf(arr);
  }
}

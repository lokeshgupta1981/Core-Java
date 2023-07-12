package com.howtodoinjava.puzzles;

import java.util.Arrays;

public class ArrayRotation {
  public static void main(String[] args) {
    int[] array = {1, 2, 3, 4, 5};
    int k = 2;

    System.out.println("Original Array: " + Arrays.toString(array));

    rotateArray(array, k);

    System.out.println("Rotated Array: " + Arrays.toString(array));
  }

  public static void rotateArray(int[] array, int k) {
    if (array == null || array.length == 0) {
      return;
    }

    int n = array.length;
    k = k % n; // Adjust k if it is greater than n

    reverseArray(array, 0, n - 1); // Reverse the entire array
    reverseArray(array, 0, k - 1); // Reverse the first k elements
    reverseArray(array, k, n - 1); // Reverse the remaining n-k elements
  }

  public static void reverseArray(int[] array, int start, int end) {

    while (start < end) {
      int temp = array[start];
      array[start] = array[end];
      array[end] = temp;
      start++;
      end--;
    }
  }
}


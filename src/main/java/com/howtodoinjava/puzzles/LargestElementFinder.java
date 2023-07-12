package com.howtodoinjava.puzzles;

public class LargestElementFinder {

  public static void main(String[] args) {
    int[] array = {10, 20, 30, 40, 90, 23, 12, 60};

    int largest = findLargestElement(array);
    System.out.println("The largest element in the array is: " + largest);
  }

  public static int findLargestElement(int[] array) {
    if (array == null || array.length == 0) {
      throw new IllegalArgumentException("Array is empty or null.");
    }

    int largest = array[0];

    for (int i = 1; i < array.length; i++) {
      if (array[i] > largest) {
        largest = array[i];
      }
    }

    return largest;
  }

}

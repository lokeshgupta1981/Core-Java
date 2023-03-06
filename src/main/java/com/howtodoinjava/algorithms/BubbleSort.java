package com.howtodoinjava.algorithms;

import java.util.Arrays;

public class BubbleSort {

  public static void main(String[] args) {
    int arr[] = {6, 2, 5, 3, 1};
    System.out.println("Original Array: " + Arrays.toString(arr));

    BubbleSort bubbleSort = new BubbleSort();

    bubbleSort.bubble_sort(arr);
    System.out.println("\nSorted array: " + Arrays.toString(arr));
  }

  static void bubble_sort(int arr[]) {

    int n = arr.length;

    // Loop for each pass
    for (int i = 0; i < n - 1; i++) {
      // Loop to iterate over each element in each pass
      for (int j = 0; j < n - i - 1; j++) {

        // compare two adjacent elements, if swapping is required then do so
        if (arr[j] > arr[j + 1]) {

          // swap both elements using temp variable
          int temp = arr[j];
          arr[j] = arr[j + 1];
          arr[j + 1] = temp;
        }
      }
    }
  }

  static void optimized_bubble_sort(int arr[]) {
    int n = arr.length;

    // Loop for each pass
    for (int i = 0; i < n - 1; i++) {

      // variable for checking swapping
      boolean swapped = false;

      // Loop to iterate over each element in each pass
      for (int j = 0; j < n - i - 1; j++) {

        // compare two adjacent elements, if swapping is required then do so
        if (arr[j] > arr[j + 1]) {

          // swap both elements using temp variable
          int temp = arr[j];
          arr[j] = arr[j + 1];
          arr[j + 1] = temp;

          // set swapped to True to ensure that
          // array still needs further iterations to be sorted
          swapped = true;
        }
      }
      // If swapped is still False after full iteration of pass
      // it means array is fully sorted now, no further iterations are needed
      if (!swapped) {
        break;
      }
    }
  }
}

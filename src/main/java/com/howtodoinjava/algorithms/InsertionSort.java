package com.howtodoinjava.algorithms;

import java.util.Arrays;

public class InsertionSort {

  public static void main(String[] args) {
    int arr[] = {13, 12, 14, 6, 7};
    System.out.println("Original Array: " + Arrays.toString(arr));

    InsertionSort insertionSort = new InsertionSort();

    insertionSort.insertionSortImproved(arr);
    System.out.println("\nSorted array: " + Arrays.toString(arr));
  }

  void insertion_sort(int arr[]) {
    int n = arr.length;

    // loop for each iteration
    for (int i = 1; i < n; i++) {

      // storing value of arr[i] in key temporarily
      int key = arr[i];
      int j = i - 1;

      // Move all the elements to the right side of the key which are greater than key value.
      while (j >= 0 && arr[j] > key) {
        arr[j + 1] = arr[j];
        j = j - 1;
      }
      arr[j + 1] = key;
    }
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  void insertionSortImproved(int[] arr) {
    for (int i = 1; i < arr.length; i++) {
      int key = arr[i];
      int jLeft = 0;
      int jRight = i - 1;
      //Check if its current position is in correct position
      if (((Comparable) arr[jRight]).compareTo(key) > 0) {
        //Perform binary search
        while (jRight - jLeft >= 2) {
          int jMiddle = (jRight - jLeft) / 2 + jLeft - 1;
          if (((Comparable) arr[jMiddle]).compareTo(key) > 0) {
            jRight = jMiddle;
          } else {
            jLeft = jMiddle + 1;
          }
        }
        if (jRight - jLeft == 1) {
          int jMiddle = jLeft;
          if (((Comparable) arr[jMiddle]).compareTo(key) > 0) {
            jRight = jMiddle;
          } else {
            jLeft = jMiddle + 1;
          }
        }
        //Place the element
        int j = i;
        for (j = i; j > jLeft; j--) {
          arr[j] = arr[j - 1];
        }
        arr[j] = key;
      }
    }
  }
}

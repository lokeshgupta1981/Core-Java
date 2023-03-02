package com.howtodoinjava.algorithms;

import java.util.Arrays;

public class MergeSort {

  /* Combining two sub-arrays of arr[] where the
  first sub-array consists of elements in the range of arr[l] to arr[m], and the
  second sub-array consists of elements in the range of arr[m+1] to arr[r]. */

  void merge(int arr[], int l, int m, int r) {

    // find out the sizes of the two subarrays that are going to be merged.
    int n1 = m - l + 1;
    int n2 = r - m;

    // Create temporary arrays
    int L[] = new int[n1];
    int R[] = new int[n2];

    // Copy all elements into temporary arrays
    for (int i = 0; i < n1; ++i) {
      L[i] = arr[l + i];
    }

    for (int j = 0; j < n2; ++j) {
      R[j] = arr[m + 1 + j];
    }

    // Merge temporary arrays
    int i = 0, j = 0; // starting indexes of array L and R
    int k = l; // starting index of merged sub-arrays

    while (i < n1 && j < n2) {

      if (L[i] <= R[j]) {

        arr[k] = L[i];
        i++;
      } else {

        arr[k] = R[j];
        j++;
      }

      k++;
    }

    // copy the elements which are remaining in L sub-array.
    while (i < n1) {

      arr[k] = L[i];
      i++;
      k++;
    }

    // copy the elements which are remaining in R sub-array.
    while (j < n2) {

      arr[k] = R[j];
      j++;
      k++;
    }
  }

  void sort(int arr[], int l, int r) {

    // check if size of an array is greater than 1 or not
    if (l < r) {

      int m = (l + r) / 2; // find middle index of an array
      sort(arr, l, m); // sort left sub-array
      sort(arr, m + 1, r); // sort right sub-array
      merge(arr, l, m, r); // merge both sorted sub-arrays
    }
  }


  public static void main(String args[]) {

    int arr[] = {10, 8, 9, 4, 3, 2};
    System.out.println("Original Array: " + Arrays.toString(arr));

    MergeSort ob = new MergeSort();

    ob.sort(arr, 0, arr.length - 1);
    System.out.println("\nSorted array: " + Arrays.toString(arr));
  }
}
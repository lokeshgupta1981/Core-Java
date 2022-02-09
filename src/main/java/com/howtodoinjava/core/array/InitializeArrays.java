package com.howtodoinjava.core.array;

import java.util.Arrays;

public class InitializeArrays {
  public static void main(String[] args) {

    String[] status = {"Active", "Inactive", "Purged"};
    System.out.println(Arrays.toString(status));

    String[] statusNew = new String[]{"Active", "Inactive", "Purged"};
    System.out.println(Arrays.toString(statusNew));

    int[] nums = new int[5];

    for (int i = 0; i < nums.length; i++) {
      nums[i] = i;
    }

    System.out.println(Arrays.toString(nums));

    int[] numbers = new int[10];
    Arrays.fill(numbers, 2, 9, 1);
    System.out.println(Arrays.toString(numbers));


    int[] squares = new int[6];
    Arrays.setAll(squares, p -> p * p);
    System.out.println(Arrays.toString(squares));

    int[] array = {0, 1, 2, 3, 4, 5};
    int[] smallCopy = Arrays.copyOf(array, 3); // [0, 1, 2]
    int[] largeCopy = Arrays.copyOf(array, 10); // [0, 1, 2, 3, 4, 5, 0, 0, 0, 0]

    System.out.println(Arrays.toString(smallCopy));
    System.out.println(Arrays.toString(largeCopy));

    int[] smallCopyRange = Arrays.copyOfRange(array, 1, 3); // [1, 2]
    int[] largeCopyRange = Arrays.copyOfRange(array, 2, 10); // [2, 3, 4, 5, 0, 0, 0, 0]

    System.out.println(Arrays.toString(smallCopyRange));
    System.out.println(Arrays.toString(largeCopyRange));
  }
}

package com.howtodoinjava.puzzles;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TwoSum {

  public static void main(String[] args) {
    int[] nums = {2, 7, 11, 18};
    int sum = 9;

    int[] result = twoSum(nums, sum);
    System.out.println(Arrays.toString(result));
  }

  private static int[] twoSum(int[] nums, int sum) {

    for (int i = 0; i < nums.length; i++) {
      int complement = sum - nums[i];
      int foundAtIndex = Arrays.binarySearch(nums, complement);

      if(foundAtIndex > 0) {
        return new int[] {nums[i], nums[foundAtIndex]};
      }
    }
    return null;
  }
}

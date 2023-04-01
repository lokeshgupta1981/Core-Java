package com.howtodoinjava.exercise;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ArrayTwoSum {

  public static void main(String[] args) {
    int[] nums = new int[]{2, 7, 11, 15};
    int target = 9;

    int[] output = twoSum(nums, target);
    System.out.println(Arrays.toString(output));  //[0, 1]
  }

  public static int[] twoSum(int[] nums, int target) {

    Map<Integer, Integer> map = new HashMap<>();
    for (int i = 0; i < nums.length; i++) {

      int complement = target - nums[i];
      if (map.containsKey(complement)) {
        return new int[]{map.get(complement), i};
      }
      map.put(nums[i], i);
    }
    return new int[]{};
  }
}

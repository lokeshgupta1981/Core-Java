package com.howtodoinjava.core.basic;

public class ForLoopExample {

  public static void main(String[] args) {

    int[] array = new int[]{0, 1, 2, 3, 4, 5};

    for (int i = 0; i < array.length; i++) {
      System.out.format("The value at index location %d is %d \n", i, array[i]);
    }
  }
}


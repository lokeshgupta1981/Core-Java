package com.howtodoinjava.newFeatures.java21;

import java.util.PriorityQueue;
import java.util.Queue;

public class UnnamedVariables {

  void main() {

    //1
    Queue<String> q = new PriorityQueue<>();
    q.add("1");

    String _ = q.remove();

    //2
    String input = "Hello, World!";

    try {
      int i = Integer.parseInt(input);
      // use i
    } catch (NumberFormatException _) {
      System.out.println("Invalid input: " + input);
    }

    //3
    int[] arr = new int[] {0,1,2,3,4};
    for (int i = 0, _ = runOnce(); i < arr.length; i++) {
      // use arr[i];
      // ... code that uses 'i' ...
    }
  }

  private static int runOnce() {
    //Some initialization code
    return -1;
  }
}

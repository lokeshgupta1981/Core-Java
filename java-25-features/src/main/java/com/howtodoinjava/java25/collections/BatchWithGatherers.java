package com.howtodoinjava.java25.collections;

import java.util.List;
import java.util.stream.Gatherers;
import java.util.stream.IntStream;

/**
 * Java 24+ example for the tutorial "Java Spliterator: trySplit(), Characteristics and Examples".
 * https://howtodoinjava.com/java/collections/java-spliterator/
 */
public class BatchWithGatherers {

  public static void main(String[] args) {
    List<Integer> orderIds = IntStream.rangeClosed(1, 7).boxed().toList();
    List<List<Integer>> windows = orderIds.stream().gather(Gatherers.windowFixed(3)).toList();
    System.out.println("windows = " + windows);   // [[1, 2, 3], [4, 5, 6], [7]]
  }
}

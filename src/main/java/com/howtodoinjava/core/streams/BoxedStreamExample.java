package com.howtodoinjava.core.streams;

import java.util.List;
import java.util.stream.*;

public class BoxedStreamExample {
  public static void main(String[] args) {
    Stream<Integer> stream = IntStream.of(1, 2, 3, 4, 5).boxed();
    Stream<Long> stream1 = LongStream.of(1, 2, 3, 4, 5).boxed();
    Stream<Double> stream2 = DoubleStream.of(1.0, 2.0, 3.0, 4.0, 5.0).boxed();

    //Compilation issue
    /*List<Integer> list = IntStream.of(1,2,3,4,5)
        .collect(Collectors.toList());*/

    List<Integer> list = IntStream.of(1,2,3,4,5)
        .boxed()
        .collect(Collectors.toList());
  }
}

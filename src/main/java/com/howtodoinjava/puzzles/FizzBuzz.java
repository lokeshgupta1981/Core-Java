package com.howtodoinjava.puzzles;

import java.util.stream.IntStream;

public class FizzBuzz {

  public static void main(String[] args) {

    int end = 100;

    //Java 8 Streams

    IntStream.rangeClosed(1, end)
        .mapToObj(i -> i % 5 == 0 ? (i % 7 == 0 ? "FizzBuzz" : "Fizz") : (i % 7 == 0 ? "Buzz" : i))
        .forEach(System.out::println);

    //Java 7

    for (int i = 1; i <= end; i++) {
      if (((i % 5) == 0) && ((i % 7) == 0)) // Is it a multiple of 5 & 7?
      {
        System.out.println("fizzbuzz");
      } else if ((i % 5) == 0) // Is it a multiple of 5?
      {
        System.out.println("fizz");
      } else if ((i % 7) == 0) // Is it a multiple of 7?
      {
        System.out.println("buzz");
      } else {
        System.out.println(i); // Not a multiple of 5 or 7
      }
    }
  }
}

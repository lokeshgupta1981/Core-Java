package com.howtodoinjava.core.streams;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class IfElseLogic {
  public static void main(String[] args) {
    ArrayList<Integer> numberList
        = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6));

    //1
    Consumer<Integer> action = i -> {
      if (i % 2 == 0) {
        System.out.println("Even number :: " + i);
      } else {
        System.out.println("Odd  number :: " + i);
      }
    };

    numberList.stream()
        .forEach(action);

    //2


    Arrays.asList(-1, 1, -2, 3, 4, -5, 6).stream()
        .forEach(
            i -> {
              if (i == 0) {
                System.out.println("Number is 0");
              } else if (i > 0) {
                System.out.println("Positive Number");
              } else {
                System.out.println("Negative Number");
              }
            }
        );


    //3

    Predicate<Integer> isEven = i -> i % 2 == 0;

    numberList.stream()
        .filter(isEven)
        .forEach(System.out::println);
  }
}

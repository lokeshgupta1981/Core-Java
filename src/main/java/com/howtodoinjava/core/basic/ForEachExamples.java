package com.howtodoinjava.core.basic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class ForEachExamples {

  public static void main(String[] args) {
    List<String> list = Arrays.asList("A","B","C","D");
    ArrayList<String> arrayList = new ArrayList<>(list);

    //1
    list.forEach(System.out::println);

    //2
    Consumer<String> action = x -> System.out.println(x.toLowerCase());
    list.forEach(action);

    list.forEach(e -> System.out.println(e.toLowerCase()));

    //3
    list.forEach(e -> {
      System.out.println(e.toLowerCase());
      //other statements
    });
  }

}

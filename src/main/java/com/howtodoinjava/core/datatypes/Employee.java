package com.howtodoinjava.core.datatypes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Employee {

  public static void main(String[] args) {
    Stream<String> tokenStream = Stream.of("A", "B", "C", "D");
    ArrayList<String> tokenList = tokenStream.collect(Collectors.toCollection(ArrayList::new));
    tokenList.add("e");
    System.out.println(tokenList);

    List list = Arrays.asList("A", "B", "C");
    list.add("e");
    System.out.println(list);

    ArrayList<String> arraylist = new ArrayList<>(Arrays.asList("A", "B", "C"));
  }

}



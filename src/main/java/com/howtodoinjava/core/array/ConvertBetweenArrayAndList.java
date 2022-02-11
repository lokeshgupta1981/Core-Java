package com.howtodoinjava.core.array;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConvertBetweenArrayAndList {
  public static void main(String[] args) {
    List<String> list = Arrays.asList("A", "B", "C");

    //List -> Array
    String[] stringArray = new String[]{"A", "B", "C"};

    //1. List.toArray()
    Object[] objArray = list.toArray();
    //System.out.println(Arrays.toString(objArray));

    //2. Stream.toArray()
    stringArray = list.stream()
        .filter(s -> s.equals("A"))
        .toArray(String[]::new);
    //System.out.println(Arrays.toString(stringArray));

    //Array -> List

    stringArray = new String[]{"A", "B", "C"};

    //1. Arrays.asList()
    list = Arrays.asList(stringArray);
    list.set(0, "Aa");

    System.out.println(list);
    System.out.println(Arrays.toString(stringArray));

    stringArray = new String[]{"A", "B", "C"};

    //2.
    list = Collections.unmodifiableList(Arrays.asList(stringArray));
    //list.set(0, "Aa"); //java.lang.UnsupportedOperationException
    System.out.println(list);

    list = Stream.of(stringArray).collect(Collectors.toList());
    list.add("D");

    System.out.println(list);
    System.out.println(Arrays.toString(stringArray));
  }
}

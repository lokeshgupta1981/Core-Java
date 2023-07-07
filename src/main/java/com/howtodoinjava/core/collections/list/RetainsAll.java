package com.howtodoinjava.core.collections.list;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class RetainsAll {

  public static void main(String[] args) {
    List<Integer> list1 = new ArrayList<>(Stream.of(1, 2, 3, 4, 5).toList());
    List<Integer> list2 = new ArrayList<>(Stream.of(1, 2, 3, 6, 7).toList());

    list1.retainAll(list2);

    System.out.println(list1);  //[1, 2, 3]
    System.out.println(list2);  //[1, 2, 3, 6, 7]
  }
}

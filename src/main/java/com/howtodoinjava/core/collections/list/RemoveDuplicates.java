package com.howtodoinjava.core.collections.list;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class RemoveDuplicates {

  public static void main(String[] args) {
    List<Integer> items = new ArrayList<>(Arrays.asList(1, 1, 2, 3, 3, 3, 4, 5, 6, 6, 6, 7, 8));

    //1 - LinkedHashSet
    LinkedHashSet<Integer> linkedHashSet = new LinkedHashSet<>(items);
    List<Integer> listWithoutDuplicates = new ArrayList<>(linkedHashSet);
    System.out.println(listWithoutDuplicates);

    //2 - Streams
    listWithoutDuplicates = items.stream().distinct().collect(Collectors.toList());
    System.out.println(listWithoutDuplicates);

    //3 - Using HashSet
    Set<Integer> set = new HashSet<>(items.size());
    items.removeIf(p -> !set.add(p));
    System.out.println(items);

  }
}

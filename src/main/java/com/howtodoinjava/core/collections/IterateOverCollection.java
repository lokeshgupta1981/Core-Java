package com.howtodoinjava.core.collections;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class IterateOverCollection {

  public static void main(String[] args) {
    List<String> list = List.of("A", "B", "C", "D");

    //1.1
    list.stream()
        //other operation as needed
        .forEach(System.out::println);

    //1.2
    list.forEach(System.out::println);

    //1.3
    for (String s : list) {
      System.out.println(s);
    }

    //1.4
    Iterator<String> itr = list.iterator();

    while (itr.hasNext()) {
      System.out.println(itr.next());
    }

    //1.5
    for (int i = 0; i < list.size(); i++) {

      System.out.println(list.get(i));
    }

    //2.1

    Set<String> set = Set.of("A", "B", "C", "D");

    //2.1
    set.stream()
        //other operation as needed
        .forEach(System.out::println);

    //2.2
    set.forEach(System.out::println);

    //1.3
    for (String s : set) {
      System.out.println(s);
    }

    //1.4
    Iterator<String> itr1 = set.iterator();

    while (itr1.hasNext()) {
      System.out.println(itr1.next());
    }

    //3

    Map<String, Integer> map = Map.of("A", 1, "B", 2, "C", 3);

    //3.1.
    map.keySet().stream().forEach(System.out::println);

    map.keySet().forEach(System.out::println);

    Iterator<String> keysItr = map.keySet().iterator();
    while (keysItr.hasNext()) {

      System.out.println(keysItr.next());
    }

    //3.2

    //Stream
    map.values().stream().forEach(System.out::println);

    //Iterable.forEach()
    map.values().forEach(System.out::println);

    //Iterator
    Iterator<Integer> valuesItr = map.values().iterator();

    while (valuesItr.hasNext()) {

      System.out.println(valuesItr.next());
    }

    //3.3

    //Stream
    map.entrySet().stream().forEach(System.out::println);

    //Iterable.forEach()
    map.entrySet().forEach(System.out::println);

    //Iterator
    Iterator <Map.Entry<String, Integer>> entryIterator = map.entrySet().iterator();

    while (entryIterator.hasNext()) {

      System.out.println(entryIterator.next().getKey());
      System.out.println(entryIterator.next().getValue());

    }
  }
}

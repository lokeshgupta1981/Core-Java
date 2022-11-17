package com.howtodoinjava.core.collections.list;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.eclipse.collections.impl.list.mutable.ListAdapter;

public class FlattenNestedList {

  public static void main(String[] args) {
    List<List<String>> nestedList = List.of(
        List.of("Alexandru", "John"),
        List.of("Emma", "Andrew", "Luke"),
        List.of("Oliver"));

    List<String> flatList = new ArrayList<>();

    //1
    nestedList.forEach(flatList::addAll);
    System.out.println(flatList);

    //2
    flatList = nestedList.stream()
        .flatMap(Collection::stream)
        .collect(Collectors.toList());
    System.out.println(flatList);

    //3
    flatList = nestedList.stream()
        .reduce(new ArrayList<>(), (l1, l2) -> {
          l1.addAll(l2);
          return l1;
        });
    System.out.println(flatList);

    //4
    flatList = ListAdapter.adapt(nestedList).flatCollect(e -> e);
    System.out.println(flatList);

    //5
    Iterable<String> iterable = Iterables.concat(nestedList);
    flatList = Lists.newArrayList(iterable);

    System.out.println(new ArrayList<>(flatList));
  }

}

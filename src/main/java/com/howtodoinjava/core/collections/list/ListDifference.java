package com.howtodoinjava.core.collections.list;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Assertions;

public class ListDifference {

  public static void main(String[] args) {

    //List Equality

    ArrayList<String> list = new ArrayList<>(Arrays.asList("a", "b", "c"));
    ArrayList<String> equalList = new ArrayList<>(Arrays.asList("c", "b", "a"));
    ArrayList<String> diffList = new ArrayList<>(Arrays.asList("a", "b", "d"));

    Collections.sort(list);
    Collections.sort(equalList);
    Assertions.assertTrue(list.equals(equalList));

    Collections.sort(diffList);
    Assertions.assertFalse(list.equals(diffList));

    Assertions.assertTrue(CollectionUtils.isEqualCollection(list, equalList));

    //Additional items

    ArrayList<String> listOne = new ArrayList<>(Arrays.asList("a", "b", "c", "d"));
    ArrayList<String> listTwo = new ArrayList<>(Arrays.asList("a", "e", "b", "f"));

    /*listOne.removeAll(listTwo);
    System.out.println(listOne);*/

    List<String> listOfAdditionalItems = listOne.stream()
        .filter(item -> !listTwo.contains(item))
        .toList();

    Assertions.assertTrue(CollectionUtils.isEqualCollection(List.of("c", "d"), listOfAdditionalItems));

    List<String> listOfAdditionalItems1 = (List<String>) CollectionUtils.removeAll(listOne, listTwo);

    Assertions.assertTrue(CollectionUtils.isEqualCollection(List.of("c", "d"), listOfAdditionalItems1));

    //Missing Items

    /*listTwo.removeAll(listOne);
    System.out.println(listTwo);*/

    List<String> listOfMissingItems = listTwo.stream()
        .filter(item -> !listOne.contains(item))
        .toList();

    Assertions.assertTrue(CollectionUtils.isEqualCollection(List.of("e", "f"), listOfMissingItems));

    List<String> listOfMissingItems1 = (List<String>) CollectionUtils.removeAll(listTwo, listOne);

    Assertions.assertTrue(CollectionUtils.isEqualCollection(List.of("e", "f"), listOfMissingItems1));


    //common items

    /*listOne.retainAll(listTwo);
    System.out.println(listOne);*/

    List<String> listOfCommonItems = listOne.stream()
        .filter(item -> listTwo.contains(item))
        .toList();

    Assertions.assertTrue(CollectionUtils.isEqualCollection(List.of("a", "b"), listOfCommonItems));

    List<String> listOfCommonItems1 = (List<String>) CollectionUtils.intersection(listTwo, listOne);

    Assertions.assertTrue(CollectionUtils.isEqualCollection(List.of("a", "b"), listOfCommonItems1));
  }
}

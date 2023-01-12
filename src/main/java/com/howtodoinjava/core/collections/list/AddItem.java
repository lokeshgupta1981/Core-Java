package com.howtodoinjava.core.collections.list;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Assertions;

public class AddItem {

  public static void main(String[] args) {

    //1

    ArrayList<String> namesList = new ArrayList<>(Arrays.asList("alex", "brian", "charles"));

    namesList.add(1, "Lokesh");

    System.out.println(namesList);

    //2

    ArrayList<String> list = new ArrayList<>(Arrays.asList("a", "b", "c"));

    list.addAll(0, List.of("1", "2", "3"));

    System.out.println(list);

    //3

    Assertions.assertThrows(IndexOutOfBoundsException.class, () -> {
      namesList.add(10, "Lokesh");
    });

  }
}

package com.howtodoinjava.core.collections.list;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.junit.jupiter.api.Assertions;


public class ArrayListSort {

  public static void main(String[] args) {

    //Create ArrayList
    ArrayList<Task> arrayList = new ArrayList<>();

    //Add items
    arrayList.add(new Task(1, "One", true));
    arrayList.add(new Task(2, "Two", false));
    arrayList.add(new Task(3, "Three", true));
    arrayList.add(new Task(4, "Four", false));
    arrayList.add(new Task(5, "Five", true));

    //Sort naturally - 1
    arrayList.sort(Comparator.naturalOrder());

    System.out.println(arrayList);

    arrayList.sort(Comparator.reverseOrder());

    System.out.println(arrayList);

    //Sort naturally - 2
    Collections.sort(arrayList);
    Collections.sort(arrayList, Comparator.reverseOrder());

    //Sort naturally - 3
    List<Task> sortedList = arrayList.stream().sorted().toList();
    sortedList = arrayList.stream().sorted(Collections.reverseOrder()).toList();

    //Custom sort
    Comparator<Task> nameSorter = Comparator.comparing(Task::name);

    Collections.sort(arrayList, nameSorter);

    System.out.println(arrayList);

    Collections.sort(arrayList, nameSorter.reversed());

    arrayList.sort(nameSorter.reversed());
    arrayList.sort(nameSorter.reversed());

    sortedList = arrayList.stream().sorted(nameSorter).toList();
    sortedList = arrayList.stream().sorted(nameSorter.reversed()).toList();

    //filter and stream
    List<Task> list = arrayList.stream().filter(t -> t.status()).sorted().toList();
    list = arrayList.stream().filter(t -> t.status()).sorted(nameSorter).toList();
    System.out.println(list);
    list = arrayList.stream().filter(t -> t.status()).sorted(nameSorter.reversed()).toList();

    //ArrayList length
    ArrayList<Integer> strList = new ArrayList<>();
    Assertions.assertEquals(10, strList.toArray().length); //Initially length = 10


    //Arraylist size
    ArrayList<Integer> numList = new ArrayList<>();
    Assertions.assertEquals(0, numList.size()); //Initially size = 0

    numList.add(1001);
    Assertions.assertEquals(1, numList.size()); //Add increments the size by 1

    numList.add(1002);
    Assertions.assertEquals(2, numList.size()); //Add increments the size by 1

    numList.set(0, 1000);
    Assertions.assertEquals(2, numList.size()); //replace does not change the size

    numList.remove(1002);
    Assertions.assertEquals(1, numList.size()); //remove decrements the size by 1

    numList.clear();
    Assertions.assertEquals(0, numList.size()); //clear resets the size to 0
  }
}

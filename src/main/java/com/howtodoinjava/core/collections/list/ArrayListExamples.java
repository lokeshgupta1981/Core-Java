package com.howtodoinjava.core.collections.list;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

public class ArrayListExamples {

  public static void main(String[] args) {

    //Initialize arraylist
    ArrayList<String> arrayList = new ArrayList<>();
    arrayList.add("a");
    arrayList.addAll(List.of("b", "c", "d"));
    System.out.println(arrayList);

    //Initializes the list with items from the List
    ArrayList<String> listFromItems = new ArrayList<>(List.of("a", "b", "c", "d"));
    System.out.println(listFromItems);
    listFromItems.set(2, "T");
    System.out.println(listFromItems);

    //Remove elements
    ArrayList<String> list = new ArrayList<>(List.of("a", "b", "c", "d"));
    list.remove("c");
    list.removeAll(List.of("b", "d"));
    list.clear();

    //List Iterator
    list = new ArrayList<>(List.of("a", "b", "c", "d"));

    list.forEach(e -> {
      System.out.println(e);
    });

    list.stream().forEach(e -> {
      System.out.println(e);
    });

    ListIterator<String> listIterator = list.listIterator();
    while (listIterator.hasNext()) {

      if (listIterator.next().equalsIgnoreCase("c")) {
        listIterator.remove();
      }
    }

    System.out.println(list);

    //Streams

    ArrayList<Integer> numbersList = new ArrayList<>(List.of(1, 2, 3, 4, 5));

    List<Integer> evenNumList = numbersList.stream()
        .filter(n -> n % 2 == 0)
        .toList();

    ArrayList<Integer> evenNumList2 = numbersList.stream()
        .filter(n -> n % 2 == 0)
        .collect(Collectors.toCollection(ArrayList::new));

    List<Integer> squareList = numbersList.stream()
        .map(n -> n * n)
        .toList();

    System.out.println(squareList);

    //Search
    ArrayList<Integer> numList = new ArrayList<>(List.of(1, 2, 2, 3, 4, 4, 4, 5));

    System.out.println(numList.contains(2));  //true
    System.out.println(numList.contains(8));  //false

    System.out.println(numList.indexOf(4));     //4
    System.out.println(numList.lastIndexOf(4));  //6

    Collections.sort(numList);
    int foundIndex = Collections.binarySearch(numList, 4);

    System.out.println(foundIndex);     //4

    //Sorting
    ArrayList<Integer> arrayList2 = new ArrayList<>(List.of(2, 1, 4, 5, 3));
    Collections.sort(arrayList2);
    System.out.println(arrayList2);

    Comparator<Item> customOrder = Comparator.comparing(Item::getName);
    Collections.sort(arrayList2, Comparator.reverseOrder());
    System.out.println(arrayList2);

    //Thread safety
    List<String> synchronizedList = Collections.synchronizedList(arrayList);

    CopyOnWriteArrayList<String> concurrentList = new CopyOnWriteArrayList<>(arrayList);

    //Sublist
    ArrayList<Integer> origList = new ArrayList<>(List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9));

    List<Integer> subListWithStream = origList.stream()
        .filter(n -> n % 2 == 0)
        .toList();

    

    List<Integer> subList = origList.subList(2, 6);
    System.out.println(subList);
    subList.add(10);
    System.out.println(origList);

  }
}

@Getter
@Setter
class Item implements Comparable<Item> {

  long id;
  String name;

  public Item(long id, String name) {
    this.id = id;
    this.name = name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Item item = (Item) o;
    return id == item.id;
  }


  @Override
  public int compareTo(Item item) {
    if (item.getName() == null || this.getName() == null) {
      return -1;
    }
    return item.getName().compareTo(this.getName());
  }
}
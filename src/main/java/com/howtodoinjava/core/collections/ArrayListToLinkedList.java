package com.howtodoinjava.core.collections;

import java.util.ArrayList;
import java.util.LinkedList;
import org.junit.jupiter.api.Assertions;

public class ArrayListToLinkedList {

  public static void main(String[] args) {
    LinkedList<String > linkedList = new LinkedList();

    //1.1.
    //add items
    linkedList.add("A");
    linkedList.add("B");
    linkedList.add("C");
    linkedList.add("D");

    ArrayList<String> arrayList = new ArrayList<>(linkedList);

    Assertions.assertEquals(4, arrayList.size());

    //1.2
    ArrayList<String> arrayList1 = new ArrayList<>();
    arrayList1.addAll(linkedList);

    Assertions.assertEquals(4, arrayList1.size());

    //2
    LinkedList<String > linkedList1 = new LinkedList(arrayList);

    Assertions.assertEquals(4, linkedList1.size());

  }
}

package com.howtodoinjava.core.collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AddMultipleItems 
{
  public static void main(final String[] args) 
  {
    ArrayList<String> arrayList = new ArrayList<>(Arrays.asList("a", "b"));

    Collections.addAll(arrayList, "c", "d");
    
    System.out.println(arrayList);
    
    //List 1
    List<String> namesList = Arrays.asList( "a", "b", "c");

    //List 2
    ArrayList<String> otherList = new ArrayList<>(Arrays.asList( "d", "e"));

    //Do not add 'a' to the new list
    namesList.stream()
        .filter(name -> !"a".equals(name))
        .forEachOrdered(otherList::add);

    System.out.println(otherList);
  }
}

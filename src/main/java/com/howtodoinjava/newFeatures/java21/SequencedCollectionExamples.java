package com.howtodoinjava.newFeatures.java21;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public class SequencedCollectionExamples {

  public static void main(String[] args) {
    ArrayList<Integer> arrayList = new ArrayList<>();

    arrayList.add(1); 	// List contains: [1]

    arrayList.addFirst(0);	// List contains: [0, 1]
    arrayList.addLast(2);		// List contains: [0, 1, 2]

    Integer firstElement = arrayList.getFirst();	// 0
    Integer lastElement = arrayList.getLast();	// 2

    List<Integer> reversed = arrayList.reversed();
    System.out.println(reversed); // Prints [2, 1, 0]

    //

    LinkedHashSet<Integer> linkedHashSet = new LinkedHashSet<>(List.of(1, 2, 3));

    firstElement = linkedHashSet.getFirst();   // 1
    lastElement = linkedHashSet.getLast();    // 3

    linkedHashSet.addFirst(0);  //List contains: [0, 1, 2, 3]
    linkedHashSet.addLast(4);   //List contains: [0, 1, 2, 3, 4]

    System.out.println(linkedHashSet.reversed());   //Prints [5, 3, 2, 1, 0]

    //

    LinkedHashMap<Integer, String> map = new LinkedHashMap<>();

    map.put(1, "One");
    map.put(2, "Two");
    map.put(3, "Three");

    map.firstEntry();   //1=One
    map.lastEntry();    //3=Three

    System.out.println(map);  //{1=One, 2=Two, 3=Three}

    Map.Entry<Integer, String> first = map.pollFirstEntry();   //1=One
    Map.Entry<Integer, String> last = map.pollLastEntry();    //3=Three

    System.out.println(map);  //{2=Two}

    map.putFirst(1, "One");     //{1=One, 2=Two}
    map.putLast(3, "Three");    //{1=One, 2=Two, 3=Three}

    System.out.println(map);  //{1=One, 2=Two, 3=Three}
    System.out.println(map.reversed());   //{3=Three, 2=Two, 1=One}

    //
    List<Integer> list = List.of(1, 2, 3);
    //list.addLast(4);  //Exception in thread "main" java.lang.UnsupportedOperationException

    TreeSet<Integer> set = new TreeSet<>(List.of(1, 2, 3));
    //set.addFirst(4);  //Exception in thread "main" java.lang.UnsupportedOperationException

    List<Integer> list1 = List.of();
    //list1.getFirst();  //Exception in thread "main" java.lang.NoSuchElementException

    //Collections Class
    //Collections.unmodifiableSequencedCollection(sequencedCollection);
    //Collections.unmodifiableSequencedSet(sequencedSet);
    //Collections.unmodifiableSequencedMap(sequencedMap);
  }

}

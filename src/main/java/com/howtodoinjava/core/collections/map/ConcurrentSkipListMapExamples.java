package com.howtodoinjava.core.collections.map;

import java.util.Iterator;
import java.util.concurrent.ConcurrentSkipListMap;

public class ConcurrentSkipListMapExamples {

  public static void main(String[] args) {
    ConcurrentSkipListMap<Integer, String> map = new ConcurrentSkipListMap<>();
    map.put(1, "A");
    map.put(2, "B");
    map.put(3, "C");
    map.put(4, "D");
    map.put(5, "E");

    System.out.println(map.headMap(3));
    System.out.println(map.tailMap(3));

    System.out.println(map.ceilingKey(3));  //4
    System.out.println(map.higherKey(3));   //4
    System.out.println(map.ceilingKey(4));  //4
    System.out.println(map.higherKey(4));   //5
    System.out.println(map.firstKey());
    System.out.println(map.lastKey());





  }

}

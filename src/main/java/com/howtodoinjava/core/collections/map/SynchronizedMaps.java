package com.howtodoinjava.core.collections.map;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SynchronizedMaps {

  public static void main(String[] args) {
    //1 ConcurrentHashMap

    ConcurrentHashMap<Integer, String> concurrHashMap = new ConcurrentHashMap<>();

    //Put require no synchronization
    concurrHashMap.put(1, "A");
    concurrHashMap.put(2, "B");

    //Get require no synchronization
    concurrHashMap.get(1);

    Iterator<Integer> itr = concurrHashMap.keySet().iterator();

    //Using synchronized block is advisable
    synchronized (concurrHashMap) {
      while (itr.hasNext()) {
        System.out.println(concurrHashMap.get(itr.next()));
      }
    }

    //2 Collections.synchronizedMap()

    Map<Integer, String> syncHashMap = Collections.synchronizedMap(new HashMap<>());

    //Put require no synchronization
    syncHashMap.put(1, "A");
    syncHashMap.put(2, "B");

    //Get require no synchronization
    syncHashMap.get(1);

    Iterator<Integer> iterator = syncHashMap.keySet().iterator();

    //Using synchronized block is advisable
    synchronized (syncHashMap) {
      while (iterator.hasNext()) {
        System.out.println(syncHashMap.get(iterator.next()));
      }
    }
  }
}

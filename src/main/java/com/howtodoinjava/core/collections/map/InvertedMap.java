package com.howtodoinjava.core.collections.map;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;

public class InvertedMap {

  public static void main(String[] args) {
    Map<String, String> map = Map.of("key1", "value1", "key2", "value2");
    System.out.println(map);

    Map<String, Integer> originalMap = new HashMap<>();
    originalMap.put("Key1", 1);
    originalMap.put("Key2", 2);

    Map<Integer, String> invertedMap = new HashMap<>();
    for (Entry<String, Integer> entry : originalMap.entrySet()) {
      invertedMap.put(entry.getValue(), entry.getKey());
    }
    System.out.println(invertedMap);     //{1=Key1, 2=Key2}

    Map<Integer, String> inverseMap2 = originalMap.entrySet()
        .stream()
        .collect(Collectors.toMap(Entry::getValue, Entry::getKey));

    Map<Integer, String> mapWithDuplicateValues = new HashMap<Integer, String>();
    mapWithDuplicateValues.put(1, "Value1");
    mapWithDuplicateValues.put(2, "Value2");
    mapWithDuplicateValues.put(3, "Value2");

    HashMap<String, List<Integer>> inverseMap = new HashMap<String, List<Integer>>();

    for (Entry<Integer, String> entry : mapWithDuplicateValues.entrySet()) {
      if (inverseMap.containsKey(entry.getValue())) {
        inverseMap.get(entry.getValue()).add(entry.getKey());
      } else {
        List<Integer> list = new ArrayList<Integer>();
        list.add(entry.getKey());
        inverseMap.put(entry.getValue(), list);
      }
    }
    System.out.println(inverseMap);

    /*inverseMap = originalMap.entrySet()
        .stream()
        .collect(Collectors.groupingBy(Entry::getValue,
            Collectors.mapping(Entry::getKey, Collectors.toList())));

    System.out.println(inverseMap);*/

    BiMap<Integer, String> biMap = ImmutableBiMap.of(1, "Key1", 2, "Key2", 3, "Key3");
    BiMap<String, Integer> inverseBiMap = biMap.inverse();

    System.out.println(inverseBiMap);              //{Key1=1, Key2=2, Key3=3}

    //
    biMap = HashBiMap.create();
    BiMap<String, Integer> inversedMap = biMap.inverse();
    System.out.println(inversedMap);          //{Key1=1, Key2=2, Key3=3}

    Multimap<Integer, String> multimap = ImmutableMultimap.of(1, "Key1", 1, "Key2", 2, "Key3");
    System.out.println(multimap); //{1=[Key1, Key2], 2=[Key3]}

    BidiMap bidiMap = new DualHashBidiMap();
    bidiMap.put(1, "Value1");
    bidiMap.put(2, "Value2");

    System.out.println(bidiMap.inverseBidiMap());         //{Value1=1, Value2=2}

    Map<Integer, String> hashMap = new HashMap<Integer, String>();
    hashMap.put(1,"Value1");
    hashMap.put(2,"Value2");
    hashMap.put(3,"Value2");

    Map<String,Integer> inversedMap1 = MapUtils.invertMap(hashMap);
    System.out.println(inversedMap1);  //{Value1=1, Value2=3}

  }

}

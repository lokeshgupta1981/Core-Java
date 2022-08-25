package com.howtodoinjava.core.collections.map;

import com.google.common.base.Predicates;
import com.google.common.collect.Maps;

import java.util.*;
import java.util.stream.Collectors;

public class SubMaps {
    public static void main(String[] args) {
        //1
        Map<String, String> map = new HashMap<>();
        map.put("key1", "Value1");
        map.put("key2", "Value2");
        map.put("key3", "Value3");

        List<String> keysToRetain = List.of("key1", "key2");
        map.keySet().retainAll(keysToRetain);

        System.out.println(map);    //{key1=Value1, key2=Value2}

        //2
        TreeMap<String, String> treeMap = new TreeMap<>();

        treeMap.put("key1", "Value1");
        treeMap.put("key2", "Value2");
        treeMap.put("key3", "Value3");

        SortedMap<String, String> subMap = treeMap.subMap("key2", "key3");
        SortedMap<String, String> headMap = treeMap.headMap("key2");
        SortedMap<String, String> tailMap = treeMap.tailMap("key2");

        System.out.println(subMap);
        System.out.println(headMap);
        System.out.println(tailMap);

        subMap.put("key2", "NEW_VALUE_2");

        System.out.println(subMap);     //{key2=NEW_VALUE_2}
        System.out.println(treeMap);    //{key1=Value1, key2=NEW_VALUE_2, key3=Value3}

        treeMap.put("key4", "value4");

        System.out.println(tailMap);     //{key2=NEW_VALUE_2, key3=Value3, key4=value4}
        System.out.println(treeMap);    //{key1=Value1, key2=NEW_VALUE_2, key3=Value3, key4=value4}

        //3
        Map<Integer, String> hashmap = new HashMap<>();
        hashmap.put(1, "Value1");
        hashmap.put(2, "Value2");
        hashmap.put(3, "Value3");

        List<Integer> keysList = Arrays.asList(1, 2);

        Map<Integer, String> subHashmap = hashmap.entrySet()
                .stream()
                .filter(x -> keysList.contains(x.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        System.out.println(subHashmap);     //{1=Value1, 2=Value2}
        System.out.println(hashmap);

        //4
        Map<Integer, String> subMap1 = Maps.filterKeys(hashmap, Predicates.in(keysList));
        System.out.print(subMap1);
    }
}

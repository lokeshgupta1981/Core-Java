package com.howtodoinjava.core.collections.map;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class HashMapExamples {

  public static void main(String[] args) {

    HashMap<Integer, Item> map = new HashMap<>();
    map.put(1, new Item(1, "Name"));

    HashMap<Integer, Item> copiedMap = new HashMap<>(map);
    copiedMap.get(1).setName("Modified Name");

    //The value is modified in both maps
    System.out.println(map.get(1));         // Item(id=1, name=Modified Name)
    System.out.println(copiedMap.get(1));   // Item(id=1, name=Modified Name)

    HashMap<String, String> hashmap = new HashMap<>();

   /* hashmap.put("+1", "USA");   // stores USA and associates with key +1
    hashmap.put("+1", "United States");  // Overwrites USA with United States

    hashmap.remove("+1");  //returns United States*/

    hashmap.put("+1", "USA");
    hashmap.put("+91", "India");

    for (String key : hashmap.keySet()) {

      System.out.println("Key: " + key);
      System.out.println("Value: " + hashmap.get(key));
    }

    for (Map.Entry<String, String> entry : hashmap.entrySet()) {

      System.out.println("Key: " + entry.getKey());
      System.out.println("Value: " + entry.getValue());
    }

    hashmap.forEach((key, value) -> System.out.println(key + ": " + value));

    Stream<Item> stream = Stream.of(new Item(1, "Item 1"), new Item(2, "Item 2"));

    Map<Long, String> itemMap = stream.collect(
        Collectors.toMap(Item::getId, Item::getName, (existing, replacement) -> existing,
            HashMap::new)
    );

    System.out.println(itemMap);  // {1=Item 1, 2=Item 2}

    //

    HashMap<String, Integer> nameMap = new HashMap<>();

    /*for (Map.Entry<String, Integer> entry : nameMap.entrySet()) {
      if (entry.getKey().startsWith("Temp-")) {

        nameMap.remove(entry.getKey()); // throws ConcurrentModificationException
      }
    }*/

    Iterator<Entry<String, Integer>> iterator = nameMap.entrySet().iterator();

    while (iterator.hasNext()) {
      Map.Entry<String, Integer> entry = iterator.next();
      if (entry.getKey().equals("Temp-")) {

        iterator.remove();  // Safely remove the element
      }
    }

    String[] items = {"apple", "banana", "orange", "apple", "grape", "banana", "apple"};

    HashMap<String, Integer> itemOccurrences = new HashMap<>();

    for (String item : items) {
      itemOccurrences.put(item, itemOccurrences.getOrDefault(item, 0) + 1);
    }

    for (String item : itemOccurrences.keySet()) {
      System.out.println(itemOccurrences);
    }
  }



}

@NoArgsConstructor
@AllArgsConstructor
@Data
class Item {

  private long id;
  private String name;
}

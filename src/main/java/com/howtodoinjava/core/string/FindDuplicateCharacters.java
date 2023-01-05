package com.howtodoinjava.core.string;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane.SystemMenuBar;
import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.BagUtils;
import org.apache.commons.collections4.bag.HashBag;


public class FindDuplicateCharacters {

  public static void main(String[] args) {
    String input = "howtodoinjava";

    Map<Character, Integer> bag = getCharBag(input);

    //duplicate chars
    List duplicateChars = bag.keySet()
        .stream()
        .filter(k -> bag.get(k) > 1)
        .collect(Collectors.toList());
    System.out.println(duplicateChars); //[a, o]

    //duplicate chars with count
    Map<Character, Integer> duplicateCharsWithCount = bag.entrySet()
        .stream()
        .filter(e -> bag.get(e.getKey()) > 1)
        .collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));

    System.out.println(duplicateCharsWithCount); //{a=2, o=3}

    //unique chars
    List uniqueChars = bag.keySet()
        .stream()
        .filter(k -> bag.get(k) == 1)
        .collect(Collectors.toList());  //[t, d, v, w, h, i, j, n]
    System.out.println(uniqueChars);

    //Guava
    Multiset multiset = HashMultiset.create();

    for (char c : input.toCharArray()) {
      multiset.add(c);
    }

    Map<Character, Integer> duplicateCharsWithCount1 = (Map<Character, Integer>) multiset.elementSet()
        .stream()
        .filter(k -> multiset.count(k) > 1)
        .collect(Collectors.toMap(p -> p, p -> multiset.count(p)));

    System.out.println(duplicateCharsWithCount1);

  }

  public static Map<Character, Integer> getCharBag(String input) {

    Map<Character, Integer> map = new HashMap<>();

    if (input == null || input.length() == 0) {
      return map;
    }

    for (char c : input.toCharArray()) {

      if (map.containsKey(c)) {
        int counter = map.get(c);
        map.put(c, ++counter);
      } else {
        map.put(c, 1);
      }
    }
    return map;
  }

}

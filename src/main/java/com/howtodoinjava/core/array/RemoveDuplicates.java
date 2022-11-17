package com.howtodoinjava.core.array;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class RemoveDuplicates {

  public static void main(String[] args) {
    usingHashMap();
    System.out.println("==========");
    usingArrayList();
    /*System.out.println("==========");
    fromSortedArray();
    System.out.println("==========");
    fromUnsortedArray();*/
  }

  private static void usingHashMap() {
    Integer[] numArray = new Integer[]{1, 2, 3, 4, 5, 1, 3, 5};

    /*Map<Integer, Long> map = new HashMap<>();
    for (int i : numArray) {
      if (map.containsKey(i)) { //this element is in the map already
        map.put(i, map.get(i) + 1);
      } else { //found a new element
        map.put(i, 1L);
      }
    }*/

    Map<Integer, Long> map = Arrays.stream(numArray)
        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

    System.out.println(map);

    long duplicateCount = map.keySet()
        .stream()
        .filter(k -> map.get(k) > 1)
        .collect(Collectors.counting());

    System.out.println("Count of duplicate elements : " + duplicateCount);

    Integer[] duplicateElementsArray = map.keySet()
        .stream()
        .filter(k -> map.get(k) > 1)
        .toArray(Integer[]::new);

    System.out.println(
        "Duplicate elements in the array : " + Arrays.toString(duplicateElementsArray));

    Integer[] uniqueElementsArray = map.keySet()
        .stream()
        .filter(k -> map.get(k) == 1)
        .toArray(Integer[]::new);

    System.out.println("Unique elements in the array : " + Arrays.toString(uniqueElementsArray));
  }

  private static void usingArrayList() {
    Integer[] numArray = new Integer[]{1, 2, 3, 4, 5, 1, 3, 5};

    Set<Integer> distinctElementsSet = new HashSet<>();
    Integer[] duplicateElementsArray = Arrays.stream(numArray)
        .filter(e -> !distinctElementsSet.add(e))
        .toArray(Integer[]::new);

    System.out.println("Duplicate elements in the array : " + Arrays.toString(duplicateElementsArray));

    int duplicateCount = duplicateElementsArray.length;

    System.out.println("Count of duplicate elements : " + duplicateCount);

    distinctElementsSet.removeAll(Arrays.asList(duplicateElementsArray));
    Integer[] uniqueElementsArray = distinctElementsSet.toArray(Integer[]::new);

    System.out.println("Unique elements in the array : " + Arrays.toString(uniqueElementsArray));
  }

  /*private static void fromSortedArray() {
    Integer[] origArray = new Integer[]{1, 1, 2, 3, 3, 3, 4, 5, 6, 6, 6, 7, 8};

    Integer[] tempArray = removeDuplicatesFromSortedArray(origArray);
    System.out.println(Arrays.toString(tempArray));
  }

  private static void fromUnsortedArray() {
    Integer[] origArray = new Integer[]{3, 1, 2, 6, 3, 1, 4, 5, 3, 6, 8, 7, 7};

    Integer[] tempArray = removeDuplicatesFromSortedArray(origArray);
    System.out.println(Arrays.toString(tempArray));
  }

  private static Integer[] removeDuplicatesFromSortedArray(Integer[] origArray) {

    Integer[] tempArray = new Integer[origArray.length];

    int indexJ = 0;
    for (int indexI = 0; indexI < origArray.length - 1; indexI++) {
      Integer currentElement = origArray[indexI];

      if (currentElement != origArray[indexI + 1]) {
        tempArray[indexJ++] = currentElement;
      }
    }
    tempArray[indexJ] = origArray[origArray.length - 1];
    return tempArray;
  }

  private static Integer[] removeDuplicatesFromUnsortedArray(Integer[] origArray) {
    for (int i = 0; i < origArray.length - 1; i++) {
      if (origArray[i] == origArray[i + 1]) {
        origArray[i] = null;
      }
    }
    return origArray;
  }*/
}

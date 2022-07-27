package com.howtodoinjava.core.collections.list;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MergeArrayLists {
    public static void main(String[] args) {
        ArrayList<String> listOne = new ArrayList<>(Arrays.asList("a", "b", "c"));

        ArrayList<String> listTwo = new ArrayList<>(Arrays.asList("c", "d", "e"));

        listOne.addAll(listTwo);  //Merge both lists

        System.out.println(listOne);
        System.out.println(listTwo);

        //2

        //Re-initialize
        listOne = new ArrayList<>(Arrays.asList("a", "b", "c"));

        List<String> combinedList = Stream.of(listOne, listTwo)
                .flatMap(x -> x.stream())
                .collect(Collectors.toList());

        System.out.println(combinedList);

        //3

        //1
        //Re-initialize
        listOne = new ArrayList<>(Arrays.asList("a", "b", "c"));

        //Add items from Lists into Set
        Set<String> set = new LinkedHashSet<>(listOne);
        set.addAll(listTwo);

        //Convert Set to ArrayList
        combinedList = new ArrayList<>(set);

        System.out.println(combinedList);

        //2

        List<String> listTwoCopy = new ArrayList<>(listTwo);
        listTwoCopy.removeAll(listOne);
        listOne.addAll(listTwoCopy);

        System.out.println(listOne);
    }
}

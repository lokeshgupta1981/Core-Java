package com.howtodoinjava.core.collections.map;

import java.util.HashMap;

public class MergeMaps {

    public static void main(String[] args) {
        //map 1
        HashMap<Integer, String> firstMap = new HashMap<>();
        firstMap.put(1, "A");
        firstMap.put(2, "B");
        firstMap.put(3, "C");
        firstMap.put(4, "D");

        //map 2
        HashMap<Integer, String> secondMap = new HashMap<>();
        secondMap.put(4, "F"); //It will replace D with F
        secondMap.put(5, "G"); //A new pair to be added

        //1

        //Merge maps without duplicates
        firstMap.putAll(secondMap);
        System.out.println(firstMap);

        //2
        //Re-initialize map for next example
        HashMap<Integer, String>  thirdMap = new HashMap<>();

        thirdMap.put(1, "A");
        thirdMap.put(2, "B");
        thirdMap.put(3, "C");
        thirdMap.put(4, "D");

        /*secondMap.forEach(
                (key, value) -> thirdMap.merge(key, value, (v1, v2) -> v1.equalsIgnoreCase(v2) ? v1 : v1 + "," + v2)
        );*/

        secondMap.forEach(
                (key, value) -> thirdMap.merge(key, value, String::concat));
        System.out.println(thirdMap);
    }
}

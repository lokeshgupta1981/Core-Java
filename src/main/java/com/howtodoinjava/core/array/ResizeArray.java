package com.howtodoinjava.core.array;

import java.util.ArrayList;
import java.util.Arrays;

public class ResizeArray {
    public static void main(String[] args) {
        String[] originalArray = {"A", "B", "C", "D", "E"};
        
        //1
        String[] resizedArray = Arrays.copyOf(originalArray, 10);
        resizedArray[5] = "F";
        System.out.println(Arrays.toString(resizedArray));
        
        //2
        ArrayList<String> list = new ArrayList<>(Arrays.asList(originalArray));
        list.add("F");
        System.out.println(list);
    }
}

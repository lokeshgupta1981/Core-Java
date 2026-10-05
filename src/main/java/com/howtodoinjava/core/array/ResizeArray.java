package com.howtodoinjava.core.array;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Demonstrates "resizing" a fixed-length array: arrays cannot be resized in
 * place, so a larger array is created with {@link Arrays#copyOf}, or the
 * contents are copied into a growable {@link ArrayList} instead.
 */
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

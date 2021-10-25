package com.howtodoinjava.core.array;

import java.util.Arrays;
import java.util.LinkedHashSet;

public class RemoveDuplicates 
{
	public static void main(String[] args)
	{
		usingLinkedHashSet();
		usingTempArray();
		fromUnsortedArray();
	}
	
	private static void usingLinkedHashSet()
	{
		//Array with duplicate elements
        Integer[] numbers = new Integer[] {1,2,3,4,5,1,3,5};
         
        //This array has duplicate elements
        System.out.println( Arrays.toString(numbers) );
         
        //Create set from array elements
        LinkedHashSet<Integer> linkedHashSet = new LinkedHashSet<>( Arrays.asList(numbers) );
         
        //Get back the array without duplicates
        Integer[] numbersWithoutDuplicates = linkedHashSet.toArray(new Integer[] {});
         
        //Verify the array content
        System.out.println( Arrays.toString(numbersWithoutDuplicates) );
	}
	
	private static void usingTempArray()
	{
		// Array with duplicate elements
        Integer[] origArray = new Integer[] { 1, 1, 2, 3, 3, 3, 4, 5, 6, 6, 6, 7, 8 };
 
        // This array has duplicate elements
        System.out.println(Arrays.toString(origArray));
 
        Integer[] tempArray = removeDuplicates(origArray);
 
        // Verify the array content
        System.out.println(Arrays.toString(tempArray));
	}
	
	private static void fromUnsortedArray()
	{
		// Array with duplicate elements
        Integer[] origArray = new Integer[] { 1, 1, 2, 3, 3, 3, 4, 5, 6, 6, 6, 7, 8 };
 
        // This array has duplicate elements
        System.out.println(Arrays.toString(origArray));
 
        Integer[] tempArray = removeDuplicates2(origArray);
 
        // Verify the array content
        System.out.println(Arrays.toString(tempArray));
	}
	
	private static Integer[] removeDuplicates(Integer[] origArray) {
		 
        Integer[] tempArray = new Integer[origArray.length];
         
        int indexJ = 0;
        for (int indexI = 0; indexI < origArray.length - 1; indexI++)
        {
            Integer currentElement = origArray[indexI];
             
            if (currentElement != origArray[indexI+1]) {
                tempArray[indexJ++] = currentElement;
            }
        }
         
        tempArray[indexJ] = origArray[origArray.length-1];
         
        return tempArray;
    }
	
	private static Integer[] removeDuplicates2(Integer[] origArray) {
		 
        for (int i = 0; i < origArray.length - 1; i++)
        {
            if (origArray[i] == origArray[i + 1]) {
                origArray[i] = null;
            }
        }
         
        return origArray;
    }
}

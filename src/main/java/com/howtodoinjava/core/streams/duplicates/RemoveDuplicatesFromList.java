package com.howtodoinjava.core.streams.duplicates;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class RemoveDuplicatesFromList 
{
	public static void main(String[] args) 
	{
		Integer[] origArray = new Integer[] { 1, 2, 4, 9, 7, 3, 4, 5, 8, 6, 1, 7, 8 };
		
		// ArrayList with duplicate elements
        ArrayList<Integer> numbersList 
        	= new ArrayList<>();
        
        //1 - remove duplicates
        List<Integer> listWithoutDuplicates = Arrays.asList(origArray).stream()
    			.distinct()
    			.collect(Collectors.toList());
     
        System.out.println(listWithoutDuplicates);
         
        //2 - remove duplicates
        Set<Integer> setWithoutDuplicates = numbersList.stream()
        			.collect(Collectors.toSet());
         
        System.out.println(setWithoutDuplicates);
        
        //3 - count duplicates
        Map<Integer, Long> elementCountMap = numbersList.stream()
        		.collect(Collectors.toMap(Function.identity(), v -> 1L, Long::sum));
        
        System.out.println(elementCountMap);
	
	}
}

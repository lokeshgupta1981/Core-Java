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
		// ArrayList with duplicate elements
        ArrayList<Integer> numbersList 
        	= new ArrayList<>(Arrays.asList(1, 1, 2, 3, 3, 3, 4, 5, 6, 6, 6, 7, 8));
        
        //1 - remove duplicates
        List<Integer> listWithoutDuplicates = numbersList.stream()
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

package com.howtodoinjava.core.predicate;

import java.util.List;
import java.util.function.IntPredicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class IntPredicateExample 
{
	public static void main(String[] args) 
	{
		IntPredicate isOdd = argument -> argument % 2 == 1;
		
		IntStream stream = IntStream.range(1, 20); 
		
		List<Integer> oddNumbers = stream.filter(isOdd.and(IntPredicateExample::isPrime))
									.boxed()
									.collect(Collectors.toList());
		
		System.out.println(oddNumbers);
	}
	
	public static boolean isPrime(int i) 
	{
        IntPredicate isDivisible = index -> i % index == 0;
        return i > 1 && IntStream.range(2, i).noneMatch(isDivisible);
	}
}

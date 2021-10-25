package com.howtodoinjava.core.streams.intstream;

import java.util.List;
import java.util.function.IntPredicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FilterExample 
{
	public static void main(String[] args) 
	{
		IntStream stream = IntStream.range(1, 100); 
		
		List<Integer> primes = stream.filter(FilterExample::isPrime)
									.boxed()
									.collect(Collectors.toList());
		
		System.out.println(primes);
	}
	
	public static boolean isPrime(int i) 
	{
        IntPredicate isDivisible = index -> i % index == 0;
        return i > 1 && IntStream.range(2, i).noneMatch(isDivisible);
	}
}

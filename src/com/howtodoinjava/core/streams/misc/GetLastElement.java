package com.howtodoinjava.core.streams.misc;

import java.util.Arrays;
import java.util.stream.Stream;

import com.google.common.collect.Streams;

public class GetLastElement 
{
	public static void main(String[] args) 
	{
		Stream<Integer> stream = Arrays.asList(1,2,3,4,5,6,7,8,9)
										.stream();
		
		//1. Java Stream API
		
		Integer lastElement = stream.reduce((first, second) -> second)
				.orElseThrow(() -> new IllegalStateException("no last element"));
		 	
		 System.out.println(lastElement);
		
		//2. Google Guava Library 
		
		//The stream of elements
		Stream<Integer> stream2 = Arrays.asList(1,2,3,4,5,6,7,8,9)
											.stream();
		
		lastElement = Streams.findLast(stream2).orElse(-1);
		
		System.out.println(lastElement);
		
		//3. Infinite streams needs to have limit
		
		Stream<Integer> infiniteStream = Stream.iterate(0, n -> n + 2);
		
		lastElement = infiniteStream.limit(100)
						.reduce((first, second) -> second)
						.orElse(-1);
		
		System.out.println(lastElement);
	}
}

package com.howtodoinjava.core.streams.conversions;

import java.util.Arrays;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class IterableToStream 
{
	public static void main(String[] args) 
	{
		//Iterable
		Iterable<String> iterable = Arrays.asList("a", "b", "c");
		
		//Iterable -> Stream
		Stream<String> stream = StreamSupport.stream(iterable.spliterator(), false);
		
		//Apply stream operations
		stream.forEach(System.out::println);
	}
}

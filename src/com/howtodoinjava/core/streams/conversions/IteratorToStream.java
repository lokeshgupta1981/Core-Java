package com.howtodoinjava.core.streams.conversions;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class IteratorToStream 
{
	public static void main(String[] args) 
	{
		// Iterator
		Iterator<String> iterator = Arrays.asList("a", "b", "c")
											.listIterator();
		
		//Works in Java 9
		
		/*Stream.generate(() -> null)
	    .takeWhile(x -> iterator.hasNext())
	    .map(n -> iterator.next())
	    .forEach(System.out::println);*/
	}
}

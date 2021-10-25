package com.howtodoinjava.core.streams;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main 
{
	public static void main(final String[] args) 
	{
		String message = "There are %d elements in the stream %n";
		
		long count = Stream.of("how", "to", "do", "in", "java").collect(Collectors.counting());
		System.out.printf(message, count);

		count = Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9).collect(Collectors.counting());
		System.out.printf(message, count);

		count = Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9).filter(i -> i % 2 == 0).collect(Collectors.counting());
		System.out.printf(message, count);
	}
}
package com.howtodoinjava.core.streams.intstream;

import java.util.stream.IntStream;

public class ForEachExample 
{
	public static void main(String[] args) 
	{
		//Normal loop
		
		for(int i=0; i < 5; i++)
		{
			doSomething(i);
		}
		
		//IntStream forEach
		
		IntStream.rangeClosed(0, 4)
			.forEach( ForEachExample::doSomething );
	}
	
	private static void doSomething(int i) {
		System.out.println(i);
	}
}

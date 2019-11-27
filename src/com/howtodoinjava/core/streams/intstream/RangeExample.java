package com.howtodoinjava.core.streams.intstream;

import java.util.stream.IntStream;

public class RangeExample 
{
	public static void main(String[] args) 
	{
		//Range
		IntStream stream = IntStream.range(5, 10); 
		stream.forEach( System.out::println );	//5,6,7,8,9
		
		//Closed Range
		IntStream closedRangeStream = IntStream.rangeClosed(5, 10); 
		closedRangeStream.forEach( System.out::println );	//5,6,7,8,9,10
	}
}

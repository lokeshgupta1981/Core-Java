package com.howtodoinjava.core.basic;

import java.util.ArrayList;
import java.util.Iterator;

public class WhileLoopExample {

	public static void main(String[] args) 
	{
		//1
		int counter = 1;
		while (counter <= 5) 
		{
		    System.out.println(counter);
		    counter++;
		}
		
		//2
		ArrayList<Integer> list = new ArrayList<>();
        
		list.add(1);
		list.add(2);
		list.add(3);

		Iterator<Integer> iterator = list.iterator();

		while (iterator.hasNext()) 
		{
		    System.out.println(iterator.next());
		}
		
		//3
		Integer[] idArray = new Integer[] {1,2,3,4,5}; 
		int index = 0;
		while (index < idArray.length) 
		{
		    System.out.println(idArray[index]);
		}
	}
}

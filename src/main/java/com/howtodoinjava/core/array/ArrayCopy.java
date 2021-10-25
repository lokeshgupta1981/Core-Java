package com.howtodoinjava.core.array;

import java.util.Arrays;

public class ArrayCopy 
{
	public static void main(String[] args) 
	{
		String[] names = {"Alex", "Brian", "Charles", "David"};
		
		System.out.println(Arrays.toString(names));         //[Alex, Brian, Charles, David]
        
		String[] cloneOfNames = names.clone();
		
		System.out.println(Arrays.toString(cloneOfNames));  //[Alex, Brian, Charles, David]
		 
		String[] copyOfNames = Arrays.copyOf(names, names.length);
		
		System.out.println(Arrays.toString(copyOfNames));  //[Alex, Brian, Charles, David]
		 
		String[] copyOfNames2 = new String[names.length];
		System.arraycopy(names, 0, copyOfNames2, 0, copyOfNames2.length);
		
		System.out.println(Arrays.toString(copyOfNames2));   //[Alex, Brian, Charles, David]
	}
}

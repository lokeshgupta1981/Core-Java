package com.howtodoinjava.core.array;

import java.util.Arrays;

public class Print2dArray 
{
	public static void main(String[] args) 
    {
        int [][] cordinates = { {1,2}, {2,4}, {3,6,9} };

        System.out.println( Arrays.deepToString( cordinates ) );
    }
}

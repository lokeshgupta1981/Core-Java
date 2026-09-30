package com.howtodoinjava.core.array;

import java.util.Arrays;

/**
 * Demonstrates printing a jagged 2D array using
 * {@link Arrays#deepToString(Object[])}.
 */
public class Print2dArray
{
	public static void main(String[] args) 
    {
        int [][] cordinates = { {1,2}, {2,4}, {3,6,9} };

        System.out.println( Arrays.deepToString( cordinates ) );
    }
}

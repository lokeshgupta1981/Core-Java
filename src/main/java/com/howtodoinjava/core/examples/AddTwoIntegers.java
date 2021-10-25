package com.howtodoinjava.core.examples;

import java.util.Scanner;

public class AddTwoIntegers 
{
	public static void main(String[] args) 
	{
		int firstInteger = 0;
		int secondInteger = 0;
		
		Scanner sc = new Scanner(System.in);
        System.out.println("Enter First Number: ");
        firstInteger = sc.nextInt();
        
        System.out.println("Enter Second Number: ");
        secondInteger = sc.nextInt();
        
        sc.close();
        
		int sum = firstInteger + secondInteger;
				
		System.out.println("The sum of both integers: " + sum);
	}
}

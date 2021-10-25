package com.howtodoinjava.core.examples;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CalculateAverage 
{
	public static void main(String[] args) 
	{
		System.out.println("How many numbers you want to enter?");
		Scanner scanner = new Scanner(System.in);
		//this is number of input values user will provide in console
		int count = scanner.nextInt();	
		
		double[] array = new double[count];
		double sum = 0;

		for (int i = 0; i < array.length; i++) {
			System.out.print("Enter input number " + (i + 1) + ": ");
			array[i] = scanner.nextDouble();
		}
		scanner.close();
		
		for (int i = 0; i < array.length; i++) {
			sum = sum + array[i];
		}

		//Get the average here
		double average = sum / count;

		System.out.format("The average is: %.2f", average);
		
		//Stream
		
		List<Double> list = new ArrayList<>(List.of(1.1, 2.2, 3.3, 4.4, 5.5));
		
		Double averageValue = list.stream()
								.mapToDouble(x -> x)
								.average()
								.orElse(Double.NaN);
		
		System.out.println(averageValue);
	}
}

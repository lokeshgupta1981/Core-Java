package com.howtodoinjava.core.basic;

import java.io.Console;
import java.io.Reader;
import java.util.Scanner;

public class ConsoleExamples 
{
	public static void main(String[] args) 
	{
		Console console = System.console();
		
		if(console == null) {
			System.out.println("Console is not available to current JVM process");
			return;
		} 
		
		//1
		String userName = console.readLine("Enter the username: ");
		System.out.println("Entered username: " + userName);
		
		//2
		char[] password = console.readPassword("Enter the password: ");
		System.out.println("Entered password: " + new String(password));
		
		//3
		Reader consoleReader = console.reader();
		Scanner scanner = new Scanner(consoleReader);
		
		System.out.println("Enter age:");
		int age = scanner.nextInt();
		System.out.println("Entered age: " + age);
		
		scanner.close();
		
		//write to console
		console.printf("My name is %s and my age is %d", userName, age);
	}
}

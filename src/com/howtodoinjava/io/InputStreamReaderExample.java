package com.howtodoinjava.io;

import java.io.FileInputStream;
import java.io.InputStreamReader;

public class InputStreamReaderExample 
{
	public static void main(String[] args) 
	{
		//1

		try (InputStreamReader input 
				= new InputStreamReader(new FileInputStream("demo.txt"))) {

			// Creates an array of character
			char[] array = new char[50];
			
			// Reads characters from the file
			input.read(array);

			System.out.println(array);
		}
		catch (Exception e) {
			e.getStackTrace();
		}
		
		//2
		try (InputStreamReader input 
				= new InputStreamReader(new FileInputStream("demo.txt"))) {

			int data = input.read();  
            while (data != -1) {  
                System.out.print((char) data);  
                data = input.read();  
            }  
		}
		catch (Exception e) {
			e.getStackTrace();
		}
	}
}

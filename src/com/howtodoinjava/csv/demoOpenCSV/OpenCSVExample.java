package com.howtodoinjava.csv.demoOpenCSV;

import java.io.FileReader;
import java.io.IOException;

import com.opencsv.CSVReader;

public class OpenCSVExample {
	
	public static void main(String[] args) 
	{
		CSVReader reader = null;
		try 
		{
			//Get the CSVReader instance with specifying the delimiter to be used
			reader = new CSVReader(new FileReader("C:\\Lokesh\\Setup\\workspace\\PlayingWithCSV\\src\\SampleCSVFile.csv"));
			String [] nextLine;
			//Read one line at a time
		    while ((nextLine = reader.readNext()) != null) 
		    {
		    	for(String token : nextLine)
				{
					//Print all tokens
					System.out.println(token);
				}
		    }
		}
	    catch (Exception e) {
			e.printStackTrace();
		}
		finally	{
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

package com.howtodoinjava.io;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileWriterExample 
{
	public static void main(String[] args) 
							throws IOException 
	{
		String fileName = "dataOut.txt";
		
		//1
		try (FileWriter fw 
				= new FileWriter(new File(fileName))) {

			fw.write("humpty dumpty");
		}
		
		//2
		try (FileWriter fw 
				= new FileWriter(new File(fileName), true)) {

			fw.append(" sat on a wall");
		}
	}
}

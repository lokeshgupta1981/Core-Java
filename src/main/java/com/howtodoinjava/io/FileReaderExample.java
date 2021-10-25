package com.howtodoinjava.io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FileReaderExample 
{
	public static void main(String[] args) throws IOException
	{
		String fileName = "demo.txt";
		
		//1
		FileReader fileReader = new FileReader(fileName);
		
	    try {
	    	char [] a = new char[2048];
			fileReader.read(a);   
		      
		    for(char c : a) {
		    	System.out.print(c);   
		    }
		} finally {
			fileReader.close();
		}
	    
	    //2
	    fileReader = new FileReader(fileName);
	    
	    try {
	    	 int i;    
	         while((i = fileReader.read()) != -1) {
	         	System.out.print((char)i);    
	         }
		} finally {
			fileReader.close();
		}
		
	    //3
	    BufferedReader br = new BufferedReader(new FileReader(fileName));
		
		try {
		    String line;
		    while ((line = br.readLine()) != null) {
		       System.out.println(line);    
		    }
		} finally {
		    br.close();
		}
	}
}

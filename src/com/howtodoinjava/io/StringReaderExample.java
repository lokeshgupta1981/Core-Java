package com.howtodoinjava.io;

import java.io.IOException;
import java.io.StringReader;

public class StringReaderExample 
{
	public static void main(String[] args) throws IOException 
	{
		String data = "humpty dumpty";
		try (StringReader stringReader 
				= new StringReader(data)) 
		{
			int ch = stringReader.read();
			while (ch != -1) 
			{
				// do something with read char
				System.out.print((char)ch);
				ch = stringReader.read();
			}
		}
	}
}

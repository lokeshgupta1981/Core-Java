package com.howtodoinjava.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ReadFileFromResourcesUsingGetResourceAsStream {
    public static void main(final String[] args) throws IOException 
    {
        //Creating instance to avoid static member methods
        ReadFileFromResourcesUsingGetResourceAsStream instance 
            = new ReadFileFromResourcesUsingGetResourceAsStream();

        InputStream is = instance.getFileAsIOStream("demo.txt");
        instance.printFileContent(is);
        
        is = instance.getFileAsIOStream("data/demo.txt");
        instance.printFileContent(is);
    }

    private InputStream getFileAsIOStream(final String fileName) 
    {
        InputStream ioStream = this.getClass()
            .getClassLoader()
            .getResourceAsStream(fileName);
        
        if (ioStream == null) {
            throw new IllegalArgumentException(fileName + " is not found");
        }
        return ioStream;
    }

    private void printFileContent(InputStream is) throws IOException 
    {
        try (InputStreamReader isr = new InputStreamReader(is); 
                BufferedReader br = new BufferedReader(isr);) 
        {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
            is.close();
        }
    }
}

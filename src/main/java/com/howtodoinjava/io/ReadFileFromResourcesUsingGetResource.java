package com.howtodoinjava.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;

public class ReadFileFromResourcesUsingGetResource {
    public static void main(final String[] args) throws IOException 
    {
        //Creating instance to avoid static member methods
        ReadFileFromResourcesUsingGetResource instance 
            = new ReadFileFromResourcesUsingGetResource();

        File file = instance.getResourceFile("demo.txt");
        instance.printFileContent(file);
        
        file = instance.getResourceFile("data/demo.txt");
        instance.printFileContent(file);
    }

    private File getResourceFile(final String fileName) 
    {
        URL url = this.getClass()
            .getClassLoader()
            .getResource(fileName);
        
        if(url == null) {
            throw new IllegalArgumentException(fileName + " is not found 1");
        }
        
        File file = new File(url.getFile());
        
        return file;
    }

    private void printFileContent(File file) throws IOException 
    {
        String content = new String(Files.readAllBytes(file.toPath()));
        System.out.println(content);
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

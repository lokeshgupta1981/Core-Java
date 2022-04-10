package com.howtodoinjava.io;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class CreateTempFile {
  public static void main(final String[] args) {
    //1
    File temp;
    try
    {
       temp = File.createTempFile("testData", ".txt");
       System.out.println("Temp file created : " + temp.getAbsolutePath());
    } 
    catch (IOException e)
    {
       e.printStackTrace();
    }
    
    //2
    try
    {
       final Path path = Files.createTempFile("myTempFile", ".txt");
       System.out.println("Temp file : " + path);
       
       //Delete file on exit
       path.toFile().deleteOnExit();
       
    } catch (IOException e)
    {
       e.printStackTrace();
    } 
  }
}

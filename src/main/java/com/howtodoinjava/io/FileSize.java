package com.howtodoinjava.io;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.apache.commons.io.FileUtils;

public class FileSize {
  public static void main(final String[] args) throws IOException {
    File file = new File("c:/temp/demo.txt");
    
    //1
    long bytes = Files.size(file.toPath());
    
    System.out.println(String.format("%,d Bytes", bytes));
    System.out.println(String.format("%,d KB", bytes / 1024));
    
    //2
    bytes = file.length();
    System.out.println(String.format("%,d Bytes", bytes));
    
    //3
    bytes = FileUtils.sizeOf(file);
    System.out.println(String.format("%,d Bytes", bytes));
    
    String displaySize = FileUtils.byteCountToDisplaySize(bytes);
    System.out.println(displaySize);

    System.out.println(FileUtils.byteCountToDisplaySize(2333444l));  //2 MB
    System.out.println(FileUtils.byteCountToDisplaySize(2333444555l));  //2 GB
  }
}

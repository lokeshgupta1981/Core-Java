package com.howtodoinjava.io;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class CheckIfFileExists {
  public static void main(final String[] args) throws IOException {
    //1
    File tempFile = new File("c:/temp/temp.txt");
    boolean exists = tempFile.exists();
    
    //2
    final Path path = Files.createTempFile("testFile", ".txt");
    Files.exists(path);     //true
    
    Path tempDirectory = Files.createTempDirectory("temp-dir");
    exists = Files.exists(tempDirectory);
  }
}

package com.howtodoinjava.io;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


public class DeleteAFile {
  public static void main(String[] args) throws IOException {
    File file = new File("c:/temp/one.txt");

    //1 File
    boolean deleted  = file.delete();
    file.deleteOnExit();

    //2 Files
    Path path = Path.of("c:/temp/one.txt");
    Files.delete(path);

    Path path2 = Path.of("c:/temp/two.txt");
    Files.deleteIfExists(path);

    //3 FileUtils
    FileUtils.delete(file);

    FileUtils.deleteQuietly(new File("c:/temp"));

    FileUtils.deleteDirectory(new File("c:/temp"));


  }
}

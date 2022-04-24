package com.howtodoinjava.io;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CreateDirectory {
  public static void main(String[] args) throws IOException {
    String dirName = "c:/temp/data";
    Path path = Paths.get(dirName);

    //1
    if (!Files.exists(path)) {
      Files.createDirectory(path);
    }

    Files.createDirectories(path);

    //2
    File newDir = new File("c:/temp/data");
    if(!newDir.exists()) {
      boolean isDirectoryCreated = newDir.mkdir();
    }

    boolean isDirectoryCreated = newDir.mkdirs();
  }
}

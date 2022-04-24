package com.howtodoinjava.io;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CheckEmptyDirectory {
  public static void main(String[] args) throws IOException {

    Path dirPath = Paths.get("C:/temp");

    //1
    boolean isEmptyDirectory = Files.list(dirPath)
                          .findAny()
                          .isPresent();

    //2
    if (Files.isDirectory(dirPath)) {
      try (DirectoryStream<Path> dirStream =
               Files.newDirectoryStream(dirPath)) {
        isEmptyDirectory = !dirStream.iterator().hasNext();
      }
    }
  }
}

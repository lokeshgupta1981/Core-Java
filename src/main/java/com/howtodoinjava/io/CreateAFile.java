package com.howtodoinjava.io;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.apache.commons.io.FileUtils;

public class CreateAFile {
  private final static String TEXT_FILE = "C:/temp/io/textFile.txt";

  @SuppressWarnings("unused")
  public static void main(final String[] args) throws IOException {
    // 1 - NIO Files.createFile()
    Path textFilePath = Paths.get(TEXT_FILE);
    Files.createFile(textFilePath); // throws IOException

    // 2 - File.createNewFile()
    File textFile = new File(TEXT_FILE);
    boolean isFileCreated = textFile.createNewFile(); // throws IOException

    // 3 - FileOutputStream
    try (OutputStream out = new BufferedOutputStream(
            Files.newOutputStream(
               textFilePath, 
               StandardOpenOption.CREATE, 
               StandardOpenOption.APPEND))) {
      // We can write data as byte[]
      //out.write(data, 0, data.length);
    }

    // 4 - Guava
    com.google.common.io.Files.touch(new File(TEXT_FILE));

    // 5 - Apache Commons IO
    FileUtils.touch(new File(TEXT_FILE));
  }
}

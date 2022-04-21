package com.howtodoinjava.io;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class WriteByteArrayToFile {
  public static void main(String[] args) throws IOException {

    byte[] bytes = "testData".getBytes();
    File file = new File("test.txt");

    //1
    Path filePath = Paths.get("/path/file");
    Files.write(filePath, bytes);

    //2
    try (FileOutputStream fos = new FileOutputStream("/path/file")) {
      fos.write(bytes);
    }

    //3
    FileUtils.writeByteArrayToFile(file, bytes);

    //4
    com.google.common.io.Files.write(bytes, file);
  }
}

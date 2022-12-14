package com.howtodoinjava.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class ReadFileToByteArray {

  public static void main(String[] args) throws IOException {
    Path path = Paths.get("C:/temp/test.txt");
    File file = new File("C:/temp/test.txt");

    //1
    byte[] data = Files.readAllBytes(path);

    //2
    byte[] bytes = new byte[(int) file.length()];
    try(FileInputStream fis = new FileInputStream(file)) {
      fis.read(bytes);
    }

    //3
    byte[] bytes1 = FileUtils.readFileToByteArray(file);
    byte[] bytes2 = IOUtils.toByteArray(new FileInputStream(file));

    //4
    byte[] bytes3 = com.google.common.io.Files.toByteArray(file);
  }

}

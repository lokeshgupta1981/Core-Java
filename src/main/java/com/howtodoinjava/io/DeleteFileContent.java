package com.howtodoinjava.io;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DeleteFileContent {
  public static void main(String[] args) {
    File file = new File("/path/file");

    //1
    try(PrintWriter pw = new PrintWriter(file)){
      //Any more operations if required
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    //2
    try(FileWriter fw = new FileWriter(file)){
      //Any more operations if required
    } catch (IOException e) {
      e.printStackTrace();
    }

    //3
    try(BufferedWriter writer = Files.newBufferedWriter(file.toPath())){
      writer.write("");
      writer.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }

    //4
    try{
      FileUtils.write(file, "", StandardCharsets.UTF_8);
    } catch (IOException e) {
      e.printStackTrace();
    }

    //5
    try(RandomAccessFile raf = new RandomAccessFile(file, "rw")){
      raf.setLength(0);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

  }
}

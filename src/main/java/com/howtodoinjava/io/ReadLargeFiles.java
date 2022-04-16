package com.howtodoinjava.io;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class ReadLargeFiles {
  public static void main(String[] args) {
    //1
    File file = new File("C:/temp/file.txt");

    try(LineIterator it = FileUtils.lineIterator(file, "UTF-8")) {
      while (it.hasNext()) {
        String line = it.nextLine();
        // do something with line
        System.out.println(line);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }


    //2
    try (Stream<String> lines = Files.lines(Paths.get("C:/temp/file.txt"))) {
      lines.forEach(line -> {
        // do something with line
        System.out.println(line);
      });
    } catch (IOException e) {
      e.printStackTrace();
    }

  }
}

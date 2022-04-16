package com.howtodoinjava.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class ReadGivenLineFromFile {
  public static void main(String[] args) {
    Path filePath = Paths.get("C:/temp/file.txt");

    //1
    List<String> lines = null;
    try {
      lines = Collections.unmodifiableList(Files.readAllLines(filePath));
    } catch (IOException e) {
      e.printStackTrace();
    }

    //Read second line
    String secondLine = lines.get(1);

    //2
    //try-with-resources
    try (Stream<String> streamOfLines = Files.lines( filePath ))
    {
      String secondLines = streamOfLines.skip(1)
          .findFirst()
          .get();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }
}

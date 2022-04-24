package com.howtodoinjava.io;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

public class CountFileLines {
  public static void main(String[] args) throws IOException {
    String fileName = "c:/temp";
    long noOfLines = -1;

    //1
    try (Stream<String> fileStream = Files.lines(Paths.get(fileName))) {
      noOfLines = (int) fileStream.count();
    }

    //2
    List<String> lines = Files.readAllLines(Paths.get(fileName));
    noOfLines = lines.size();

    //3
    try(LineNumberReader lineNumberReader =
        new LineNumberReader(new FileReader(new File(fileName)))) {
      lineNumberReader.skip(Long.MAX_VALUE);
      noOfLines = lineNumberReader.getLineNumber() + 1;
    }
  }
}

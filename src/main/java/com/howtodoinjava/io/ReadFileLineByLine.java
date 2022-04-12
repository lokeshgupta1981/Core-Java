package com.howtodoinjava.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;

import com.google.common.io.LineProcessor;

public class ReadFileLineByLine {
  public static void main(final String[] args) {
    Path filePath = Paths.get("c:/temp", "data.txt");

    // 1. try-with-resources
    try (Stream<String> lines = Files.lines(filePath)) {
      lines.forEach(System.out::println);
    } catch (IOException e) {
      e.printStackTrace();
    }

    // 2. With Filtering
    try (Stream<String> lines = Files.lines(filePath)) {
      List<String> filteredLines = lines.filter(s -> s.contains("password"))
        .collect(Collectors.toList());

      filteredLines.forEach(System.out::println);
    } catch (IOException e) {
      e.printStackTrace();
    }

    // 3. FileReader
    File file = new File("c:/temp/data.txt");

    try (FileReader fr = new FileReader(file);
      BufferedReader br = new BufferedReader(fr);) {
      String line;
      while ((line = br.readLine()) != null) {
        System.out.println(line);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    // 4. Guava
    try {
      List<String> lines = com.google.common.io.Files.readLines(file,
        Charset.defaultCharset());
    } catch (IOException e) {
      e.printStackTrace();
    }

    // With LineProcessor
    LineProcessor<List<String>> lineProcessor = new LineProcessor<>() {
      final List<String> result = new ArrayList<>();

      @Override
      public boolean processLine(final String line) throws IOException {
        result.add(line.replace('.', '/'));
        return true; // keep reading
      }

      @Override
      public List<String> getResult() {
        return result;
      }
    };
    try {
      List<String> lines = com.google.common.io.Files
        .asCharSource(file, Charset.defaultCharset())
        .readLines(lineProcessor);
    } catch (IOException e) {
      e.printStackTrace();
    }

    // Apache Commons IO
    try {
      List<String> lines = FileUtils.readLines(file, Charset.defaultCharset());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}

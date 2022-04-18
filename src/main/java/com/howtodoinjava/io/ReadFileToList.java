package com.howtodoinjava.io;

import com.google.common.base.Charsets;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("unused")
public class ReadFileToList {
  public static void main(String[] args) {
    //1
    try {
      List<String> list = Files.readAllLines(
          new File("data.txt").toPath(), Charsets.UTF_8 );
    } catch (IOException e) {
      e.printStackTrace();
    }

    //2
    try (Stream<String> stream = Files.lines(Paths.get("data.txt"))) {
      ArrayList<String> arrayList = stream
          .collect(Collectors.toCollection(ArrayList::new));
    }
    catch (IOException e) {
      e.printStackTrace();
    }

    //3
    try {
      List<String> lines = FileUtils.readLines(
          new File("/path/to/file.txt"), StandardCharsets.UTF_8);
    } catch (IOException e) {
      e.printStackTrace();
    }

    //4
    try {
      List<String> lines = com.google.common.io.Files.readLines(
          new File("/path/to/file.txt"), StandardCharsets.UTF_8);
    } catch (IOException e) {
      e.printStackTrace();
    }

    //5
    try(Scanner s = new Scanner(new File("data.txt"))) {
      ArrayList<String> list = new ArrayList<>();
      while (s.hasNext()) {
        list.add(s.next());
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    //6
    try (BufferedReader reader = new BufferedReader(
          new FileReader("data.txt"))) {
      ArrayList<String> list = new ArrayList<>();
      while (reader.ready()) {
        list.add(reader.readLine());
      }
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }
}

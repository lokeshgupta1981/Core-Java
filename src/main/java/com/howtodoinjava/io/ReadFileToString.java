package com.howtodoinjava.io;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;

import com.google.common.base.Charsets;

public class ReadFileToString {
  @SuppressWarnings("resource")
  public static void main(final String[] args) throws IOException {

    String ls = System.lineSeparator();

    // Java 11
    Path fileName = Path.of("demo.txt");
    String content = "hello world !!";
    Files.writeString(fileName, content);

    String actual = Files.readString(fileName);
    System.out.println(actual);

    // Java 8
    StringBuilder contentBuilder = new StringBuilder();
    Stream<String> stream = Files.lines(fileName, StandardCharsets.UTF_8);
    stream.forEach(s -> contentBuilder.append(s)
      .append(ls));
    System.out.println(contentBuilder.toString());

    // Java 7
    content = new String(Files.readAllBytes(fileName));
    System.out.println(content);

    // Apache Commons
    content = FileUtils.readFileToString(new File("demo.txt"),
      StandardCharsets.UTF_8);
    System.out.println(content);

    // Guava
    content = com.google.common.io.Files
      .asCharSource(new File("demo.txt"), Charsets.UTF_8)
      .read();
    System.out.println(content);
  }
}

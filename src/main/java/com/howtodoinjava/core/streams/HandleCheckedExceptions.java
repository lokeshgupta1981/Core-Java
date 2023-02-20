package com.howtodoinjava.core.streams;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.commons.lang3.stream.Streams;

public class HandleCheckedExceptions {

  public static void main(String[] args) {

    List<Path> pathList = List.of(
        Path.of("c:/temp/test1.txt"),
        Path.of("c:/temp/test2.txt"),
        Path.of("c:/temp/test3.txt"));

    //1
    /*List<String> fileContents = pathList.stream()
        .map(Files::readString)
        .toList();*/

    //2 - Inline exception handling
    List<String> fileContents = pathList.stream()
        .map(path -> {
          try {
            return Files.readString(path);
          } catch (IOException e) {
            return null;
          }
        }).toList();

    //3 - Safe method extraction
    fileContents = pathList.stream()
        .map(HandleCheckedExceptions::safeReadString)
        .filter(Objects::nonNull)
        .toList();

    //4 - Using Optional
    List<Optional<String>> fileContentsList = pathList.stream()
        .map(HandleCheckedExceptions::readString)
        .toList();

    //5
    fileContents = Streams.stream(pathList.stream())
        .map(Files::readString)
        .collect(Collectors.toList());
  }

  private static String safeReadString(Path path) {
    try {
      return Files.readString(path);
    } catch (IOException e) {
      return null;
    }
  }

  static Optional<String> readString(Path path) {
    try {
      return Optional.of(Files.readString(path));
    } catch (IOException e) {
      return Optional.empty();
    }
  }
}
package com.howtodoinjava.io;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IterateDirectoryExample {

  public static void main(final String[] args) throws IOException {

    // The source directory
    String directory = "C:/temp";

    // 1. Reading only files in the given directory

    // 1.1. Files.list()
    try {
      List<File> fileList = Files.list(Paths.get(directory))
        .map(Path::toFile)
        .filter(File::isFile)
        .collect(Collectors.toList());

      fileList.forEach(System.out::println);
    } catch (IOException e) {
      e.printStackTrace();
    }

    // 1.2. DirectoryStream
    List<File> fileList = new ArrayList<>();
    try (DirectoryStream<Path> stream = Files
      .newDirectoryStream(Paths.get(directory))) {
      for (Path path : stream) {
        if (!Files.isDirectory(path)) {
          fileList.add(path.toFile());
        }
      }
    }
    fileList.forEach(System.out::println);

    System.out.println("==============");

    // 2. Listing All Files in Given Directory and Sub-directories

    // 2.1. Files.walk()
    List<Path> pathList = new ArrayList<>();
    try (Stream<Path> stream = Files.walk(Paths.get(directory))) {
      // Do something with the stream.
      pathList = stream.map(Path::normalize)
        .filter(Files::isRegularFile)
        .collect(Collectors.toList());
    }
    pathList.forEach(System.out::println);

    System.out.println("=======xxx=======");

    // 2.2. Recursion
    fileList = listFiles(directory);
    fileList.forEach(System.out::println);
    
    System.out.println("=======yyy=======");
    
    //3. Files of certain extension
    try (Stream<Path> stream = Files.walk(Paths.get(directory))) {
      // Do something with the stream.
      pathList = stream.map(Path::normalize)
        .filter(Files::isRegularFile)
        .filter(path -> path.getFileName().toString().endsWith(".log"))
        .collect(Collectors.toList());
    }
    pathList.forEach(System.out::println);
  }

  @SuppressWarnings("unchecked")
  private static List<File> listFiles(final String directory) {
    if (directory == null) {
      return Collections.EMPTY_LIST;
    }
    List<File> fileList = new ArrayList<>();
    File[] files = new File(directory).listFiles();
    for (File element : files) {
      if (element.isDirectory()) {
        fileList.addAll(listFiles(element.getPath()));
      } else {
        fileList.add(element);
      }
    }
    return fileList;
  }
}

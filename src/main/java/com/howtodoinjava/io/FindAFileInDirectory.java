package com.howtodoinjava.io;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class FindAFileInDirectory {

  public static void main(String[] args) throws IOException {

    String fileNameToFind = "test.txt";
    File rootDirectory = new File("c:\\temp");
    final List<File> foundFiles = new ArrayList<>();

    //1. Java walk file tree

    try (Stream<Path> walkStream = Files.walk(rootDirectory.toPath(), 5)) {
      walkStream.filter(p -> p.toFile().isFile())
          .forEach(f -> {
            if (f.toString().endsWith(fileNameToFind)) {
              foundFiles.add(f.toFile());
            }
          });
    }

    System.out.println(foundFiles);


    Optional<Path> foundFile;
    try (Stream<Path> walkStream = Files.walk(rootDirectory.toPath(), 5)) {
      foundFile = walkStream.filter(p -> p.toFile().isFile())
          .filter(p -> p.toString().endsWith(fileNameToFind))
          .findFirst();
    }

    if(foundFile.isPresent()) {
      System.out.println(foundFile.get());
    } else {
      System.out.println("File not found");
    }



    //2. Using recursion

    final List<File> foundFilesList = new ArrayList<>();
    findFilesByName(rootDirectory, fileNameToFind, foundFilesList);
    System.out.println(foundFilesList);
  }

  public static void findFilesByName(File rootDir, String filenameToSearch, List<File> finalList) {
    Collection<File> files = List.of(rootDir.listFiles());
    for (Iterator iterator = files.iterator(); iterator.hasNext(); ) {
      File file = (File) iterator.next();
      System.out.println(file.getName());
      if (file.isDirectory()) {
        findFilesByName(file, filenameToSearch, finalList);
      } else if(file.getName().equalsIgnoreCase(filenameToSearch)){
        finalList.add(file);
      }
    }
  }
}



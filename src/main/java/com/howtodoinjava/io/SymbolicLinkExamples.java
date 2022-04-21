package com.howtodoinjava.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SymbolicLinkExamples {
  public static void main(String[] args) throws IOException {
    Path target = Paths.get("c:/temp/data.txt");
    Path link = Paths.get("c:/temp/links/data_link.txt");

    //1
    if (Files.exists(link)) {
      Files.delete(link);
    }
    Files.createSymbolicLink(link, target);

    //2
    boolean isLink = Files.isSymbolicLink(link); //true
  }
}

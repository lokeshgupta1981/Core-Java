package com.howtodoinjava.io;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class FilePath {
  public static void main(String[] args) throws IOException {
    File file = new File("com/howtodoinjava/io/foo/bar/../foo.txt");

    //1
    String givenPath = file.getPath();
    String absPath = file.getAbsolutePath();
    String canPath = file.getCanonicalPath();

    System.out.println(givenPath);
    System.out.println(absPath);
    System.out.println(canPath);

    //2
    Path path = Path.of("com/howtodoinjava/io/foo/bar/../foo.txt");

    givenPath = path.toString();
    absPath = path.toAbsolutePath().toString();
    canPath = path.toRealPath().toString();

    System.out.println(givenPath);
    System.out.println(absPath);
    System.out.println(canPath);
  }
}

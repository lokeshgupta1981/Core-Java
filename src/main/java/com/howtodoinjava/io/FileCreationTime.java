package com.howtodoinjava.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.DosFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

public class FileCreationTime {
  public static void main(String[] args) throws IOException {
    Path filePath = Paths.get("c:/temp/data.txt");

    //1
    FileTime creationTime = (FileTime) Files.getAttribute(filePath, "creationTime");

    System.out.println(creationTime.toString());
    //2022-04-13T05:33:21.9147295Z

    //2
    BasicFileAttributes fileAttrs = Files.readAttributes(filePath,
        BasicFileAttributes.class);
    FileTime fileTime = fileAttrs.creationTime();

    //3
    long millis = creationTime.to(TimeUnit.MILLISECONDS);
    System.out.println(millis);
    //1649828001914

    Instant instant = creationTime.toInstant();
    System.out.println(instant.toString());
    //2022-04-13T05:33:21.914729500Z

    try {
      DosFileAttributes attr =
          Files.readAttributes(filePath, DosFileAttributes.class);
      System.out.println("isReadOnly is " + attr.isReadOnly());
      System.out.println("isHidden is " + attr.isHidden());
      System.out.println("isArchive is " + attr.isArchive());
      System.out.println("isSystem is " + attr.isSystem());
    } catch (UnsupportedOperationException x) {
      System.err.println("DOS file" +
          " attributes not supported:" + x);
    }

  }
}

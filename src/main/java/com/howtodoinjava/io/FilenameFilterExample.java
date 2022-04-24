package com.howtodoinjava.io;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FilenameFilterExample {
  public static void main(String[] args) throws IOException {
    String targetDirectory = "c:\\temp";
    File dir = new File(targetDirectory);

    //Find out all log files
    //1
    String[] logFiles = dir.list(new LogFilterFilter());

    //2
    /*FilenameFilter logFileFilter = (d, s) -> {
      return s.toLowerCase().endsWith(".log");
    };

    String[] logFiles = dir.list(logFileFilter);*/


    //3
    /*String[] logFiles = dir.list((d, s) -> {
      return s.matches("[a-zA-z]+\\.[a-z]+");
    });*/

    //If no log file found; no need to go further
    if (logFiles.length == 0)
      return;

    //This code will delete all log files one by one
    for (String fileName : logFiles)
    {
      String logFile = new StringBuffer(targetDirectory)
          .append(File.separator)
          .append(fileName)
          .toString();

      Files.delete(Paths.get(logFile));
      System.out.println("Log file : " + logFile + " is deleted");
    }
  }
}

class LogFilterFilter implements FilenameFilter
{
  @Override
  public boolean accept(File dir, String fileName)
  {
    return fileName.endsWith(".log");
  }
}
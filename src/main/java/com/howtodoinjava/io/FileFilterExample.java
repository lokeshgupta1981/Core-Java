package com.howtodoinjava.io;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;

public class FileFilterExample {
  public static void main(String[] args)
  {
    File directory = new File("c:/temp");

    //Verify if it is a valid file name
    if (!(directory.exists() && directory.isDirectory()))
    {
      System.out.println(String.format("Directory %s does not exist", directory));
      return;
    }

    FileFilter logFilefilter = new FileFilter() {
      public boolean accept(File file) {
        if (file.getName().endsWith(".log")) {
          return true;
        }
        return false;
      }
    };

    /*FileFilter logFileFilter = (file) -> {
      return file.getName().endsWith(".log");
    };*/

    File[] files = directory.listFiles(logFilefilter);

    for (File f: files)
    {
      System.out.println(f.getName());
    }
  }
}

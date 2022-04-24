package com.howtodoinjava.io;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.FileFileFilter;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

public class CopyDirectoryExample {
  public static void main(String[] args) throws IOException {
    File srcDir = new File("c:\\temp");
    File destDir = new File("c:\\tempNew");

    //1
    FileUtils.copyDirectory(srcDir, destDir,
        DirectoryFileFilter.DIRECTORY, false);

    //Create a filter for text files
    IOFileFilter txtFileFilter = FileFilterUtils.suffixFileFilter(".txt");

    // Create a filter for either directories or ".txt" files
    IOFileFilter complexFilter =
        DirectoryFileFilter.DIRECTORY.or(txtFileFilter);

    FileUtils.copyDirectory(srcDir, destDir, complexFilter, true);

    //2
    copyFolder(srcDir.toPath(), destDir.toPath());
  }

  public static void copyFolder(Path src, Path dest) throws IOException {
    try (Stream<Path> stream = Files.walk(src)) {
      stream.forEach(source -> copy(source, dest.resolve(src.relativize(source))));
    }
  }

  private static void copy(Path source, Path dest) {
    try {
      Files.copy(source, dest, StandardCopyOption.REPLACE_EXISTING);
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage(), e);
    }
  }
}

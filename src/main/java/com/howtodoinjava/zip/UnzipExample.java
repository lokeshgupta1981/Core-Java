package com.howtodoinjava.zip;

import org.apache.commons.io.FilenameUtils;

import java.io.*;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class UnzipExample
{
  public static void main(String[] args)
  {
    Path zipFile = Path.of("c:/temp/data.zip");
    unzipFile(zipFile);
  }

  private static void unzipFile(Path filePathToUnzip) {

    Path parentDir = filePathToUnzip.getParent();
    String fileName = filePathToUnzip.toFile().getName();
    Path targetDir = parentDir.resolve(FilenameUtils.removeExtension(fileName));

    //Open the file
    try (ZipFile zip = new ZipFile(filePathToUnzip.toFile())) {

      FileSystem fileSystem = FileSystems.getDefault();
      Enumeration<? extends ZipEntry> entries = zip.entries();

      //We will unzip files in this folder
      if (!targetDir.toFile().isDirectory()
          && !targetDir.toFile().mkdirs()) {
        throw new IOException("failed to create directory " + targetDir);
      }

      //Iterate over entries
      while (entries.hasMoreElements()) {
        ZipEntry entry = entries.nextElement();

        File f = new File(targetDir.resolve(Path.of(entry.getName())).toString());

        //If directory then create a new directory in uncompressed folder
        if (entry.isDirectory()) {
          if (!f.isDirectory() && !f.mkdirs()) {
            throw new IOException("failed to create directory " + f);
          }
        }

        //Else create the file
        else {
          File parent = f.getParentFile();
          if (!parent.isDirectory() && !parent.mkdirs()) {
            throw new IOException("failed to create directory " + parent);
          }

          try(InputStream in = zip.getInputStream(entry)) {
            Files.copy(in, f.toPath());
          }

        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}

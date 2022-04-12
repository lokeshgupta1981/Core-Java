package com.howtodoinjava.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class WriteToTempFile {
  public static void main(final String[] args) throws IOException {
    // 1
    FileWriter fw = null;
    BufferedWriter bw = null;
    try {
      File tempFile = File.createTempFile("data", ".txt");

      fw = new FileWriter(tempFile);
      bw = new BufferedWriter(fw);
      bw.write("This is the temporary data written to temp file");
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      fw.close();
      bw.close();
    }

    // 2
    try {
      final Path path = Files.createTempFile("myTempFile", ".txt");

      // Writing data here
      byte[] buf = "some data".getBytes();
      Files.write(path, buf);

      // For appending to the existing file
      // Files.write(path, buf, StandardOpenOption.APPEND);

      // Delete file on exit
      path.toFile().deleteOnExit();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}

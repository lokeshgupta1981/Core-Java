package com.howtodoinjava.io;

import java.io.File;

public class ReadOnlyFile {
  @SuppressWarnings("unused")
  public static void main(final String[] args) {
    File file = new File("c:/temp/testReadOnly.txt");
    
    // Mark it read only
    boolean success = file.setReadOnly();

    // Mark it read only
    success = file.setWritable(false);
  }
}

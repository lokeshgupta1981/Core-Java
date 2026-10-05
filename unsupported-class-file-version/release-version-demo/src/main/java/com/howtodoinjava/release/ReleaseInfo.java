package com.howtodoinjava.release;

import java.io.IOException;
import java.io.InputStream;

/**
 * Prints the JVM that runs the class and the class file version javac wrote.
 * Uses java.lang.IO, which is final in Java 25, so the source needs release 25.
 */
public class ReleaseInfo {

  public static void main(String[] args) throws IOException {
    IO.println("Running on Java " + Runtime.version().feature());
    IO.println("Class file major version " + majorVersion());
  }

  static int majorVersion() throws IOException {
    try (InputStream in = ReleaseInfo.class.getResourceAsStream("ReleaseInfo.class")) {
      if (in == null) {
        throw new IOException("ReleaseInfo.class not found on the class path");
      }
      byte[] header = in.readNBytes(8);
      if (header.length < 8) {
        throw new IOException("Class file header is too short");
      }
      return ((header[6] & 0xFF) << 8) | (header[7] & 0xFF);
    }
  }
}

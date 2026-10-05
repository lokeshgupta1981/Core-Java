package com.howtodoinjava.release;

import java.io.IOException;
import java.io.Reader;

/**
 * Reader.of(CharSequence) exists since JDK 25.
 */
public class ReaderDemo {

  public static void main(String[] args) throws IOException {
    try (Reader reader = Reader.of("apple")) {
      char[] buffer = new char[5];
      int read = reader.read(buffer);
      System.out.println("Read " + read + " chars: " + new String(buffer));
    }
  }
}

package com.howtodoinjava.core.exceptions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class SuppressedException {

  public static void main(String[] args) {
    try {
      readFile();
    } catch (Exception e) {
      System.out.println("Exception caught is : " + e.getClass());
      Arrays.stream(e.getSuppressed()).forEach(System.out::println);
    }
  }

  public static void operation() {
    try {
      throw new NullPointerException("NULL");
    } catch (NullPointerException e) {
      //handle exception
    } finally {
      throw new NumberFormatException("INVALID NUMBER");
    }
  }

  public static void readFile() throws IOException {
    try (FileReader fr = new FileReader(new File("c:/temp/not-found.txt"));
        BufferedReader br = new BufferedReader(fr);) {

      //read file
    } finally {
      throw new NumberFormatException("Null");
    }
  }
}

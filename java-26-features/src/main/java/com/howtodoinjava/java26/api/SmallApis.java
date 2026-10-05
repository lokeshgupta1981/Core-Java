package com.howtodoinjava.java26.api;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

/** Smaller final API additions in Java 26. */
public class SmallApis {

  public static void main(String[] args) throws IOException, InterruptedException {
    Instant end = Instant.MAX.plusSaturating(Duration.ofDays(1));
    Duration longest = Duration.MAX;
    System.out.println("end     = " + end);
    System.out.println("longest = " + longest);

    try (Process process = new ProcessBuilder("java", "-version").start()) {
      int exitCode = process.waitFor();
      System.out.println("exit    = " + exitCode);
    }
  }
}

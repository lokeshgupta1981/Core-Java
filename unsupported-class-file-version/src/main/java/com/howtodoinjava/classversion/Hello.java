package com.howtodoinjava.classversion;

/** A minimal main class. Compiled with release 25, it fails to load on JDK 21. */
public class Hello {

  public static void main(String[] args) {
    System.out.println("Hello from Java " + Runtime.version().feature());
  }
}

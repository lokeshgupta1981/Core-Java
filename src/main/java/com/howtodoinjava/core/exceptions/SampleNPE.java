package com.howtodoinjava.core.exceptions;

/**
 * The smallest program that throws a NullPointerException. Run it to see the helpful message
 * (JEP 358) and the stack trace the JVM prints for an uncaught NPE.
 * Run: mvn -q compile exec:java -Dexec.mainClass=com.howtodoinjava.core.exceptions.SampleNPE
 */
public class SampleNPE {

  public static void main(String[] args) {
    String name = null;
    System.out.println(name.length());
  }
}

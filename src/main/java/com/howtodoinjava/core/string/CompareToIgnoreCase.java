package com.howtodoinjava.core.string;

import org.junit.jupiter.api.Assertions;

public class CompareToIgnoreCase {

  public static void main(String[] args) {

    String name = "Alex";

    Assertions.assertEquals(0, name.compareToIgnoreCase("Alex"));
    Assertions.assertEquals(0, name.compareToIgnoreCase("alex"));
    Assertions.assertEquals(0, name.compareToIgnoreCase("ALEX"));

    Assertions.assertEquals(-1, name.compareToIgnoreCase("alexA"));
    Assertions.assertEquals(1, name.compareToIgnoreCase("ale"));
  }
}

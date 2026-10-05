package com.howtodoinjava.java26.runtime;

import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/** JEP 516: small app used for an AOT cache training run and a production run with ZGC. */
public class StartupApp {

  public static void main(String[] args) {
    Map<String, Integer> lengths = Stream.of("apple", "banana", "cherry")
        .collect(Collectors.toMap(s -> s, String::length, (a, b) -> a, TreeMap::new));
    System.out.println(lengths);
  }
}

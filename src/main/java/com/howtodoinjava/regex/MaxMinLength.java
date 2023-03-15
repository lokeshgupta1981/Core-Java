package com.howtodoinjava.regex;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MaxMinLength {

  public static void main(String[] args) {

    List<String> names = List.of("LOKESH", "JAVACRAZY", "LOKESHGUPTAINDIA", "LOKESH123");

    String regex = "^[A-Z]{1,10}$";

    Pattern pattern = Pattern.compile(regex);

    for (String name : names) {
      Matcher matcher = pattern.matcher(name);
      System.out.println(matcher.matches());
    }

  }
}

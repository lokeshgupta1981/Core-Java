package com.howtodoinjava.regex;

import java.util.List;
import java.util.regex.*;

public class StartsWithEndsWith {

  public static void main(String[] args) {

    List<String> lines = List.of(
        "The cat is cute",
        "The category is empty",
        "The noncategory is also empty");

    Pattern pattern = Pattern.compile("\\bcat\\b", Pattern.CASE_INSENSITIVE);

    for(String line: lines) {
      Matcher matcher = pattern.matcher(line);
      while (matcher.find()) {
        //System.out.println(STR."Match found: \{matcher.group()}");
      }
    }

    pattern = Pattern.compile("\\b\\w*cat\\w*\\b");

    for(String line: lines) {
      Matcher matcher = pattern.matcher(line);
      while (matcher.find()) {
        System.out.println(STR."Match found: \{matcher.group()}");
      }
    }
  }
}

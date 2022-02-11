package com.howtodoinjava.core.array;

import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class JoinArrayItems {
  public static void main(final String[] args) {
    int[] intArray = {};
    String[] strArray = { "a", "b", "c" };
    String joined2 = String.join(",", strArray);

    StringJoiner joiner = new StringJoiner(", ", "[", "]");

    String joinedString = joiner.add("How")
      .add("To")
      .add("Do")
      .add("In")
      .add("Java")
      .toString();

    System.out.println(joinedString); // [How, To, Do, In, Java]

    List<String> tokens = Arrays.asList("How", "To", "Do", "In", "Java");

    
    joinedString = tokens.stream()
      .collect(Collectors.joining(", ", "[", "]"));

    System.out.println(joinedString);
  }
}

package com.howtodoinjava.core.string;

import java.util.StringJoiner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("StringTemplateMigration")
public class MultilineStringExample {

  public static void main(String[] args) {

    //Text Blocks
    String content = """
        Line 1
        'Line 2'
        "Line 3"
        Line 4 
        """;

    System.out.println(content);

    String json = """
        {
          "name": "John Doe",
          "age": 30,
          "city": "New York"
        }
        """;
    System.out.println(json);

    // String Concatenation

    String NEW_LINE = System.getProperty("line.separator");

    content = "Line 1" +
        NEW_LINE +
        "'Line 2'" +
        NEW_LINE +
        "\"Line 3\"" +
        NEW_LINE +
        "Line 4 ";

    System.out.println(content);

    StringBuffer sb = new StringBuffer();

    content = sb.append("Line 1")
        .append(NEW_LINE)
        .append("'Line 2'")
        .append(NEW_LINE)
        .append("\"Line 3\"")
        .append(NEW_LINE)
        .append("Line 4 ").toString();

    System.out.println(content);

    //String Join
    content = String.join(NEW_LINE,
        "Line 1",
        "'Line 2'",
        "\"Line 3\"",
        "Line 4 ");

    System.out.println(content);

    //StringJoiner

    StringJoiner stringJoiner = new StringJoiner(NEW_LINE);

    content = stringJoiner.add("Line 1")
        .add("'Line 2'")
        .add("\"Line 3\"")
        .add("Line 4 ").toString();

    System.out.println(content);

    //Stream API

    content = Stream.of("Line 1", "'Line 2'", "\"Line 3\"", "Line 4 ")
        .collect(Collectors.joining(NEW_LINE));

    System.out.println(content);
  }
}

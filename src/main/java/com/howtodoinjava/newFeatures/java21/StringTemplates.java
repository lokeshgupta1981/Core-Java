package com.howtodoinjava.newFeatures.java21;

public class StringTemplates {

  public static void main(String[] args) {
    String name = "Alex";
    String message = STR."Greetings \{name}!";
    System.out.println(message);

    /*//FMT
    message = FMT."Greetings %-12s\{name}.";

    //RAW
    StringTemplate st = RAW."Greetings \{name}.";
    String message = STR.process(st);*/
  }
}

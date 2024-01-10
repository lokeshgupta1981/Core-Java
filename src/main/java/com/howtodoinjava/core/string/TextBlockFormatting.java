package com.howtodoinjava.core.string;

import java.text.MessageFormat;

public class TextBlockFormatting {

  public static void main(String[] args) {

    //Expected Output
    /*{
      "id": 1001,
        "name": "John Doe",
        "age": 34
    }*/

    int id = 1001;
    int age = 34;
    String name = "John Doe";

    Object[] formatArguments = {
        1001,     // id
        "John Doe",  // name
        34       // age
    };


    /*String greeting = """
                  Hello, """ + name + """
                  Welcome to Java text blocks!
                  """;

    System.out.println(greeting.toString());  //Prints "Hello,John DoeWelcome to Java text blocks!"*/



    String textBlock = """
        {
          "id": %d,
          "name": "%s",
          "age": %d
        }
        """;

    String formattedTextBlock1 = String.format(textBlock, formatArguments);
    System.out.println(formattedTextBlock1);

    String formattedTextBlock2 = String.format(textBlock, id, name, age);
    System.out.println(formattedTextBlock2);


    String json = """
        '{'
           "id": {0, number, #},
           "name": "{1}",
           "salary": "{2, number, integer}"
        '}'
        """;

    Object[] arguments = {
        1001,     // id
        "John Doe",  // name
        34000       // salary
    };

    String formattedTextBlock3 = MessageFormat.format(json, arguments);
    System.out.println(formattedTextBlock3);
  }
}

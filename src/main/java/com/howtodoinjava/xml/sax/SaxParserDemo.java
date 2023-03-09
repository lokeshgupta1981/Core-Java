package com.howtodoinjava.xml.sax;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class SaxParserDemo {

  public static void main(String[] args) throws FileNotFoundException {
    //Locate the file
    File xmlFile = new File("D:/temp/sample.xml");

    //Create the parser instance
    UserParser parser = new UserParser();

    //Parse the file
    ArrayList<User> users = parser.parseXml(new FileInputStream(xmlFile));

    //Verify the result
    System.out.println(users);
  }
}

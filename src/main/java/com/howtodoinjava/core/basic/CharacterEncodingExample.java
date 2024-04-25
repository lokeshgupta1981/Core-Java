package com.howtodoinjava.core.basic;

import java.nio.charset.Charset;

public class CharacterEncodingExample {

  public static void main(String[] args) {
    String fileCoding = System.getProperty("file.encoding");
    System.out.println(STR."Default Character Encoding: \{fileCoding}");

    String filePathEncoding = System.getProperty("sun.jnu.encoding");
    System.out.println(STR."Default File Path Encoding: \{filePathEncoding}");

    String defaultCharset = Charset.defaultCharset().displayName();
    System.out.println(STR."Default Charset Name: \{defaultCharset}");

    /*System.setProperty("file.encoding", "UTF-16");
    System.setProperty("sun.jnu.encoding", "UTF-16");*/

    defaultCharset = System.out.charset().displayName();
    System.out.println(STR."Default Character Encoding: \{defaultCharset}");

    System.out.println("The façade pattern is a software-design pattern.");
  }
}

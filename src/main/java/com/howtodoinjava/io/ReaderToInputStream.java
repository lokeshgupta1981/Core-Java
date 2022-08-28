package com.howtodoinjava.io;

import com.google.common.io.CharStreams;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.input.ReaderInputStream;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class ReaderToInputStream {
  public static void main(String[] args) throws IOException {

    String content = "Hello world";
    //Temporarily holds data
    StringBuilder builder = new StringBuilder();

    try(Reader reader = new BufferedReader(
        new StringReader(content))){

      char[] charBuffer = new char[8 * 1024];

      int numCharsRead;
      while ((numCharsRead = reader.read(charBuffer, 0,
          charBuffer.length)) != -1) {
        builder.append(charBuffer, 0, numCharsRead);
      }
    }

    try(InputStream inputStream = new ByteArrayInputStream(
        builder.toString().getBytes(StandardCharsets.UTF_8))){
      //Use InputStream
    }

    //2 ReaderInputStream
    try(Reader reader = new BufferedReader(new StringReader("Hello world"));
        InputStream inputStream = new ReaderInputStream(reader, StandardCharsets.UTF_8);){

      //Use inputStream
    }

    //3
    try(
        Reader reader = new BufferedReader(new StringReader(content));
        InputStream inputStream = new ByteArrayInputStream(
          CharStreams.toString(reader).getBytes(StandardCharsets.UTF_8))){

      //Use InputStream
    }
  }
}

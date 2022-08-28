package com.howtodoinjava.io;

import com.google.common.io.ByteStreams;
import org.apache.commons.io.IOUtils;

import java.io.*;

public class InputStreamToOutputStream {
  public static void main(String[] args) {
    // Java 9


    try (
        InputStream inputStream
            = new ByteArrayInputStream("howtodoinjava".getBytes());
        OutputStream outputStream
            = new ByteArrayOutputStream();) {

      inputStream.transferTo(outputStream);
    } catch (IOException e) {
      //handle exception
    }

    //2
    try (InputStream inputStream = new ByteArrayInputStream("howtodoinjava".getBytes());
         OutputStream outputStream = new ByteArrayOutputStream()) {

      byte[] buf = new byte[8192];
      int length;
      while ((length = inputStream.read(buf)) > 0) {
        outputStream.write(buf, 0, length);
      }
    } catch (IOException e) {
      //handle exception
    }

    //3
    try (InputStream inputStream = new ByteArrayInputStream("howtodoinjava".getBytes());
         OutputStream outputStream = new ByteArrayOutputStream()) {

      ByteStreams.copy(inputStream, outputStream);
    } catch (IOException e) {
      //handle exception
    }

    //4
    try (InputStream inputStream = new ByteArrayInputStream("howtodoinjava".getBytes());
         OutputStream outputStream
             = new ByteArrayOutputStream()) {

      IOUtils.copy(inputStream, outputStream);
    } catch (IOException e) {
      //handle exception
    }
  }
}

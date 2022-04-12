package com.howtodoinjava.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

public class ReadwriteUTF8Data {
  public static void main(final String[] args) {

    // 1
    File fileDir = new File("c:\\temp\\test.txt");
    try (Writer out = new BufferedWriter(new OutputStreamWriter(
      new FileOutputStream(fileDir), StandardCharsets.UTF_8))) {

      out.append("Howtodoinjava.com")
        .append("\r\n")
        .append("UTF-8 Demo")
        .append("\r\n")
        .append("क्षेत्रफल = लंबाई * चौड़ाई")
        .append("\r\n");

      out.flush();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }

    // 2
    try (BufferedReader in = new BufferedReader(
      new InputStreamReader(new FileInputStream(fileDir), "UTF8"))) {
      String str;

      while ((str = in.readLine()) != null) {
        System.out.println(str);
      }

    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }
}

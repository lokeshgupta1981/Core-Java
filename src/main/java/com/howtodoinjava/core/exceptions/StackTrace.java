package com.howtodoinjava.core.exceptions;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.exception.ExceptionUtils;

public class StackTrace {

  public static void main(String[] args) {
    NullPointerException npe = new NullPointerException("Custom error");

    //1. Core Java
    String errorStr = null;

    try (StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw)) {
      npe.printStackTrace(pw);
      errorStr = sw.toString();

    } catch (IOException e) {
      throw new RuntimeException("Error while converting the stacktrace");
    }

    System.out.println(errorStr);

    //2. ExceptionUtils

    errorStr = ExceptionUtils.getStackTrace(npe);
    System.out.println(errorStr);

  }
}

package com.howtodoinjava.report;

import com.howtodoinjava.greeter.Greeter;

/**
 * Built against greeter 1.0.0.
 */
public class ReportHeader {

  public static String header(String name) {
    return Greeter.greet(name) + " - weekly report";
  }
}

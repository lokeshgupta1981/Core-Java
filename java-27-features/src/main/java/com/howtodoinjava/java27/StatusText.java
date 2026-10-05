package com.howtodoinjava.java27;

/**
 * JEP 532: Primitive Types in Patterns, instanceof, and switch (Fifth Preview).
 */
public class StatusText {

  public static String describe(int status) {
    return switch (status) {
      case 200 -> "OK";
      case int code when code >= 500 -> "server error " + code;
      case int code -> "client error " + code;
    };
  }

  public static String asByte(int value) {
    return value instanceof byte b ? "byte " + b : "does not fit in a byte";
  }

  public static void main(String[] args) {
    System.out.println(describe(200));
    System.out.println(describe(503));
    System.out.println(describe(404));
    System.out.println(asByte(100));
    System.out.println(asByte(300));
  }
}

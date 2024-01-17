package com.howtodoinjava.core.basic;

public class MathAbsoluteExamples {

  public static void main(String[] args) {

    double doubleValue = -10.56;
    float floatValue = -7.8f;
    int intValue = -15;
    long longValue = -123456789L;

    System.out.println(STR."Absolute value of double: \{Math.abs(doubleValue)}");
    System.out.println(STR."Absolute value of float: \{Math.abs(floatValue)}");
    System.out.println(STR."Absolute value of int: \{Math.abs(intValue)}");
    System.out.println(STR."Absolute value of long: \{Math.abs(longValue)}");

    //Overflow / Underflow Issue

    int intMinValue = Integer.MIN_VALUE;
    long longMinValue = Long.MIN_VALUE;

    System.out.println(STR."Absolute value of int: \{Math.abs(intMinValue)}");
    System.out.println(STR."Absolute value of long: \{Math.abs(longMinValue)}");

    System.out.println(STR."Absolute value of int: \{Math.absExact(intMinValue)}");
    System.out.println(STR."Absolute value of long: \{Math.absExact(longMinValue)}");
  }
}

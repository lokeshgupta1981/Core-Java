package com.howtodoinjava.core.basic;

public class UnderscoreInLiterals {

  public static void main(String[] args) {

    //Supported in byte
    byte improvedByte = 0b0010_0101;

    //Supported in int
    int improvedInt = 10_00_000;

    //Supported in long
    long improvedLong = 10_00_000l;

    //Supported in float
    float improvedFloat = 10_00_000.16f;

    //Supported in double
    double improvedDouble = 10_00_000.56d;

    System.out.println(improvedByte);     //37
    System.out.println(improvedInt);      //1000000
    System.out.println(improvedLong);     //1000000
    System.out.println(improvedFloat);    //1000000.2
    System.out.println(improvedDouble);   //1000000.56

    //NOT ALLOWED

    /*//adjacent to decimal point
    float value = 10_.12F;
    float value1 = 10._12F;

    //adjacent to F or L
    float value2 = 10.12F_;
    float value3 = 10_.12_F;

    //at start or end
    int value = 10_;
    int value = _10;*/
  }

}

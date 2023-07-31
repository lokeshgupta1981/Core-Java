package com.howtodoinjava.core.datatypes.decimalandhexadecimal.hextodecimal;


public class FloatingPointHexToDecimal {

  public static void main(String[] args) {

    String myString = "BF800000";
    Long i = Long.parseLong(myString, 16);
    Float f = Float.intBitsToFloat(i.intValue());

    //converted float value
    System.out.println(f);

    //Convert the float back to hex and verify
    System.out.println(Integer.toHexString(Float.floatToIntBits(f)));

  }

}

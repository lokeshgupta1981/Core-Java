package com.howtodoinjava.core.datatypes.decimalandhexadecimal.hextodecimal;

public class ExampleUsingIntegerClass {

  private static int usingParseInt(String number) throws NumberFormatException {

    return Integer.parseInt(number, 16);
  }

  private static int usingValueOf(String number) throws NumberFormatException {

    return Integer.valueOf(number, 16);
  }

  private static int usingDecode(String number) throws NumberFormatException {

    return Integer.decode("0X" + number);
  }

  public static void main(String[] args) {

    /*Integer.parseInt(hex, radix);
    Integer.valueOf(hex, radix);
    Integer.decode(hex);

    Long.parseLong(hex, radix);
    Long.valueOf(hex, radix);
    Long.decode(hex);*/

    /*Double.parseDouble(hex);
    Float.parseFloat(hex);
*/
    double number = 333.44;
    String hex = Double.toHexString(number);

    System.out.println(hex);


    try {

      String hexadecimalNumber = "10D";

      /*System.out.println(usingParseInt(hexadecimalNumber));
      System.out.println(usingValueOf(hexadecimalNumber));
      System.out.println(usingDecode(hexadecimalNumber));*/
    } catch (NumberFormatException exception) {
      System.out.println(exception.getMessage());
    }
  }
}

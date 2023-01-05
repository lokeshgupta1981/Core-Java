package com.howtodoinjava.core.string;

import java.text.ParseException;
import javax.swing.text.MaskFormatter;
import org.junit.jupiter.api.Assertions;

public class MaskExceptLast4Chars {

  public static void main(String[] args) throws ParseException {
    String str = "123456789";

    Assertions.assertEquals("xxxxx6789", maskString1("123456789"));
    Assertions.assertEquals("6789", maskString1("6789"));
    Assertions.assertEquals("789", maskString1("789"));
    Assertions.assertEquals("", maskString1("")); //empty string
    Assertions.assertEquals("NULL", maskString1(null)); //null
  }

  public static String maskString2(String input) {
    if(input == null) {
      return "NULL";
    }
    //return input.replaceAll(".(?=.{4})", "x");  //Matches any chars
    return input.replaceAll("[^-](?=.{4})", "x");  //Matches any chars except hyphen
  }

  public static String maskString1(String input) {
    if(input == null) {
      return "NULL";
    }
    int maskLength = input.length() - 4;
    if (maskLength <= 0)
      return input; //
    return "x".repeat(maskLength) + input.substring(maskLength);
  }
}

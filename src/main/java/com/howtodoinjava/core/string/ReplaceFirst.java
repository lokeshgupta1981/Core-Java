package com.howtodoinjava.core.string;

public class ReplaceFirst {

  public static void main(String[] args) {
    //simple string
    String str = "howtodoinjava";
    String newStr = str.replaceFirst("java", "JAVA");
    System.out.println(newStr); //howtodoinJAVA

    //regex
    String string = "how to do in java";
    String updatedString = string.replaceFirst("\\s", "-");
    System.out.println(updatedString); //how-to do in java

    //metacharacters
    String string2 = "how+to+do+in+java";
    String updatedString2 = string2.replaceFirst("\\+", "-");
    System.out.println(updatedString2); //how-to+do+in+java

    //null is not allowed
    String string1 = "how to do in java";
    //String updatedString1 = string1.replaceFirst(null, "-"); //Throws NullPointerException
  }
}
